package com.liberty.parsers

import java.io.FileInputStream
import java.util

import com.liberty.builders.{ClassBuilder, FunctionBuilder}
import com.liberty.common.Implicits._
import com.liberty.common.{ProjectConfig, TypeMapper}
import com.liberty.model._
import com.liberty.operations.{PatternOperation, Variable}
import com.liberty.traits.{CustomImport, JavaPackage, LocationPackage, NoPackage}
import com.liberty.types.primitives.StringType
import com.liberty.types.{DataType, ObjectType}
import japa.parser.JavaParser
import japa.parser.ast.`type`.{ClassOrInterfaceType, ReferenceType, Type}
import japa.parser.ast.body._
import japa.parser.ast.expr.{AnnotationExpr, MemberValuePair, NormalAnnotationExpr, SingleMemberAnnotationExpr}
import japa.parser.ast.stmt.{ExpressionStmt, TryStmt}

import scala.collection.JavaConversions._

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class JavaClassParser(fileName: String, basePackage: LocationPackage = ProjectConfig.basePackage) extends Parsable {
  this: Pathable =>

  def parse(baseTemplatePackage: String): TemplateClass = {
    val in = new FileInputStream(getTemplatePath + fileName)
    val cu = JavaParser.parse(in)
    val imports = cu.getImports

    def getPackage(typeName: String): JavaPackage = {
      imports.map(_.getName.toString).find(_.endsWith(typeName)) match {
        case Some(s: String) => JavaPackage.parse(s, baseTemplatePackage, ProjectConfig.basePackage.packagePath)
        case _ => new NoPackage
      }
    }

    def getDataType(typeDeclaration: Type): DataType = {
      typeDeclaration match {
        case ref: ReferenceType =>
          val typeName = ref.toString
          val t = ref.getType match {
            case cls: ClassOrInterfaceType =>
              if (cls.getTypeArgs != null)
                if (cls.getTypeArgs.size() == 1) {
                  TypeMapper.getStandardOneContainedCollectionTypes(cls.getName, getSimpleDataType(cls.getTypeArgs.head))
                } else if (cls.getTypeArgs.size() == 2) {
                  TypeMapper.getStandardTwoContainedCollectionTypes(cls.getName, getSimpleDataType(cls.getTypeArgs.get(0)), getSimpleDataType(cls.getTypeArgs.get(1)))
                } else
                  None // TODO : Generic type modify. Add custom type support
              else None
            case _ => None
          }
          t.map(dt => dt).getOrElse(getSimpleDataType(typeDeclaration))
        case _ => getSimpleDataType(typeDeclaration)
      }
    }

    def getSimpleDataType(typeDeclaration: Type): DataType = {
      val typeName = typeDeclaration.toString
      TypeMapper.getStandardType(typeName) match {
        case Some(t: DataType) => t
        case _ =>

          val dataType = ObjectType(typeName, getPackage(typeName))
          dataType
      }
    }

    def getAnnotations(exprs: util.List[AnnotationExpr]): List[JavaAnnotation] = {
      if (exprs == null)
        return Nil
      def createAnnotationBase(a: AnnotationExpr): JavaAnnotation = {
        JavaAnnotation(a.getName.getName, getPackage(a.getName.getName))
      }
      val result = exprs.map {
        case a@(ann: NormalAnnotationExpr) =>
          val annotation = createAnnotationBase(a)
          if (a.getChildrenNodes != null) {
            a.getChildrenNodes.foreach {
              case pair: MemberValuePair =>
                val name = pair.getName
                val value = pair.getValue
                annotation.addParameter(name, getAnnotationValue(value.toString))
              case _ =>
            }
          }
          annotation
        case a@(ann: SingleMemberAnnotationExpr) =>
          val value = ann.getMemberValue.toString
          if (value.contains("\""))
            new SingleMemberAnnotation(a.getName.getName, getAnnotationValue(ann.getMemberValue.toString), getPackage(a.getName.getName))
          else
            new SingleMemberAnnotation(a.getName.getName, ann.getMemberValue.toString, getPackage(a.getName.getName), true)
        case a => createAnnotationBase(a)
      }
      result.toList
    }

    val concreteType = cu.getTypes.get(0)
    val builder = ClassBuilder.templateBuilder(concreteType.getName)
    def addExtendsOrImplements(declaration: TypeDeclaration) {
      declaration match {
        case cl: ClassOrInterfaceDeclaration =>
          if (cl.getExtends != null) {
            cl.getExtends.map(_.toString).foreach(builder.addExtendTemplate)
          }
          if (cl.getImplements != null) {
            cl.getImplements.map(_.toString).foreach(builder.addImplementTemplate)
          }
        case _ =>
      }
    }

    addExtendsOrImplements(concreteType)
    getAnnotations(concreteType.getAnnotations).foreach(builder.addAnnotation)

    var functions: List[JavaFunction] = Nil
    var fields: List[JavaField] = Nil
    val members = concreteType.getMembers
    // Parse fields
    members.foreach {
      case fieldDeclaration: FieldDeclaration =>
        val dataType = getDataType(fieldDeclaration.getType)
        val variable = fieldDeclaration.getVariables.get(0)
        val modifier = getModifier(fieldDeclaration.getModifiers)
        val value = getValue(variable, dataType)
        val field = JavaField(variable.getId.getName, dataType, modifier, value)
        val annotations = getAnnotations(fieldDeclaration.getAnnotations)
        annotations.foreach(field.addAnnotation)
        fields = fields ::: List(field)
      case _ =>
    }
    // Parse methods
    members.foreach {
      case m: MethodDeclaration =>
        val functionBuilder = FunctionBuilder(getModifier(m.getModifiers), m.getName)
        functionBuilder.addAnnotations(getAnnotations(m.getAnnotations))

        if (m.getThrows != null)
          m.getThrows.foreach(thr => functionBuilder.addThrow(getJavaException(thr.getName)))

        if (m.getParameters != null)
          m.getParameters.foreach { param =>
            val functionParameter = FunctionParameter(param.getId.getName, getDataType(param.getType))
            if (param.getAnnotations != null)
              getAnnotations(param.getAnnotations).foreach(functionParameter.addAnnotation)
            functionBuilder.addParam(functionParameter)
          }

        if (m.getType != null)
          functionBuilder.addReturn(getDataType(m.getType))

        m.getBody.getStmts.foreach {
          case expr: TryStmt =>
            val tryable = functionBuilder.tryable {
              expr.getTryBlock.getStmts.foreach(ex => {
                val exprTemp = ex.toString
                val expression = if (exprTemp.endsWith(";")) exprTemp.substring(0, exprTemp.size - 1) else exprTemp.toString
                functionBuilder.addOperation(new PatternOperation(expression, Nil))
              })
            }
            expr.getCatchs.headOption map {
              c => val ops = c.getCatchBlock.getStmts.map(s => {
                val stm = s.toString
                val expression = if (stm.endsWith(";")) stm.substring(0, stm.size - 1) else stm.toString
                new PatternOperation(expression, Nil)
              }).toList
                val javaException = c.getExcept.getTypes.headOption.map(e => getJavaException(e.toString))
                javaException.fold(tryable.catchOperation(ops))(tryable.catchOperation(ops, _))
            }
          case e => val exprTemp = e.toString
            val expression = if (exprTemp.endsWith(";")) exprTemp.substring(0, exprTemp.size - 1) else exprTemp.toString
            functionBuilder.addOperation(new PatternOperation(expression, Nil))
        }
        functions = functions ::: List(functionBuilder.getFunction)
      case _ =>
    }

    def getJavaException(className: String): JavaException = {
      JavaException(className, getPackage(className))
    }

    imports.foreach(i => builder.addCustomImport {
      if (i.isAsterisk) CustomImport(i.getName.toString + ".*") else CustomImport(i.getName.toString)
    })
    fields.foreach(builder.addField)
    builder.addFunctions(functions)
    builder.addPackage(getClassPackage(concreteType.getName)(basePackage))
    builder.getTemplateJavaClass
  }

  /**
   * Returns variable that  method invoked in <b>expr</b>. If it is not  ObjectInvokeOperation returns None
   * @param expr
   * @param fields
   * @return
   */
  private def getObjectInvokeOperation(expr: ExpressionStmt, fields: List[JavaField]): Option[Variable] = {
    val result = expr.getExpression.toString
    val splitted = result.split(".")
    if (splitted != null && splitted.nonEmpty) {
      val potentialField = splitted(0)
      fields.find(f => f.name == potentialField).map(f => javaFieldToVariableOption(f)).getOrElse(None)
    }
    else None
  }


}

object JavaClassParser {
  def apply(fileName: String, basePackage: LocationPackage = ProjectConfig.basePackage): JavaClassParser = {
    new JavaClassParser(fileName, basePackage) with InternalPath
  }
}

trait Parsable {
  def getAnnotationValue(initialValue: String) = {
    if (initialValue.startsWith("\""))
      initialValue.substring(1, initialValue.size - 1)
    else
      initialValue
  }

  def getValue(variable: VariableDeclarator, dataType: DataType): Option[String] = {
    if (variable.getInit != null) {
      val value = variable.getInit.toString
      dataType match {
        case StringType => Some(value.substring(1, value.size - 1))
        case _ => Some(value)
      }
    } else
      None
  }

  def getModifier(id: Int): Modifier = {
    id match {
      case 10 => PrivateStaticModifier
      case 9 => PublicStaticModifier
      case 1 => PublicModifier
      case 2 => PrivateModifier
      case 4 => ProtectedModifier
      case 12 => ProtectedStaticModifier
      case 8 => StaticModifier
      case 0 => DefaultModifier
      case _ => DefaultModifier
    }
  }

  /**
   * Returns JavaPackage instance based on basePackage and class name
   * @param className Class name
   */
  def getClassPackage(className: String)(basePackage: LocationPackage) = {
    val suffix = className match {
      case s: String if s.toLowerCase.contains("resource") | s.toLowerCase.contains("rest") | s.toLowerCase.contains("rs") => "rest"
      case s: String if s.toLowerCase.contains("bean") => "beans"
      case s: String if s.toLowerCase.contains("dao") => "daos"
      case s: String if s.toLowerCase.contains("servlet") => "servlets"
      case s: String if s.toLowerCase.contains("exception") => "errors"
      case s: String if s.toLowerCase.contains("model") => "models"
      case s: String if s.toLowerCase.contains("entity") => "entities"
      case s: String if s.toLowerCase.contains("request") => "requests"
      case s: String if s.toLowerCase.contains("response") => "responses"
      case _ => "common"
    }
    basePackage.nested(suffix).nestedClass(className)
  }
}

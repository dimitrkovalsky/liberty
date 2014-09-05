package com.liberty.parsers

import java.io.{File, FileInputStream}
import java.util

import com.liberty.builders.{ClassBuilder, FunctionBuilder}
import com.liberty.common.Implicits._
import com.liberty.common.{ProjectConfig, TypeMapper}
import com.liberty.model._
import com.liberty.operations.{PatternOperation, Variable}
import com.liberty.traits.{JavaPackage, LocationPackage, NoPackage}
import com.liberty.types.primitives.StringType
import com.liberty.types.{DataType, ObjectType}
import japa.parser.JavaParser
import japa.parser.ast.body.{FieldDeclaration, MethodDeclaration, VariableDeclarator}
import japa.parser.ast.expr.{AnnotationExpr, MemberValuePair, NormalAnnotationExpr, SingleMemberAnnotationExpr}
import japa.parser.ast.stmt.{ExpressionStmt, TryStmt}

import scala.collection.JavaConversions._

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class JavaClassParser(fileName: String, basePackage: LocationPackage = ProjectConfig.basePackage) {
  val PROJECT_PATH = getProjectFolder
  val TEMPLATES_PATH = PROJECT_PATH + "//generation//templates//"

  private def getProjectFolder = {
    val path = new File(".").getAbsolutePath
    path.substring(0, path.length() - 1)
  }


  def parse(): JavaClass = {
    val in = new FileInputStream(TEMPLATES_PATH + "DepartmentResource.tmpl")
    // parse the file
    val cu = JavaParser.parse(in)
    //println(cu.getPackage)
    val imports = cu.getImports
    //imports.foreach(i => println(i.getName))

    def getPackage(typeName: String): JavaPackage = {
      imports.map(_.getName.toString).find(_.endsWith(typeName)) match {
        case Some(s: String) => JavaPackage.parse(s)
        case _ => new NoPackage
      }
    }

    def getDataType(typeName: String): DataType = {
      TypeMapper.getStandardType(typeName) match {
        case Some(t: DataType) => t
        case _ =>
          val dataType = ObjectType(typeName, getPackage(typeName))
          dataType
      }
    }

    val concreteType = cu.getTypes.get(0)

    val builder = ClassBuilder(concreteType.getName)
    var functions: List[JavaFunction] = Nil
    var fields: List[JavaField] = Nil
    val members = concreteType.getMembers
    members.foreach {
      case fieldDeclaration: FieldDeclaration =>
        val typeName = fieldDeclaration.getType.toString
        val dataType = getDataType(typeName)
        val variable = fieldDeclaration.getVariables.get(0)
        val modifier = getModifier(fieldDeclaration.getModifiers)
        val value = getValue(variable, dataType)
        val field = JavaField(variable.getId.getName, dataType, modifier, value)
        val annotations = getAnnotations(fieldDeclaration.getAnnotations)
        annotations.foreach(field.addAnnotation)
        fields = fields ::: List(field)
      case _ =>
    }

    members.foreach {
      case m: MethodDeclaration =>
        val functionBuilder = FunctionBuilder(getModifier(m.getModifiers), m.getName)
        functionBuilder.addAnnotations(getAnnotations(m.getAnnotations))
        m.getBody.getStmts.foreach {
          e => println(e.getClass)
            e match {
              case expr: TryStmt =>
                val tryable = functionBuilder.tryable {
                  expr.getTryBlock.getStmts.foreach(ex => functionBuilder.addOperation(new PatternOperation(ex.toString, Nil)))
                }
                expr.getCatchs.headOption map {
                  c => val ops =  c.getCatchBlock.getStmts.map(s => new PatternOperation(s.toString, Nil)).toList
                    // TODO: parse Exceptions c.getExcept
                    // TODO : remove \t before try
                    tryable.catchOperation(ops)
                }

              case _ => functionBuilder.addOperation(new PatternOperation(e.toString, Nil))
            }

        }
        functions = functions ::: List(functionBuilder.getFunction)


      case _ =>

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
        case a@(ann: SingleMemberAnnotationExpr) => new SingleMemberAnnotation(a.getName.getName,
          getAnnotationValue(ann.getMemberValue.toString), getPackage(a.getName.getName))
        case a => createAnnotationBase(a)
      }
      result.toList
    }
    fields.foreach(builder.addField)
    builder.addFunctions(functions)
    builder.addPackage(getClassPackage(concreteType.getName))
    builder.getJavaClass
  }

  /**
   * Returns variable that  method invoked in <b>expr</b>. If it is not  ObjectInvokeOperation returns None
   * @param expr
   * @param fields
   * @return
   */
  private def getObjectInvokeOperation(expr: ExpressionStmt, fields: List[JavaField]): Option[Variable] = {
    val result = expr.getExpression.toString
    val splited = result.split(".")
    if (splited != null && splited.nonEmpty) {
      val potentialField = splited(0)
      fields.find(f => f.name == potentialField).map(f => javaFieldToVariableOption(f)).getOrElse(None)
    }
    else None
  }

  /**
   * Returns JavaPackage instance based on basePackage and class name
   * @param className Class name
   */
  private def getClassPackage(className: String) = {
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

  private def getAnnotationValue(initialValue: String) = {
    if (initialValue.startsWith("\""))
      initialValue.substring(1, initialValue.size - 1)
    else
      initialValue
  }

  private def getValue(variable: VariableDeclarator, dataType: DataType): Option[String] = {
    if (variable.getInit != null) {
      val value = variable.getInit.toString
      dataType match {
        case StringType => Some(value.substring(1, value.size - 1))
        case _ => Some(value)
      }
    } else
      None
  }

  private def getModifier(id: Int): Modifier = {
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
}



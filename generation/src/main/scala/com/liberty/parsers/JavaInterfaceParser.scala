package com.liberty.parsers


import java.io.FileInputStream
import java.util

import com.liberty.builders.{FunctionBuilder, InterfaceBuilder}
import com.liberty.common.{ProjectConfig, TypeMapper}
import com.liberty.model._
import com.liberty.traits.{CustomImport, JavaPackage, LocationPackage, NoPackage}
import com.liberty.types.{DataType, ObjectType}
import japa.parser.JavaParser
import japa.parser.ast.`type`.{ClassOrInterfaceType, ReferenceType, Type}
import japa.parser.ast.body._
import japa.parser.ast.expr.{AnnotationExpr, MemberValuePair, NormalAnnotationExpr, SingleMemberAnnotationExpr}

import scala.collection.JavaConversions._

/**
 * Created by Dmytro_Kovalskyi on 22.09.2014.
 */
class JavaInterfaceParser(fileName: String, basePackage: LocationPackage = ProjectConfig.basePackage) extends Parsable {
  this: Pathable =>
  def parse(): JavaInterface = {
    val in = new FileInputStream(getTemplatePath + fileName)
    val cu = JavaParser.parse(in)
    val imports = cu.getImports

    def getPackage(typeName: String): JavaPackage = {
      imports.map(_.getName.toString).find(_.endsWith(typeName)) match {
        case Some(s: String) => JavaPackage.parse(s)
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
    val builder = InterfaceBuilder(concreteType.getName)

    // TODO: ignored interface extends
    // addExtends(concreteType)
    getAnnotations(concreteType.getAnnotations).foreach(builder.addAnnotation)

    var signatures: List[FunctionSignature] = Nil
    val members = concreteType.getMembers

    // Parse methods
    members.foreach {
      case m: MethodDeclaration =>
        val functionBuilder = FunctionBuilder(getModifier(m.getModifiers), m.getName)
        functionBuilder.addAnnotations(getAnnotations(m.getAnnotations))
        if (m.getThrows != null)
          m.getThrows.foreach(thr => functionBuilder.addThrow(getJavaException(thr.getName)))
        //functionBuilder.addReturn()
        if (m.getParameters != null)
          m.getParameters.foreach(param => functionBuilder.addParam(FunctionParameter(param.getId.getName, getDataType(param.getType))))
        if (m.getType != null)
          functionBuilder.addReturn(getDataType(m.getType))
        signatures = signatures ::: List(functionBuilder.getFunction.signature)
      case _ =>
    }

    def getJavaException(className: String): JavaException = {
      JavaException(className, getPackage(className))
    }

    imports.foreach(i => builder.addCustomImport(if (i.isAsterisk) CustomImport(i.getName.toString + ".*") else CustomImport(i.getName.toString)))
    signatures.foreach(builder.addFunctionSignature)
    builder.addPackage(getClassPackage(concreteType.getName)(basePackage))
    builder.getInterface
  }
}

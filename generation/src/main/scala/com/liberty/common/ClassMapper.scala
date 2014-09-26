package com.liberty.common

import com.liberty.builders.{ClassBuilder, InterfaceBuilder}
import com.liberty.common.Implicits._
import com.liberty.model._
import com.liberty.operations.{CatchOperation, Operation, PatternOperation, TryOperation}
import com.liberty.traits.{CustomImport, JavaPackage}
import com.liberty.types.DataType
import com.liberty.types.collections.CollectionType

/**
 * Changes models in parsed classes for appropriate model
 * Created by Dmytro_Kovalskyi on 22.09.2014.
 */
case class ClassMapper(baseModel: JavaClass) {
  def changeCustomImport(customImport: CustomImport, model: String): Option[CustomImport] = {
    if (customImport.importString.contains(baseModel.name))
      Some(CustomImport(customImport.importString.replace(baseModel.name, model)))
    else
      Some(customImport)
  }

  private val baseModelName = baseModel.name

  /**
   * Validate if function contains work with foreign key
   * @param signature
   * @param model
   * @return
   */
  private implicit def shouldExist(signature: FunctionSignature, model: JavaClass): Boolean = {
    val line = signature.name
    // if function contains work with foreign key
    if (line.toLowerCase.containsOr(List("get", "by"), List("find", "by"))) {
      // TODO: contains id ignoring PK
      model.fields.count(_.name.toLowerCase.contains("id")) > 2
    }
    else true
  }

  def changeFunction(function: JavaFunction, model: JavaClass)(implicit validate: (FunctionSignature, JavaClass) => Boolean = shouldExist): Option[JavaFunction] = {
    if (shouldExist(function.signature, model)) {
      val signature = changeSignature(function.signature, model)
      val body = changeBody(function.body, model)
      val newFunction = JavaFunction(signature, body)
      // TODO: annotations deep copy
      newFunction.annotations = function.annotations
      Some(newFunction)
    }
    else
      None
  }

  def changeModel(template: TemplateClass, model: JavaClass, newClassName: String, newPackage: JavaPackage): JavaClass = {
    val annotations = template.annotations.map(changeAnnotation(_, model))
    val functions = template.functions.map(f => changeFunction(f, model)).flatten
    val fields = template.fields.map(changeField(_, model))
    val classBuilder = ClassBuilder(newClassName)
    classBuilder.addFunctions(functions)
    template.customImports.map(changeCustomImport(_, model.name)).flatten.foreach(classBuilder.addCustomImport)
    fields.foreach(classBuilder.addField)
    classBuilder.addPackage(newPackage)
    annotations.foreach(classBuilder.addAnnotation)
    classBuilder.getJavaClass
  }

  def changeModel(template: JavaInterface, model: JavaClass, newInterfaceName: String, newPackage: JavaPackage): JavaInterface = {
    val interfaceBuilder = InterfaceBuilder(newInterfaceName)
    val interfaceSignatures = template.signatures.map(changeSignatureWithFilter(_, model)).flatten
    interfaceSignatures foreach interfaceBuilder.addFunctionSignature
    template.annotations.foreach(interfaceBuilder.addAnnotation)
    template.customImports.map(changeCustomImport(_, model.name)).flatten.foreach(interfaceBuilder.addCustomImport)
    interfaceBuilder.addPackage(newPackage)
    interfaceBuilder.getInterface
  }

  private def changeAnnotation(baseAnnotation: JavaAnnotation, model: JavaClass): JavaAnnotation = {
    baseAnnotation match {
      case simple: SingleMemberAnnotation =>
        val param = if (simple.getParam.shouldChange) simple.getParam.replaceModel(baseModelName, model.name) else simple.getParam
        new SingleMemberAnnotation(simple.name, param, simple.pack)
      case _ =>
        val params = baseAnnotation.parameters.map {
          case (key: String, value: String) => if (value.shouldChange) (key, value.replaceModel(value, model.name))
        }
        val result = JavaAnnotation(baseAnnotation.name, baseAnnotation.javaPackage)
        params.foreach { case (key: String, value: String) => result.addParameter(key, value)}
        result
    }
  }

  /**
   * Changes field to appropriate model if fields should be changed. Field has name or dataType from old model
   */
  // TODO:  May be some field should be missed. Validate it
  def changeField(field: JavaField, model: JavaClass): JavaField = {
    val name = if (field.name.shouldChange) changeName(field.name, model) else field.name
    val dataType = if (field.dataType.toString.shouldChangeNotIgnoreCase) changeDataType(field.dataType, model.dataType) else field.dataType
    // TODO: can be potential error with  field.value
    val newField = JavaField(name, dataType, field.modifier, field.value)
    newField.annotations = field.annotations
    newField
  }

  def changeBody(body: FunctionBody, model: JavaClass) = {
    def changeOperation(op: Operation) = {
      op match {
        case PatternOperation(pattern, _) if pattern.shouldChange =>
          val newPattern = pattern.replace(baseModelName, model.name).replace(baseModelName.toLowerCase, model.name.toLowerCase)
          Some(PatternOperation(newPattern, Nil)) // TODO: add import
        case p: PatternOperation => Some(p)
        case _ => None
      }
    }

    val ops = body.operations.map {
      case tr: TryOperation =>
        val tryOps = tr.c.ops.map(changeOperation).flatten
        val catchResult = tr.c.result match {
          case op: CatchOperations =>
            CatchOperations(op.operations.map(changeOperation).flatten)
          case _ => tr.c.result
        }
        val catchOp = CatchOperation(tr.c.e, tryOps, catchResult)
        Some(TryOperation(catchOp))
      case c: Operation => changeOperation(c)
      case _ => None
    }
    val result = new FunctionBody()
    ops.flatten.foreach(result.addOperation)
    result
  }

  /**
   * Adds additional string methods for validation and replacement based on models
   */
  implicit class ModelString(s: String) {
    def shouldChangeNotIgnoreCase: Boolean = s contains baseModelName

    def shouldChange: Boolean = s.toLowerCase contains baseModelName.toLowerCase

    def isComplexTypeWithModel(model: String) = s.contains(model)

    def replaceModel(base: String, model: String) = {
      if (s.shouldChangeNotIgnoreCase) base.replace(baseModelName, model)
      else if (s.contains(base.toLowerCase)) s.replace(base.toLowerCase, model.toLowerCase)
      else base
    }
  }

  def changeName(name: String, model: JavaClass) = {
    if (name.shouldChangeNotIgnoreCase) name.replace(baseModelName, model.name)
    else name.replace(baseModelName.toLowerCase, model.name.toLowerCase)
  }

  def changeSignature(signature: FunctionSignature, model: JavaClass) = {
    // TODO: process id in annotations
    val output = if (signature.output.toString.shouldChangeNotIgnoreCase) changeDataType(signature.output, model.dataType) else signature.output
    val name = if (signature.name.shouldChange) changeName(signature.name, model) else signature.name
    val input = signature.input.map(in => {
      // if it is idField
      if (baseModel.getIdField.exists(f => f.name.equals(in.paramName.name) && f.dataType.equals(in.paramType)) && model.getIdField.isDefined) {
        val idField = model.getIdField.get
        val param = FunctionParameter(in.paramName.name, idField.dataType)
        in.annotations.foreach(param.addAnnotation)
        param
      } else {
        val newName = if (in.paramName.name.shouldChange) changeName(in.paramName.name, model) else in.paramName.name
        val newType = if (in.paramType.toString.shouldChangeNotIgnoreCase) changeDataType(in.paramType, model.dataType) else in.paramType
        val param = FunctionParameter(newName, newType)
        in.annotations.foreach(param.addAnnotation)
        param
      }
    })

    // TODO: process throws
    FunctionSignature(name, output, signature.modifier, input, signature.functionThrows)
  }

  /**
   * Validate that signature should be present in new interface
   * @param signature potential signature
   * @param model model that should be used for generated class
   */
  def changeSignatureWithFilter(signature: FunctionSignature, model: JavaClass): Option[FunctionSignature] = {
    if (shouldExist(signature, model))
      Some(changeSignature(signature, model))
    else
      None
  }

  private def changeDataType(oldType: DataType, newType: DataType): DataType = {
    if (TypeMapper.isCollection(oldType))
      TypeMapper.changeType(oldType.asInstanceOf[CollectionType], oldType, newType).getOrElse(oldType)
    // IDepartmentBean [model : Department] => I[newModel]Bean
    else if (oldType.toString.isComplexTypeWithModel(baseModelName))
      TypeMapper.changeComplexType(oldType, baseModelName, newType.getTypeName)
    else
      newType
  }
}
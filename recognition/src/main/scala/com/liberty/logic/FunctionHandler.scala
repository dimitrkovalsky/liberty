package com.liberty.logic

import com.liberty.commands.{FunctionCommands, GeneralCommands}
import com.liberty.entities.{FunctionEntity, RecognitionResult}
import com.liberty.generic.{OperationResult, VoiceNotifier}
import com.liberty.helpers.GrammarRegistry

/**
 * User: dkovalskyi
 * Date: 15.07.13
 * Time: 16:08
 */
class FunctionHandler extends VoiceNotifier {
  var currentFunction: FunctionEntity = new FunctionEntity()
  var previousCommand = GeneralCommands.NO_COMMAND


  override def onRecognized(recognized: RecognitionResult): Option[OperationResult] = {
    println("[FunctionHandler] onRecognized : " + recognized.best.label)

    var result: Option[OperationResult] = recognized.best.label match {
      case FunctionCommands.CREATE_FUNCTION => createFunction()
      case FunctionCommands.RENAME => renameFunction(recognized)
      case FunctionCommands.FUNCTION_PARAMS => previousCommand = FunctionCommands.FUNCTION_PARAMS; Option[OperationResult](OperationResult())
      case _ => None
    }
    if (result == None)
      result = validatePreviousCommand(recognized)
    println(currentFunction)
    result
  }

  def validatePreviousCommand(recognized: RecognitionResult): Option[OperationResult] = {
    val result: Option[OperationResult] = previousCommand match {
      case FunctionCommands.CREATE_FUNCTION => renameFunction(recognized)
      case FunctionCommands.FUNCTION_PARAMS => renameFunction(recognized)
      case _ => None
    }
    result
  }

  def createFunction(): Option[OperationResult] = {
    // TODO previous function validation
    currentFunction = new FunctionEntity()
    println("[FunctionHandler] function created")
    previousCommand = FunctionCommands.CREATE_FUNCTION
    Option(OperationResult())
  }

  def renameFunction(recognized: RecognitionResult): Option[OperationResult] = {
    if (!GrammarRegistry.isName(recognized))
      return None
    currentFunction.name = recognized.best.sentence
    previousCommand = FunctionCommands.RENAME
    Option(OperationResult())
  }

  def setParams(recognized: RecognitionResult): Option[OperationResult] = {

    Option(OperationResult())
  }
}

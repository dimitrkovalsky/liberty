package liberty.logic

import liberty.commands.{FunctionCommands, GeneralCommands}
import liberty.entities.{FunctionEntity, RecognitionResult}
import liberty.generic.{OperationResult, VoiceNotifier}
import liberty.helpers.GrammarRegistry

/**
 * User: dkovalskyi
 * Date: 15.07.13
 * Time: 16:08
 */
class FunctionHandler extends VoiceNotifier {
  var currentFunction: FunctionEntity = new FunctionEntity()
  var previousCommand = GeneralCommands.NO_COMMAND


  override def onRecognized(recognized: RecognitionResult): Option[OperationResult] = {
    println("[FunctionHandler] onRecognized : " + recognized.dictation)

    var result: Option[OperationResult] = recognized.label match {
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
    currentFunction.name = recognized.dictation
    previousCommand = FunctionCommands.RENAME
    Option(OperationResult())
  }

  def setParams(recognized: RecognitionResult): Option[OperationResult] = {

    Option(OperationResult())
  }
}

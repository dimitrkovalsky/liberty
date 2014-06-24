package liberty.generic

import liberty.entities.RecognitionResult

/**
 * User: dkovalskyi
 * Date: 15.07.13
 * Time: 16:06
 */
trait VoiceNotifier {
  def onRecognized(recognized: RecognitionResult): Option[OperationResult]
}

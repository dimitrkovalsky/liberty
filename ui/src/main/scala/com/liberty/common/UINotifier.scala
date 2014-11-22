package com.liberty.common

import com.liberty.handlers.IUIHandler

/**
 * Created by Maxxis on 11/8/2014.
 */
class UiNotifier(processAction: IUIHandler) extends Subscriber {

override protected def onActionReceived: Received = {
    case un: UserNotificationAction => processAction.onAction(un)
      Right("OK")
    case RecognizedAction(r) => processAction.onRecognized(r)
      Right("OK")
  }

  override protected def getSubscriptionTopics: List[String] = Topics.USER_NOTIFICATION :: Topics.RECOGNITION_NOTIFICATION :: Nil
}

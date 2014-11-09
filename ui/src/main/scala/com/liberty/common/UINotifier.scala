package com.liberty.common

/**
 * Created by Maxxis on 11/8/2014.
 */
class UiNotifier(processAction: (UserNotificationAction) => Unit) extends Subscriber {
  override protected def onActionReceived: Received = {
    case un: UserNotificationAction => processAction(un)
      Right("OK")
  }

  override protected def getSubscriptionTopics: List[String] = Topics.USER_NOTIFICATION :: Nil
}

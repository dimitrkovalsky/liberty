package com.liberty.common

/**
 * Created by Maxxis on 11/8/2014.
 */
class UINotifier(actionToUnit: (UserNotificationAction) => Unit) extends Subscriber {
  override protected def onActionReceived: Received = {
    case un: UserNotificationAction => actionToUnit(un)
      Right("OK")
  }

  override protected def getSubscriptionTopics: List[String] = Topics.USER_NOTIFICATION :: Nil
}

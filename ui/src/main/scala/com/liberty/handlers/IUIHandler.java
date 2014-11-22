package com.liberty.handlers;

import com.liberty.common.UserNotificationAction;
import com.liberty.entities.RecognitionResult;

/**
 * User: Maxxis
 * Date: 22.11.2014
 * Time: 14:28
 */
public interface IUIHandler {
    public void onAction(UserNotificationAction notification);
    public void onRecognized(RecognitionResult recognitionResult);
}

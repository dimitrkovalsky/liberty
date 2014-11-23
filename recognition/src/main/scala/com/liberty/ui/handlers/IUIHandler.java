package com.liberty.ui.handlers;

import com.liberty.common.UserNotificationAction;
import com.liberty.entities.RecognitionResult;
import com.liberty.model.JavaClass;

/**
 * User: Maxxis
 * Date: 22.11.2014
 * Time: 14:28
 */
public interface IUIHandler {
    public void onAction(UserNotificationAction notification);

    public void onRecognized(RecognitionResult recognitionResult);

    public void onClassChanged(JavaClass clazz);
}

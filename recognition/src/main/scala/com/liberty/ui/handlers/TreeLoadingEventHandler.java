package com.liberty.ui.handlers;

import com.liberty.ui.controllers.JavaController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * User: Maxxis
 * Date: 09.11.2014
 * Time: 13:38
 */
public class TreeLoadingEventHandler implements EventHandler<ActionEvent> {
    private JavaController controller;
    private Stage stage;

    public TreeLoadingEventHandler(JavaController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    @Override public void handle(ActionEvent t) {
        controller.scanProjectDirectory(stage);
    }
}

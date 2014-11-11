package com.liberty.handlers;

import com.liberty.controllers.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * User: Maxxis
 * Date: 09.11.2014
 * Time: 13:38
 */
public class TreeLoadingEventHandler implements EventHandler<ActionEvent> {
    private Controller controller;
    private Stage stage;

    public TreeLoadingEventHandler(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    @Override public void handle(ActionEvent t) {
        controller.scanProjectDirectory(stage);
    }
}

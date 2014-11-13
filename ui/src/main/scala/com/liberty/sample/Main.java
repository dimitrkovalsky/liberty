package com.liberty.sample;

import com.liberty.controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private static FXMLLoader loader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load the scene fxml UI.
        // grabs the UI scenegraph view from the loader.
        // grabs the UI controller for the view from the loader.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/uipattern.fxml"));
        this.loader = loader;
        Parent root = (Parent) loader.load();

        // continuously refresh the TreeItems.
        // demonstrates using controller methods to manipulate the controlled UI.
        Controller controller = loader.<Controller>getController();
        controller.scanProjectDirectory(primaryStage);
//        controller.setCodeStyleArea("");
//        Timeline timeline = new Timeline(
//                new KeyFrame(
//                        Duration.seconds(10),
//                        new TreeLoadingEventHandler(controller, primaryStage)
//                )
//        );
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();

        // initialize the stage.
        primaryStage.setScene(new Scene(root, 1280, 760));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    public static FXMLLoader getLoader() {
        return loader;
    }
}

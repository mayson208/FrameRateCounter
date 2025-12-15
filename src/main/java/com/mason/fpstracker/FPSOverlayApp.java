package com.mason.fpstracker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FPSOverlayApp extends Application {

    @Override
    public void start(Stage stage) {

        Label label = new Label("FPS Tracker Starting...");
        label.setTextFill(Color.LIME);

        VBox root = new VBox(label);
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        root.setSpacing(10);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setX(20);
        stage.setY(20);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

package com.mason.fpstracker;

import javafx.animation.AnimationTimer;
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

        FPSCounter fpsCounter = new FPSCounter();

        Label fpsLabel = new Label("FPS: 0");
        Label cpuLabel = new Label("CPU: 0%");
        Label ramLabel = new Label("RAM: 0 MB");

        fpsLabel.setTextFill(Color.LIME);
        cpuLabel.setTextFill(Color.LIME);
        ramLabel.setTextFill(Color.LIME);

        VBox root = new VBox(fpsLabel, cpuLabel, ramLabel);
        root.setSpacing(8);
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setX(20);
        stage.setY(20);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {

      
                fpsCounter.frame();
                fpsLabel.setText("FPS: " + fpsCounter.getFPS());

          
                double cpu = SystemStats.getCpuUsage();
                cpuLabel.setText(String.format("CPU: %.1f%%", cpu));

             
                double ram = SystemStats.getUsedMemoryMB();
                ramLabel.setText(String.format("RAM: %.0f MB", ram));
            }
        }.start();
    }

    public static void main(String[] args) {
        launch();
    }
}

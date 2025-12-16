package com.mason.fpstracker;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FPSOverlayApp extends Application {

    private boolean darkMode = true;
    private boolean visible = true;
    private double offsetX;
    private double offsetY;

    @Override
    public void start(Stage stage) {

        FPSCounter fpsCounter = new FPSCounter();

        Label fpsLabel = new Label("FPS: 0");
        Label cpuLabel = new Label("CPU: 0%");
        Label ramLabel = new Label("RAM: 0 MB");

        VBox root = new VBox(8, fpsLabel, cpuLabel, ramLabel);

        applyDarkMode(root, fpsLabel, cpuLabel, ramLabel);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setX(20);
        stage.setY(20);
        stage.show();

        root.setOnMousePressed(event -> {
            offsetX = event.getSceneX();
            offsetY = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - offsetX);
            stage.setY(event.getScreenY() - offsetY);
        });

        scene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.D) {
                darkMode = !darkMode;
                if (darkMode) {
                    applyDarkMode(root, fpsLabel, cpuLabel, ramLabel);
                } else {
                    applyLightMode(root, fpsLabel, cpuLabel, ramLabel);
                }
            }

            if (event.getCode() == KeyCode.H) {
                visible = !visible;
                root.setVisible(visible);
                root.setManaged(visible);
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                fpsCounter.frame();
                fpsLabel.setText("FPS: " + fpsCounter.getFPS());
                cpuLabel.setText(String.format("CPU: %.1f%%", SystemStats.getCpuUsage()));
                ramLabel.setText(String.format("RAM: %.0f MB", SystemStats.getUsedMemoryMB()));
            }
        }.start();
    }

    private void applyDarkMode(VBox root, Label... labels) {
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10; -fx-background-radius: 12;");
        for (Label label : labels) {
            label.setTextFill(Color.LIME);
        }
    }

    private void applyLightMode(VBox root, Label... labels) {
        root.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-padding: 10; -fx-background-radius: 12;");
        for (Label label : labels) {
            label.setTextFill(Color.BLACK);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

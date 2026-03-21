package com.mason.fpstracker;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class FPSOverlayApp extends Application {

    private boolean darkMode = true;
    private boolean visible = true;
    private double offsetX;
    private double offsetY;

    private final List<Integer> fpsHistory = new ArrayList<>();
    private static final int MAX_HISTORY = 60;

    // FPS thresholds for color coding
    private static final int FPS_GOOD = 60;
    private static final int FPS_OK   = 30;

    @Override
    public void start(Stage stage) {

        FPSCounter fpsCounter = new FPSCounter();

        Label titleLabel   = new Label("Performance Monitor");
        Label fpsLabel     = new Label("FPS: —");
        Label minMaxLabel  = new Label("MIN: —  MAX: —");
        Label avgFpsLabel  = new Label("AVG: —");
        Label frameLabel   = new Label("Frame: — ms");
        Label cpuLabel     = new Label("CPU:   —%");
        Label ramLabel     = new Label("RAM:   — / —");
        Label sessionLabel = new Label("Session: 0:00");
        Label hintLabel    = new Label("[D] theme  [H] hide  [R] reset stats");

        titleLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        fpsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        hintLabel.setStyle("-fx-font-size: 9px;");

        Canvas graphCanvas = new Canvas(190, 55);
        GraphicsContext gc = graphCanvas.getGraphicsContext2D();

        VBox root = new VBox(4,
                titleLabel,
                fpsLabel,
                minMaxLabel,
                avgFpsLabel,
                frameLabel,
                cpuLabel,
                ramLabel,
                sessionLabel,
                graphCanvas,
                hintLabel
        );

        applyTheme(root, darkMode, titleLabel, minMaxLabel, avgFpsLabel,
                   frameLabel, cpuLabel, ramLabel, sessionLabel, hintLabel);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setTitle("Performance Monitor");
        stage.setX(20);
        stage.setY(20);
        stage.show();

        root.setOnMousePressed(e -> { offsetX = e.getSceneX(); offsetY = e.getSceneY(); });
        root.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - offsetX);
            stage.setY(e.getScreenY() - offsetY);
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.D) {
                darkMode = !darkMode;
                applyTheme(root, darkMode, titleLabel, minMaxLabel, avgFpsLabel,
                           frameLabel, cpuLabel, ramLabel, sessionLabel, hintLabel);
            }
            if (e.getCode() == KeyCode.H) {
                visible = !visible;
                root.setVisible(visible);
                root.setManaged(visible);
            }
            if (e.getCode() == KeyCode.R) {
                fpsCounter.resetStats();
                fpsHistory.clear();
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                fpsCounter.frame();
                int fps = fpsCounter.getFPS();

                // Color-code FPS: green ≥ 60, yellow ≥ 30, red < 30
                Color fpsColor;
                if (fps >= FPS_GOOD)    fpsColor = darkMode ? Color.LIME      : Color.web("#22c55e");
                else if (fps >= FPS_OK) fpsColor = darkMode ? Color.YELLOW    : Color.web("#eab308");
                else                    fpsColor = darkMode ? Color.ORANGERED : Color.web("#ef4444");

                fpsLabel.setText("FPS: " + fps);
                fpsLabel.setTextFill(fpsColor);

                int mn = fpsCounter.getMinFPS();
                int mx = fpsCounter.getMaxFPS();
                minMaxLabel.setText("MIN: " + mn + "  MAX: " + mx);

                frameLabel.setText(String.format("Frame: %.2f ms", fpsCounter.getFrameTimeMs()));
                cpuLabel.setText(String.format("CPU:   %.1f%%", SystemStats.getCpuUsage()));

                double usedGB  = SystemStats.getUsedMemoryMB() / 1024.0;
                double totalGB = SystemStats.getTotalMemoryGB();
                ramLabel.setText(String.format("RAM:   %.1f / %.1fGB", usedGB, totalGB));

                long secs = fpsCounter.getSessionSeconds();
                sessionLabel.setText(String.format("Session: %d:%02d", secs / 60, secs % 60));

                updateHistory(fps);
                int avg = calculateAverage();
                avgFpsLabel.setText("AVG: " + avg + " fps");
                drawGraph(gc, graphCanvas.getWidth(), graphCanvas.getHeight(), avg);
            }
        }.start();
    }

    private void updateHistory(int fps) {
        if (fpsHistory.size() >= MAX_HISTORY) fpsHistory.remove(0);
        fpsHistory.add(fps);
    }

    private int calculateAverage() {
        if (fpsHistory.isEmpty()) return 0;
        return (int) fpsHistory.stream().mapToInt(i -> i).average().orElse(0);
    }

    private void drawGraph(GraphicsContext gc, double width, double height, int avg) {
        gc.clearRect(0, 0, width, height);
        if (fpsHistory.size() < 2) return;

        int maxFps = Math.max(FPS_GOOD, fpsHistory.stream().max(Integer::compareTo).orElse(FPS_GOOD));
        double xStep = width / (MAX_HISTORY - 1);

        // 60 fps baseline
        double baselineY = height - (FPS_GOOD / (double) maxFps) * height;
        gc.setStroke(darkMode ? Color.gray(0.35) : Color.gray(0.65));
        gc.setLineWidth(1);
        gc.setLineDashes(4, 4);
        gc.strokeLine(0, baselineY, width, baselineY);

        // Average line
        if (avg > 0) {
            double avgY = height - (avg / (double) maxFps) * height;
            gc.setStroke(darkMode ? Color.web("#89b4fa80") : Color.web("#3b82f680"));
            gc.setLineWidth(1);
            gc.setLineDashes(3, 3);
            gc.strokeLine(0, avgY, width, avgY);
        }

        gc.setLineDashes(); // clear dash pattern for main graph line
        gc.setLineWidth(1.8);
        for (int i = 1; i < fpsHistory.size(); i++) {
            int f1 = fpsHistory.get(i - 1);
            int f2 = fpsHistory.get(i);
            double x1 = (i - 1) * xStep;
            double y1 = height - (f1 / (double) maxFps) * height;
            double x2 = i * xStep;
            double y2 = height - (f2 / (double) maxFps) * height;

            Color seg = f2 >= FPS_GOOD
                    ? (darkMode ? Color.LIME           : Color.web("#22c55e"))
                    : f2 >= FPS_OK
                        ? (darkMode ? Color.YELLOW     : Color.web("#eab308"))
                        : (darkMode ? Color.ORANGERED  : Color.web("#ef4444"));
            gc.setStroke(seg);
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    private void applyTheme(VBox root, boolean dark, Label... labels) {
        if (dark) {
            root.setStyle("-fx-background-color: rgba(0,0,0,0.72); -fx-padding: 12; -fx-background-radius: 12;");
            for (Label l : labels) l.setTextFill(Color.web("#d4d4d4"));
        } else {
            root.setStyle("-fx-background-color: rgba(255,255,255,0.92); -fx-padding: 12; -fx-background-radius: 12;");
            for (Label l : labels) l.setTextFill(Color.web("#1a1a1a"));
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

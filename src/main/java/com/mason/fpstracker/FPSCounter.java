package com.mason.fpstracker;

public class FPSCounter {

    private int frames = 0;
    private int fps = 0;
    private long lastTime = System.nanoTime();
    private long lastFrameTime = System.nanoTime();
    private double frameTimeMs = 0;

    public void frame() {
        long now = System.nanoTime();

        frameTimeMs = (now - lastFrameTime) / 1_000_000.0;
        lastFrameTime = now;

        frames++;

        if (now - lastTime >= 1_000_000_000L) {
            fps = frames;
            frames = 0;
            lastTime = now;
        }
    }

    public int getFPS() {
        return fps;
    }

    public double getFrameTimeMs() {
        return frameTimeMs;
    }
}

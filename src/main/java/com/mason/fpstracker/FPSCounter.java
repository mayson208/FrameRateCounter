package com.mason.fpstracker;

public class FPSCounter {

    private int frames = 0;
    private int fps = 0;
    private long lastTime = System.nanoTime();
    private long lastFrameTime = System.nanoTime();
    private double frameTimeMs = 0;

    private int minFps = Integer.MAX_VALUE;
    private int maxFps = 0;
    private final long sessionStart = System.nanoTime();

    public void frame() {
        long now = System.nanoTime();

        frameTimeMs = (now - lastFrameTime) / 1_000_000.0;
        lastFrameTime = now;

        frames++;

        if (now - lastTime >= 1_000_000_000L) {
            fps = frames;
            frames = 0;
            lastTime = now;

            if (fps > 0) {
                if (fps < minFps) minFps = fps;
                if (fps > maxFps) maxFps = fps;
            }
        }
    }

    public int getFPS() { return fps; }

    public double getFrameTimeMs() { return frameTimeMs; }

    public int getMinFPS() { return minFps == Integer.MAX_VALUE ? 0 : minFps; }

    public int getMaxFPS() { return maxFps; }

    /** Elapsed session time in whole seconds. */
    public long getSessionSeconds() {
        return (System.nanoTime() - sessionStart) / 1_000_000_000L;
    }

    public void resetStats() {
        minFps = Integer.MAX_VALUE;
        maxFps = 0;
    }
}

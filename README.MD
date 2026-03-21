# FPS & Performance Monitor

A transparent always-on-top JavaFX overlay that tracks FPS, CPU, RAM, and session stats in real time — with a live colour-coded graph.

![Java](https://img.shields.io/badge/Java-21-blue) ![JavaFX](https://img.shields.io/badge/JavaFX-21-orange)

## Features

- **Live FPS** with green / yellow / red colour coding (≥60 / ≥30 / <30)
- **Min, Max, and Rolling Average** FPS tracked across the session
- **Frame time** in milliseconds
- **CPU usage** via `OperatingSystemMXBean`
- **RAM** — used / total in GB
- **Session timer** — elapsed time displayed as MM:SS
- **Live graph** with per-segment colour coding, 60fps baseline, and dashed average line
- **Dark / Light theme** toggle with `[D]`
- **Hide / Show** with `[H]`
- **Reset stats** (min/max/history) with `[R]`
- **Drag to reposition** anywhere on screen
- Transparent borderless window, always on top

## Running

```bash
mvn javafx:run
```

## Requirements

- Java 21+
- Maven 3.9+

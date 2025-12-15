# FPS Tracker Overlay

A lightweight JavaFX desktop overlay that displays real-time performance metrics, including FPS, CPU usage, and memory usage.  
Designed as a simple always-on-top HUD with a clean interface and minimal setup.

## Features
- Real-time FPS tracking using a JavaFX animation loop
- Live CPU usage monitoring
- Live RAM usage display
- Always-on-top transparent overlay window
- Draggable window positioning
- Runtime dark and light mode toggle

## Tech Stack
- Java 21
- JavaFX
- Maven

## Controls
- Click and drag anywhere on the overlay to move it
- Press **D** to toggle between dark and light mode

## Notes
- FPS represents the frame rate of the overlay itself, not external games or applications
- The project does not hook into other processes or system graphics APIs

## How to Run
```bash
mvn clean javafx:run

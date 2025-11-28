package com.comp2042.data;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Timer {

    private static int totalSeconds = 0;   // total time in seconds
    private static int remainingSeconds; // time left
    private static boolean running = false;

    private static Label displayLabel; // optional, updates UI

    public static void setTimer(int minutes) {
        totalSeconds = minutes * 60;
        remainingSeconds = totalSeconds;
    }

    // optional: pass a label to automatically update UI
    public static void setDisplayLabel(Label label, VBox vBox) {
        if (totalSeconds == 0)
            vBox.setVisible(false);
        else {
            displayLabel = label;
            updateLabel();
        }
    }

    public static void start() {
        if (running) return;

        running = true;

        Thread timerThread = new Thread(() -> {
            try {
                while (remainingSeconds > 0) {
                    Thread.sleep(1000);

                    if (running) {  // only count down when not paused
                        remainingSeconds--;
                        updateLabel();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        timerThread.setDaemon(true);
        timerThread.start();
    }


    public static void pause(boolean isPause) {
        running = !isPause;
    }

    public static void reset() {
        if (totalSeconds == 0){
            return;
        }
        running = false;
        remainingSeconds = totalSeconds;
        updateLabel();
    }

    private static String getFormattedTime() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private static void updateLabel() {
        if (displayLabel != null) {
            Platform.runLater(() -> displayLabel.setText(getFormattedTime()));
        }
    }

    public static void stop() {
        running = false;
        displayLabel = null;    // prevent updating old UI label
    }

    public static int getTotalSeconds(){
        return totalSeconds;
    }

}

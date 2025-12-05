package com.comp2042.data;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Manages a countdown timer for timed game modes.
 *
 * <p>The {@code Timer} class provides functionality for setting, starting, pausing,
 * resetting, and stopping a countdown timer. It can also display the remaining
 * time in a JavaFX {@link Label} and updates the UI in a thread-safe manner
 * using {@link Platform#runLater}.</p>
 *
 * <p>This class is responsible for:</p>
 * <ul>
 *     <li>Setting the timer duration</li>
 *     <li>Starting the timer</li>
 *     <li>Pausing and resuming the timer</li>
 *     <li>Resetting the timer</li>
 *     <li>Stopping the timer</li>
 * </ul>
 *
 * <p>All fields and methods are static, as only one global timer is active
 * at any given time.</p>
 */

public class Timer {

    /** The start time in seconds */
    private static int startingTime = 0;

    /** The remaining time in seconds */
    private static int remainingTime;

    /** Indicates whether the timer is currently running. */
    private static boolean running = false;

    /** The JavaFX label used to display the remaining time. */
    private static Label label;

    /**
     * Sets the timer duration in minutes.
     *
     * @param minutes the countdown duration in minutes
     */
    public static void setTimer(int minutes) {
        startingTime = minutes * 60;
        remainingTime = startingTime;
    }

    /**
     * Assigns a JavaFX label to display the remaining time.
     *
     * <p>If the timer is set to 0 minutes, the {@link VBox} will be hidden.</p>
     *
     * @param label the label to update with remaining time
     * @param vBox the container holding the label
     */
    public static void setLabel(Label label, VBox vBox) {
        if (startingTime == 0)
            vBox.setVisible(false);
        else {
            Timer.label = label;
            updateLabel();
        }
    }

    /**
     * Starts the countdown timer in a background thread.
     *
     * <p>The timer decrements every second and updates the assigned display label.</p>
     */
    public static void start() {
        if (running) return;

        running = true;

        Thread timerThread = new Thread(() -> {
            try {
                while (remainingTime > 0) {
                    Thread.sleep(1000);

                    if (running) {  // only count down when not paused
                        remainingTime--;
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

    /**
     * Pauses or resumes the timer.
     *
     * @param isPause true to pause the timer, false to resume
     */
    public static void pause(boolean isPause) {
        running = !isPause;
    }

    /**
     * Resets the timer to its original duration.
     *
     * <p>If the timer duration is zero, this method does nothing.</p>
     */
    public static void reset() {
        if (startingTime == 0){
            return;
        }
        running = false;
        remainingTime = startingTime;
        updateLabel();
    }

    /**
     * Returns the remaining time formatted as "MM:SS".
     *
     * @return the formatted remaining time string
     */
    private static String getFormattedTime() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Updates the display label with the current remaining time.
     */
    private static void updateLabel() {
        if (label != null) {
            Platform.runLater(() -> label.setText(getFormattedTime()));
        }
    }

    /**
     * Stops the timer and clears the assigned display label.
     */
    public static void stop() {
        running = false;
        label = null;    // prevent updating old UI label
    }

    /**
     * Returns the total duration of the timer in seconds.
     *
     * <p>Used in {@link Score} to determine the correct save line</p>
     *
     * @return the total timer duration in seconds
     */
    public static int getStartingTime(){
        return startingTime;
    }

    /**
     * Returns the remaining seconds left in the countdown.
     *<p>Primarily used for testing</p>
     *
     * @return the remaining seconds
     */
    public static int getRemainingSeconds(){
        return remainingTime;
    }

}

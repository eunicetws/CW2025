package com.comp2042.data;

import com.comp2042.enums.SaveDataType;
import com.comp2042.logic.Timer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;

/**
 * Tracks the player's current score and handles automatic saving of high scores.
 *
 * <p>This class tracks the player's score using a JavaFX {@link IntegerProperty},
 * allowing UI components to bind to the score and update automatically whenever
 * the value changes.</p>
 *
 * <p>Whenever the score changes, the class checks whether it exceeds the stored
 * high score for the current timer setting and updates the save file accordingly.</p>
 */

public final class Score {

    /**
     * Initialises the score in the current game session to 0.
     * This property supports JavaFX property binding for automatic UI updates.
     */
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Returns the score.
     *
     * <p>Used for binding UI components so the display updates automatically when the count changes.</p>
     *
     * @return the {@code IntegerProperty} storing the score
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Increases the total scores by the specified amount.
     * <p>
     *     Increases the score by the specified amount and save the scores if it is higher than the HighScore.
     * </p>
     *
     * @param i the number of points to add
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
        saveScore();
    }

    /**
     * Resets the score to zero.
     * <p>
     *     Save the scores if it is higher than the HighScore and rest the score.
     * </p>
     */
    public void reset() {
        score.setValue(0);
        saveScore();
    }

    /**
     * Saves the score if it surpasses the existing high score for the
     * currently selected timer mode.
     *
     * <p>The correct saved line of the high score is chosen based on {@link Timer#getStartingTime()}.
     * The HighScore is saved using {@link SaveData} and the line number is assigned based on {@link SaveDataType}</p>
     *
     * <p>If an I/O error occurs while reading or writing save data,
     * a {@link RuntimeException} is thrown.</p>
     */
    private void saveScore() {

        try {
            int minutes = Timer.getStartingTime() / 60;

            switch (minutes) {
                case 0 -> { // No timer
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(SaveDataType.HIGHSCORE))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(SaveDataType.HIGHSCORE));
                    }
                }
                case 5 -> {
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(SaveDataType.HIGHSCORE_5))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(SaveDataType.HIGHSCORE_5));
                    }
                }
                case 10 -> {
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(SaveDataType.HIGHSCORE_10))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(SaveDataType.HIGHSCORE_10));
                    }
                }
                case 15 -> {
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(SaveDataType.HIGHSCORE_15))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(SaveDataType.HIGHSCORE_15));
                    }
                }
                case 20 -> {
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(SaveDataType.HIGHSCORE_20))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(SaveDataType.HIGHSCORE_20));
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }
    }

package com.comp2042.data;

import com.comp2042.enums.KeyEventType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void add(int i){
        score.setValue(score.getValue() + i);
        saveScore();
    }

    public void reset() {
        score.setValue(0);
        saveScore();
    }

    private void saveScore() {

        try {
            int minutes = Timer.getTotalSeconds() / 60;

            switch (minutes) {
                case 0 -> { // No timer
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(KeyEventType.HIGHSCORE))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(KeyEventType.HIGHSCORE));
                    }
                }
                case 5 -> {
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(KeyEventType.HIGHSCORE_5))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(KeyEventType.HIGHSCORE_5));
                    }
                }
                case 10 -> {
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(KeyEventType.HIGHSCORE_10))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(KeyEventType.HIGHSCORE_10));
                    }
                }
                case 15 -> {
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(KeyEventType.HIGHSCORE_15))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(KeyEventType.HIGHSCORE_15));
                    }
                }
                case 20 -> {
                    if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(KeyEventType.HIGHSCORE_20))) {
                        SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(KeyEventType.HIGHSCORE_20));
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }
    }

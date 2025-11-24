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

    private void saveScore(){
        try {
            if (score.getValue() > SaveData.ReadFileInt(SaveData.getKeyEvent(KeyEventType.HIGHSCORE)))
                SaveData.overWriteFile(score.getValue(), SaveData.getKeyEvent(KeyEventType.HIGHSCORE));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

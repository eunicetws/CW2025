package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class LinesCleared {

    private final IntegerProperty totalLinesCleared = new SimpleIntegerProperty(0);

    public IntegerProperty PropertiesTotalLinesCleared() {
        return totalLinesCleared;
    }

    public void add(int i){
        totalLinesCleared.setValue(totalLinesCleared.getValue() + i);
    }

    public void reset() {
        totalLinesCleared.setValue(0);
    }
}

package com.comp2042.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Tracks the total number of lines cleared by the player during a game.
 *
 * <p>This class uses a JavaFX {@link IntegerProperty} so the value can be bound
 * directly to UI elements, allowing the display to update automatically whenever
 * the number of cleared lines changes.</p>
 */
public final class LinesCleared {

    /**
     * Initialises the total number of lines cleared in the current game session to 0.
     * This property supports JavaFX bindings for automatic UI updates.
     */
    private final IntegerProperty totalLinesCleared = new SimpleIntegerProperty(0);

    /**
     * Returns the total number of cleared lines.
     *
     * <p>Used for binding UI components so the display updates automatically when the count changes.</p>
     *
     * @return the {@code IntegerProperty} storing the total lines cleared
     */
    public IntegerProperty PropertiesTotalLinesCleared() {
        return totalLinesCleared;
    }

    /**
     * Increases the total number of cleared lines by the specified amount.
     *
     * @param i the number of lines to add
     */
    public void add(int i){
        totalLinesCleared.setValue(totalLinesCleared.getValue() + i);
    }

    /**
     * Resets the line counter to zero.
     *
     * <p>This is typically called at the start of a new game session.</p>
     */
    public void reset() {
        totalLinesCleared.setValue(0);
    }
}

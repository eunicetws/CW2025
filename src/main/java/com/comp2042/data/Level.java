package com.comp2042.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Tracks the player's level and handles level progression logic during the game.
 *
 * <p>
 * This class tracks the player's level using a JavaFX {@link IntegerProperty},
 *  so the value can be bound to the level UI and update automatically whenever
 * the value changes.
 * </p>
 * <p>
 *     It is responsible for:
 * </p>
 *     <ul>
 *          <li>Storing the current level</li>
 *          <li>Determining the level requirement</li>
 *          <li>Increasing or resetting the level</li>
 *          <li>Calculating the speed of the game</li>
 *      </ul>
 */


public final class Level {

    /**
     * Initialises the level in the current game session to 1.
     * This property supports JavaFX property binding for automatic UI updates.
     */
    private final IntegerProperty level = new SimpleIntegerProperty(1);

    /**
     * Returns the current level.
     *
     * <p>Used for binding UI components so the display updates automatically when the count changes.</p>
     *
     * @return the {@code IntegerProperty} storing the level
     */
    public IntegerProperty propertiesLevel() {
        return level;
    }

    /**
     * Increases the level by 1.
     */
    public void levelUp(){
        level.setValue(level.getValue() + 1);
    }

    /**
     * Resets the level to one.
     *
     * <p>This is typically called at the start of a new game session.</p>
     */
    public void reset() {
        level.setValue(1);
    }

    /**
     * Checks if the number of cleared lines meets the requirement to level up.
     * <p>
     * The total cleared line required to level up increases by 10 each time the player levels ups.
     * This is calculated using the mathematical formula: lines >= (5 * (level^2)) + (5 * level)
     * The maximum level is 20.
     * </p>
     *
     * @param lines the total number of cleared lines
     * @return {@code true} if the player can level up; {@code false} otherwise
     */
    public boolean reachLevelRequirement(int lines){
        int level = this.level.getValue()-1;
        return lines >= (5 * (level * level)) + (5 * level) && level != 20;
            // Cleared lines >= level Up Requirement and stop increasing after lvl20
    }

    /**
     * Calculates the speed of the game.
     * <p>
     * The speed starts around 400ms and decreases to approximately 90ms at level 20.
     * </p>
     *
     * @return the fall speed in milliseconds
     */
    public int calcSpeed(){
        int x = this.level.getValue() - 1;
        return (int) (1065.0 / (x + 3) + 45);
    }
}

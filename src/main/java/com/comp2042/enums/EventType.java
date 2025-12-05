package com.comp2042.enums;

/**
 * Represents the different types of user input events in the game.
 * <p>
 *      Event Types include:
 * </p>
 * <ul>
 *     <li>{@code DOWN} : Move the current piece downward.</li>
 *     <li>{@code LEFT} : Move the current piece to the left.</li>
 *     <li>{@code RIGHT} : Move the current piece to the right.</li>
 *     <li>{@code ROTATE} : Rotate the current piece.</li>
 *     <li>{@code HOLD} : Hold the current piece for later use.</li>
 * </ul>
 */

public enum EventType {
    DOWN, LEFT, RIGHT, ROTATE, HOLD
}

package com.comp2042.enums;

/**
 * Represents the different types of data that can be saved and loaded in the game.
 * <p>
 *  Each enum corresponds to a specific line number defined in {@link com.comp2042.data.SaveData}.
 *</p>
 *  <ul>
 *     <li>
 *         {@code HIGHSCORE} : The highest score for no timer.
 *     </li>
 *     <li>
 *         {@code HIGHSCORE_5} : High score for the timer of 5-minute mode.
 *     </li>
 *     <li>
 *         {@code HIGHSCORE_10} : High score for the timer of 10-minute.
 *     </li>
 *     <li>
 *         {@code HIGHSCORE_15} : High score for the timer of 15-minute mode.
 *     </li>
 *     <li>
 *         {@code HIGHSCORE_20} : High score for the timer of 20-minute mode.
 *     </li>
 *     <li>
 *         {@code LEFT} : Keyboard shortcut for moving left.
 *     </li>
 *     <li>
 *         {@code RIGHT} : Keyboard shortcut for moving right.
 *     </li>
 *     <li>
 *         {@code DOWN} : Keyboard shortcut for moving down.
 *     </li>
 *     <li>
 *         {@code ROTATE} : Keyboard shortcut for rotating pieces.
 *     </li>
 *     <li>
 *         {@code HOLD} : Keyboard shortcut for holding a piece.
 *     </li>
 *     <li>
 *         {@code PAUSE} : Keyboard shortcut for pausing the game.
 *     </li>
 *     <li>
 *         {@code RESTART} : Keyboard shortcut for restarting the game.
 *     </li>
 *     <li>
 *         {@code HARDDROP} : Keyboard shortcut for hard dropping a piece.
 *     </li>
 *     <li>
 *         {@code MUSIC} : Volume setting for the background music.
 *     </li>
 *     <li>
 *         {@code BUTTONS} : Paths and volume setting for button sounds.
 *     </li>
 *     <li>
 *         {@code CLEARLINES} : Volume setting for line clear sound.
 *     </li>
 *     <li>
 *         {@code TOGGLE_HOLD} : Boolean setting to show/hide hold piece.
 *     </li>
 *     <li>
 *         {@code TOGGLE_NEXT} : Boolean setting to show/hide next piece preview.
 *     </li>
 *     <li>
 *         {@code TOGGLE_GHOST} : Boolean setting to show/hide ghost piece.
 *     </li>
 *     <li>
 *         {@code TOGGLE_CONTROLS} : Boolean setting to show/hide keyboard controls.
 *     </li>
 * </ul>
 */

public enum SaveDataType {
    HIGHSCORE,
    LEFT,
    RIGHT,
    DOWN,
    ROTATE,
    HOLD,
    PAUSE,
    RESTART,
    HARDDROP,
    MUSIC,
    BUTTONS,
    CLEARLINES,
    TOGGLE_HOLD,
    TOGGLE_NEXT,
    TOGGLE_GHOST,
    HIGHSCORE_5,
    HIGHSCORE_10,
    HIGHSCORE_15,
    HIGHSCORE_20,
    TOGGLE_CONTROLS
}

package com.comp2042.enums;

/**
 * Represents the source of an event in the game.
 * <p>
 *     Event Source includes:
 * </p>
 * <ul>
 *     <li>
 *         {@code USER} indicates that the event was triggered by the player.
 *     </li>
 *     <li>
 *         {@code THREAD} indicates that the event was triggered by the system.
 *     </li>
 * </ul>
 */
public enum EventSource {
    USER, THREAD
}

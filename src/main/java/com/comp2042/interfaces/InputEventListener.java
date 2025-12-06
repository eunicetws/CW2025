package com.comp2042.interfaces;

import com.comp2042.data.DownData;
import com.comp2042.data.MoveEvent;
import com.comp2042.view.ViewData;

/**
 * This interface handles the player input events and translates them into game actions.
 * <p>
 * The implementations from this interface process movement, rotation,
 * holding, and game-reset actions, updating the game state and returning
 * the appropriate response data for rendering or logic updates.
 * </p>
 */
public interface InputEventListener {

    /**
     * Handles a downward movement event, including soft drop or hard drop.
     *
     * @param event the down movement event
     * @param isHardDrop {@code true} if this action is a hard drop; otherwise {@code false}
     * @return a {@link DownData} object containing updated board and scoring information
     */
    DownData onDownEvent(MoveEvent event, boolean isHardDrop);

    /**
     * Handles left movement event.
     *
     * @param event the left movement event
     * @return {@link ViewData} object reflecting the updated state of the board
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles right movement event.
     *
     * @param event the right movement event
     * @return {@link ViewData} object showing the new position of the brick
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles rotation event.
     *
     * @param event the rotation event
     * @return {@link ViewData} representing the rotated state of the brick
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles hold-brick action.
     *
     * @param event the hold event
     * @return {@link ViewData} reflect the hold and swapped brick
     */
    ViewData onHoldEvent(MoveEvent event);

    /**
     * Starts a new game and resets the board state.
     */
    void createNewGame();
}

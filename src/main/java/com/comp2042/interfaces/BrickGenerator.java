package com.comp2042.interfaces;

/**
 * This interface provides methods to get a new active brick for the game.
 * <p>
 * This implementation of this interface manages the creation of the current and upcoming bricks
 * </p>
 */
public interface BrickGenerator {

    /**
     * Returns the current brick to be placed on the board.
     *
     * @return the current piece data
     */
    Brick getBrick();

    /**
     * Returns the next brick that will appear after the current one.
     *
     * @return the next piece data
     */
    Brick getNextBrick();
}

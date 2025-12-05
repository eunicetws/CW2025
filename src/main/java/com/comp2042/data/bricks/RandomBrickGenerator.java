package com.comp2042.data.bricks;

import com.comp2042.interfaces.Brick;
import com.comp2042.interfaces.BrickGenerator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Generates new bricks using 7-bag randomization system.
 * <p>
 * This method ensures that all seven brick types appear only once per bag,
 * preventing long streaks where a certain brick never appears. It
 * shuffles a new bag when the current bag is almost emptied and adds it to the queue.
 * </p>
 */
public class RandomBrickGenerator implements BrickGenerator {

    /** The list containing all seven Tetris brick types. */
    private final List<Brick> brickList;
    /** The queue of upcoming bricks */
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Creates a new {@code RandomBrickGenerator} and initializes the queue
     * with two shuffled bags to ensure multiple upcoming previews.
     */
    public RandomBrickGenerator() {
        brickList = List.of(
                new IBrick(), new JBrick(), new LBrick(),
                new OBrick(), new SBrick(), new TBrick(), new ZBrick()
        );

        refillBag();
        refillBag(); // Fill the queue enough for next-brick previews
    }

    private void refillBag() {
        List<Brick> bag = new ArrayList<>(brickList);
        Collections.shuffle(bag);
        nextBricks.addAll(bag);
    }

    /**
     * Retrieves and removes the next brick from the queue.
     * <p>
     * If the queue has less than 7 bricks remaining, a new bag is added to
     * ensure continuous randomization.
     * </p>
     *
     * @return the next {@link Brick} to be placed into the game
     */
    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 7) { // when low, refill again
            refillBag();
        }
        return nextBricks.poll();
    }
    /**
     * Retrieves the data of the next brick.
     * <p>
     * This is used in {@link com.comp2042.logic.SimpleBoard} to check the next brick data
     * </p>
     *
     * @return the next {@link Brick} in the queue, or {@code null} if empty
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}

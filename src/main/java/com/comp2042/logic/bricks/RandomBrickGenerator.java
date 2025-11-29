package com.comp2042.logic.bricks;

import com.comp2042.interfaces.Brick;
import com.comp2042.interfaces.BrickGenerator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = List.of(
                new IBrick(), new JBrick(), new LBrick(),
                new OBrick(), new SBrick(), new TBrick(), new ZBrick()
        );

        refillBag();
        refillBag(); // fill 2 previews
    }

    private void refillBag() {
        List<Brick> bag = new ArrayList<>(brickList);
        Collections.shuffle(bag);
        nextBricks.addAll(bag);
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 7) { // when low, refill again
            refillBag();
        }
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}

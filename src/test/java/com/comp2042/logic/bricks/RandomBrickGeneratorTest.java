package com.comp2042.logic.bricks;

import com.comp2042.data.bricks.RandomBrickGenerator;
import com.comp2042.interfaces.Brick;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    @Test
    void testBagContainsAllSevenUniqueBricks() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        // Draw 7 bricks = one full bag
        Set<Class<?>> seen = new HashSet<>();
        for (int i = 0; i < 7; i++) {
            Brick b = generator.getBrick();
            seen.add(b.getClass());
        }

        assertEquals(7, seen.size(), "Bag should contain 7 unique brick types");
    }

    @Test
    void testNoDuplicatesWithinSingleBag() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        Set<Class<?>> seen = new HashSet<>();
        for (int i = 0; i < 7; i++) {
            Brick b = generator.getBrick();
            assertFalse(seen.contains(b.getClass()),
                    "Duplicate brick found inside a single 7-bag");
            seen.add(b.getClass());
        }
    }

    @Test
    void testNextBrickMatchesGetBrick() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        Brick next = generator.getNextBrick();
        Brick actual = generator.getBrick();

        assertEquals(next.getClass(), actual.getClass(),
                "`getNextBrick()` should preview the next brick to be returned.");
    }

    @Test
    void testManyBricksStillValid() {
        RandomBrickGenerator generator = new RandomBrickGenerator();

        for (int i = 0; i < 200; i++) {
            Brick b = generator.getBrick();
            assertNotNull(b, "Brick should never be null");
        }
    }
}

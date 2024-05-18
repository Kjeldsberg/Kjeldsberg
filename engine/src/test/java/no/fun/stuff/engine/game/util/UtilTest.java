package no.fun.stuff.engine.game.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @Test
    void testRand() {
        float min = 0.5f;
        float max = 1.5f;
        assertTrue(checkBetween(Util.rand(min, max), min, max));
        assertTrue(checkBetween(Util.rand(min, max), min, max));
        assertTrue(checkBetween(Util.rand(min, max), min, max));
        assertTrue(checkBetween(Util.rand(min, max), min, max));
        assertTrue(checkBetween(Util.rand(min, max), min, max));
    }
    private boolean checkBetween(float value, float min, float max) {
        System.out.println("Value: " + value);
        return !(value < min || value > max);
    }

}
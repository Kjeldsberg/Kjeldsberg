package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SATTest {
    @Test
    void test() {
        final Vector2D[] objA = new Vector2D[] {new Vector2D(0f, 2f), new Vector2D(-2f, 0f), new Vector2D(0f, -2f)};
        final Vector2D[] objb = new Vector2D[] {new Vector2D(1f, 1f), new Vector2D(-1f, 0f), new Vector2D(1f, -1f)};
        SAT sat = new SAT();
        CollisionInfo collide = sat.collide(objA, objb);
        assertTrue(collide.isCollide());
        assertTrue(Util.compare(collide.getDepth(), 1f));

    }
    @Test
    void noCollide() {
        final Vector2D[] objA2 = new Vector2D[] {new Vector2D(-1f, 0f), new Vector2D(-1f, -1f), new Vector2D(0f, -1f)};
        final Vector2D[] objb2 = new Vector2D[] {new Vector2D(1f, 0f), new Vector2D(1f, -1f), new Vector2D(2f, 0f)};
        SAT sat = new SAT();
        CollisionInfo collide = sat.collide(objA2, objb2);
        assertFalse(collide.isCollide());
    }
}
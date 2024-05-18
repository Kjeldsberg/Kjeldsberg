package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.NewRectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AABBTest {

    @Test
    void collide() {
        NewRectangle rectangle = new NewRectangle(2f, 2f);
        NewRectangle rectangle2 = new NewRectangle(2f, 2f);
        assertTrue(AABB.collide(rectangle2, rectangle));
        rectangle.moveTo(1f, 0f);
        assertTrue(AABB.collide(rectangle2, rectangle));
        rectangle.moveTo(2f, 0f);
        assertFalse(AABB.collide(rectangle2, rectangle));

        rectangle.moveTo(0f, 1f);
        assertTrue(AABB.collide(rectangle2, rectangle));
        rectangle.moveTo(0f, 2f);
        assertFalse(AABB.collide(rectangle2, rectangle));

    }
}
package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.NewRectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AABBTest {

    @Test
    void collide() {
        Collision collision = new Collision();
        NewRectangle rectangle = new NewRectangle(2f, 2f);
        NewRectangle rectangle2 = new NewRectangle(2f, 2f);
        assertTrue(BoundingBox.intersectBoundingBoxes(rectangle2.getBoundingBox(), rectangle.getBoundingBox()));
        rectangle.moveTo(1f, 0f);
        assertTrue(BoundingBox.intersectBoundingBoxes(rectangle2.getBoundingBox(), rectangle.getBoundingBox()));
        rectangle.moveTo(2f, 0f);
        assertFalse(BoundingBox.intersectBoundingBoxes(rectangle2.getBoundingBox(), rectangle.getBoundingBox()));

        rectangle.moveTo(0f, 1f);
        assertTrue(BoundingBox.intersectBoundingBoxes(rectangle2.getBoundingBox(), rectangle.getBoundingBox()));
        rectangle.moveTo(0f, 2f);
        assertFalse(BoundingBox.intersectBoundingBoxes(rectangle2.getBoundingBox(), rectangle.getBoundingBox()));

    }
}
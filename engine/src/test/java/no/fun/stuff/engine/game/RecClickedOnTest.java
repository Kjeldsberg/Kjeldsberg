package no.fun.stuff.engine.game;

import no.fun.stuff.engine.game.objects.NewRectangle;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecClickedOnTest {
    private final Matrix3x3 m = new Matrix3x3();
    @Test
    void clickedOn() {
        NewRectangle rectangle = new NewRectangle(1f, 1f);
        RecClickedOn rectangleClickedOn = new RecClickedOn(rectangle);
        boolean b = rectangleClickedOn.clickedOn(new Vector2D(0.1f, 0.1f), m);
        assertFalse(rectangleClickedOn.clickedOn(new Vector2D(0.5f, -0.4f), m));
        assertTrue(b);

    }
    @Test
    void notClickedOn() {
        NewRectangle rectangle = new NewRectangle(1f, 1f);
        RecClickedOn rectangleClickedOn = new RecClickedOn(rectangle);
        assertFalse(rectangleClickedOn.clickedOn(new Vector2D(2f, 0f), m));
        assertFalse(rectangleClickedOn.clickedOn(new Vector2D(0f, 2f), m));
        assertFalse(rectangleClickedOn.clickedOn(new Vector2D(-2f, 0f), m));
        assertFalse(rectangleClickedOn.clickedOn(new Vector2D(0f, -2f), m));
        assertFalse(rectangleClickedOn.clickedOn(new Vector2D(0.5f, -0.5f), m));

    }
}
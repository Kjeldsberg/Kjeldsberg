package no.fun.stuff.engine.game;

import no.fun.stuff.engine.game.objects.Triangle;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriangleClickedOnTest {

    public static final Triangle TRIANGLE = new Triangle(new Vector2D(1f, 1f),
            new Vector2D(-1, -1),
            new Vector2D(1, -1), 0);
    static {
        Vector2D[] localCoordinate = TRIANGLE.toWorldCoordinate();
        Vector2D localCenter = TRIANGLE.getLocalCenter();
        Vector2D minus = new Vector2D().minus(localCenter);
        for(Vector2D v : localCoordinate) {
            v.sub(minus);
        }
    }
    @Test
    void insideTest() {
        TriangleClickedOn triangleClickedOn = new TriangleClickedOn(TRIANGLE);
        boolean b = triangleClickedOn.clickedOn(new Vector2D(0.5f, -0.5f), new Matrix3x3());
        assertTrue(b);

    }
    @Test
    void outsideTest() {
        TriangleClickedOn triangleClickedOn = new TriangleClickedOn(TRIANGLE);

        boolean c = triangleClickedOn.clickedOn(new Vector2D(-0.5f, -0.5f), new Matrix3x3());
        assertFalse(c);

    }

}
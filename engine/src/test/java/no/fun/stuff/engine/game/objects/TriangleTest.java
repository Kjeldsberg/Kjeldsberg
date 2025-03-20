package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    @Test
    void test() {
        final Vector2D[] p = {new Vector2D(1f, 3f), new Vector2D(1f, 1f), new Vector2D(3f, 1f)};
        final Vector2D[] pp = {new Vector2D(1f, 3f), new Vector2D(1f, 1f), new Vector2D(3f, 1f)};
        final Vector2D[] ppp = {new Vector2D(1f, 3f), new Vector2D(1f, 1f), new Vector2D(3f, 1f)};
        Vector2D center = Triangle.getCenter(p);
        Triangle triangle = new Triangle(p[0], p[1], p[2], 0xff112233);
        triangle.calculateModel();
        triangle.getModel().mul(p, pp);
        triangle.moveTo(center);
        triangle.calculateModel();
        triangle.getModel().mul(p, ppp);
        int est = 0;

    }
}
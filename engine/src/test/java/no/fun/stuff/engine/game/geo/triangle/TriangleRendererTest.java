package no.fun.stuff.engine.game.geo.triangle;

import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriangleRendererTest {

    @Test
    void rotateToLowestY() {

        TriangleRenderer triangleRenderer = new TriangleRenderer(null);
        Vector2D v1 = new Vector2D(0, 1f);
        Vector2D v0 = new Vector2D(0, 0f);
        Vector2D v3 = new Vector2D(0, 3f);
        Vector2D[] test = new Vector2D[] {v0, v1, v3};
        Vector2D[] vector2DS = triangleRenderer.rotateToLowestY(test);
        for(int i=0;i<test.length;i++) {
            assertEquals(test[i], vector2DS[i]);
        }
        Vector2D[] test2 = new Vector2D[] {v1, v0, v3};
        Vector2D[] vector2DS2 = triangleRenderer.rotateToLowestY(test2);
        assertEquals(0f, vector2DS2[0].getY());
        assertEquals(3f, vector2DS2[1].getY());
        assertEquals(1f, vector2DS2[2].getY());
        Vector2D[] test5 = new Vector2D[] {v1, v3, v0};
        Vector2D[] vector2DS5 = triangleRenderer.rotateToLowestY(test5);
        assertEquals(0f, vector2DS5[0].getY());
        assertEquals(1f, vector2DS5[1].getY());
        assertEquals(3f, vector2DS5[2].getY());

        Vector2D[] test4 = new Vector2D[] {v3, v1, v0};
        Vector2D[] vector2DS4 = triangleRenderer.rotateToLowestY(test4);
        assertEquals(0f, vector2DS4[0].getY());
        assertEquals(3f, vector2DS4[1].getY());
        assertEquals(1f, vector2DS4[2].getY());



    }
}
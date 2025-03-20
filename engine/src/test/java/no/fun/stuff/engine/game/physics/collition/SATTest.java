package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.NewRectangle;
import no.fun.stuff.engine.game.objects.Triangle;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SATTest {
    @Test
    void EgdeBodytest() {
        Triangle triangle = new Triangle(new Vector2D(0f, 4f), new Vector2D(0f, 2f), new Vector2D(2f, 4f), 0xff111111);
        final Vector2D[] tri = new Vector2D[] {new Vector2D(0f, 4f), new Vector2D(0f, 2f), new Vector2D(2f, 4f)};
        final Vector2D[] rec = new Vector2D[] {new Vector2D(1f, 2f), new Vector2D(1f, 0f), new Vector2D(3f, 0f), new Vector2D(3f, 2f)};
        NewRectangle rectangle = new NewRectangle(2f, 2f);
        rectangle.moveTo(2f,1f);
        SAT sat = new SAT();
//        CollisionInfo collide = sat.polygonCollide(tri, rec);
        CollisionInfo collide1 = sat.polygonCollide(rectangle, triangle);
//        assertTrue(!collide.isCollide());
        assertTrue(!collide1.isCollide());
//        assertTrue(Util.compare(collide.getDepth(), 1f));

    }

    @Test
    void test2() {
//        Triangle triangle = new Triangle(new Vector2D(0f, 4f), new Vector2D(0f, 2f), new Vector2D(2f, 4f), 0xff111111);
//        final Vector2D[] tri = new Vector2D[] {new Vector2D(0f, 4f), new Vector2D(0f, 2f), new Vector2D(2f, 4f)};
//        final Vector2D[] rec = new Vector2D[] {new Vector2D(1f, 2f), new Vector2D(1f, 0f), new Vector2D(3f, 0f), new Vector2D(3f, 2f)};
        NewRectangle rectangle = new NewRectangle(2f, 2f);
        NewRectangle rectangle1 = new NewRectangle(2f, 2f);
        rectangle.moveTo(1f,0f);
        rectangle1.moveTo(-1f,0f);
        SAT sat = new SAT();
//        CollisionInfo collide = sat.polygonCollide(tri, rec);
        CollisionInfo collide1 = sat.polygonCollide(rectangle, rectangle1);
//        assertTrue(!collide.isCollide());
        assertTrue(!collide1.isCollide());
//        assertTrue(Util.compare(collide.getDepth(), 1f));

    }
    @Test
    void test() {
        Triangle triangle = new Triangle(new Vector2D(0f, 4f), new Vector2D(0f, 2f), new Vector2D(2f, 4f), 0xff111111);
        final Vector2D[] tri = new Vector2D[] {new Vector2D(0f, 4f), new Vector2D(0f, 2f), new Vector2D(2f, 4f)};
        final Vector2D[] rec = new Vector2D[] {new Vector2D(1f, 2f), new Vector2D(1f, 0f), new Vector2D(3f, 0f), new Vector2D(3f, 2f)};
        NewRectangle rectangle = new NewRectangle(2f, 2f);
        rectangle.moveTo(2f,1f);
        SAT sat = new SAT();
//        CollisionInfo collide = sat.polygonCollide(tri, rec);
        CollisionInfo collide1 = sat.polygonCollide(rectangle, triangle);
//        assertTrue(!collide.isCollide());
        assertTrue(!collide1.isCollide());
//        assertTrue(Util.compare(collide.getDepth(), 1f));

    }
    @Test
    void recToTriangleTest() {

        Triangle objA = new Triangle(new Vector2D(0f, 2f), new Vector2D(-2f, 0f), new Vector2D(0f, -2f), 0);
        Triangle objB = new Triangle(new Vector2D(1f, 1f), new Vector2D(-1f, 0f), new Vector2D(1f, -1f), 0);
        SAT sat = new SAT();
        CollisionInfo collide = sat.polygonCollide(objA, objB);
        assertTrue(collide.isCollide());
        assertTrue(Util.compare(collide.getDepth(), 1f));

    }
    @Test
    void noCollide() {
        Triangle objA2 = new Triangle(new Vector2D(-1f, 0f), new Vector2D(-1f, -1f), new Vector2D(0f, -1f), 0);
        Triangle objb2 = new Triangle(new Vector2D(1f, 0f), new Vector2D(1f, -1f), new Vector2D(2f, 0f), 0);
        SAT sat = new SAT();
        CollisionInfo collide = sat.polygonCollide(objA2, objb2);
        assertFalse(collide.isCollide());
    }
}
package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.NewRectangle;
import no.fun.stuff.engine.game.physics.collition.CollisionInfo;
import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static no.fun.stuff.engine.game.util.Util.compareVector;
import static org.junit.jupiter.api.Assertions.*;

class ContactPointTest {

    @Test
    void polygonPolygonContactPoint() {
        NewRectangle big = new NewRectangle(2.0f, 2.0f);
        NewRectangle small = new NewRectangle(1.0f, 1.0f);
        small.moveTo(new Vector2D(0f, -1.5f));

        CollisionInfo c = new CollisionInfo();
        c.setShapeA(big);
        c.setShapeB(small);
        Vector2D[] vector2DS = ContactPoint.polygonPolygonContactPoint(c);
        assertEquals( vector2DS.length , 2);
        Vector2D point = new Vector2D(-0.5f, -1.0f);
        Vector2D point2 = new Vector2D(0.5f, -1.0f);

        boolean b = compareVector(vector2DS[0], point);
        boolean b1 = compareVector(vector2DS[0], point2);
        boolean yeah = b | b1;
        boolean d = compareVector(vector2DS[1], point);
        boolean d1 = compareVector(vector2DS[1], point2);
        boolean yeah2 = d | d1;
        assertTrue(yeah2 && yeah);
    }
    @Test
    void polygonPolygonContactPoint3() {
        float HEIGHT = 4.0f;
        NewRectangle big = new NewRectangle(4f, HEIGHT);
        NewRectangle small = new NewRectangle(2f, 2f);
        small.rotate(3.1415f/4f);
        Vector2D[] rec = big.toWorldCoordinate();
        Vector2D[] rec1 = small.toWorldCoordinate();
//
        Vector2D ap = rec[2].minus(rec1[0]);
        Vector2D ab = rec[2].minus(rec[1]);
        float dot = ab.dot(ap);
        float v = ab.lengthSquare();
        Vector2D cp = rec[2].add(ap.scale(dot / v));
        float x = 0f;
        float y = 0f;
        for(int i =0;i<rec1.length;i++) {
            x += rec1[i].getX();
            y += rec1[i].getY();
        }
        x /= rec.length;
        y /= rec.length;
        Vector2D contactPoint = new Vector2D(x, y+HEIGHT/2f).minus(rec1[0]);
        small.moveTo(contactPoint);

        CollisionInfo c = new CollisionInfo();
        c.setShapeA(big);
        c.setShapeB(small);
        Vector2D[] vector2DS = ContactPoint.polygonPolygonContactPoint(c);
        assertEquals( vector2DS.length , 1);
        Vector2D point = new Vector2D(0.0f, 2.0f);

        boolean b = compareVector(vector2DS[0], point);
        assertTrue(b);
    }
}
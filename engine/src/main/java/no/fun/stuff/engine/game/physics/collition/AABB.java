package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Vector2D;

public class AABB {
    public static boolean collide(final Body a, final Body b) {
        BoundingBox boundingA = a.getBoundingBox();
        BoundingBox boundingB = b.getBoundingBox();
        Vector2D pA = a.getPos();
        Vector2D pB = b.getPos();
        float dx = pA.getX() - pB.getX();
        float halfx = boundingA.getHalfXY().getX() + boundingB.getHalfXY().getX();
        float overlap = halfx - Math.abs(dx);
        if(overlap <= 0.0f) {
            return false;
        }
        float dy = pA.getY() - pB.getY();
        float halfeny = boundingA.getHalfXY().getY() + boundingB.getHalfXY().getY();
        float overlapy = halfeny - Math.abs(dy);
        if(overlapy <= 0.0f) {
            return false;
        }
        return true;
    }

}

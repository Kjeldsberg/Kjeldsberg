package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Vector2D;

public class SAT {

    final Vector2D egde = new Vector2D();
    final Vector2D normal = new Vector2D();
    private final CollisionInfo collitionInfo = new CollisionInfo();
    public CollisionInfo collide(final Vector2D[] objectA,
                                 final Vector2D[] objectB) {
        CollisionInfo collisionInfoAB = collideOne(objectA, objectB);
        CollisionInfo collisionInfoBA = collideOne(objectB, objectA);
        if(collisionInfoAB.isCollide() || collisionInfoBA.isCollide()) {
            CollisionInfo collisionInfo = collisionInfoBA.getDepth() < collisionInfoAB.getDepth() ? collisionInfoBA : collisionInfoAB;
            Vector2D centerVec = Body.getCenter(objectB).minus(Body.getCenter(objectA));
            float length = 1f/collisionInfo.getNormal().length();
            float depth = collisionInfo.getDepth()*length;
            collisionInfo.setDepth(depth);
            collisionInfo.getNormal().mul(length);
            float dot = centerVec.dot(collisionInfo.getNormal());
            if(dot > 0) {
                collisionInfo.getNormal().mul(-1f);
            }
            return collisionInfo;
        }
        return collisionInfoAB;
    }
    private CollisionInfo collideOne(final Vector2D[] objectA,
                                     final Vector2D[] objectB) {
        collitionInfo.setCollide(true);
        collitionInfo.setDepth(Float.MAX_VALUE);
        for (int i = 0; i < objectA.length; i++) {
            final Vector2D a = objectA[i];
            int index = i == objectA.length - 1 ? 0 : i + 1;
            final Vector2D b = objectA[index];
            egde.setXY(b);
            egde.sub(a);
            normal.setXY(egde.getY(), -egde.getX());
            float dot = objectB[0].dot(normal);
            float minb = dot;
            float maxb = dot;
            for (int j = 1; j < objectB.length; j++) {
                dot = normal.dot(objectB[j]);
                if (maxb < dot) maxb = dot;
                if (minb > dot) minb = dot;
            }
            float dotA = objectA[0].dot(normal);
            float maxa = dotA;
            float mina = dotA;
            for (int j = 1; j < objectA.length; j++) {
                dotA = normal.dot(objectA[j]);
                if (maxa < dotA) maxa = dotA;
                if (mina > dotA) mina = dotA;
            }
            if (mina >= maxb || minb >= maxa) {
                collitionInfo.setCollide(false);
                return collitionInfo;
            }
            float distance = Math.min(Math.abs(maxb - mina), Math.abs(maxa - minb));
            if (distance < collitionInfo.getDepth()) {
                collitionInfo.setDepth(distance);
                collitionInfo.getNormal().setXY(normal);
            }
        }
        return collitionInfo;
    }

    public CollisionInfo collideTriangleCircle(Vector2D[] triangleWorld, Vector2D[] circleWorld) {

//        float
        return null;
    }
}

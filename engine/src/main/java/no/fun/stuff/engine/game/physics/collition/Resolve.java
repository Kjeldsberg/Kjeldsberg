package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Vector2D;

public class Resolve {
    public static void easyResolve(final Body shapeA, final Body shapeB, final CollisionInfo collide) {
        float len = collide.getDepth() / 2f;
        Vector2D scaleA = collide.getNormal().scale(len);
        Vector2D scaleB = collide.getNormal().scale(-len);
        shapeA.move(scaleA);
        shapeB.move(scaleB);
    }
    public static void impulse(final Body shapeA, final Body shapeB, final CollisionInfo collide) {
        float e = Math.min(shapeA.getRestitution(), shapeB.getRestitution());
        Vector2D velocityA = shapeA.getVelocity();
        Vector2D velocityB = shapeB.getVelocity();
        Vector2D Vab = velocityB.minus(velocityA);
        float dot = Vab.dot(collide.getNormal());
        if(dot < 0f) {
            return;
        }

        float j = -(1f + e) * dot / (shapeA.getInverseMass() + shapeB.getInverseMass());

        Vector2D addA = collide.getNormal().scale(j * shapeA.getInverseMass());
        Vector2D addB = collide.getNormal().scale(j * shapeB.getInverseMass());
        velocityA.sub(addA);
        velocityB.pluss(addB);
    }
}

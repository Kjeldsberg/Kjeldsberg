package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.physics.collition.CollisionInfo;
import no.fun.stuff.engine.matrix.Vector2D;

public class ContactPoint {

    public static void findContactsPoints(final CollisionInfo info) {
        Body shapeA = info.getShapeA();
        Body shapeB = info.getShapeB();
        if (shapeA.getShapeType() == Body.Shape.Polygon) {
            if (shapeB.getShapeType() == Body.Shape.Polygon) {
            }
            if (shapeB.getShapeType().equals(Body.Shape.Circle)) {
            }
        }
        if (shapeA.getShapeType() == Body.Shape.Circle) {
            if (shapeB.getShapeType() == Body.Shape.Circle) {
                circleCircleContactPoint(info);
            }
            if (shapeB.getShapeType().equals(Body.Shape.Polygon)) {
            }
        }
    }
    public static Vector2D circleCircleContactPoint(final CollisionInfo info) {
        float radius = info.getShapeA().getRadius();
        final Vector2D normal = info.getNormal().scale(radius);
        Vector2D pos = info.getShapeA().getPos();
        Vector2D contactPoint = pos.add(normal);
        info.getContact1().setXY(contactPoint);
        info.setContactCount(1);
        return contactPoint;
    }

}

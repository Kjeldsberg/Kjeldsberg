package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Vector2D;

public class Collision {
    public static final int maxIteration = 128;
    private final SAT sat = new SAT();
    public void collision(final SceneObject scene, float dt) {
        boolean collisionFound = true;
        int iteration = 0;
        while(collisionFound && iteration++ < maxIteration) {
            collisionFound = false;
            for (int i = 0; i < scene.getChild().size() - 1; i++) {
                final Body shapeA = (Body) scene.getChild().get(i);
                for (int j = i + 1; j < scene.getChild().size(); j++) {

                    final Body shapeB = (Body) scene.getChild().get(j);
                    if(shapeA.isStatic() && shapeB.isStatic()) {
                        continue;
                    }
                    if (shapeA.getShapeType() == Body.Shape.Polygon) {
                        if (shapeB.getShapeType() == Body.Shape.Polygon) {
                            CollisionInfo collide = sat.polygonCollide(shapeA.toWorldCoordinate(), shapeB.toWorldCoordinate());
                            if (collide.isCollide()) {
                                Resolve.easyResolve(shapeA, shapeB, collide);
                                Resolve.impulse(shapeA, shapeB, collide);
                                collisionFound = true;
                            }
                        }
                        if (shapeB.getShapeType().equals(Body.Shape.Circle)) {
                            final Vector2D[] circleWorld = shapeB.toWorldCoordinate();
                            Vector2D[] worldCoordinate = shapeA.toWorldCoordinate();
                            final CollisionInfo collide = sat.circlePolygonCollide(worldCoordinate, circleWorld[0], circleWorld[1].getY());
                            if (collide.isCollide()) {
                                Resolve.easyResolve(shapeA, shapeB, collide);
                                Resolve.impulse(shapeA, shapeB, collide);
                            }
                        }
                    }
                    if (shapeA.getShapeType() == Body.Shape.Circle) {
                        if (shapeB.getShapeType() == Body.Shape.Circle) {
                            final Vector2D[] circleAWorld = shapeA.toWorldCoordinate();
                            final Vector2D[] circleBWorld = shapeB.toWorldCoordinate();
                            CollisionInfo collide = sat.circleCircleCollide(circleAWorld[0], circleAWorld[1].getY(),
                                    circleBWorld[0], circleBWorld[1].getY());
                            if (collide.isCollide()) {
                                Resolve.easyResolve(shapeA, shapeB, collide);
                                Resolve.impulse(shapeA, shapeB, collide);
                                collisionFound = true;
                            }
                        }
                        if (shapeB.getShapeType().equals(Body.Shape.Polygon)) {
                            final Vector2D[] circleWorld = shapeA.toWorldCoordinate();
                            final CollisionInfo collide = sat.circlePolygonCollide(shapeB.toWorldCoordinate(), circleWorld[0], circleWorld[1].getY());
                            if (collide.isCollide()) {
                                Resolve.easyResolve(shapeA, shapeB, collide);
                                Resolve.impulse(shapeA, shapeB, collide);
                                collisionFound = true;
                            }
                        }
                    }
                }
            }
        }
    }
}

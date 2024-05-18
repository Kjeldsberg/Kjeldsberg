package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Collision {
    public static final int maxIteration = 128;
    private final SAT sat = new SAT();
    private final List<CollisionInfo> collisions = new ArrayList<>();
    public void collision(final SceneObject scene, float dt) {
        boolean collisionFound = true;
        int iteration = 0;
        collisions.clear();
        while(collisionFound && iteration++ < maxIteration) {
            collisionFound = false;
            for (int i = 0; i < scene.getChild().size() - 1; i++) {
                final Body shapeA = (Body) scene.getChild().get(i);
                for (int j = i + 1; j < scene.getChild().size(); j++) {

                    final Body shapeB = (Body) scene.getChild().get(j);
                    if(shapeA.isStatic() && shapeB.isStatic()) {
                        continue;
                    }
                    if(!AABB.collide(shapeA, shapeB)) {
                        continue;
                    }
                    if (shapeA.getShapeType() == Body.Shape.Polygon) {
                        if (shapeB.getShapeType() == Body.Shape.Polygon) {
                            CollisionInfo collide = sat.polygonCollide(shapeA, shapeB);
                            if (collide.isCollide()) {
                                collide.setShapeA(shapeA);
                                collide.setShapeB(shapeB);
                                collisions.add(collide);
                                collisionFound = true;
                            }
                        }
                        if (shapeB.getShapeType().equals(Body.Shape.Circle)) {
                            final Vector2D[] circleWorld = shapeB.toWorldCoordinate();
                            final CollisionInfo collide = sat.circlePolygonCollide(shapeA, circleWorld[0], circleWorld[1].getY());
                            if (collide.isCollide()) {
                                collide.setShapeA(shapeA);
                                collide.setShapeB(shapeB);
                                collisions.add(collide);
                                collisionFound = true;
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
                                collide.setShapeA(shapeA);
                                collide.setShapeB(shapeB);
                                collisions.add(collide);
                                collisionFound = true;
                            }
                        }
                        if (shapeB.getShapeType().equals(Body.Shape.Polygon)) {
                            final Vector2D[] circleWorld = shapeA.toWorldCoordinate();
                            final CollisionInfo collide = sat.circlePolygonCollide(shapeB, circleWorld[0], circleWorld[1].getY());
                            if (collide.isCollide()) {
                                collide.setShapeA(shapeB);
                                collide.setShapeB(shapeA);
                                collisions.add(collide);
                                collisionFound = true;
                            }
                        }
                    }
                }
            }
            for(CollisionInfo c : collisions) {
                Resolve.easyResolve(c.getShapeA(), c.getShapeB(), c);
//                ContactPoint.findContactsPoints(c);
                Resolve.impulse(c);
            }
            collisions.clear();
        }
    }

    public List<CollisionInfo> getCollisions() {
        return collisions;
    }
}

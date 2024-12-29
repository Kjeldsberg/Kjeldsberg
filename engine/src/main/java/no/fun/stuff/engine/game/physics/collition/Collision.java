package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.game.physics.ContactPoint;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Collision {
    public static final int maxIteration = 128;
    private final SAT sat = new SAT();
    private final List<Vector2D> contactPoints = new ArrayList<>();

    public List<Vector2D> getContactPoints() {
        return contactPoints;
    }

    private final List<CollisionInfo> collisions = new ArrayList<>();

    public void collision(final SceneObject scene, float dt) {
        boolean moreToResolve = true;
//        for (int i = 0; i < 128 && moreToResolve; i++) {
            findAllPossibleCollision(scene);
            List<CollisionInfo> collides2 = accurateCollisionCheck(collisions);
            collisions.clear();
            collisions.addAll(collides2);
            for (CollisionInfo c : collides2) {
                Resolve.easyResolve(c.getShapeA(), c.getShapeB(), c);
//                if(Resolve.easyResolve(c.getShapeA(), c.getShapeB(), c)) {
                ContactPoint.findContactsPoints(c);
                    Resolve.impulseWithRotation(c);
//                Resolve.impulse(c);
                    if (c.getContactCount() > 0) {
                        contactPoints.add(c.getContact1());
                    }
//                }
            }
            moreToResolve = collides2.size() > 0;
//        }

    }

    private List<CollisionInfo> accurateCollisionCheck(final List<CollisionInfo> wideCollision) {
        List<CollisionInfo> accurate = new ArrayList<>();
        for (CollisionInfo c : wideCollision) {
            Body shapeA = c.getShapeA();
            Body shapeB = c.getShapeB();
            if (shapeA.getShapeType() == Body.Shape.Polygon) {
                if (shapeB.getShapeType() == Body.Shape.Polygon) {
                    CollisionInfo collide = sat.polygonCollide(shapeA, shapeB);
                    if (collide.isCollide()) {
                        collide.setShapeA(shapeA);
                        collide.setShapeB(shapeB);
                        accurate.add(collide);
                    }
                }
                if (shapeB.getShapeType().equals(Body.Shape.Circle)) {
                    final Vector2D[] circleWorld = shapeB.toWorldCoordinate();
                    final CollisionInfo collide = sat.circlePolygonCollide(shapeA, circleWorld[0], circleWorld[1].getY());
                    if (collide.isCollide()) {
                        collide.setShapeA(shapeA);
                        collide.setShapeB(shapeB);
                        accurate.add(collide);
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
                        accurate.add(collide);
                    }
                }
                if (shapeB.getShapeType().equals(Body.Shape.Polygon)) {
                    final Vector2D[] circleWorld = shapeA.toWorldCoordinate();
                    final CollisionInfo collide = sat.circlePolygonCollide(shapeB, circleWorld[0], circleWorld[1].getY());
                    if (collide.isCollide()) {
                        collide.setShapeA(shapeB);
                        collide.setShapeB(shapeA);
                        accurate.add(collide);
                    }
                }
            }
        }
        return accurate;
    }

    public void findAllPossibleCollision(SceneObject scene) {
        collisions.clear();
        for (int i = 0; i < scene.getChild().size() - 1; i++) {
            final Body shapeA = (Body) scene.getChild().get(i);
            for (int j = i + 1; j < scene.getChild().size(); j++) {
                final Body shapeB = (Body) scene.getChild().get(j);
                if (shapeA.isStatic() && shapeB.isStatic()) {
                    continue;
                }

                if (intersectBoundingBoxes(shapeA.getBoundingBox(), shapeB.getBoundingBox())) {
                    CollisionInfo ci = new CollisionInfo();
                    ci.setShapeA(shapeA);
                    ci.setShapeB(shapeB);
                    collisions.add(ci);
                }
            }
        }
    }

    public boolean intersectBoundingBoxes(BoundingBox a, BoundingBox b) {
        boolean noSeparation = a.maxx <= b.minx || b.maxx <= a.minx ||
                a.maxy <= b.miny || b.maxy <= a.miny;
        return !noSeparation;
    }

    public List<CollisionInfo> getCollisions() {
        return collisions;
    }
}

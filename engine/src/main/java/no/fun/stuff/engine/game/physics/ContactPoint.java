package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.physics.collition.CollisionInfo;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

public class ContactPoint {
    private static ClosestPoint closestPoint = new ClosestPoint();
    static class ClosestPoint {
        public Vector2D cp;
        public Vector2D cp2;
        public int count = 0;

        public ClosestPoint() {
            this.dist = -Float.MAX_VALUE;
            cp = new Vector2D();
            cp2 = new Vector2D();
        }

        public float dist;
        public void set(final ClosestPoint original) {
            cp.setXY(original.cp);
            dist = original.dist;
        }
    }

    public static void findContactsPoints(final CollisionInfo info) {
        Body shapeA = info.getShapeA();
        Body shapeB = info.getShapeB();
        if (shapeA.getShapeType() == Body.Shape.Polygon) {
            if (shapeB.getShapeType() == Body.Shape.Polygon) {
                Vector2D[] vector2DS = polygonPolygonContactPoint(info);
                if(vector2DS.length == 2) {
                    info.getContact1().setXY(vector2DS[0]);
                    info.getContact2().setXY(vector2DS[1]);
                    info.setContactCount(2);
                }else
                if(vector2DS.length == 1) {
                    info.getContact1().setXY(vector2DS[0]);
                    info.setContactCount(1);
                }else {
                    info.setContactCount(0);
                }

            }
            if (shapeB.getShapeType().equals(Body.Shape.Circle)) {
                circlePolygonContactPoint(shapeB, shapeA, info);
            }
        }
        if (shapeA.getShapeType() == Body.Shape.Circle) {
            if (shapeB.getShapeType() == Body.Shape.Circle) {
                circleCircleContactPoint(info);
            }
            if (shapeB.getShapeType().equals(Body.Shape.Polygon)) {
                circlePolygonContactPoint(shapeA, shapeB, info);
            }
        }
    }
    public static Vector2D[] polygonPolygonContactPoint(final CollisionInfo info) {
        ClosestPoint tcp = new ClosestPoint();
        tcp.dist = Float.MAX_VALUE;
        tcp.cp = new Vector2D();

        Body shapeA = info.getShapeA();
        Body shapeB = info.getShapeB();
        Vector2D[] worldCoordinateA = shapeA.toWorldCoordinate();
        Vector2D[] worldCoordinateB = shapeB.toWorldCoordinate();
        getVector2DS(tcp, worldCoordinateA, worldCoordinateB);
        getVector2DS(tcp, worldCoordinateB, worldCoordinateA);
        if(tcp.count == 2) {
            Vector2D[] vector2DS = {new Vector2D(tcp.cp), new Vector2D(tcp.cp2)};
            return vector2DS;
        } else if(tcp.count == 1) {
            Vector2D[] vector2DS = {new Vector2D(tcp.cp)};
            return vector2DS;
        }
        return new Vector2D[] {};
    }

    private static void getVector2DS(ClosestPoint tcp, Vector2D[] worldCoordinateA, Vector2D[] worldCoordinateB) {
        for(int i = 0; i< worldCoordinateA.length; i++) {
            final Vector2D a = worldCoordinateA[i];
            final Vector2D b = worldCoordinateA[i == worldCoordinateA.length - 1 ? 0 : i + 1];
            for(int j = 0; j< worldCoordinateB.length; j++) {
                closestPointOnSegment(worldCoordinateB[j], a, b, closestPoint);
                if(tcp.dist > closestPoint.dist) {
                    tcp.set(closestPoint);
                    tcp.count = 1;
                } else if(Util.compare(closestPoint.dist, tcp.dist)) {
                    boolean samePoint = compareVector(closestPoint.cp, tcp.cp);
                    if(!samePoint) {
                        tcp.cp2.setXY(closestPoint.cp);
                        tcp.count = 2;
                    }
                }
            }
        }
    }

    public static Vector2D circlePolygonContactPoint(Body circle, Body polygon, final CollisionInfo info) {
        Vector2D circlePos = circle.getPos();
        Vector2D[] polygonVertices = polygon.toWorldCoordinate();
        ClosestPoint tcp = new ClosestPoint();
        tcp.dist = Float.MAX_VALUE;
        tcp.cp = new Vector2D();
        closestPoint.dist = Float.MAX_VALUE;
        for(int i = 0; i<polygonVertices.length ; i++) {
            Vector2D a = polygonVertices[i];
            Vector2D b = polygonVertices[i == polygonVertices.length - 1 ? 0 : i + 1];
            closestPointOnSegment(circlePos, a, b, closestPoint);
            if(tcp.dist > closestPoint.dist) {
                tcp.set(closestPoint);
            }
        }
        info.getContact1().setXY(tcp.cp);
        info.setContactCount(1);
        return tcp.cp;
    }
    public static boolean compareVector(final Vector2D a, final Vector2D b) {
        boolean compareX = Util.compare(a.getX(), b.getX());
        boolean compareY = Util.compare(a.getY(), b.getY());
        return compareY && compareX;
    }

    public static ClosestPoint closestPointOnSegment(final Vector2D p,
                                              final Vector2D a,
                                              final Vector2D b,
                                              final ClosestPoint closestPoint) {
        Vector2D pa = p.minus(a);
        Vector2D ba = b.minus(a);
        float dot = pa.dot(ba);
        float d = dot / ba.lengthSquare();
        if(d <= 0.0f) {
            closestPoint.cp.setXY(a);
        }else if(d >= 1.0f) {
            closestPoint.cp.setXY(b);
        } else {
            Vector2D cp = a.add(ba.mul(d));
            closestPoint.cp.setXY(cp);
        }
        float dist = p.minus(closestPoint.cp).lengthSquare();
        closestPoint.dist = dist;
        return closestPoint;
    }
    public static Vector2D circleCircleContactPoint(final CollisionInfo info) {
        float radius = info.getShapeA().getRadius();
        Vector2D axis = info.getShapeB().getPos().minus(info.getShapeA().getPos());
        axis.normaize();
        axis.mul(radius);
        Vector2D contactPoint = info.getShapeA().getPos().add(axis);
        info.getContact1().setXY(contactPoint);
        info.setContactCount(1);
        return contactPoint;
    }

}

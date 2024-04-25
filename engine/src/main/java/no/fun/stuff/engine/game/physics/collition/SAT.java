package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Vector2D;

public class SAT {


    public CollisionInfo polygonCollide(final Vector2D[] objectA,
                                        final Vector2D[] objectB) {
        CollisionInfo collisionInfo = new CollisionInfo();
        collisionInfo.setCollide(true);
        collisionInfo.setDepth(Float.MAX_VALUE);
        final MinMax obja = new MinMax();
        final MinMax objb = new MinMax();
        Vector2D normal = new Vector2D();
        for (int i = 0; i < objectA.length; i++) {
            int index = i == objectA.length - 1 ? 0 : i + 1;
            final Vector2D edge = objectA[index].minus(objectA[i]);
            normal.setXY(edge.getY(), -edge.getX());
            normal.normaize();
            projectToAxis(objectB, normal, objb);
            projectToAxis(objectA, normal, obja);

            boolean separation = checkForSeparation(collisionInfo, obja, objb, normal);
            if (separation) return collisionInfo;
        }

        Vector2D centerVec = Body.getCenter(objectB).minus(Body.getCenter(objectA));
        float dot = centerVec.dot(collisionInfo.getNormal());
        if (dot > 0) {
            collisionInfo.getNormal().mul(-1f);
        }
        return collisionInfo;
    }
    
    public CollisionInfo circlePolygonCollide(final Vector2D[] poly, final Vector2D circleCenter, float radius) {
        CollisionInfo collisionInfo = new CollisionInfo();
        final MinMax obja = new MinMax();
        final MinMax objb = new MinMax();
        final Vector2D normal = new Vector2D();
        for (int i = 0; i < poly.length; i++) {
            int index = i == poly.length - 1 ? 0 : i + 1;
            Vector2D axis = poly[index].minus(poly[i]);
            normal.setXY(axis.getY(), -axis.getX());
            normal.normaize();
            projectToAxis(poly, normal, objb);
            projectToCircle(circleCenter, radius, normal, obja);
            boolean separation = checkForSeparation(collisionInfo, obja, objb, normal);
            if (separation) return collisionInfo;
        }
        collisionInfo.setCollide(true);
        Vector2D axis = closedPointToCircle(poly, circleCenter);
        axis.normaize();
        projectToAxis(poly, axis, objb);
        projectToCircle(circleCenter, radius, axis, obja);
        boolean separation = checkForSeparation(collisionInfo, obja, objb, axis);
        if (separation) return collisionInfo;
        Vector2D center = circleCenter.minus(Body.getCenter(poly));
        float dot = center.dot(collisionInfo.getNormal());
        if (dot > 0.0) {
            collisionInfo.getNormal().mul(-1);
        }
        return collisionInfo;
    }

    private static boolean checkForSeparation(CollisionInfo collisionInfo, MinMax obja, MinMax objb, Vector2D normal) {
        if (obja.min >= objb.max || objb.min >= obja.max) {
            collisionInfo.setCollide(false);
            return true;
        }
        float distance = Math.min(objb.max - obja.min, obja.max - objb.min);
        if (distance < collisionInfo.getDepth()) {
            collisionInfo.setDepth(distance);
            collisionInfo.getNormal().setXY(normal);
        }
        return false;
    }

    private void projectToCircle(Vector2D circleCenter, float radius, Vector2D axis, MinMax cminMax) {
        Vector2D scale = axis.scale(radius);
        Vector2D right = circleCenter.add(scale);
        Vector2D left = circleCenter.minus(scale);
        cminMax.min = right.dot(axis);
        cminMax.max = left.dot(axis);
        if (cminMax.min > cminMax.max) {
            float temp = cminMax.max;
            cminMax.max = cminMax.min;
            cminMax.min = temp;
        }
    }

    private Vector2D closedPointToCircle(Vector2D[] poly, Vector2D circleCenter) {
        Vector2D closest = poly[0].minus(circleCenter);
        float length = closest.length();
        for (int i = 1; i < poly.length; i++) {
            Vector2D current = poly[i].minus(circleCenter);
            float nextLength = current.length();
            if (nextLength < length) {
                length = nextLength;
                closest = current;
            }
        }
        return closest;
    }

    public CollisionInfo circleCircleCollide(Vector2D posA, float radiusA, Vector2D posB, float radiusB) {
        CollisionInfo collisionInfo = new CollisionInfo();
        Vector2D axis = posA.minus(posB);
        float length = axis.length();
        float radius = radiusA + radiusB;
        if (radius > length) {
            collisionInfo.setDepth(radius - length);
            collisionInfo.getNormal().setXY(axis.mul(1f / length));
            collisionInfo.setCollide(true);
        }
        return collisionInfo;
    }

    private void projectToAxis(final Vector2D[] vertices, final Vector2D axis, final MinMax minMax) {
        float max = -Float.MAX_VALUE;
        float min = Float.MAX_VALUE;
        for (Vector2D vertex : vertices) {
            float dot = vertex.dot(axis);
            if (max < dot) max = dot;
            if (min > dot) min = dot;
        }
        minMax.min = min;
        minMax.max = max;
    }

    static class MinMax {
        public float min = Float.MAX_VALUE, max = -Float.MAX_VALUE;
    }

}

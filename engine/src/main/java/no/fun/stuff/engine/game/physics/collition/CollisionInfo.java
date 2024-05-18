package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Vector2D;

public class CollisionInfo {
    private final Vector2D normal;
    private float depth;
    private boolean collide;
    private final Vector2D contact1;
    private final Vector2D contact2;
    private Body shapeA;
    private Body shapeB;
    private int contactCount;
    public CollisionInfo() {
        normal = new Vector2D();
        contact1 = new Vector2D();
        contact2 = new Vector2D();
        depth = Float.MAX_VALUE;
        collide = false;
    }

    public boolean isCollide() {
        return collide;
    }
    public void setCollide(boolean collide) {
        this.collide = collide;
    }

    public Vector2D getNormal() {
        return normal;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public Vector2D getContact1() {
        return contact1;
    }

    public Vector2D getContact2() {
        return contact2;
    }

    public int getContactCount() {
        return contactCount;
    }

    public void setContactCount(int contactCount) {
        this.contactCount = contactCount;
    }

    public Body getShapeA() {
        return shapeA;
    }

    public Body getShapeB() {
        return shapeB;
    }

    public void setShapeA(Body shapeA) {
        this.shapeA = shapeA;
    }

    public void setShapeB(Body shapeB) {
        this.shapeB = shapeB;
    }

}

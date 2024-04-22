package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.matrix.Vector2D;

public class CollisionInfo {
    private final Vector2D normal;
    private float depth;
    private boolean collide;


    public CollisionInfo() {
        normal = new Vector2D();
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
}

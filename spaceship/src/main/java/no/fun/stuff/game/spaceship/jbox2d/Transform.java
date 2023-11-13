package no.fun.stuff.game.spaceship.jbox2d;

import no.fun.stuff.engine.matrix.Vector2D;

public class Transform {
    private float angel;
    private final Vector2D pos;

    public Transform(float angel, Vector2D pos) {
        this.angel = angel;
        this.pos = pos;
    }

    public float getAngel() {
        return angel;
    }

    public Vector2D getPos() {
        return pos;
    }
}

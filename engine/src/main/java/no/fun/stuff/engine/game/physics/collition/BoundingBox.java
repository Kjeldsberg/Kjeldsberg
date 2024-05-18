package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.matrix.Vector2D;

public class BoundingBox {
    private Vector2D pos;
    private Vector2D halfXY;
    private Vector2D min;
    private Vector2D max;

    public BoundingBox(Vector2D pos, Vector2D halfXY) {
        this.pos = pos;
        this.halfXY = halfXY;
    }

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    public Vector2D getHalfXY() {
        return halfXY;
    }

    public void setHalfXY(Vector2D halfXY) {
        this.halfXY = halfXY;
    }
}

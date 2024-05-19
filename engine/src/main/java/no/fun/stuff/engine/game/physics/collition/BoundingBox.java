package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.matrix.Vector2D;

public class BoundingBox {
    public float minx;
    public float maxx;
    public float miny;
    public float maxy;

    public BoundingBox(float minx, float maxx, float miny, float maxy) {
        this.minx = minx;
        this.maxx = maxx;
        this.miny = miny;
        this.maxy = maxy;
    }

}

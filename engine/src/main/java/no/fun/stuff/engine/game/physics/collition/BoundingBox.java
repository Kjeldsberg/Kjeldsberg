package no.fun.stuff.engine.game.physics.collition;


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
    public static boolean intersectBoundingBoxes(final BoundingBox a, final BoundingBox b) {
        boolean noSeparation = a.maxx <= b.minx || b.maxx <= a.minx ||
                a.maxy <= b.miny || b.maxy <= a.miny;
        return !noSeparation;
    }

}

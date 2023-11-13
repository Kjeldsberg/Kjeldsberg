package no.fun.stuff.engine.game.geo;

import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

public class SideScan {
    public final float dx, dy, dl;
    public Vector2D p0, p1;

    public boolean flatLine = false;

    public SideScan(final Vector2D p0, final Vector2D p1) {
        this.p0 = p0;
        this.p1 = p1;
        dx = p0.getX() - p1.getX();
        dy = p0.getY() - p1.getY();
        flatLine = Math.abs(dy) < Util.epsilon;
        float oneOverDy = 1 / dy;
        if(dy < 1.0f) {
            dl = flatLine ? 0 : dx/dy;
        }else {
            dl = flatLine ? 0 : dx * oneOverDy;

        }
    }

    public SideScan toY(final float y) {
        final Vector2D interSectionPoint = new Vector2D(this.xOfY(y), y);
        return new SideScan(interSectionPoint, this.p1);
    }

    public SideScan fromPoint(final SideScan in) {
        return new SideScan(this.p0, in.p0);
    }



    public int getYStep() {
        int top = (int) p0.getY();
        int bottom = (int) p1.getY();
        return top - bottom;
    }

    public float xOfY(float y) {
        // x = x1 + (y-y1)*((x2-x1)/(y2-y1))

        return p1.getX() + (y - p1.getY()) * (dl);
    }

}

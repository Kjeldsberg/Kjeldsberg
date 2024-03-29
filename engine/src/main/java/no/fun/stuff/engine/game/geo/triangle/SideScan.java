package no.fun.stuff.engine.game.geo.triangle;

import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

public class SideScan {
    public float dx, dy, dl;
    public Vector2D p0, p1;

    public boolean flatLine = false;
    public boolean leftside = false;
    public SideScan(final Vector2D p0, final Vector2D p1) {
        this.p0 = p0;
        this.p1 = p1;
        newSlope();
    }
    public void setNewEgde(final Vector2D p0, final Vector2D p1) {
        this.p0.setXY(p0);
        this.p1.setXY(p1);
        newSlope();
    }
    private void newSlope() {
        dx = p0.getX() - p1.getX();
        dy = p0.getY() - p1.getY();
        flatLine = Math.abs(dy) < Util.epsilon;
        dl = flatLine ? 0 : dx/(dy);
    }

    public SideScan toY(final float y) {
        final Vector2D interSectionPoint = new Vector2D(this.xOfY(y), y);
        return new SideScan(interSectionPoint, this.p1);
    }

    public SideScan fromPoint(final SideScan in) {
        return new SideScan(this.p0, in.p0);
    }



    public int getYStep() {
        int top = (int) Math.floor(p0.getY());
        int bottom = (int) Math.floor(p1.getY());
        return top - bottom;
    }

    public float xOfY(float y) {
        // x = x1 + (y-y1)*((x2-x1)/(y2-y1))

        return p1.getX() + (y - p1.getY()) * (dl);
    }

}

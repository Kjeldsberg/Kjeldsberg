package no.fun.stuff.engine.game.geo.triangle;


import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.Vector;

public class TextureSideScan {
    public float dx, dy, dl;
    public float uDelta, vDelta;
    public float oneOverDy;
    public Vector2D p0, p1;
    public Vector2D uv0, uv1;
    public final static float THRESHOLD = 0.0001f;
    public boolean flatLine;
    public boolean leftside;

    public TextureSideScan(final Vector2D p0, final Vector2D p1, final Vector2D uv0, final Vector2D uv1) {
        newSlope(p0, p1, uv0, uv1);
    }

    private void newSlope(Vector2D p0, Vector2D p1, Vector2D uv0, Vector2D uv1) {
        this.p0 = p0;
        this.p1 = p1;
        this.uv0 = uv0;
        this.uv1 = uv1;
        dx = p0.getX() - p1.getX();
        dy = p0.getY() - p1.getY();
        flatLine = Math.abs(dy) < THRESHOLD;
        oneOverDy = 1 / dy;
        dl = flatLine ? 0 : dx * oneOverDy;
        float tdu = uv1.getX() - uv0.getX();
        float tdv = uv1.getY() - uv0.getY();

        uDelta = oneOverDy * tdu;
        vDelta = oneOverDy * tdv;
    }

    public void setNewEgde(final Vector2D p0, final Vector2D p1, final Vector2D uv0, final Vector2D uv1) {
        newSlope(p0, p1, uv0, uv1);
    }
    private Vector2D a;
    private final Vector2D b = new Vector2D();
    private final Vector2D res = new Vector2D();
    private final Vector2D tmp = new Vector2D();
    private final Vector2D BA = new Vector2D();
    private final Vector2D CA = new Vector2D();
    public void barycentricToCartesianCoordinate(final Vector2D a, final Vector2D b, final Vector2D c,
                                                 float u, float v, final Vector2D result) {
        CA.setXY(c);
        CA.minus(a);
        BA.setXY(b);
        BA.minus(a);
        CA.mul(u);
        BA.mul(v);
        result.setXY(a);
        result.pluss(CA);
        result.pluss(BA);

    }
    public void barycentricCooridnate(final Vector2D p0, final Vector2D p1, final Vector2D p2,
                                      float w, float u, float v, final Vector2D result) {
        result.setXY(p0);
        result.mul(w);
        tmp.setXY(p1);
        tmp.mul(u);
        result.pluss(tmp);
        tmp.setXY(p2);
        tmp.mul(v);
        result.pluss(tmp);
    }
    public void lerp(float delta, final Vector2D result) {
        a = result;
        if(delta <= Util.epsilon) {
            a.setXY(uv0);
            return;
        }
        if(delta >= 1.0f) {
            a.setXY(uv1);
            return;
        }
        b.setXY(uv1);
        a.setXY(uv0);
        b.sub(a);
        b.mul(delta);
        a.pluss(b);
    }
    public void lerp(float delta, final Vector2D p0, Vector2D p1, final Vector2D result) {
        this.a = result;
        if(delta <= Util.epsilon) {
            a.setXY(p0);
            return;
        }
        if(delta >= 1.0f) {
            a.setXY(p1);
            return;
        }
        b.setXY(p1);
        a.setXY(p0);
        b.sub(a);
        b.mul(delta);
        a.pluss(b);
    }
    public static boolean isOutsideScreen(Vector2D v1, Vector2D v2, Vector2D v3, int screenWidth, int screenHeight) {
        int ret = 0;
        boolean b1 = v1.getX() < 0 || v1.getX() > screenWidth || v1.getY() < 0 || v1.getY() > screenHeight;
        if (!b1) {
            ret |=1;
        }
        boolean b2 = v2.getX() < 0 || v2.getX() > screenWidth || v2.getY() < 0 || v2.getY() > screenHeight;
        if (!b2) {
            ret |=2;
        }
        boolean b3 = v3.getX() < 0 || v3.getX() > screenWidth || v3.getY() < 0 || v3.getY() > screenHeight;
        if (!b3) {
            ret |=4;
        }
        return ret == 0;
    }
}

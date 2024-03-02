package no.fun.stuff.engine.game.geo.triangle;

import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

public class Barycentric {
    private Vector2D uv1;
    private Vector2D uv2;
    private Vector2D uv3;
    private final Vector2D left = new Vector2D();
    private final Vector2D right = new Vector2D();
    private final Vector2D  leftDirection = new Vector2D();
    private final Vector2D  rightDirection = new Vector2D();
    private final Vector2D  uvRightDirection = new Vector2D();
    private final Vector2D  uvLeftDirection = new Vector2D();
    private final Vector2D  leftStart = new Vector2D();
    private final Vector2D  rightStart = new Vector2D();

    public void calckulateUV(final Vector2D p0, final Vector2D p1, final Vector2D p2,
                             final Vector2D uv0, final Vector2D uv1, final Vector2D uv2) {

        leftDirection.setXY(p0); leftDirection.sub(p1);
        rightDirection.setXY(p0); rightDirection.sub(p2);
        uvLeftDirection.setXY(uv0); uvLeftDirection.sub(uv1);
        uvRightDirection.setXY(uv0); uvRightDirection.sub(uv2);
        leftStart.setXY(uv1);
        rightStart.setXY(uv2);

    }
    private final Vector2D a = new Vector2D();
    private final Vector2D b = new Vector2D();
    public void lerpLeft(float delta, final Vector2D result) {
        result.setXY(leftStart);
        if(delta <= Util.epsilon) {
            return;
        }
        b.setXY(leftDirection);
        a.setXY(leftStart);
        b.mul(delta);
        a.pluss(b);
    }
    public void lerpRight(float delta, final Vector2D result) {
        result.setXY(rightStart);
        if(delta <= Util.epsilon) {
            return;
        }
        b.setXY(rightDirection);
        a.setXY(rightStart);
        b.mul(delta);
        a.pluss(b);
    }


}

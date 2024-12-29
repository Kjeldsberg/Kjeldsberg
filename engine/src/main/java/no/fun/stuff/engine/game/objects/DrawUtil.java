package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class DrawUtil {

    public static void drawBox(final Vector2D screenPos, float size, int color, final Renderer r) {
        float halfSize = size*0.5f;
        Vector2D ul = screenPos.add(new Vector2D(-halfSize, -halfSize));
        Vector2D bl = screenPos.add(new Vector2D(-halfSize, halfSize));
        Vector2D ur = screenPos.add(new Vector2D(halfSize, -halfSize));
        Vector2D br = screenPos.add(new Vector2D(halfSize, halfSize));


        drawLine(ul, bl, r, color);
        drawLine(bl, br, r, color);
        drawLine(br, ur, r, color);
        drawLine(ul, ur, r, color);

    }
    public static void drawLine(final Vector2D p1, final Vector2D p2, final Renderer r, int color) {
        r.drawBresenhamLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY(), color);
    }

}

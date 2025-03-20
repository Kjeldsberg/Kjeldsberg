package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.physics.collition.BoundingBox;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class DrawBoundingBoxTriangle extends Triangle {
    public DrawBoundingBoxTriangle(Vector2D p0, Vector2D p1, Vector2D p2, int color) {
        super(p0, p1, p2, color);
    }

    public static void drawLine(final Vector2D p1, final Vector2D p2, final Renderer r, int color) {
        r.drawBresenhamLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY(), color);
    }
    public void update(final SceneObject sceneObject, float dt) {
        super.update(sceneObject, dt);
        rotate(0.001f);
    }
    @Override
    public void render(SceneObject parent, Renderer r) {
        super.render(parent, r);
        drawBox(this, parent, r, 0xffffffff);
    }
    public static void drawBox(Body a, SceneObject parent, Renderer r, int color) {
        BoundingBox b = a.getBoundingBox();
        Vector2D[] coord = {
        new Vector2D(b.minx, b.miny),
        new Vector2D(b.minx, b.maxy),
        new Vector2D(b.maxx, b.maxy),
        new Vector2D(b.maxx, b.miny)};
        Vector2D[] re = new Vector2D[] {new Vector2D(), new Vector2D(), new Vector2D(), new Vector2D()};
        Matrix3x3 model1 = parent.getModel();
        model1.mul(coord, re);

        drawLine(re[0], re[1], r, color);
        drawLine(re[1], re[2], r, color);
        drawLine(re[2], re[3], r, color);
        drawLine(re[3], re[0], r, color);
    }
}

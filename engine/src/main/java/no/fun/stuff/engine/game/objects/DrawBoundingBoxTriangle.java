package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.physics.collition.BoundingBox;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class DrawBoundingBoxTriangle extends Triangle {
    private  NewRectangle rec;
    public DrawBoundingBoxTriangle(Vector2D p0, Vector2D p1, Vector2D p2, int color) {
        super(p0, p1, p2, color);
        BoundingBox b = getBoundingBox();
        rec = new NewRectangle(b.maxx - b.minx, b.maxy - b.miny);
    }

    public static void drawLine(final Vector2D p1, final Vector2D p2, final Renderer r, int color) {
        r.drawBresenhamLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY(), color);
    }
    public void update(final SceneObject sceneObject, float dt) {
        super.update(sceneObject, dt);
    }
    @Override
    public void render(SceneObject parent, Renderer r) {
        super.render(parent, r);
        BoundingBox b = getBoundingBox();
        rec = new NewRectangle(b.maxx - b.minx, b.maxy - b.miny);
        rec.moveTo(getPos());
        rec.setColor(getColor());
        rec.render(parent, r);
    }
    public static void drawBox(Body a, SceneObject parent, Renderer r, int color) {
        BoundingBox b = a.getBoundingBox();
        Vector2D ul = new Vector2D(b.minx, b.maxy);
        Vector2D bl = new Vector2D(b.minx, b.miny);
        Vector2D ur = new Vector2D(b.maxx, b.maxy);
        Vector2D br = new Vector2D(b.minx, b.maxy);
        ((SceneObject)a).calculateViewModel(parent);
        Matrix3x3 norotate = ((SceneObject) a).getViewModel();


        drawLine(norotate.mul(ul), norotate.mul(bl), r, color);
        drawLine(norotate.mul(bl), norotate.mul(br), r, color);
        drawLine(norotate.mul(br), norotate.mul(ur), r, color);
        drawLine(norotate.mul(ul), norotate.mul(ur), r, color);
    }
}

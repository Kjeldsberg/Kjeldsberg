package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Grid extends SceneObject {
    private final Vector2D size;
    private final int color;
    private List<Vector2D> points = new ArrayList<>();
    public Grid(int w, int h, int color, int screenX, int screenY) {
        this.color = color;
        size = new Vector2D(w, h);
        Matrix3x3 mat = new Matrix3x3();
        Vector2D position = new Vector2D(5, 5);
        Vector2D add = new Vector2D();
        mat.translate(position);
        for(int y = 0;y<=screenY;y+=w) {
            for(int x=0;x<=screenX;x+=w) {
                points.add(new Vector2D(x, 0));
                points.add(new Vector2D(x, screenY));
                points.add(new Vector2D(0, y));
                points.add(new Vector2D(screenX, y));
            }
        }
    }

    @Override
    public void update(SceneObject parent, float dt) {

    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        final Matrix3x3 viewModel = parent.getModel().fastMulCopy(model);
        final Point[] p = new Point[]{new Point(), new Point()};
        for(int i=0;i<points.size();i+=2) {
            p[0].setVector(points.get(i));
            p[1].setVector(points.get(i+1));
            Point[] mul = viewModel.mul(p);
            drawLine(mul, color, r);
        }
    }
    void drawLine(final Point[] h0, int color, final Renderer r) {
        for(int i=0, y = 1;i<h0.length - 1;i++, y++) {
            r.drawBresenhamLine((int)h0[i].getX(), (int)h0[i].getY(), (int)h0[y].getX(), (int)h0[y].getY(), color);
        }
        r.drawBresenhamLine((int)h0[0].getX(), (int)h0[0].getY(), (int)h0[h0.length -1].getX(), (int)h0[h0.length - 1].getY(), color);
    }

}

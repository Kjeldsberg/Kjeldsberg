package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.SceneObject;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class Triangle extends SceneObject {
    private Vector2D p1, p2, p3, w1, w2, w3, localCenter, worldCenter;
    private Vector2D[] localCoordinate;
    private Vector2D[] worldCoordinate;
    private int color;
    public Triangle(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        this.p1 = p0;
        this.p2 = p1;
        this.p3 = p2;
        this.color = color;
        init();
    }
    public Triangle() {
//        this.pos.setXY(new Vector2D(50, 50));
        p1 = new Vector2D(0, 20);
        p2 = new Vector2D(30, 20);
        p3 = new Vector2D(15, 40);
        init();
    }

    private void init() {
        localCenter = new Vector2D((p1.getX() + p2.getX() + p3.getX())/3.0f, (p1.getY() + p2.getY() + p3.getY())/3.0f);
        Vector2D center = new Vector2D();
        center.sub(localCenter);
        localCoordinate = new Vector2D[] {p1, p2, p3, localCenter};
        for(final Vector2D v: localCoordinate) {
            v.pluss(center);
        }
        worldCoordinate = new Vector2D[] {new Vector2D(), new Vector2D(), new Vector2D(), new Vector2D()};
        w1 = worldCoordinate[0];
        w2 = worldCoordinate[1];
        w3 = worldCoordinate[2];
        worldCenter = worldCoordinate[3];
//        getRotate().rotate(0.01f);
    }

    @Override
    public void update(SceneObject parent, float dt) {
//        translate.translate(worldCenter);
//        rotate.rotate(-0.01f);
//        translate.translate(pos);
//        scale.scale(6.0f);
    }

    @Override
    public void render(SceneObject parent, Renderer r) {

        this.model.set(this.translate.fastMulCopy(this.rotate).fastMulCopy(this.scale).getCopy());
        Matrix3x3 viewModel = new Matrix3x3();
        if(parent != null) {
            viewModel.set(parent.getModel().fastMulCopy(model));
        } else {
            viewModel.set(model);
        }
        viewModel.mul(localCoordinate, worldCoordinate);
        r.fillTriangle(worldCoordinate[0], worldCoordinate[1], worldCoordinate[2], color);
//        r.drawBresenhamLine((int)w1.getX(), (int)w1.getY(), (int)w2.getX(), (int)w2.getY(), 0xff000000);
//        r.drawBresenhamLine((int)w1.getX(), (int)w1.getY(), (int)w3.getX(), (int)w3.getY(), 0xff000000);
//        r.drawBresenhamLine((int)w2.getX(), (int)w2.getY(), (int)w3.getX(), (int)w3.getY(), 0xff000000);
    }
}

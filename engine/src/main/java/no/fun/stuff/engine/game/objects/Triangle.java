package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.game.TriangleClickedOn;
import no.fun.stuff.engine.game.physics.Integrator;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class Triangle extends Body implements Clickable {
    private Vector2D p1, p2, p3, w1, w2, w3, localCenter, worldCenter;
    private int color;
    private TriangleClickedOn clickedOn;
    public Triangle(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        this.p1 = p0;
        this.p2 = p1;
        this.p3 = p2;
        this.color = color;
        init();
    }
    public Triangle() {
        p1 = new Vector2D(0, 20);
        p2 = new Vector2D(30, 20);
        p3 = new Vector2D(15, 40);
        init();
    }

    private void init() {
        shapeType = Shape.Polygon;
        localCenter = new Vector2D((p1.getX() + p2.getX() + p3.getX())/3.0f, (p1.getY() + p2.getY() + p3.getY())/3.0f);
        moveTo(localCenter);
        Vector2D center = new Vector2D();
        center.sub(localCenter);
        localCoordinate = new Vector2D[] {p1, p2, p3};
        for(final Vector2D v: localCoordinate) {
            v.pluss(center);
        }
        worldCoordinate = new Vector2D[] {new Vector2D(), new Vector2D(), new Vector2D()};
        w1 = worldCoordinate[0];
        w2 = worldCoordinate[1];
        w3 = worldCoordinate[2];
        clickedOn = new TriangleClickedOn(this);
    }

    @Override
    public void update(SceneObject parent, float dt) {
//        translate.translate(worldCenter);
//        rotate.rotate(-0.001f);
//        translate.translate(pos);
//        scale.scale(6.0f);
    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        calculateViewModel(parent);
        int c = isStatic() ? 0xff111111 : color;
        r.fillTriangle(
                getViewModel().mul(localCoordinate[0]),
                getViewModel().mul(localCoordinate[1]),
                getViewModel().mul(localCoordinate[2]), c);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public Vector2D getLocalCenter() {
        return localCenter;
    }

    private final Vector2D center = new Vector2D();

    @Override
    public boolean clickedOn(Vector2D position, final Matrix3x3 cameraMatrix) {
        toWorldCoordinate();
        return clickedOn.clickedOn(position, cameraMatrix);
    }
}

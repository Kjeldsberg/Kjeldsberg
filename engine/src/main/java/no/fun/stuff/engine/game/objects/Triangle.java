package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.game.TriangleClickedOn;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class Triangle extends Body implements Clickable {
    private Vector2D p1, p2, p3, localCenter;
    private int color;
    private TriangleClickedOn clickedOn;
    private boolean wireFrame = false;
    public Triangle(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        this.p1 = p0;
        this.p2 = p1;
        this.p3 = p2;
        this.color = color;
        init();
    }

    public void setWireFrame(boolean wireFrame) {
        this.wireFrame = wireFrame;
    }

    public Triangle() {
        p1 = new Vector2D(0, 20);
        p2 = new Vector2D(30, 20);
        p3 = new Vector2D(15, 40);
        init();
    }

    private void init() {
        shapeType = Shape.Polygon;
        localCoordinate = new Vector2D[]{p1, p2, p3};
        localCenter = getCenter(localCoordinate);
        Vector2D center = new Vector2D();
        center.sub(localCenter);
        for (final Vector2D v : localCoordinate) {
            v.pluss(center);
        }
        localCenter.setXY(getCenter(localCoordinate));

        worldCoordinate = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
        clickedOn = new TriangleClickedOn(this);
        float len[] = {
                localCoordinate[0].minus(new Vector2D()).length(),
                localCoordinate[1].minus(new Vector2D()).length(),
                localCoordinate[2].minus(new Vector2D()).length()};
        float length = -Float.MAX_VALUE;
        for (float v : len) {
            if(v > length) length = v;
        }
        reCalculateProperties();
//        setRestitution(0.4f);
    }

    public void reCalculateProperties() {
        float b = localCoordinate[2].minus(localCoordinate[0]).length();
        float h = localCoordinate[1].minus(localCoordinate[0]).length();

        setArea(b * h /2f);
        setDensity(Body.WaterDensity);
        setMass(getArea()*getDensity());

//        Inerta = 1/36 * b * h^3

        setInertia((1f / (12*2)) * getMass() * (b * b + h * h));
//        setInertia(getMass() * b * h * h * h /36f);
        setInertiaInverse(1/getInertia());
    }

    @Override
    public void update(SceneObject parent, float dt) {
        if(!isStatic()) {
            this.rotate(getAngularVelocity()*dt);
            toWorldCoordinate();
        }
//        translate.translate(worldCenter);
//        rotate.rotate(-0.001f);
//        translate.translate(pos);
//        scale.scale(6.0f);
    }
    @Override
    public void render(SceneObject parent, Renderer r) {
        calculateViewModel(parent);
        int c = isStatic() ? 0xff111111 : color;
        if(wireFrame) {
            r.drawLine(getViewModel().mul(localCoordinate[0]),
                    getViewModel().mul(localCoordinate[1]), c);
            r.drawLine(getViewModel().mul(localCoordinate[1]),
                    getViewModel().mul(localCoordinate[2]), c);
            r.drawLine(getViewModel().mul(localCoordinate[2]),
                    getViewModel().mul(localCoordinate[0]), c);

        }   else {

            r.fillTriangle(
                    getViewModel().mul(localCoordinate[0]),
                    getViewModel().mul(localCoordinate[1]),
                    getViewModel().mul(localCoordinate[2]), c);
        }
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

    @Override
    public Vector2D applyForces() {
        return Vector2D.ZERO;
    }
}

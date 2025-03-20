package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class Circle extends Body implements Clickable {
    private Vector2D position;
    private final float radius;
    private float radiusInWorldCoordinate;
    private int color;
    private final Vector2D up;
    private boolean drawDirectionLine = true;
    int counter = 0;

    public Circle(final Vector2D position, float radius, int color) {
        this(radius, color);
        this.position.setXY(position);
        init();
        moveTo(position);
    }
    public Circle(float radius, int color) {
        this.position = new Vector2D();
        this.radius = radius;
        this.color = color;
        this.shapeType = Shape.Circle;
        init();
        up = new Vector2D(0.0f, radius);
        localCoordinate = new Vector2D[]{new Vector2D(), up};
        worldCoordinate = new Vector2D[]{new Vector2D(), new Vector2D()};
    }
    private void init() {
        setArea(radius * radius * (float)Math.PI);
        this.setMass(getArea() * getDensity());
        setInertia((1f / 2f) * getMass() * radius * radius);
        setInertiaInverse(1f/getInertia());
        setRestitution(0.5f);
    }
    public float getRadiusInWorldCoordinate() {
        return worldCoordinate[1].getY();
    }

    @Override
    public Vector2D applyForces() {
        return Vector2D.ZERO;
    }

    @Override
    public Vector2D[] toWorldCoordinate() {
        if(isReCalculateCoordinate()) {
            calculateModel();
            worldCoordinate[0] = model.mul(localCoordinate[0]);
            worldCoordinate[1] = getScale().mul(up);
            setReCalculateCoordinate(false);
        }
        return worldCoordinate;
    }
    @Override
    public void update(SceneObject parent, float dt) {
        if(!isStatic()) {
            this.rotate(getAngularVelocity()*dt);
        }
    }

    @Override
    public void render(SceneObject parent, Renderer renderer) {

        calculateViewModel(parent);
        Matrix3x3 scaleMatrix = new Matrix3x3();
        if (parent != null) {
            scaleMatrix.set(parent.getScale());
            scaleMatrix.fastMul(getScale());

        } else {
            scaleMatrix.set(scaleMatrix);
        }
        Vector2D radius = scaleMatrix.mul(localCoordinate[1]);
        Vector2D screenPos = viewModel.mul(localCoordinate[0]);
        Vector2D edgePoint = viewModel.mul(localCoordinate[1]);
        renderer.drawText("counter: " + counter++, 50, 500, 0xffffffff);
        renderer.drawCircle(screenPos, radius.getY(), color);
        if(drawDirectionLine) {
            Vector2D egde = edgePoint.minus(screenPos);
            float len = egde.length();
            egde.normaize();
            Vector2D eg = screenPos.add(egde.scale(len));
            DrawUtil.drawLine(screenPos, eg, renderer, 0xff000000);
        }
    }

    @Override
    public boolean clickedOn(Vector2D position, final Matrix3x3 cameraMatrix) {
        Matrix3x3 inverseModel = calculateInverseModel();
        Matrix3x3 matrix3x3 = inverseModel.mulCopy(cameraMatrix);
        Vector2D lo = matrix3x3.mul(position);
        Vector2D mul = inverseModel.mul(pos);
        lo.sub(mul);
        float length = lo.length();

        return length < localCoordinate[1].getY();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }
}

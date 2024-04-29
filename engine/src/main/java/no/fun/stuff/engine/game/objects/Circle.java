package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class Circle extends Body implements Clickable {
    private final Vector2D position;
    private final float radius;
    private int color;
    private final Vector2D up;
    int counter = 0;

    public Circle(final Vector2D position, float radius, int color) {
        this.position = position;
        this.radius = radius;
        this.color = color;
        this.shapeType = Shape.Circle;
        up = new Vector2D(0.0f, radius);
        localCoordinate = new Vector2D[]{position, up};
        worldCoordinate = new Vector2D[] {new Vector2D(), new Vector2D()};
    }
    public Circle(float radius, int color) {
        this.position = new Vector2D();
        this.radius = radius;
        this.color = color;
        this.shapeType = Shape.Circle;
        up = new Vector2D(0.0f, radius);
        localCoordinate = new Vector2D[]{position, up};
        worldCoordinate = new Vector2D[]{new Vector2D(), new Vector2D()};
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
        renderer.drawText("counter: " + counter++, 50, 500, 0xffffffff);
        renderer.drawCircle(screenPos, radius.getY(), color);
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

}

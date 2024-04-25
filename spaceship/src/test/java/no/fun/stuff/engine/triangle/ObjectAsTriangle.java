package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.game.geo.triangle.SideScan;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class ObjectAsTriangle extends SceneObject {
    private List<Vector2D> list = new ArrayList<>();
    private int xDim, yDim;
    private List<Integer> indexList = new ArrayList<>();
    private Vector2D[] localCoordinate;
    private Vector2D[] worldCoordinate;
    private Vector2D localCenter = new Vector2D();

    private boolean rotating = false;
    private float rotation = 0.001f;
    private float scaleValue = 1.0f;
    private Vector2D upLeft = new Vector2D(-10.0f, -10.0f);
    private Vector2D upRight = new Vector2D(10.0f, -10.0f);
    private Vector2D lowerLeft = new Vector2D(-10.0f, 10.0f);
    private Vector2D lowerRight = new Vector2D(10.0f, 10.0f);

    private final int color = 0x88556677;

    public ObjectAsTriangle(int xDim, int yDim) {
        this.xDim = xDim;
        this.yDim = yDim;
        final SideScan upperLine = new SideScan(upLeft, upRight);
        final SideScan leftDown = new SideScan(upLeft, lowerLeft);

        float xDimLength = upRight.minus(upLeft).length();
        float yDimLength = lowerLeft.minus(upLeft).length();

        float xStep = xDimLength / xDim;
        float yStep = yDimLength / yDim;
        float uStep = 1.0f/xDim;
        float vStep = 1.0f/yDim;
        for (int y = 0; y <= yDim; y++) {
            float currY = leftDown.p0.getY() + y * yStep;
            for (int x = 0; x <= xDim; x++) {
                float currX = upperLine.p0.getX() + x * xStep;
                list.add(new Vector2D(currX, currY));
            }
        }
        if (xDim == 1 && yDim == 1) {
            indexList.add(0);
            indexList.add(2);
            indexList.add(1);
            indexList.add(1);
            indexList.add(2);
            indexList.add(3);

        } else {
            for (int x = 0; x < xDim; x++) {
                int secondLine = (xDim + 1);
                indexList.add(x);
                indexList.add(secondLine + x);
                indexList.add(x + 1);

                indexList.add(x + 1);
                indexList.add(secondLine + x);
                indexList.add(secondLine + x + 1);

            }

            for (int y = 1; y < yDim; y++) {
                int lineStartElement = y * (xDim + 1);
                int secondLineStartElement = lineStartElement + (xDim + 1);
                for (int x = 0; x < xDim; x++) {
                    int startElement = lineStartElement + x;
                    int lineUnderElement = secondLineStartElement + x;
                    indexList.add(startElement);
                    indexList.add(lineUnderElement);
                    indexList.add(startElement + 1);

                    indexList.add(startElement + 1);
                    indexList.add(lineUnderElement);
                    indexList.add(lineUnderElement + 1);

                }
            }
        }
        initCoordinates();
    }

    public ObjectAsTriangle() {

        list.add(new Vector2D(0.0f, -3.0f));
        list.add(new Vector2D(-1.0f, -1.0f));
        list.add(new Vector2D(1.0f, -1.0f));

        list.add(new Vector2D(-1.0f, -1.0f));
        list.add(new Vector2D(-1.0f, 3.0f));
        list.add(new Vector2D(1.0f, 3.0f));

        list.add(new Vector2D(-1.0f, -1.0f));
        list.add(new Vector2D(1.0f, 3.0f));
        list.add(new Vector2D(1.0f, -1.0f));

        list.add(new Vector2D(-1.0f, 0.0f));
        list.add(new Vector2D(-5.0f, 3.0f));
        list.add(new Vector2D(-1.0f, 3.0f));

        list.add(new Vector2D(1.0f, 0.0f));
        list.add(new Vector2D(1.0f, 3.0f));
        list.add(new Vector2D(5.0f, 3.0f));
        initCoordinates();
    }

    public float getScaleValue() {
        return scaleValue;
    }

    public void setScaleValue(float scaleValue) {
        this.scaleValue = scaleValue;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }

    private void initCoordinates() {
        localCoordinate = new Vector2D[list.size()];
        worldCoordinate = new Vector2D[list.size()];
        for (int i = 0; i < list.size(); i++) {
            localCoordinate[i] = new Vector2D();
            worldCoordinate[i] = new Vector2D();
        }
        final Vector2D org[] = new Vector2D[list.size()];
        list.toArray(org);
        final Matrix3x3 mod = new Matrix3x3();
        mod.scale(10.0f);
        mod.mul(org, localCoordinate);
    }

    @Override
    public void update(SceneObject parent, float dt) {
        scale.scale(scaleValue);
        if (this.rotating) {
            final float rad = 0.011f;
            this.rotation += rad;
            rotate.rotate(rad);
        }
    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        this.model.fastMul(translate, rotate, scale);
        Matrix3x3 viewModel = new Matrix3x3();
        if (parent != null) {
            viewModel.set(parent.getModel().fastMulCopy(model));
        } else {
            viewModel.set(model);
        }
        viewModel.mul(localCoordinate, worldCoordinate);
        if (indexList == null || indexList.isEmpty()) {
            for (int i = 0; i < localCoordinate.length; i += 3) {
                r.fillTriangle(worldCoordinate[i], worldCoordinate[i + 1], worldCoordinate[i + 2], color);
            }
        } else {
            for (int i = 0; i < indexList.size(); i += 3) {
                Integer p0 = indexList.get(i);
                Integer p1 = indexList.get(i + 1);
                Integer p2 = indexList.get(i + 2);
                r.fillTriangle(worldCoordinate[p0], worldCoordinate[p1], worldCoordinate[p2], color);
            }
        }
    }
}

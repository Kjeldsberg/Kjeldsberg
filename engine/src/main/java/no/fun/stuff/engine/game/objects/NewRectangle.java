package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.game.RecClickedOn;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class NewRectangle extends Body implements Clickable {
    public int color = 0xffaabbcc;
    private final float width;
    private final float height;
    private final boolean wireframe = true;
    private final RecClickedOn clickedOn;
    public NewRectangle(float width, float height) {
        this.shapeType = Shape.Polygon;
        this.height = height;
        this. width = width;
        float halfWidth = width/2f;
        float halfHeight = height/2f;
        setStatic(false);
        recalckProperties(width, height);
        this.clickedOn = new RecClickedOn(this);
        initCoordinates(halfWidth, halfHeight);
    }
    public NewRectangle(float width, float height, boolean isStatic) {
        this.shapeType = Shape.Polygon;
        this.height = height;
        this. width = width;
        float halfWidth = width/2f;
        float halfHeight = height/2f;
        setStatic(isStatic);
        recalckProperties(width, height);
        initCoordinates(halfWidth, halfHeight);
        this.clickedOn = new RecClickedOn(this);
    }

    private void initCoordinates(float halfWidth, float halfHeight) {
        localCoordinate = new Vector2D[4];
        worldCoordinate = new Vector2D[4];
        localCoordinate[0] = new Vector2D(-halfWidth, -halfHeight);
        localCoordinate[1] = new Vector2D(-halfWidth, halfHeight);
        localCoordinate[2] = new Vector2D(halfWidth, halfHeight);
        localCoordinate[3] = new Vector2D(halfWidth, -halfHeight);
        worldCoordinate[0] = new Vector2D();
        worldCoordinate[1] = new Vector2D();
        worldCoordinate[2] = new Vector2D();
        worldCoordinate[3] = new Vector2D();
    }

    public void recalckProperties(float width, float height) {
        this.setArea(width * height);
        if(isStatic()) {
            this.setMass(0f);
            this.setInertia(0);
            this.setInertiaInverse(0);
        } else {
            this.setMass(getArea() * getDensity());
            this.setInertia((1f / 12) * getMass() * (width * width + height * height));
            this.setInertiaInverse(1f/getInertia());
        }
    }

    @Override
    public void update(SceneObject parent, float dt) {
        if(!isStatic()) {
            float test = getAngularVelocity() * dt;
            this.rotate(test);
        }
    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        calculateViewModel(parent);
        if(wireframe) {
            drawLine(viewModel.mul(localCoordinate[0]), viewModel.mul(localCoordinate[1]), r, color);
            drawLine(viewModel.mul(localCoordinate[1]), viewModel.mul(localCoordinate[2]), r, color);
            drawLine(viewModel.mul(localCoordinate[2]), viewModel.mul(localCoordinate[3]), r, color);
            drawLine(viewModel.mul(localCoordinate[3]), viewModel.mul(localCoordinate[0]), r, color);
        }
    }
    private void drawLine(final Vector2D v1, final Vector2D v2, final Renderer r, int c) {
        r.drawBresenhamLine((int)v1.getX(), (int)v1.getY(),(int)v2.getX(),(int)v2.getY(), c);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    @Override
    public boolean clickedOn(final Vector2D position, final Matrix3x3 cameraMatrix) {
        return this.clickedOn.clickedOn(position, cameraMatrix);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public Vector2D applyForces() {
        return Vector2D.ZERO;
    }
}

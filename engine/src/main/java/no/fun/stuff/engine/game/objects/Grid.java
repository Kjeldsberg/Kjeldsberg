package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Grid extends Body {
    private int columns;
    private int rows;
    private float width;
    private float height;
    private final List<Vector2D> points;

    public Grid(int columns, int rows, float width, float height) {
        this.columns = columns;
        this.rows = rows;
        this.width = width;
        this.height = height;
        float totalWidth = columns * width;
        float totalHeight = rows * height;
        float halfWidth = totalWidth / 2;
        float halfHeight = totalHeight / 2;
        setStatic(true);
        Vector2D upperLeft = new Vector2D(-halfWidth, -halfHeight);
        Vector2D upperRight = new Vector2D(halfWidth, -halfHeight);
        Vector2D lowerLeft = new Vector2D(-halfWidth, halfHeight);
        Vector2D lowerRight = new Vector2D(halfWidth, halfHeight);
        Vector2D downSlope = new Vector2D(0.0f, totalHeight / rows);
        float columnsSlope = 1.0f / columns;
        float rowSlope = 1.0f / rows;
        Vector2D result = new Vector2D();
        Vector2D result2 = new Vector2D();
        points = new ArrayList<>();
        for (int i = 0; i <= columns; i++) {
            float delta = i * columnsSlope;
            Util.lerp(delta, upperLeft, upperRight, result);
            Util.lerp(delta, lowerLeft, lowerRight, result2);
            points.add(new Vector2D(result));
            points.add(new Vector2D(result2));

        }
        for (int i = 0; i <= columns; i++) {
            float ata = i * rowSlope;
            Util.lerp(ata, upperLeft, lowerLeft, result);
            Util.lerp(ata, upperRight, lowerRight, result2);
            points.add(new Vector2D(result));
            points.add(new Vector2D(result2));

        }
        localCoordinate = new Vector2D[points.size()];
        worldCoordinate = new Vector2D[points.size()];
        points.toArray(localCoordinate);
        for (int i = 0; i < worldCoordinate.length; i++) {
            worldCoordinate[i] = new Vector2D();
        }

    }
    public Vector2D[] getLocalCoordiate() {
        return localCoordinate;
    }
    @Override
    public void update(SceneObject parent, float dt) {

    }
//    @Override
//    public void scale(float scaleValue) {
//        Vector2D center = Body.getCenter(localCoordinate);
//
//
//        for(int i=0;i<centerList.size();i++) {
//            triangleList.get(i).moveTo(centerList.get(i).mul(scale));
//            triangleList.get(i).scale(scale);
//        }
//
//    }
    @Override
    public void render(SceneObject parent, Renderer r) {
        calculateModel();
        if (parent != null) {
            this.viewModel.set(parent.getModel());
            viewModel.fastMul(this.getModel());
        } else {
            viewModel.set(getModel());
        }
        viewModel.mul(localCoordinate, worldCoordinate);
        for(int i = 0; i<worldCoordinate.length; i+=2) {
            final Vector2D left = worldCoordinate[i];
            final Vector2D right = worldCoordinate[i+1];
            r.drawBresenhamLine((int)left.getX(), (int)left.getY(), (int)right.getX(),(int)right.getY(), 0xff101010);
        }
    }

    @Override
    public Vector2D applyForces() {
        return Vector2D.ZERO;
    }
}

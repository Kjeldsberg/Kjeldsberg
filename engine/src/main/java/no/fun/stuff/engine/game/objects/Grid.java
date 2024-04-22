package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Grid extends SceneObject {
    private int columns;
    private int rows;
    private float width;
    private float height;
    private final List<Vector2D> points;
    final private Vector2D[] localCoordinates;
    private Vector2D[] worldCoordinates;

    public Grid(int columns, int rows, float width, float height) {
        this.columns = columns;
        this.rows = rows;
        this.width = width;
        this.height = height;
        float totalWidth = columns * width;
        float totalHeight = rows * height;
        float halfWidth = totalWidth / 2;
        float halfHeight = totalHeight / 2;

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
        localCoordinates = new Vector2D[points.size()];
        worldCoordinates = new Vector2D[points.size()];
        points.toArray(localCoordinates);
        for (int i = 0; i < worldCoordinates.length; i++) {
            worldCoordinates[i] = new Vector2D();
        }

    }

    @Override
    public void update(SceneObject parent, float dt) {

    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        calculateModel();
        if (parent != null) {
            this.viewModel.set(parent.getModel());
            viewModel.fastMul(this.getModel());
        } else {
            viewModel.set(getModel());
        }
        viewModel.mul(localCoordinates, worldCoordinates);
        for(int i = 0; i<worldCoordinates.length; i+=2) {
            final Vector2D left = worldCoordinates[i];
            final Vector2D right = worldCoordinates[i+1];
            r.drawBresenhamLine((int)left.getX(), (int)left.getY(), (int)right.getX(),(int)right.getY(), 0xffffffff);
        }
    }

}

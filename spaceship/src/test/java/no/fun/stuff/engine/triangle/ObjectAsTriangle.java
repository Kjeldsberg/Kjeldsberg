package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.SceneObject;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class ObjectAsTriangle extends SceneObject {
    private List<Vector2D> list = new ArrayList<>();
    private Vector2D[] localCoordinate;
    private Vector2D[] worldCoordinate;
    private Vector2D localCenter = new Vector2D();

    private boolean rotating = false;
    private float rotation = 0.001f;

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

    private final int color = 0x88556677;
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
        localCoordinate = new Vector2D[list.size()];
        worldCoordinate = new Vector2D[list.size()];
        for(int i=0;i<list.size();i++) {
            localCoordinate[i] = new Vector2D();
            worldCoordinate[i] = new Vector2D();
        }
        final Vector2D org[] = new Vector2D[list.size()];
        list.toArray(org);
        final Matrix3x3 mod = new Matrix3x3();
        mod.scale(30f);
        mod.mul(org, localCoordinate);
    }
    @Override
    public void update(SceneObject parent, float dt) {
        if(this.rotating) {
            final float rad = 0.001f;
            this.rotation += rad;
            rotate.rotate(rad);
        }
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
        for(int i = 0;i<localCoordinate.length;i+=3) {
            r.fillTriangle(worldCoordinate[i], worldCoordinate[i+1], worldCoordinate[i+2], color);
        }


    }
}

package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Vector2D;

public class LookAtCamera extends SceneObject {
    private SceneObject lookatObject = null;
    private final Vector2D screenSize;
    private boolean dirty;
    public LookAtCamera(Vector2D screenSize) {
        this.screenSize = screenSize;
    }
    public LookAtCamera(final SceneObject lookatObject, final Vector2D screenSize) {
        this.screenSize = screenSize;
        this.lookatObject = lookatObject;
    }

    @Override
    public void update(SceneObject parent, float dt) {
        if (lookatObject == null) {
            return;
        }

        final Vector2D pos = new Vector2D(lookatObject.getPos().getX(), lookatObject.getPos().getY());
        pos.setXY(pos);
        dirty = true;
    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        if (lookatObject == null) {
            return;
        }

        if (dirty) {
            model.setIdentity();
            Vector2D halfScreen = new Vector2D(screenSize.getX() * 0.5f, screenSize.getY() * 0.5f);
            final Vector2D minus = halfScreen.minus(pos);
            translate.translate(minus);
            model.set(translate.fastMulCopy(rotate).fastMulCopy(scale));
            dirty = false;
        }
    }
}

package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Vector2D;

public class LookAtCamera extends SceneObject {
    private SceneObject lookatObject = null;
    private final Vector2D screenSize;
    private float zoom = 1.0f;

    private Vector2D viewPort = new Vector2D(40.0f, 30.0f);

    private boolean zoomDirty = false;
    public float getZoom() {
        return zoom;
    }
    public void setZoom(float zoom) {
        Vector2D pluss = scale.getScale().pluss(zoom, zoom);
        scale.scale(pluss);
        dirty = true;
    }
    private boolean dirty;
    public LookAtCamera(Vector2D screenSize) {
        this.screenSize = screenSize;
    }
    public LookAtCamera(final SceneObject lookAtObject, final Vector2D screenSize) {
        this.screenSize = screenSize;
        this.lookatObject = lookAtObject;
        float scaleX = screenSize.getX() / viewPort.getX();
        float scaleY = screenSize.getY() / viewPort.getY();
        getScale().scale(new Vector2D(scaleX, scaleY));
    }
    public LookAtCamera(final SceneObject lookAtObject,
                        final Vector2D screenSize,
                        final Vector2D viewPort) {
        this.viewPort = viewPort;
        this.screenSize = screenSize;
        this.lookatObject = lookAtObject;
        float scaleX = screenSize.getX() / viewPort.getX();
        float scaleY = screenSize.getY() / viewPort.getY();
        getScale().scale(new Vector2D(scaleX, scaleY));
    }

    @Override
    public void update(SceneObject parent, float dt) {
        if (lookatObject == null) {
            return;
        }
//        if(zoomDirty) {
//            zoomDirty = false;
//            Vector2D currentScale = scale.getScale().pluss(zoom,zoom);
//            scale.scale(currentScale);
//        }
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
            Vector2D halfScreen = new Vector2D(screenSize.getX() * 0.5f, screenSize.getY() * 0.5f);
            final Vector2D minus = halfScreen.minus(pos);

            translate.translate(minus);
            calculateModel();
            dirty = false;
        }
    }
}

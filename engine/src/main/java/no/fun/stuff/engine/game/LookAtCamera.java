package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Input;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Vector2D;

import java.awt.event.KeyEvent;

public class LookAtCamera extends Body {
    private SceneObject lookatObject = null;
    private final Vector2D screenSize;
    private float zoom = 1.0f;
    private Input input;
    private Vector2D viewPort = new Vector2D(40.0f, 30.0f);
    private Vector2D halfViewPort = new Vector2D(viewPort.getX() * 0.5f, viewPort.getY() * 0.5f);

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
        this.halfViewPort.setX(viewPort.getX()*0.5f);
        this.halfViewPort.setY(viewPort.getY()*0.5f);
        float scaleX = screenSize.getX() / viewPort.getX();
        float scaleY = screenSize.getY() / viewPort.getY();
        getScale().scale(new Vector2D(scaleX, scaleY));
    }

    @Override
    public void update(SceneObject parent, float dt) {
        if (lookatObject == null) {
            return;
        }
        if(input != null) {
            if(input.isKey(KeyEvent.VK_A)) {
                setPos(getPos().add(new Vector2D(-1f, 0f)));
            }
            if(input.isKey(KeyEvent.VK_D)) {
                setPos(getPos().add(new Vector2D(1f, 0f)));
            }
            if(input.isKey(KeyEvent.VK_W)) {
                setPos(getPos().add(new Vector2D(0f, 1f)));
            }
            if(input.isKey(KeyEvent.VK_S)) {
                setPos(getPos().add(new Vector2D(0f, -1f)));
            }
        }

        Vector2D viewScale = getScale().getScale();
        Vector2D objectPos = lookatObject.getPos();
        Vector2D cameraPos = new Vector2D(  (halfViewPort.getX() - objectPos.getX()) * viewScale.getX(),
                                            (halfViewPort.getY() - objectPos.getY()) * viewScale.getY());
        moveTo(cameraPos);
    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        if (lookatObject == null) {
            return;
        }
        calculateModel();

    }

    @Override
    public Vector2D applyForces() {
        return Vector2D.ZERO;
    }

    public void setInput(Input input) {
        this.input = input;
    }
}

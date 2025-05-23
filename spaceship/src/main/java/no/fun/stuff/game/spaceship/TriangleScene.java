package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.game.physics.collition.BoundingBox;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class TriangleScene extends SceneObject {
    private GameContainer gc;
    private Body camera;
    private Clickable clicedOn = null;
    private List<Clickable> clickedList = new ArrayList<>();
    public List<Clickable> checkPicked(final Vector2D clickedScreenPosition) {
        clickedList.clear();
        Matrix3x3 inverseCamera = camera.calculateInverseModel();
        for(SceneObject sceneObject : child) {
            if(sceneObject instanceof Clickable c) {
                if(c.clickedOn(clickedScreenPosition, inverseCamera)) {
                    clickedList.add(c);
                };
            }
        }
        return clickedList;
    }
    @Override
    public void update(SceneObject parent, float dt) {
        for (int i = 0; i < child.size(); i++) {
            SceneObject sceneObject = child.get(i);
            sceneObject.update(camera, dt);
            if (sceneObject.isDead()) {
                child.remove(i);
                i--;
            }
        }
        if(camera != null) {
            camera.update(parent, dt);
        }
    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        if(camera != null) {
            camera.render(parent, r);
        }
        final BoundingBox cameraBoundingBox = camera.getBoundingBox();
        for(SceneObject obj: this.child) {
            if(obj instanceof Body b) {
                final BoundingBox boundingBox = b.getBoundingBox();
                boolean drawObject = BoundingBox.intersectBoundingBoxes(boundingBox, cameraBoundingBox);
                if(drawObject) {
                    obj.render(camera, r);
                }
            } else {
                obj.render(camera, r);
            }
        }
    }

    public void setCamera(Body camera) {
        this.camera = camera;
    }

    public void setGc(GameContainer gc) {
        this.gc = gc;
    }
}

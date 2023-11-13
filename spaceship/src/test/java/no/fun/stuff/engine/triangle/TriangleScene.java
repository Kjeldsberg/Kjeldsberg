package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.SceneObject;

public class TriangleScene extends SceneObject {
    private GameContainer gc;
    private SceneObject camera;

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
        for(SceneObject obj: this.child) {
            obj.render(camera, r);
        }
    }

    public void setCamera(SceneObject camera) {
        this.camera = camera;
    }

    public void setGc(GameContainer gc) {
        this.gc = gc;
    }
}

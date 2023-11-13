package no.fun.stuff.game.spaceship.jbox2d;

import no.fun.stuff.engine.game.SceneObject;
import no.fun.stuff.game.spaceship.physics.PhysicInterface;
import org.jbox2d.dynamics.Body;

public class PhysicsInterfaceImpl implements PhysicInterface {
    private final SceneObject sceneObject;
    private final Body body;

    public PhysicsInterfaceImpl(SceneObject sceneObject, Body body) {
        this.sceneObject = sceneObject;
        this.body = body;
    }

    @Override
    public void translateToPhysicEngine(Transform transform) {
//        body.createFixture()
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public Transform translateFromPhysicEngine() {
        return null;
    }
}

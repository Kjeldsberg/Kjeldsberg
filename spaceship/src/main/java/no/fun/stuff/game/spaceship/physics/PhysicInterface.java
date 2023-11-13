package no.fun.stuff.game.spaceship.physics;

import no.fun.stuff.game.spaceship.jbox2d.Transform;

public interface PhysicInterface {
    void translateToPhysicEngine(final Transform transform);
    void update(float dt);
    Transform translateFromPhysicEngine();

}

package no.fun.stuff.game.spaceship.jbox2d;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.Rect;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.game.spaceship.physics.PhysicInterface;

public class Box extends Rect {
    private PhysicInterface physicEngine;
    private float mass = 1.0f;
    private float inversMass = 1.0f/mass;
    private Vector2D gravity = new Vector2D(0.0f, -9.81f);
    public Box(Vector2D size, Vector2D pos) {
        super(size, pos);
    }


    @Override
    public void update(SceneObject parent, float dt) {
        Transform transform = physicEngine.translateFromPhysicEngine();
        getPos().setXY(transform.getPos());
        setAngle(transform.getAngel());
        final Vector2D velocity = pos.minus(oldPos);
        final Vector2D acc = gravity.scale(inversMass * dt * dt);
        final Vector2D nextPos = pos.add(velocity);
        nextPos.pluss(acc);
        oldPos.setXY(pos);
        pos.setXY(nextPos);

        physicEngine.update(dt);
        Transform fromPhysicEngine = physicEngine.translateFromPhysicEngine();
        pos.setXY(fromPhysicEngine.getPos());
        setAngle(fromPhysicEngine.getAngel());

    }

    @Override
    public void render(SceneObject parent, Renderer r) {

    }
}

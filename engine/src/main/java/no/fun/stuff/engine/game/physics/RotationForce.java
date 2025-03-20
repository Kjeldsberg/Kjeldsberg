package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Vector2D;

public class RotationForce {
    private Vector2D pos;
    private Vector2D forceVector;

    public RotationForce(Vector2D pos, Vector2D forceVector) {
        this.pos = pos;
        this.forceVector = forceVector;
    }
    public void applyForce(final Body b) {
        Vector2D cp = this.pos.minus(b.getPos());
        Vector2D cpPerp = new Vector2D(-cp.getY(), cp.getX());
        Vector2D angularVelocity = cpPerp.scale(b.getAngularVelocity());
        float angel = angularVelocity.cross(forceVector) * b.getInertiaInverse();
        float v = b.getAngularVelocity() + angel;
        b.setAngularVelocity(v);
    }
}

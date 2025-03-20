package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class VerletWithVelocityIntegration implements Integrate {
    private Vector2D pos = new Vector2D();
    private Vector2D acc = new Vector2D();
    private Vector2D vel = new Vector2D();
    private Vector2D sumForces = new Vector2D();
    private Vector2D new_pos = new Vector2D();
    private Vector2D new_acc = new Vector2D();
    private Vector2D new_vel = new Vector2D();
    private final Vector2D gravity = new Vector2D(0f, 9.81f);
    private List<Vector2D> worldForces = new ArrayList<>();

    public void addForce(final Vector2D aForce) {
        worldForces.add(aForce);
    }
    public void removeForce(final Vector2D aForce) {
        worldForces.remove(aForce);
    }

    public Vector2D getGravity() {
        return gravity;
    }

    @Override
    public void integrate(Body body, float dt) {
        if(body.isStatic()) {
            return;
        }
        float dtHalve = dt * 0.5f;
        pos.setXY(body.getPos());
        new_pos.setXY(body.getPos());
        new_pos.pluss(body.getVelocity().scale(dt));
        new_pos.pluss(body.getAcceleration().scale(dt*dtHalve));
        new_acc = applyForces(body);//.scale(dt);
        new_vel.setXY(body.getVelocity());
        new_vel.pluss(body.getAcceleration().add(new_acc).scale(dtHalve));
        body.getPos().setXY(new_pos);
        body.getVelocity().setXY(new_vel);
        body.getAcceleration().setXY(new_acc);

    }
    public Vector2D applyForces(final Body a) {
        sumForces.setXY(gravity);
        for(final Vector2D f : worldForces) {
            sumForces.pluss(f);
        }
        sumForces.pluss(a.applyForces());
        return sumForces;
    }

    public List<Vector2D> getWorldForces() {
        return worldForces;
    }
}

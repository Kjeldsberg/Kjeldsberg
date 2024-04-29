package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.Circle;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Integrator {
    private final List<Vector2D> forces;
    private final Vector2D oldPos;
    private final Vector2D pos;

    public Integrator() {
        forces = new ArrayList<>();
        oldPos = new Vector2D();
        pos = new Vector2D();
    }

    public void integrate(final Body body, float dt) {
        Vector2D pos = body.getPos();
        Vector2D vel = new Vector2D(body.getVelocity()).mul(dt);
        Vector2D acc = new Vector2D(body.getAcceleration()).mul(dt * dt * 0.5f);
        Vector2D new_pos = new Vector2D(pos).add(vel).add(acc);
        Vector2D new_acc = applyForces(body);
        Vector2D new_vel = body.getVelocity().add(acc.add(new_acc).mul(dt * 0.5f));
        body.getPos().setXY(new_pos);
        body.getVelocity().setXY(new_vel);
        body.getAcceleration().setXY(acc);
        body.moveTo(new_pos);
//        oldPos.setXY(temp_x, temp_y);
    }
    public Vector2D applyForces(Body a) {
        Vector2D mul = new Vector2D(a.getForce()).mul(a.getInverseMass());
        if(a instanceof Circle x) {
            int est = 0;
            Vector2D force = a.getForce();
            if(!Util.compare(0f, force.getY())) {
                int test = 0;
            }
            if(!Util.compare(0f, force.getX())) {
                int test = 0;
            }
        }
        return mul;
    }
    public void addForce(final Vector2D force) {
        forces.add(force);
    }

}

package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.Circle;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Integrator implements Integrate {
    private final List<Vector2D> forces;
    private final Vector2D oldPos;
    private final Vector2D pos;

    public Integrator() {
        forces = new ArrayList<>();
        oldPos = new Vector2D();
        pos = new Vector2D();
    }

    private Vector2D new_pos = new Vector2D();
    private Vector2D new_acc = new Vector2D();
    private Vector2D new_vel = new Vector2D();
    public void integrate(final Body body, float dt) {
        if(body.isStatic()) {
            return;
        }
        Vector2D pos = body.getPos();
        Vector2D velocity = body.getVelocity();
        Vector2D vel = new Vector2D(velocity).mul(dt);

        float halfDT = dt * 0.5f;
        Vector2D acceleration = body.getAcceleration();
        Vector2D acc = new Vector2D(acceleration).mul(dt * halfDT);
//        Vector2D new_pos = new Vector2D(pos).add(vel).add(acc);
        new_pos.setXY(pos);
        new_pos.pluss(vel);
        new_pos.pluss(acc);
        new_acc.setXY(applyForces(body));
        Vector2D new_vel = velocity.add(acc.add(new_acc).mul(halfDT));
        body.getPos().setXY(new_pos);
        velocity.setXY(new_vel);
        acceleration.setXY(acc);
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
        for(final Vector2D force : forces) {
            mul.pluss(force);
        }
        return mul;
    }
    public void addForce(final Vector2D force) {
        forces.add(force);
    }

}

package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class VerletIntegrate extends Integrator{
    private List<Vector2D> apply = new ArrayList<>();
    public VerletIntegrate(List<Vector2D> forces) {
        super(forces);
    }

    @Override
    public void integrate(Body body, float dt) {
        if(body.isStatic()) {
            body.getVelocity().setXY(0f, 0f);
            body.setAngularVelocity(0f);

            return;
        }


        //Xnext = Xcurr + (Xcurr - Xprev) + acurr*dt*dt
        Vector2D acc = applyForces(body, dt).scale(dt*dt);
        if(body.isReCalculateOldPosition()) {
            Vector2D vel = body.getVelocity().scale(-1f);
            Vector2D a = body.getAcceleration().scale(-1f);
//            Vector2D a = body.getAcceleration().scale(dt*dt*0.5f);
//            vel.pluss(a);
            body.getOldPos().setXY(body.getPos());
            body.getOldPos().add(vel.add(a));
            body.setReCalculateOldPosition(false);
        }
        Vector2D velocity = body.getPos().minus(body.getOldPos());
        Vector2D pos = body.getPos().add(velocity).add(acc);
        body.getAcceleration().setXY(acc);
        body.getVelocity().setXY(velocity);
        body.getOldPos().setXY(body.getPos());
        body.moveTo(pos);
//        body.getPos().setXY(pos);
    }
    public Vector2D applyForces(Body a, float dt) {
        apply.clear();
        apply.addAll(forces);
//        apply.add(a.getVelocity().scale(1f/dt));
//        Vector2D mul = new Vector2D(a.getForce()).mul(a.getInverseMass());
//        if(a instanceof Circle x) {
//            int est = 0;
//            Vector2D force = a.getForce();
//            if(!Util.compare(0f, force.getY())) {
//                int test = 0;
//            }
//            if(!Util.compare(0f, force.getX())) {
//                int test = 0;
//            }
//        }
        Vector2D mul = new Vector2D();
        for(final Vector2D force : apply) {
            mul.pluss(force.scale(a.getInverseMass()));
        }
        return mul;
    }

}

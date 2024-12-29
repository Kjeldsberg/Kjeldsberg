package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.Circle;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

public class VerletIntegrate extends Integrator{
    @Override
    public void integrate(Body body, float dt) {
        if(body.isStatic()) {
            return;
        }

        //Xnext = Xcurr + (Xcurr - Xprev) + acurr*dt*dt
        Vector2D acc = super.applyForces(body).scale(dt*dt);
        Vector2D velocity = body.getPos().minus(body.getOldPos());
        Vector2D pos = body.getPos().add(velocity).add(acc);
        body.setAcceleration(acc);
        body.getVelocity().setXY(velocity);
        body.getOldPos().setXY(body.getPos());
        body.moveTo(pos);
//        body.getPos().setXY(pos);
    }
    public Vector2D applyForces(Body a) {
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
        for(final Vector2D force : forces) {
            mul.pluss(force.scale(a.getInverseMass()));
        }
        return mul;
    }

}

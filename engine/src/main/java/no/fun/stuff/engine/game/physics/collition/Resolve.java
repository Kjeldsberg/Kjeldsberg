package no.fun.stuff.engine.game.physics.collition;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Resolve {
    private static ImpulseHolder holder = new ImpulseHolder();
    /**
     *
     * @param shapeA
     * @param shapeB
     * @param collide
     * @return true for further calculation, false otherwise.
     */
    static class ImpulseHolder {
        List<Vector2D> ra = new ArrayList<>();
        List<Vector2D> rb = new ArrayList<>();
        List<Vector2D> impulse = new ArrayList<>();
        public ImpulseHolder() {
            ra.add(new Vector2D());
            ra.add(new Vector2D());
            rb.add(new Vector2D());
            rb.add(new Vector2D());
            impulse.add(new Vector2D());
            impulse.add(new Vector2D());
        }
        public void clearAll() {
            for(int i=0;i<2;i++) {
                ra.get(i).setXY(0f, 0f);
                rb.get(i).setXY(0f, 0f);
                impulse.get(i).setXY(0f, 0f);
            }
        }
    }
    public static boolean easyResolve(final Body shapeA, final Body shapeB, final CollisionInfo collide) {
        Vector2D minus = shapeB.getVelocity().minus(shapeA.getVelocity());
        Vector2D normal = collide.getNormal();
        if(minus.dot(normal) < 0f) {
            return false;
        }
        float depth = collide.getDepth();
        if(shapeA.isStatic()) {
            shapeB.move(normal.scale(-depth));
        } else if(shapeB.isStatic()) {
            shapeA.move(normal.scale(depth));
        }else {
            float len = depth * 0.5f;
            Vector2D scaleA = normal.scale(len);
            Vector2D scaleB = normal.scale(-len);
            shapeA.move(scaleA);
            shapeB.move(scaleB);
        }
        return true;
    }

    public static void impulseWithRotation(final CollisionInfo collide) {
        Body shapeA = collide.getShapeA();
        Body shapeB = collide.getShapeB();
        Vector2D normal = collide.getNormal();
        Vector2D contact1 = collide.getContact1();
        Vector2D contact2 = collide.getContact2();
        int contactCount = collide.getContactCount();
        float e = Math.min(shapeA.getRestitution(), shapeB.getRestitution());
        Vector2D[] contactList = new Vector2D[]{contact1, contact2};
        holder.clearAll();
        if(contactCount == 2) {
            int test = 0;
        }
        for (int i = 0; i < contactCount; i++) {

            Vector2D ra = contactList[i].minus(shapeA.getPos());
            Vector2D rb = contactList[i].minus(shapeB.getPos());
            Vector2D raPerp = new Vector2D(-ra.getY(), ra.getX());
            Vector2D rbPerp = new Vector2D(-rb.getY(), rb.getX());

            Vector2D angularLinearVelocityA = raPerp.scale(shapeA.getAngularVelocity());
            Vector2D angularLinearVelocityB = rbPerp.scale(shapeB.getAngularVelocity());
            Vector2D relativeVelocityB = shapeB.getVelocity().add(angularLinearVelocityB);
            Vector2D relativeVelocityA = shapeA.getVelocity().add(angularLinearVelocityA);
            Vector2D relativeVelocity = relativeVelocityB
                    .minus(relativeVelocityA);

            float contactVelocityMag = relativeVelocity.dot(normal);
            holder.ra.get(i).setXY(ra);
            holder.rb.get(i).setXY(rb);
            if (contactVelocityMag < 0.0f) {

                continue;
            }
            float raPerpDotN = raPerp.dot(normal);
            float rbPerpDotN = rbPerp.dot(normal);

            float denominator = shapeA.getInverseMass() + shapeB.getInverseMass() +
                    (raPerpDotN * raPerpDotN) * shapeA.getInertiaInverse() +
                    (rbPerpDotN * rbPerpDotN) * shapeB.getInertiaInverse();
            float j = -(1f + e) * contactVelocityMag;
            j /= denominator;
            j /= (float)contactCount;
            System.out.println("the J: " + j);
            Vector2D impulse = normal.scale(j);

            holder.impulse.get(i).setXY(impulse);
        }
        for(int i = 0;i<contactCount;i++) {
            Vector2D impulse = holder.impulse.get(i);
            Vector2D ra = holder.ra.get(i);
            Vector2D rb = holder.rb.get(i);

            Vector2D velocityA = shapeA.getVelocity();
            Vector2D velocityB = shapeB.getVelocity();
            Vector2D addA = impulse.scale(shapeA.getInverseMass());
            Vector2D addB = impulse.scale(shapeB.getInverseMass());
            velocityA.sub(addA);
            velocityB.pluss(addB);
            float va = ra.cross(impulse) * shapeA.getInertiaInverse();
            float vb = rb.cross(impulse) * shapeB.getInertiaInverse();
            float angularVelocity = shapeA.getAngularVelocity();
            float angularVelocity1 = shapeB.getAngularVelocity();
            angularVelocity -= va;
            angularVelocity1 += vb;
//            float angularVelocityB = angularVelocity1 + vb;
            shapeA.setAngularVelocity(angularVelocity);
            shapeB.setAngularVelocity(angularVelocity1);
//            shapeA.setAngularVelocity(angularVelocityA);
//            shapeB.setAngularVelocity(angularVelocityB);
//            shapeA.rotate(angularVelocityA);
//            shapeB.rotate(angularVelocityB);
        }
    }

    public static void impulse(final CollisionInfo collide) {
        Body shapeA = collide.getShapeA();
        Body shapeB = collide.getShapeB();
        float e = Math.min(shapeA.getRestitution(), shapeB.getRestitution());
        Vector2D velocityA = shapeA.getVelocity();
        Vector2D velocityB = shapeB.getVelocity();
        Vector2D Vab = velocityB.minus(velocityA);
        float dot = Vab.dot(collide.getNormal());
        if(dot < 0f) {
            return;
        }
        float length = collide.getNormal().length();

        float j = -(1f + e) * dot / (shapeA.getInverseMass() + shapeB.getInverseMass());

        Vector2D addA = collide.getNormal().scale(j * shapeA.getInverseMass());
        Vector2D addB = collide.getNormal().scale(j * shapeB.getInverseMass());
        velocityA.sub(addA);
        velocityB.pluss(addB);
    }
}

package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.game.physics.collition.BoundingBox;
import no.fun.stuff.engine.matrix.Vector2D;

public abstract class Body extends SceneObject {
    public enum Shape {
        Polygon, Circle
    }
    public static float MinDensity = 0.5f;     // g/cm^3
    public static float MaxDensity = 21.4f;

    // restitution mellom 0.0 og 1.0;
    private float restitution = 0.5f;
    private float density = 1.0f;
    private float mass = 1.0f;
    private float inverseMass = 1/mass;
    private float inertia;
    private float inertiaInverse;
    private float area;
    private float radius;
    // friction between 0.0 og 1.0;
    private float staticFriction = 0.6f;
    private float dynamicFriction = 0.4f;
    private Vector2D oldPos = new Vector2D();
    private Vector2D force = new Vector2D();
    private Vector2D velocity = new Vector2D();
    private Vector2D acceleration = new Vector2D();
    protected Shape shapeType;
    private boolean reCalculateCoordinate = true;
    private boolean reCalculateBoundingBox = true;
    private boolean isStatic = false;
    protected Vector2D[] localCoordinate;
    protected Vector2D[] worldCoordinate;
    private BoundingBox boundingBox;

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    private float angularVelocity;
    public Vector2D[] toWorldCoordinate() {
        if(reCalculateCoordinate) {
            calculateModel();
            getModel().mul(localCoordinate, worldCoordinate);
            reCalculateCoordinate = false;
        }
        return worldCoordinate;
    }
    public void initPos(final Vector2D initPos) {
        pos.setXY(initPos);
        oldPos.setXY(initPos);
    }
    public boolean isReCalculateCoordinate() {
        return reCalculateCoordinate;
    }
    public void setReCalculateCoordinate(boolean reCalculateCoordinate) {
        this.reCalculateCoordinate = reCalculateCoordinate;
    }

    public void rotate(float angle) {
        this.angle += angle;
        reCalculateCoordinate = true;
        reCalculateBoundingBox = true;
    }
    public void rotateTo(float angle) {
        this.angle = angle;
        reCalculateCoordinate = true;
        reCalculateBoundingBox = true;
    }
    public void moveTo(final Vector2D position) {
        pos.setXY(position);
        reCalculateCoordinate = true;
        reCalculateBoundingBox = true;
    }
    public void moveTo(float x, float y) {
        pos.setXY(x, y);
        reCalculateCoordinate = true;
        reCalculateBoundingBox = true;
    }
    public void move(final Vector2D motionVector) {
        pos.pluss(motionVector);
        reCalculateCoordinate = true;
        reCalculateBoundingBox = true;
    }

    public static Vector2D  getCenter(final Vector2D[] vertex) {
        float sumX = 0;
        float sumy = 0;
        for(int i = 0;i<vertex.length;i++) {
            Vector2D theVertex = vertex[i];
            sumX += theVertex.getX();
            sumy += theVertex.getY();
        }
        sumX /= vertex.length;
        sumy /= vertex.length;
//        center.setXY(sumX, sumy);
        return new Vector2D(sumX, sumy);
    }
    public Shape getShapeType() {
        return shapeType;
    }

    public float getRestitution() {
        return restitution;
    }

    public Vector2D getOldPos() {
        return oldPos;
    }

    public void setOldPos(Vector2D oldPos) {
        this.oldPos = oldPos;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public float getInverseMass() {
        return inverseMass;
    }

    public void setForce(Vector2D force) {
        this.force = force;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.inverseMass = isStatic ? 0f : 1f/mass;
        this.mass = mass;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2D getForce() {
        return force;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
        if(aStatic) {
            inverseMass = 0;
        }
    }
    public BoundingBox getBoundingBox() {
        this.toWorldCoordinate();
        if(reCalculateBoundingBox) {
            reCalculateBoundingBox = false;
            if(Shape.Polygon.equals(shapeType)) {
                float minx = Float.MAX_VALUE;
                float maxx = -Float.MAX_VALUE;
                float miny = Float.MAX_VALUE;
                float maxy = -Float.MAX_VALUE;
                for(Vector2D w : worldCoordinate) {
                    float y = w.getY();
                    float x = w.getX();
                    if(y < miny) miny = y;
                    if(y > maxy) maxy = y;
                    if(x < minx) minx = x;
                    if(x > maxx) maxx = x;
                }
                boundingBox = new BoundingBox(minx, maxx, miny, maxy);
            } else if(Shape.Circle.equals(shapeType)) {
                float r = worldCoordinate[1].getY();
                Vector2D p = worldCoordinate[0];
                float minx = p.getX() - r;
                float maxx = p.getX() + r;
                float miny = p.getY() - r;
                float maxy = p.getY() + r;
                boundingBox = new BoundingBox(minx, maxx, miny, maxy);
            }else {
                throw new RuntimeException("No shape found.");
            }
        }
        return boundingBox;
    }

    public void setBoundingBox(final BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    public float getInertia() {
        return inertia;
    }

    public void setInertia(float inertia) {
        this.inertia = inertia;
    }

    public float getInertiaInverse() {
        return inertiaInverse;
    }

    public void setInertiaInverse(float inertiaInverse) {
        this.inertiaInverse = inertiaInverse;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getStaticFriction() {
        return staticFriction;
    }

    public float getDynamicFriction() {
        return dynamicFriction;
    }
}

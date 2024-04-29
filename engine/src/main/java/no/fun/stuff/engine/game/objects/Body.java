package no.fun.stuff.engine.game.objects;

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
    private Vector2D force = new Vector2D();
    private Vector2D velocity = new Vector2D();
    private Vector2D acceleration = new Vector2D();
    protected Shape shapeType;
    private boolean reCalculateCoordinate = true;
    private boolean isStatic = false;
    private Vector2D center = new Vector2D();
    protected Vector2D[] localCoordinate;
    protected Vector2D[] worldCoordinate;

    public Vector2D[] toWorldCoordinate() {
        if(reCalculateCoordinate) {
            calculateModel();
            getModel().mul(localCoordinate, worldCoordinate);
            reCalculateCoordinate = false;
        }
        return worldCoordinate;
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
    }
    public void rotateTo(float angle) {
        this.angle = angle;
        reCalculateCoordinate = true;
    }
    public void moveTo(final Vector2D position) {
        pos.setXY(position);
        reCalculateCoordinate = true;
    }
    public void moveTo(float x, float y) {
        pos.setXY(x, y);
        reCalculateCoordinate = true;
    }
    public void move(final Vector2D motionVector) {
        pos.pluss(motionVector);
        reCalculateCoordinate = true;
    }

    public Vector2D[] getVertex() {
        return null;
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
}

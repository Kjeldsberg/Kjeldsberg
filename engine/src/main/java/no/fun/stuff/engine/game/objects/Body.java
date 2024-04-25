package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.matrix.Vector2D;

public abstract class Body extends SceneObject {
    public enum Shape {
        Polygon, Circle
    }
    protected Shape shapeType;
    private boolean reCalculateCoordinate = true;
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
        rotate.rotate(angle);
        reCalculateCoordinate = true;
    }
    public void moveTo(final Vector2D position) {
        translate.translate(position);
        reCalculateCoordinate = true;
    }
    public void moveTo(float x, float y) {
        translate.translate(x, y);
        reCalculateCoordinate = true;
    }
    public void move(final Vector2D motionVector) {
        translate.move(motionVector);
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
}

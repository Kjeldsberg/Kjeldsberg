package no.fun.stuff.engine.game;

import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class RecClickedOn implements Clickable {

    private final Body body;

    public RecClickedOn(final Body body) {
        this.body = body;
    }

    @Override
    public boolean clickedOn(Vector2D position, final Matrix3x3 cameraMatrix) {
        Vector2D[] w = body.toWorldCoordinate();
        Vector2D v0 = w[0];
        Vector2D v1 = w[1];
        Vector2D v2 = w[2];
        Vector2D v3 = w[3];
        Vector2D p = cameraMatrix.mul(position);
        boolean bv0 = p.getX() > v0.getX() && p.getY() > v0.getY();
        boolean bv1 = p.getX() > v1.getX() && p.getY() < v1.getY();
        boolean bv2 = p.getX() < v2.getX() && p.getY() < v2.getY();
        boolean bv3 = p.getX() < v3.getX() && p.getY() > v3.getY();
        return bv0 && bv1 && bv2 && bv3;
    }
//    public boolean clickedOn(Vector2D position) {
//        Vector2D p = new Vector2D(position);
//        p.sub(w[0]);
//        Vector2D v10 = new Vector2D(w[1]);
//        v10.sub(w[0]);
//        Vector2D v21 = new Vector2D(w[2]);
//        v21.sub(w[1]);
//        Vector2D v32 = new Vector2D(w[2]);
//        v32.sub(w[3]);
//        Vector2D v03 = new Vector2D(w[3]);
//        v03.sub(w[0]);
//        float dot = v10.dot(position);
//        if(dot < 0f) return false;
//        float dot1 = v21.dot(position);
//        if(dot1 < 0f) return false;
//        float dot2 = v32.dot(position);
//        if(dot2 < 0f) return false;
//        float dot3 = v03.dot(position);
//        if(dot3 < 0f) return false;
//        return true;
//    }
}

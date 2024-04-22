package no.fun.stuff.engine.game;

import no.fun.stuff.engine.game.objects.Triangle;
import no.fun.stuff.engine.matrix.Matrix2x2;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class TriangleClickedOn implements Clickable {
    private final Vector2D v0 = new Vector2D();
    private final Vector2D v1 = new Vector2D();
    private final Vector2D v2 = new Vector2D();
    private final Triangle triangle;
    public TriangleClickedOn(final Triangle triangle) {
        this.triangle = triangle;
    }

    @Override
    public boolean clickedOn(final Vector2D screenCoordinate, final Matrix3x3 cameraMatrix) {
        Vector2D v = cameraMatrix.mul(screenCoordinate);
        Vector2D[] screen = triangle.toWorldCoordinate();
        v0.setXY(screen[1]);
        v1.setXY(screen[2]);
        v2.setXY(screen[0]);
        v2.sub(screen[1]);
        v1.sub(screen[1]);
//        a	=	(det(v v_2)-det(v_0 v_2))/(det(v_1 v_2))
        float det_v_v2 = v.cross(v2);
        float det_v0_v2 = v0.cross(v2);

//        b	=	-(det(v v_1)-det(v_0 v_1))/(det(v_1 v_2))
        float determinant = v1.cross(v2);
        float a = (det_v_v2 - det_v0_v2)/determinant;
        float det_v_v1 = v.cross(v1);

        float det_v0_v1 = v0.cross(v1);
        float b = -((det_v_v1 - det_v0_v1)/ determinant);
        boolean aConst = a > 0;
        boolean bConst = b > 0;
        float sum = a + b;
        boolean theSum = sum < 1.0f;
        return aConst && bConst && theSum;
    }
}

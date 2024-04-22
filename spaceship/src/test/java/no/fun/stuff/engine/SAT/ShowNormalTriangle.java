package no.fun.stuff.engine.SAT;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.game.TriangleClickedOn;
import no.fun.stuff.engine.game.objects.Circle;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.game.objects.Triangle;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class ShowNormalTriangle extends Triangle implements Clickable {
    private boolean fockus = false;
    private final Matrix3x3 scaleMatrix = new Matrix3x3();

    private Vector2D[] screen = null;
    private final Vector2D mousePosition = new Vector2D();
    private final Vector2D oldMousePosition = new Vector2D();
    private final TriangleClickedOn triangleClickedOn;
    private Circle circle = new Circle(0.1f, 0xffbbbbbb);
    private Circle circle1 = new Circle(0.1f, 0xffbbbbbb);
    private Circle circle2 = new Circle(0.1f, 0xffbbbbbb);


    public boolean isFockus() {
        return fockus;
    }

    public void setFockus(boolean fockus) {
        this.fockus = fockus;
    }

    public void intiMousePosition(final Vector2D mousePosition) {
        oldMousePosition.setXY(mousePosition);
        mousePosition.setXY(mousePosition);

    }

    public void setTheMousePosition(final Vector2D newPosition) {
        oldMousePosition.setXY(mousePosition);
        mousePosition.setXY(newPosition);
    }

    public ShowNormalTriangle(Vector2D p0, Vector2D p1, Vector2D p2, int color) {
        super(p0, p1, p2, color);
        triangleClickedOn = new TriangleClickedOn(this);
        Vector2D v1 = localCoordinate[0].minus(localCoordinate[1]);
        Vector2D v2 = localCoordinate[1].minus(localCoordinate[2]);
        Vector2D v3 = localCoordinate[2].minus(localCoordinate[0]);
        Vector2D drawV1 = new Vector2D(-v1.getY(), v1.getX());
        Vector2D drawV2 = new Vector2D(-v2.getY(), v2.getX());
        Vector2D drawV3 = new Vector2D(-v3.getY(), v3.getX());
        Vector2D mul = getModel().mul(drawV1);
        Vector2D mul2 = getModel().mul(drawV2);
        Vector2D mul3 = getModel().mul(drawV3);
        float length = mul.length();
        float length1 = mul2.length();
        float length2 = mul3.length();
        Vector2D pp1 = mul.add(mul.scale(1 / length));
        Vector2D pp2 = mul2.add(mul2.scale(1 / length1));
        Vector2D pp3 = mul3.add(mul3.scale(1 / length2));

        pos.setXY(getTranslate().getMotionVector());
    }

    @Override
    public void update(final SceneObject gc, float dt) {
        super.update(gc, dt);
        if (!fockus) {
            super.rotate(-0.011f);
//            rotate.rotate(-0.001f);
        }
        if (fockus) {
            Vector2D mVector = mousePosition.minus(oldMousePosition);
            System.out.println("diff: " + mVector);
            float length = mVector.length();
            boolean lengthIsNull = Util.compare(length, 0f);
            if (lengthIsNull) {
                return;
            }
            Matrix3x3 modelInverse = this.calculateInverseModel();
            if (gc != null) {
                Matrix3x3 cameraInverse = gc.calculateInverseModel();
                modelInverse.fastMul(cameraInverse);
            }
            Vector2D mousePos = modelInverse.mul(mousePosition);
            Vector2D oldMousePos = modelInverse.mul(oldMousePosition);
            Vector2D motionVector1 = getTranslate().getMotionVector();
            Vector2D nc = mousePos.minus(motionVector1);
            Vector2D pc = oldMousePos.minus(motionVector1);
            Vector2D minus = nc.minus(pc);
            motionVector1.pluss(minus);
            this.getTranslate().translate(motionVector1);


//                Vector2D mul = modelInverse.mul(mVector);
//                Vector2D minus1 = mousePos.minus(centerPos);
//                Vector2D minus3 = nc.minus(pc);
//                Vector2D minus2 = minus1. minus(pc);
//                Vector2D a = mousePos.minus(oldMousePos);
//                Vector2D oldPosMinusPos = oldMousePos.minus(pos);
//                System.out.println("diff: " + a);
//                float length = a.length();
//                if(!Util.compare(length, 0f)) {
//                    Vector2D add3 = mousePos.minus(pos);//.add(a);
//                    Vector2D add = getTranslate().getMotionVector().add(add3);
//                    this.getTranslate().translate(add);
//                }
        }

    }

    private Vector2D getNewPosition(final Vector2D mousePosition) {
        Vector2D minus = getLocalCenter().minus(mousePosition);
//            minus
        return null;
    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        super.render(parent, r);
        if (parent != null) {
            scaleMatrix.set(parent.getScale());
            scaleMatrix.fastMul(getScale());
        }
        Vector2D mid1 = new Vector2D();
        Vector2D mid2 = new Vector2D();
        Vector2D mid3 = new Vector2D();
        Util.lerp(0.5f, localCoordinate[0], localCoordinate[1], mid1);
        Util.lerp(0.5f, localCoordinate[1], localCoordinate[2], mid2);
        Util.lerp(0.5f, localCoordinate[2], localCoordinate[0], mid3);
        Vector2D v1 = localCoordinate[0].minus(localCoordinate[1]);
        Vector2D v2 = localCoordinate[1].minus(localCoordinate[2]);
        Vector2D v3 = localCoordinate[2].minus(localCoordinate[0]);
        Vector2D drawV1 = new Vector2D(v1.getY(), -v1.getX());
        Vector2D drawV2 = new Vector2D(v2.getY(), -v2.getX());
        Vector2D drawV3 = new Vector2D(v3.getY(), -v3.getX());
        float length = drawV1.length();
        float length1 = drawV2.length();
        float length2 = drawV3.length();
        Vector2D normal = drawV1.scale(1 / length);
        Vector2D normal2 = drawV2.scale(1 / length1);
        Vector2D normal3 = drawV3.scale(1 / length2);
        Vector2D pp1 = mid1.add(normal.scale(length));
        Vector2D pp2 = mid2.add(normal2.scale(length1));
        Vector2D pp3 = mid3.add(normal3.scale(length2));
        final Vector2D[] mid = new Vector2D[] {mid1, mid2, mid3, pp1, pp2, pp3};
        if(screen == null ) {
            screen = new Vector2D[] {new Vector2D(), new Vector2D(), new Vector2D(), new Vector2D(), new Vector2D(), new Vector2D()};
        }
        viewModel.mul(mid, screen);
        Matrix3x3 matrix3x3 = parent.getScale().mulCopy(this.scale);
        Vector2D scale1 = matrix3x3.getScale();
        r.drawCircle(screen[3], 0.1f*scale1.getY(), 0xffeeeeee);
        r.drawCircle(screen[4], 0.1f*scale1.getY(), 0xffeeeeee);
        r.drawCircle(screen[5], 0.1f*scale1.getY(), 0xffeeeeee);
        r.drawBresenhamLine((int) screen[0].getX(), (int) screen[0].getY(), (int) screen[3].getX(), (int) screen[3].getY(), 0xff33bbbb);
        r.drawBresenhamLine((int) screen[1].getX(), (int) screen[1].getY(), (int) screen[4].getX(), (int) screen[4].getY(), 0xff33bbbb);
        r.drawBresenhamLine((int) screen[2].getX(), (int) screen[2].getY(), (int) screen[5].getX(), (int) screen[5].getY(), 0xff33bbbb);
    }

    @Override
    public boolean clickedOn(final Vector2D position, final Matrix3x3 cameraMatrix) {
        fockus = super.clickedOn(position, cameraMatrix);
        return fockus;
    }

    public Vector2D getMousePosition() {
        return mousePosition;
    }

    public void setMousePosition(final Vector2D mousePosition) {
        mousePosition.setXY(mousePosition);
    }

}

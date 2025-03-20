package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.game.objects.Triangle;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.List;
import java.util.Map;

public class TheShip extends Triangle {
    private Vector2D leftRocket = new Vector2D();
    private Vector2D leftHallNormal = new Vector2D();

    private Vector2D leftNormal = new Vector2D(-1f, 0f);
    private Vector2D rightRocket = new Vector2D();
    private Vector2D rightHallNormal = new Vector2D();
    private Vector2D rightNormal = new Vector2D(1f, 0f);
    private Vector2D mainRocketDirection = new Vector2D();
    private Vector2D mainRocketPosition = new Vector2D();
    private Vector2D upVector = new Vector2D(0f, -1f);

    private Vector2D rr = new Vector2D();
    private Vector2D rl = new Vector2D();
    private Vector2D rrPrep = new Vector2D();
    private Vector2D rlPrep = new Vector2D();
    private boolean leftPress = false;
    private boolean rightPress = false;
    private boolean upPress = false;
    private float mainRocketForce = 21;
    private float steeringRocketForce = 5;
    final Vector2D theRocketForce = new Vector2D(5f, 0);
    private List<Vector2D> internalForces;
    private final Vector2D flame[] = {
            new Vector2D(0f, 0f),
            new Vector2D(-1f, 3f),
            new Vector2D(-0.5f, 2.7f),
            new Vector2D(-0.9f, 3.5f),
            new Vector2D(-0.25f, 3.25f),
            new Vector2D(0f, 3.5f),
            new Vector2D(0.25f, 3.25f),
            new Vector2D(0.9f, 3.5f),
            new Vector2D(0.5f, 2.7f),
            new Vector2D(1f, 3f),
            new Vector2D(0f, 0f)

    };
    private final Vector2D flameTranslated[] = new Vector2D[flame.length];

    public Vector2D getMainRocketDirection() {
        return mainRocketDirection;
    }

    public boolean isUpPress() {
        return upPress;
    }

    public void setUpPress(boolean upPress) {
        this.upPress = upPress;
    }

    public boolean isRightPress() {
        return rightPress;
    }

    public void setRightPress(boolean rightPress) {
        this.rightPress = rightPress;
    }

    public boolean isLeftPress() {
        return leftPress;
    }

    public void setLeftPress(boolean leftPress) {
        this.leftPress = leftPress;
    }

    public TheShip(final Vector2D p0, Vector2D p1, Vector2D p2, int color) {
        super(p0, p1, p2, color);
        Util.lerp(0.25f, localCoordinate[0], localCoordinate[1], leftRocket);
        Util.lerp(0.25f, localCoordinate[0], localCoordinate[2], rightRocket);
        Util.lerp(0.5f, localCoordinate[1], localCoordinate[2], mainRocketPosition);
        Vector2D leftHall = localCoordinate[1].minus(localCoordinate[0]);
        Vector2D rightHall = localCoordinate[2].minus(localCoordinate[0]);
        leftHallNormal.setXY(leftHall.getY(), -leftHall.getX());
        rightHallNormal.setXY(rightHall.getY(), -rightHall.getX());
        rl.setXY(leftRocket);
        rl.sub(getLocalCenter());
        rr.setXY(getRightRocket());
        rr.sub(getLocalCenter());
        rlPrep.setXY(rl.getY(), -rl.getX());
        rrPrep.setXY(rr.getY(), -rr.getX());
        setDensity(Body.WaterDensity);
//        float v = getInertia() * 20f;
//        setInertia(v);
//        setInertiaInverse(1f/v);
        setRestitution(0.3f);
        setStaticFriction(0.01f);
        reCalculateProperties();
        setWireFrame(true);
//        mainRocketForce *= getMass();
        Matrix3x3 translate = new Matrix3x3();
        Matrix3x3 scale = new Matrix3x3();
        scale.scale(0.5f);
        translate.translate(mainRocketPosition);
        translate.fastMul(scale);
        for (int i = 0; i < flameTranslated.length; i++) {
            flameTranslated[i] = new Vector2D();
        }
        translate.mul(flame, flameTranslated);
    }

    public Vector2D getLeftRocket() {
        return leftRocket;
    }

    public Vector2D getRightRocket() {
        return rightRocket;
    }

    @Override
    public Vector2D applyForces() {
//        Vector2D ret = new Vector2D();
//        for(final Vector2D f : internalForces) {
//            ret.pluss(f);
//        }
//        return ret;
        if (this.upPress) {
            int test = 0;
        }
        return mainRocketDirection;
    }

    @Override
    public void update(SceneObject parent, float dt) {

        final Vector2D theRocketForce = new Vector2D(rightNormal).scale(steeringRocketForce);
        if (leftPress) {
            float angularVelocity = getAngularVelocity();
            float v = rlPrep.cross(theRocketForce) * getInertiaInverse();
            angularVelocity += v;
            setAngularVelocity(angularVelocity);
        }
        if (rightPress) {
            final Vector2D rightForce = theRocketForce.scale(-1);
            float angularVelocity = getAngularVelocity();
            float v = rlPrep.cross(rightForce) * getInertiaInverse();
            angularVelocity += v;
            setAngularVelocity(angularVelocity);
        }
        this.rotate(getAngularVelocity() * dt);
        if (upPress) {
            mainRocketDirection.setXY(this.rotate.mul(upVector));
            mainRocketDirection.mul(mainRocketForce);
        } else {
            mainRocketDirection.setXY(0f, 0f);
        }
    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        super.render(parent, r);
        Matrix3x3 mat = getViewModel();
//        Vector2D mul = mat.mul(localCoordinate[0]);
//        Vector2D mul2 = mat.mul(localCoordinate[1]);
//        Vector2D mul3 = mat.mul(localCoordinate[2]);
//        r.drawCircle(mul, 6f, 0xff00bb00);
//        r.drawCircle(mul2, 6f, 0xff00bb00);
//        r.drawCircle(mul3, 6f, 0xff00bb00);
//        r.drawCircle(mat.mul(getLocalCenter()), 6f, 0xff00bb00);

        final Vector2D rocLeftPer = new Vector2D(rlPrep);
        float length = rocLeftPer.length();
        rocLeftPer.mul(1f / length);

        if (leftPress) {
            Vector2D pos = rightRocket.add(rightNormal.scale(steeringRocketForce));
            r.drawLine(mat.mul(rightRocket), mat.mul(pos), 0xff00bbbb);
        }
        if (rightPress) {
            Vector2D pos = leftRocket.add(leftNormal.scale(steeringRocketForce));
            r.drawLine(mat.mul(leftRocket), mat.mul(pos), 0xff00bbbb);
        }
        if (upPress) {
            for (int i = 0; i < flameTranslated.length - 1; i++) {
                r.drawLine(mat.mul(flameTranslated[i]), mat.mul(flameTranslated[i + 1]), 0xff770077);
            }

        }
//        setRightPress(false);
//        setLeftPress(false);
    }
}

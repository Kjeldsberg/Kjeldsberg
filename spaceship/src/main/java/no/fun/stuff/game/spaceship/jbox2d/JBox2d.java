package no.fun.stuff.game.spaceship.jbox2d;

import no.fun.stuff.engine.game.Rect;
import no.fun.stuff.engine.game.SceneObject;
import no.fun.stuff.engine.matrix.Vector2D;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class JBox2d {
    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f/60.0f;
    private int velocityIteration = 8;
    private int positionIteration = 3;

    private final World jBox2dWorld;
    public JBox2d(final Vector2D gravity) {
        jBox2dWorld = new World(new Vec2(gravity.getX(), gravity.getY()));
    }

    public void add(final SceneObject object) {
        if(object instanceof Rect) {
            final Rect ob = (Rect) object;
            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = object.getAngle();

            bodyDef.type = BodyType.valueOf(ob.getBodyType().name());
            bodyDef.position.set(ob.getPos().getX(), ob.getPos().getY());

            Body ground = jBox2dWorld.createBody(bodyDef);

            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(ob.getHalfWidth(), ob.getHalfWidth());
            ground.createFixture(polygonShape,0.0f);
        }
    }

    public void update(float dt) {
        physicsTime += dt;
        if(physicsTime >= 0) {
            physicsTime -= physicsTimeStep;
            jBox2dWorld.step(physicsTimeStep, velocityIteration, positionIteration);
        }
    }
}

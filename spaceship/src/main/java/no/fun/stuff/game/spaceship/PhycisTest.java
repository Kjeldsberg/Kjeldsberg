package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.BodyType;
import no.fun.stuff.engine.game.Rect;
import no.fun.stuff.engine.game.SceneObject;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.game.spaceship.jbox2d.JBox2d;

public class PhycisTest extends SceneObject {
    private final Rect rec;
    private final JBox2d jBox2d = new JBox2d(new Vector2D(0.0f, -9.81f));

    public PhycisTest() {

        Rect ground = new Rect(new Vector2D(100.0f, 10.f), new Vector2D(100, 100));
        ground.setBodyType(BodyType.STATIC);
        this.addChild(ground);
        jBox2d.add(ground);

        rec = new Rect(new Vector2D(10, 10), pos);
        rec.setBodyType(BodyType.DYNAMIC);
        


    }
    @Override
    public void update(SceneObject parent, float dt) {

    }

    @Override
    public void render(SceneObject parent, Renderer r) {
        rec.render(parent, r);
    }
}

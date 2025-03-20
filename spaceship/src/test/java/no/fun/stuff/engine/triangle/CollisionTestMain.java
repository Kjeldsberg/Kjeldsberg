package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.game.objects.NewRectangle;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.game.spaceship.TriangleScene;

public class CollisionTestMain extends AbstractGame {
    public static final float HEIGHT = 4f;
    private final TriangleScene scene = new TriangleScene();
    private LookAtCamera camera;
    class Re extends NewRectangle {

        public Re(float width, float height) {
            super(width, height);
        }

        @Override
        public void update(SceneObject parent, float dt) {
            super.update(parent, dt);
            rotate(0.01f);
        }
    }
    @Override
    public void init(GameContainer gc) {
        NewRectangle rectangle = new NewRectangle(4f, HEIGHT);
        NewRectangle rectangle1 = new NewRectangle(2f, 2f);
        NewRectangle rectangle2 = new NewRectangle(0.5f, 0.5f);
        scene.addChild(rectangle);
        scene.addChild(rectangle1);
        scene.addChild(rectangle2);
        rectangle1.rotate(3.1415f/4f);
        Vector2D[] rec = rectangle.toWorldCoordinate();
        Vector2D[] rec1 = rectangle1.toWorldCoordinate();
//
        Vector2D ap = rec[2].minus(rec1[0]);
        Vector2D ab = rec[2].minus(rec[1]);
        float dot = ab.dot(ap);
        float v = ab.lengthSquare();
        Vector2D cp = rec[2].add(ap.scale(dot / v));
        float x = 0f;
        float y = 0f;
        for(int i =0;i<rec1.length;i++) {
            x += rec1[i].getX();
            y += rec1[i].getY();
        }
        x /= rec.length;
        y /= rec.length;
        Vector2D minus1 = new Vector2D(x, y+HEIGHT/2f).minus(rec1[0]);
        rectangle1.moveTo(minus1);
        rectangle2.moveTo(rectangle1.toWorldCoordinate()[0]);
        Vector2D screen = new Vector2D(gc.getWith(), gc.getHeight());
        camera = new LookAtCamera(rectangle, screen, new Vector2D(10f, 8f));
        scene.setCamera(camera);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        scene.update(null, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(null, r);
    }
    public static void main(final String[] args) {
        GameContainer gc = new GameContainer(new CollisionTestMain());
        gc.setWith(800);
        gc.setHeight(600);
        gc.setScale(1.0f);
        gc.start();
    }

}

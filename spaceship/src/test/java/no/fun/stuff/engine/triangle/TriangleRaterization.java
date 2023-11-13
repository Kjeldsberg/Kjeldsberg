package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.matrix.Vector2D;

import java.awt.event.KeyEvent;

public class TriangleRaterization extends AbstractGame {

    private TriangleScene scene = new TriangleScene();
    private Triangle flatTop;
    private boolean spaceDown = false;
    private boolean leftKey = false;
    private boolean rightKey = false;
    @Override
    public void init(GameContainer gc) {
        gc.getRenderer().setAmbientcolor(-1);

        flatTop = new Triangle(
                new Vector2D(0, 20),
                new Vector2D(15, 40),
                new Vector2D(30, 20), 0xffff0000);
        flatTop.getScale().scale(3.0f);
        flatTop.getTranslate().translate(new Vector2D(160, 120));
        Triangle flatBottom = new Triangle(
                new Vector2D(15, 20),
                new Vector2D(0, 40),
                new Vector2D(30, 40), 0xff00ff00);
        flatBottom.getScale().scale(3.0f);
        flatBottom.getTranslate().translate(new Vector2D(60, 75));
        Triangle triangle = new Triangle(
                new Vector2D(0, 20),
                new Vector2D(30, 30),
                new Vector2D(0, 40), 0xffdddddd);
        triangle.getScale().scale(3.0f);
        triangle.getTranslate().translate(new Vector2D(160, 65));
        triangle.setAngle(0.01f);
        LookAtCamera camera = new LookAtCamera(flatTop, new Vector2D(gc.getWith(), gc.getHeight()));
//        scene.setCamera(camera);
        scene.getChild().add(flatTop);
        flatTop.getRotate().rotate(0.0f);
//        scene.getChild().add(flatBottom);
//        scene.getChild().add(triangle);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if(gc.getInput().isKeyDown(KeyEvent.VK_LEFT)) {
            leftKey = true;
        }
        if(!gc.getInput().isKeyDown(KeyEvent.VK_LEFT) && leftKey) {
            flatTop.getRotate().rotate(0.01f);
            leftKey = false;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_RIGHT)) {
            rightKey = true;
        }
        if(!gc.getInput().isKeyDown(KeyEvent.VK_RIGHT) && rightKey) {
            flatTop.getRotate().rotate(0.01f);
            rightKey = false;
        }
        scene.update(null, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(null, r);
    }

    public static void main(final String[] args) {
        GameContainer gc = new GameContainer(new TriangleRaterization());
        gc.setWith(320);
        gc.setHeight(240);
        gc.setScale(3.0f);
        gc.start();

    }
}


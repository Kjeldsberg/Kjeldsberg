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
    private ObjectAsTriangle objectAsTriangle;
    @Override
    public void init(GameContainer gc) {
        gc.getRenderer().setAmbientcolor(-1);

        flatTop = new Triangle(
                new Vector2D(0, 20),
                new Vector2D(15, 40),
                new Vector2D(30, 20), 0xffff0000);
        flatTop.getScale().scale(10.0f);
        flatTop.getTranslate().translate(new Vector2D(160, 120));
        Triangle flatBottom = new Triangle(
                new Vector2D(15, 20),
                new Vector2D(0, 40),
                new Vector2D(30, 40), 0xff00ff00);
        flatBottom.getScale().scale(3.0f);
        flatBottom.getTranslate().translate(new Vector2D(60, 75));
        flatBottom.getRotate().rotate(-0.02f);
        Triangle triangle = new Triangle(
                new Vector2D(0, 20),
                new Vector2D(30, 30),
                new Vector2D(0, 40), 0xffdddddd);
        triangle.getScale().scale(3.0f);
        triangle.getTranslate().translate(new Vector2D(160, 65));
        objectAsTriangle = new ObjectAsTriangle();
        objectAsTriangle.getScale().scale(1.0f);
        triangle.setAngle((float)Math.PI);
        LookAtCamera camera = new LookAtCamera(objectAsTriangle, new Vector2D(gc.getWith(), gc.getHeight()));
        scene.setCamera(camera);
        scene.getChild().add(flatTop);
        flatTop.getRotate().rotate(0.02f);
        scene.getChild().add(flatBottom);
        scene.getChild().add(triangle);
        scene.getChild().add(objectAsTriangle);
    }

    @Override
    public void update(GameContainer gc, float dt) {
//        if(gc.getInput().)
        if(gc.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
            objectAsTriangle.setRotating(!objectAsTriangle.isRotating());
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_LEFT)) {
            leftKey = true;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            float scaleValue = objectAsTriangle.getScaleValue();
            scaleValue += 0.01f;
            objectAsTriangle.setScaleValue(scaleValue);
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_S)) {
            float scaleValue = objectAsTriangle.getScaleValue();
            scaleValue -= 0.01f;
            objectAsTriangle.setScaleValue(scaleValue);
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
        float rotation = objectAsTriangle.getRotation();
        gc.getRenderer().drawText("Rad: " + rotation, 10, 230, 0xffffffff);

    }

    public static void main(final String[] args) {
        GameContainer gc = new GameContainer(new TriangleRaterization());
        gc.setWith(320);
        gc.setHeight(240);
//        gc.setWith(640);
//        gc.setHeight(480);
        gc.setScale(3.0f);
        gc.start();

    }
}


package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.game.spaceship.TriangleScene;

import java.awt.event.KeyEvent;

public class ManyManyTriangles extends AbstractGame {
    private final TriangleScene scene = new TriangleScene();
    private ObjectAsTriangle objectAsTriangle;
    private boolean rotate = false;
    private LookAtCamera lookAtCamera;
    @Override
    public void init(GameContainer gc) {

        objectAsTriangle = new ObjectAsTriangle(100, 100);

        //∕        e = new SimpleSquareWithTriangle.Square(gc);
        scene.addChild(objectAsTriangle);
//        scene.addChild(new ObjectAsTriangle());
        Vector2D screenSize = new Vector2D(gc.getWith(), gc.getHeight());
        lookAtCamera = new LookAtCamera(objectAsTriangle, screenSize, screenSize);
        scene.setCamera(lookAtCamera);

    }

    @Override
    public void update(GameContainer gc, float dt) {
        if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            float scaleValue = objectAsTriangle.getScaleValue();
            scaleValue += 0.05f;
            lookAtCamera.setZoom(0.05f);
//            objectAsTriangle.setScaleValue(scaleValue);
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_S)) {
            float scaleValue = objectAsTriangle.getScaleValue();
            scaleValue -= 0.05f;
            lookAtCamera.setZoom(-0.05f);
//            objectAsTriangle.setScaleValue(scaleValue);
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
            rotate = !rotate;
        }
        objectAsTriangle.setRotating(rotate);
        scene.update(null, dt);

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(null, gc.getRenderer() );
    }
    public static void main(final String[] args) {
        GameContainer gc = new GameContainer(new ManyManyTriangles());
        gc.setWith(800);
        gc.setHeight(600);
//        gc.setWith(640);
//        gc.setHeight(480);
        gc.setScale(1.0f);
        gc.start();

    }

}

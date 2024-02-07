package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.matrix.Vector2D;

import java.awt.event.KeyEvent;

public class ManyManyTriangles extends AbstractGame {
    private final TriangleScene scene = new TriangleScene();
    private ObjectAsTriangle objectAsTriangle;
    private boolean rotate = false;
    @Override
    public void init(GameContainer gc) {

        objectAsTriangle = new ObjectAsTriangle(50, 50);
        gc.getRenderer().setAmbientcolor(-1);
//∕        e = new SimpleSquareWithTriangle.Square(gc);
        scene.addChild(objectAsTriangle);
        LookAtCamera lookAtCamera = new LookAtCamera(objectAsTriangle, new Vector2D(gc.getWith(), gc.getHeight()));
        scene.setCamera(lookAtCamera);

    }

    @Override
    public void update(GameContainer gc, float dt) {
        if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            float scaleValue = objectAsTriangle.getScaleValue();
            scaleValue += 0.01f;
            objectAsTriangle.setScaleValue(scaleValue);
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_S)) {
            float scaleValue = objectAsTriangle.getScaleValue();
            scaleValue -= 0.01f;
            objectAsTriangle.setScaleValue(scaleValue);
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
        gc.setWith(320);
        gc.setHeight(240);
//        gc.setWith(640);
//        gc.setHeight(480);
        gc.setScale(3.0f);
        gc.start();

    }

}

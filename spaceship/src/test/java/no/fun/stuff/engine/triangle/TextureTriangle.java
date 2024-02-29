package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.matrix.Vector2D;

import java.awt.event.KeyEvent;

public class TextureTriangle extends AbstractGame {
    private PlaneWithTexture planeWithTexture;// = new PlaneWithTexture(1, 1);
    private TriangleScene scene = new TriangleScene();
    private boolean rotate;
    @Override
    public void init(GameContainer gc) {
        planeWithTexture = new PlaneWithTexture(2, 2);
        gc.getRenderer().setAmbientcolor(-1);
//∕        e = new SimpleSquareWithTriangle.Square(gc);
        scene.addChild(planeWithTexture);
        LookAtCamera lookAtCamera = new LookAtCamera(planeWithTexture, new Vector2D(gc.getWith(), gc.getHeight()));
        scene.setCamera(lookAtCamera);

    }

    @Override
    public void update(GameContainer gc, float dt) {
        if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            float scaleValue = planeWithTexture.getScaleValue();
            scaleValue += 0.01f;
            planeWithTexture.setScaleValue(scaleValue);
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_S)) {
            float scaleValue = planeWithTexture.getScaleValue();
            scaleValue -= 0.01f;
            planeWithTexture.setScaleValue(scaleValue);
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
            rotate = !rotate;
        }
        planeWithTexture.setRotating(rotate);
        scene.update(null, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(null, gc.getRenderer() );

    }
    public static void main(final String[] args) {
        GameContainer gc = new GameContainer(new TextureTriangle());
        gc.setWith(800);
        gc.setHeight(600);
//        gc.setWith(640);
//        gc.setHeight(480);
        gc.setScale(1.0f);
        gc.start();

    }

}

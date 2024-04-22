package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Input;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.game.objects.Circle;
import no.fun.stuff.engine.game.objects.Grid;
import no.fun.stuff.engine.matrix.Vector2D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public class CircleMain extends AbstractGame {

    Circle circle = new Circle(5.0f, 0xff000000);
    private final TriangleScene scene = new TriangleScene();
    private LookAtCamera camera;
    public void init(GameContainer gc) {
        scene.addChild(circle);
        scene.addChild(new Grid(40, 30, 1.0f, 1.0f));
        camera = new LookAtCamera(circle, new Vector2D(gc.getWith(), gc.getHeight()));
//        camera.setZoom(1.0f);
        scene.setCamera(camera);

    }
    private final Vector2D mousePosition = new Vector2D();
    @Override
    public void update(GameContainer gc, float dt) {
        final Input input = gc.getInput();
        if(input.isButton(MouseEvent.BUTTON1)) {
            mousePosition.setXY(input.getMouseX(), input.getMouseY());
            List<Clickable> clickable = scene.checkPicked(mousePosition);
            if(!clickable.isEmpty()) {
                camera.getRotate().rotate(0.11f);
                int test = 0;
            }
        }

        if(input.isKey(KeyEvent.VK_A)) {
            int test = 0;
            camera.setZoom(1.0f);
        }
        if(input.isKey(KeyEvent.VK_S)) {
            camera.setZoom(-1.0f);
        }
        scene.update(null, dt);

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(null, gc.getRenderer() );
    }
    public static void main(final String[] args) {
        GameContainer gc = new GameContainer(new CircleMain());
        gc.setWith(800);
        gc.setHeight(600);
//        gc.setWith(640);
//        gc.setHeight(480);
        gc.setScale(1.0f);
        gc.start();

    }

}

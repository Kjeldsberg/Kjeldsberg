package no.fun.stuff.engine.SAT;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Input;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Camera2D;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.game.objects.*;
import no.fun.stuff.engine.game.physics.Integrator;
import no.fun.stuff.engine.game.physics.collition.Collision;
import no.fun.stuff.engine.game.physics.collition.SAT;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.game.spaceship.TriangleScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SATMain extends AbstractGame {
    private final TriangleScene scene = new TriangleScene();
    private boolean rotate = false;
    private Camera2D lookAtCamera;
    private final List<Clickable> clickable = new ArrayList<>();
    private final SAT sat = new SAT();
    private Integrator integrator = new Integrator(Arrays.asList(new Vector2D(0f, 9.81f)));
    class SATTriangle extends Triangle {
        @Override
        public void render(SceneObject parent, Renderer r) {
            int tempColor = getColor();
            setColor(isStatic() ? 0xff111111 : getColor());
            super.render(parent, r);
            setColor(tempColor);
        }
    }
    @Override
    public void init(GameContainer gc) {
        Vector2D screenSize = new Vector2D(gc.getWith(), gc.getHeight());
        Vector2D viewPort = new Vector2D(20.0f, 15.0f);
        lookAtCamera = new Camera2D(screenSize, viewPort);
//        integrator = new Integrator();
//        integrator.addForce(new Vector2D(0f, 9.81f));

//        Triangle triangle = new ShowNormalTriangle(new Vector2D(0.0f, 0.0f),
//                new Vector2D(0.0f, 1.0f),
//                new Vector2D(1.0f, 0.0f), 0xffaa11aa);
//        Triangle  triangle2 = new ShowNormalTriangle(new Vector2D(0.0f, 0.0f),
//                new Vector2D(0.0f, 1.0f),
//                new Vector2D(1.0f, 0.0f), 0xffaaaaaa);
//        triangle.setMass(1.5f);
//        scene.addChild(triangle);
//        scene.addChild(triangle2);
//        Triangle triangl = new Triangle(new Vector2D(0.0f, 0.0f),
//                new Vector2D(0.0f, 1.0f),
//                new Vector2D(1.0f, 0.0f), 0xff0000aa);
//        scene.addChild(triangl);
        for (int i = 0; i < 10; i++) {
            Triangle triangle1 = new Triangle(new Vector2D(0.0f, 0.0f),
                    new Vector2D(0.0f, 1.0f),
                    new Vector2D(1.0f, 0.0f), 0xffaa11aa);
            final Vector2D triangle1Pos = new Vector2D(
            (float) Math.random() * viewPort.getX(),
            (float) Math.random() * viewPort.getY());
            boolean aStaticOne = Math.random() > 0.5;
            triangle1.setStatic(aStaticOne);
            if(aStaticOne) {
                triangle1.setColor(0xff111111);
            }
            triangle1.moveTo(triangle1Pos);
            scene.addChild(triangle1);
        }
        for (int i = 0; i < 3; i++) {
            Circle circle = new Circle(0.5f, 0xff99aa22);
            final Vector2D randPos = new Vector2D(
                    (float) Math.random() * viewPort.getX(),
                    (float) Math.random() * viewPort.getY());
            boolean aStatic = Math.random() > 0.5;
            circle.setStatic(false);
            if(aStatic) circle.setColor(0xff111111);
            circle.setMass(1.5f);
            circle.moveTo(randPos);
            scene.addChild(circle);
        }
        for (int i = 0; i < 1; i++) {
            NewRectangle rectangle = new NewRectangle(1f, 1f);
            final Vector2D randPos = new Vector2D(
                    (float) Math.random() * viewPort.getX(),
                    (float) Math.random() * viewPort.getY());
            rectangle.setMass(1.1f);
            boolean aStatic = Math.random() > 0.5;
            rectangle.setStatic(true);
            if(aStatic) {
                rectangle.setColor(0xff111111);
            }
            Vector2D inTheMiddle = new Vector2D(viewPort.getX() / 2f, viewPort.getY() / 2f);
            rectangle.moveTo(inTheMiddle);
            rectangle.rotateTo(3.14f/4f);
            scene.addChild(rectangle);
        }
        for (int i = 0; i < 5; i++) {
            NewRectangle rectangle = new NewRectangle(1f, 1f);
            final Vector2D randPos = new Vector2D(
                    (float) Math.random() * viewPort.getX(),
                    (float) Math.random() * viewPort.getY());
            rectangle.setMass(20.1f);
            rectangle.moveTo(randPos);
            scene.addChild(rectangle);
        }
        scene.setCamera(lookAtCamera);

    }

    @Override
    public void update(GameContainer gc, float dt) {
        Input input = gc.getInput();
        if (input.isKey(KeyEvent.VK_A)) {
            lookAtCamera.setZoom(1.5f);
        }
        if (input.isKey(KeyEvent.VK_S)) {
            lookAtCamera.setZoom(-1.5f);
        }
        if (input.isKey(KeyEvent.VK_SPACE)) {
            rotate = !rotate;
        }
        float dx = 0;
        float dy = 0;
        if (input.isKey(KeyEvent.VK_LEFT)) {
            dx--;
        }
        if (input.isKey(KeyEvent.VK_RIGHT)) {
            dx++;
        }
        if (input.isKey(KeyEvent.VK_UP)) {
            dy--;
        }
        if (input.isKey(KeyEvent.VK_DOWN)) {
            dy++;
        }
        float force = 20f;
        Vector2D direction = new Vector2D(dx, dy);
        direction.normaize();

        if (input.isButton(MouseEvent.BUTTON1)) {
            clickable.clear();
            Vector2D position = new Vector2D(input.getMouseX(), input.getMouseY());
            clickable.addAll(scene.checkPicked(position));
        }
        if (clickable.size() > 0) {
            for (Clickable cli : clickable) {
//                ((Body) cli).move(direction);
                if (cli instanceof ShowNormalTriangle s) {
                    Vector2D mousePos = new Vector2D();
                    mousePos.setX(input.getMouseX());
                    mousePos.setY(input.getMouseY());
                    s.setTheMousePosition(mousePos);
                } else if (cli instanceof Triangle t) {
                    t.setColor((t.getColor() + 4) | 0xff000000);
                    Vector2D acc = direction.mul(force);
//                    c.getForce().setXY(acc);
//                    c.set(acc);
                    t.setForce(acc);
                } else if (cli instanceof NewRectangle t) {
                    int color = t.getColor() + 0x1000;
                    t.setColor(color | 0xff000000);
                    if(!t.isStatic()) {
                        t.getForce().setXY(direction.mul(force));
                    }
                } else if (cli instanceof Circle c) {
                    Vector2D acc = direction.mul(force);
//                    c.getForce().setXY(acc);
//                    c.set(acc);
                    c.setForce(acc);
                    int color = c.getColor() + 0x1000;
                    c.setColor(color | 0xff000000);
                }
            }
        }
//        if (!input.isButton(MouseEvent.BUTTON1)) {
//            if (clickable != null) {
//                if (clickable instanceof ShowNormalTriangle) {
//                    ((ShowNormalTriangle) clickable).setFockus(false);
//                }
//            }
//            clickable = null;
//        }
        input.updata();
        for(SceneObject s : scene.getChild()) {
            if(s instanceof Body b) {
                integrator.integrate(b, dt);
            }
        }
        Collision collision = new Collision();
        collision.collision(scene, dt);
        scene.update(null, dt);
        wrapScreen(scene);

    }
    private void wrapScreen(SceneObject sceneObject) {
        for(int i=0;i< sceneObject.getChild().size();i++) {
            SceneObject sceneObject1 = sceneObject.getChild().get(i);
            if(sceneObject1 instanceof Body b) {
                Vector2D motionVector = sceneObject1.getTranslate().getMotionVector();
                Vector2D screenSize = lookAtCamera.getViewPort();
                if(motionVector.getX() < 0f)
                    b.moveTo(motionVector.add(new Vector2D(screenSize.getX(), 0)));
                if(motionVector.getX() > screenSize.getX())
                    b.moveTo(motionVector.minus(new Vector2D(screenSize.getX(), 0)));
                if(motionVector.getY() < 0f)
                    b.moveTo(motionVector.add(new Vector2D(0, screenSize.getY())));
                if(motionVector.getY() > screenSize.getY())
                    b.moveTo(motionVector.minus(new Vector2D(0,screenSize.getY())));

            }
        }
    }
    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(null, gc.getRenderer());
    }

    public static void main(final String[] args) {
        GameContainer gc = new GameContainer(new SATMain());
        gc.setWith(800);
        gc.setHeight(600);
//        gc.setWith(640);
//        gc.setHeight(480);
        gc.setScale(1.0f);
        gc.start();

    }

}

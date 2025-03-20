package no.fun.stuff.engine.stacking;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Input;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Camera2D;
import no.fun.stuff.engine.game.objects.*;
import no.fun.stuff.engine.game.physics.VerletWithVelocityIntegration;
import no.fun.stuff.engine.game.physics.Integrate;
import no.fun.stuff.engine.game.physics.collition.Collision;
import no.fun.stuff.engine.game.physics.collition.CollisionInfo;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.game.spaceship.TriangleScene;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class StackingMain extends AbstractGame {
    private final TriangleScene scene = new TriangleScene();
    private final Collision collision = new Collision();
    private Camera2D camera2D;
    private long startTime = System.currentTimeMillis();
    private long aSecond = System.currentTimeMillis();
    private long updateTime = 0;
    private int updateNummer = 0;
    private static GameContainer gc;
    private Vector2D viewPort;
//    private Integrator integrator = new Integrator(Arrays.asList(new Vector2D(0f, 9.81f)));
    private Integrate integrator = new VerletWithVelocityIntegration();
//    private Integrate integrator = new VerletIntegrate(Arrays.asList(new Vector2D(0f, 9.81f)));
    private List<SceneObject> contactPointList = new ArrayList<>();
    @Override
    public void init(GameContainer gc) {
        viewPort = new Vector2D(40f, 30f);
        camera2D = new Camera2D(new Vector2D(gc.getWith(), gc.getHeight()), viewPort);
        scene.setCamera(camera2D);
        NewRectangle rectangle = new NewRectangle(viewPort.getX() * 0.9f, viewPort.getY() * 0.1f);
        rectangle.setStatic(true);
        rectangle.initPos(new Vector2D(viewPort.getX() * 0.5f, viewPort.getY() * 0.9f));
        scene.addChild(rectangle);
        NewRectangle leftRank = new NewRectangle(viewPort.getX() * 0.6f, viewPort.getY() * 0.05f);
        leftRank.setStatic(true);
        leftRank.initPos(new Vector2D(viewPort.getX() * 0.3f, viewPort.getY() * 0.5f));
        leftRank.rotate(3.14f/6);
        scene.addChild(leftRank);
        NewRectangle rightRank = new NewRectangle(viewPort.getX() * 0.6f, viewPort.getY() * 0.05f);
        rightRank.setStatic(true);
        rightRank.initPos(new Vector2D(viewPort.getX() * 0.8f, viewPort.getY() * 0.2f));
        rightRank.rotate(-3.14f/5);
        scene.addChild(rightRank);
        Triangle triangle = new Triangle(new Vector2D(0.0f, 0.0f),
                new Vector2D(0.0f, 1.0f),
                new Vector2D(1.0f, 0.0f), 0xffaa11aa);
        triangle.moveTo(viewPort.getX() * 0.5f, viewPort.getY() * 0.7f);
        triangle.initPos(triangle.getPos());
        triangle.getScale().scale(2f);
        scene.addChild(triangle);
        Circle circle = new Circle(2f, 0xff11ee22);
        circle.initPos(new Vector2D(viewPort.getX() * 0.5f, viewPort.getY() * 0.7f));
//        scene.addChild(circle);
//        NewRectangle rectangle1 = new NewRectangle(Util.rand(0.75f, 1.25f), Util.rand(0.75f, 1.25f));
        NewRectangle rectangle1 = new NewRectangle(5.0f, 4.0f);
        rectangle1.initPos(new Vector2D(viewPort.getX() * 0.5f, viewPort.getY() * 0.7f));
        rectangle1.rotateTo(3.14f/4f);
//        scene.addChild(rectangle1);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        long start = System.currentTimeMillis();
        updateNummer++;
        final Input input = gc.getInput();
        boolean keyUp = input.isKeyUp(MouseEvent.BUTTON1);
        if(keyUp) {
            int test = 0;
        }
        boolean buttonDown = input.isButtonDown(MouseEvent.BUTTON1);
        boolean buttonDown2 = input.isButtonDown(MouseEvent.BUTTON2);
        boolean buttonDown3 = input.isButtonDown(MouseEvent.BUTTON3);
        final Vector2D mousePosition = new Vector2D(input.getMouseX(), input.getMouseY());
        final Matrix3x3 inverseCameraModel = camera2D.calculateInverseModel();
        Vector2D worldPosition = inverseCameraModel.mul(mousePosition);
        if(buttonDown) {
            double rand = Math.random() * 3;
            if(rand < 1.0) {
                scene.addChild(new Circle(worldPosition, Util.rand(0.5f, 1.25f), 0xff77ff66));
            } else if (rand > 1.0f && rand < 2.0f) {
                Triangle triangle = new Triangle(new Vector2D(0f, 0f),
                        new Vector2D(0f, 1f),
                        new Vector2D(1f, 0f)
                        , 0xffee4444);
                triangle.initPos(worldPosition);
            } else if (rand > 2.0) {

            }
        }
        if(buttonDown2) {
            NewRectangle rectangle = new NewRectangle(Util.rand(0.75f, 1.25f), Util.rand(0.75f, 1.25f));
            rectangle.initPos(worldPosition);
            rectangle.rotateTo(3.14f/4f);
            scene.addChild(rectangle);

        }
        if(buttonDown3) {
            int r = (int)Util.rand(0f, 256f);
            int g = (int)Util.rand(0f, 256f);
            int b = (int)Util.rand(0f, 256f);
            int color = (0xff << 3*8);
            color |= (r << 2*8);
            color |= (g << 8);
            color |= b;
//            int color = 0xffaa11aa;
            Triangle triangle = new DrawBoundingBoxTriangle(new Vector2D(0.0f, 0.0f),
                    new Vector2D(-1.0f, 1.0f),
                    new Vector2D(1.0f, 1.0f), color);
            triangle.initPos(worldPosition);
            triangle.getScale().scale(Util.rand(1f, 2f));
                scene.addChild(triangle);

        }
        for(SceneObject s : scene.getChild()) {
            if(s instanceof Body b) {
                integrator.integrate(b, dt);
            }
        }
        collision.collision(scene, dt);
        scene.update(null, dt);
        updateTime = System.currentTimeMillis() - start;
        List<CollisionInfo> collisions = collision.getCollisions();
        for(CollisionInfo info : collisions) {
            Body shapeA = info.getShapeA();
            Body shapeB = info.getShapeB();
            if(shapeA instanceof Circle && shapeB instanceof Circle) {
                if(info.getContactCount() > 0) {
                    Vector2D contact1 = info.getContact1();
                    NewRectangle r = new NewRectangle(0.25f, 0.25f);
                    r.moveTo(contact1);
//                    contactPointList.add(r);
                }
            }
        }
    }

    long currTime;
    long frameTime = 0;
    long l;
    int size = 0;
    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(null, gc.getRenderer());

        r.drawText("Frametime: " + frameTime/1000.0 , 10, 100, 0xffffffff);
        r.drawText("Objects: " + size, 10, 110, 0xffffffff);
        r.drawText("Updates: " + updateNummer, 10, 120, 0xffffffff);
        r.drawText("UpdateTime: " + updateTime, 10, 130, 0xffffffff);
        l = System.currentTimeMillis() - aSecond;
        updateNummer = 0;
        if(l > 1000l) {
            currTime  = System.currentTimeMillis();
            size = scene.getChild().size();
            frameTime = currTime - startTime;
            startTime = System.currentTimeMillis();
            aSecond = currTime;
        }
//        for (Vector2D s : collision.getContactPoints()) {
//            Vector2D screen = camera2D.getModel().mul(s);
//            DrawUtil.drawBox(screen, 10f, 0xff667766, r);
//        }
        collision.getContactPoints().clear();
        contactPointList.clear();

        for(SceneObject o : scene.getChild()) {
            if(o.getPos().getY() > viewPort.getY()) {
                o.setDead(true);
            }
        }
    }
    public static void main(final String[] args) {
        gc = new GameContainer(new StackingMain());
        gc.setWith(800);
        gc.setHeight(600);
        gc.setScale(1.0f);
        gc.start();
    }

}

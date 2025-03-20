package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Input;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.NewRectangle;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.game.objects.Triangle;
import no.fun.stuff.engine.game.physics.Integrate;
import no.fun.stuff.engine.game.physics.VerletWithVelocityIntegration;
import no.fun.stuff.engine.game.physics.collition.Collision;
import no.fun.stuff.engine.matrix.Vector2D;

import java.awt.event.KeyEvent;
import java.util.List;

public class FirstLevel extends AbstractGame {
    private final TriangleScene scene = new TriangleScene();
    private final Collision collision = new Collision();
    private LookAtCamera camera2D;
    private static GameContainer gc;
    private Vector2D viewPort;
    private Integrate integrator = new VerletWithVelocityIntegration();
    no.fun.stuff.engine.game.objects.Grid grid = new no.fun.stuff.engine.game.objects.Grid(80, 60, 10f, 10f);
    private TheShip theShip = new TheShip(new Vector2D(0f, 0f),
            new Vector2D(-2.0f, 5f),
            new Vector2D(2.0f, 5f), 0xff00aabb);
    private Level1 level1 = new Level1();
    @Override
    public void init(GameContainer gc) {

        viewPort = new Vector2D(800/4f, 600/4f);
        camera2D = new LookAtCamera(theShip, new Vector2D(gc.getWith(), gc.getHeight()), viewPort);
        Vector2D worldview = viewPort.scale(1);
        scene.setCamera(camera2D);
        scene.addChild(grid);
        level1.scale(1.0f);
        level1.setStatic(true);
//        scene.addChild(level1);
        List<Triangle> triangleList = level1.getTriangleList();
        for(final Triangle t : triangleList) {
            scene.addChild(t);
        }
        NewRectangle leftSide = new NewRectangle(worldview.getX() * 0.1f, worldview.getY(), true);
        leftSide.initPos(new Vector2D(0f, worldview.getY()/2f));
        leftSide.setStaticFriction(0.01f);
//        scene.addChild(leftSide);
        NewRectangle rightSide = new NewRectangle(worldview.getX() * 0.1f, worldview.getY(), true);
        rightSide.initPos(new Vector2D(worldview.getX(), worldview.getY()/2f));
        scene.addChild(rightSide);
        NewRectangle button = new NewRectangle(worldview.getX(), worldview.getY()*0.1f, true);
        button.initPos(new Vector2D(worldview.getX()*0.5f, worldview.getY()));
        button.setRestitution(0.9f);
        button.setStatic(true);
        scene.addChild(button);
//        rectangle = new NewRectangle(Util.rand(0.75f, 1.25f), Util.rand(0.75f, 1.25f));
////            rightSide.setStatic(true);
//        rectangle.initPos(new Vector2D(viewPort.getX()*0.7f, viewPort.getY()*0.8F));
//        rectangle.setRestitution(0.9f);

        scene.addChild(theShip);
        Vector2D position = new Vector2D(viewPort.getX() * 0.5f, viewPort.getY() * 0.1f);
//        theShip.moveTo(position);
        camera2D.moveTo(new Vector2D());
        theShip.rotateTo(0.1f);
        theShip.moveTo(new Vector2D(0f, -40f));
        theShip.setDensity(Body.WaterDensity);
//        camera2D.lookAt(theShip);
//        ((VerletWithVelocityIntegration)integrator).addForce(((VerletWithVelocityIntegration)integrator).getGravity());//        triangle.set
        float width = 1f;
        float height = 3;
        NewRectangle test = new NewRectangle(width,width);
        test.initPos(new Vector2D(viewPort.getX()*0.5f, 0f));
        test.rotate(3.14f*0.21f);
//        test.setDensity(Body.AluminiumDensity);
        test.setDensity(20f);
        test.setRestitution(1.0f);
        test.recalckProperties(width, width);

    }

    @Override
    public void update(GameContainer gc, float dt) {
        collision.getContactPoints().clear();
        Input input = gc.getInput();
        camera2D.setInput(input);
        theShip.setLeftPress(input.isKey(KeyEvent.VK_LEFT));
        theShip.setRightPress(input.isKey(KeyEvent.VK_RIGHT));
        if (input.isKey(KeyEvent.VK_SPACE)) {
            theShip.setAngle(0f);
        }
//        if (input.isButtonDown(MouseEvent.BUTTON1)) {
//            Circle circle = new Circle(Util.rand(1f, 3f), 0xffccbbbb);
//            Vector2D mousePosition = new Vector2D((float) input.getMouseX(), (float) input.getMouseY());
//            circle.moveTo(camera2D.calculateInverseModel().mul(mousePosition));
//            scene.addChild(circle);
//        }
//        if (input.isButtonDown(MouseEvent.BUTTON3)) {
////            NewRectangle rectangle = new NewRectangle(Util.rand(1f, 3f), Util.rand(1f, 3f));
//            Triangle rectangle = new Triangle(new Vector2D(0f, -1f), new Vector2D(-1f, 1f), new Vector2D(1f, 1f), 0xffaabbcc);
//            rectangle.setColor(0xffccbbaa);
//            Vector2D mousePosition = new Vector2D((float) input.getMouseX(), (float) input.getMouseY());
//            rectangle.moveTo(camera2D.calculateInverseModel().mul(mousePosition));
//            scene.addChild(rectangle);
//        }
        boolean keyup = input.isKey(KeyEvent.VK_UP);
        theShip.setUpPress(keyup);

        for(SceneObject s : scene.getChild()) {
            if(s instanceof Body b) {
                integrator.integrate(b, dt);
            }
        }
        collision.collision(scene, dt);

//        for(SceneObject o : scene.getChild()) {
//            if(o.getPos().getY() > viewPort.getY()) {
//                o.setDead(true);
//            }
//        }
        scene.update(null, dt);
        for(Vector2D p : collision.getContactPoints()) {
            NewRectangle newRectangle = new NewRectangle(10, 10);
            newRectangle.setStatic(true);
//            newRectangle.initPos(p);
            newRectangle.moveTo(p);
            newRectangle.setColor(0xffffffff);
            newRectangle.render(camera2D, gc.getRenderer());
//            Vector2D[] w = newRectangle.toWorldCoordinate();
//            drawLine(w[0], w[1], gc.getRenderer(), 0xffffffff);
//            drawLine(w[1], w[2], gc.getRenderer(), 0xffffffff);
//            drawLine(w[2], w[3], gc.getRenderer(), 0xffffffff);
//            drawLine(w[3], w[0], gc.getRenderer(), 0xffffffff);
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(camera2D, r);
//        grid.render(camera2D, r);
//        theShip.render(camera2D, r);

    }
    public static void main(final String[] args) {
        gc = new GameContainer(new FirstLevel());
        gc.setWith(800);
        gc.setHeight(600);
        gc.setScale(1.0f);
        gc.start();
    }

}

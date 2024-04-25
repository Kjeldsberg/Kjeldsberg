package no.fun.stuff.engine.SAT;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Input;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Camera2D;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.game.objects.*;
import no.fun.stuff.engine.game.physics.collition.CollisionInfo;
import no.fun.stuff.engine.game.physics.collition.SAT;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.engine.triangle.TriangleScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SATMain extends AbstractGame {
    private final TriangleScene scene = new TriangleScene();
    private boolean rotate = false;
    private Camera2D lookAtCamera;
    private final List<Clickable> clickable = new ArrayList<>();
    private final SAT sat = new SAT();

    @Override
    public void init(GameContainer gc) {
        Vector2D screenSize = new Vector2D(gc.getWith(), gc.getHeight());
        Vector2D viewPort = new Vector2D(20.0f, 15.0f);
        lookAtCamera = new Camera2D(screenSize, viewPort);

        Triangle triangle = new ShowNormalTriangle(new Vector2D(0.0f, 0.0f),
                new Vector2D(0.0f, 1.0f),
                new Vector2D(1.0f, 0.0f), 0xffaa11aa);
        Triangle  triangle2 = new ShowNormalTriangle(new Vector2D(0.0f, 0.0f),
                new Vector2D(0.0f, 1.0f),
                new Vector2D(1.0f, 0.0f), 0xffaaaaaa);
        scene.addChild(triangle);
        scene.addChild(triangle2);
        Triangle triangl = new Triangle(new Vector2D(0.0f, 0.0f),
                new Vector2D(0.0f, 1.0f),
                new Vector2D(1.0f, 0.0f), 0xff0000aa);
        scene.addChild(triangl);
        for (int i = 0; i < 1; i++) {
            Triangle triangle1 = new Triangle(new Vector2D(0.0f, 0.0f),
                    new Vector2D(0.0f, 1.0f),
                    new Vector2D(1.0f, 0.0f), 0xffaa11aa);
            float x = (float) Math.random() * viewPort.getX();
            float y = (float) Math.random() * viewPort.getY();
            triangle1.moveTo(x,y);
            scene.addChild(triangle1);
        }
        for (int i = 0; i < 5; i++) {
            NewRectangle rectangle = new NewRectangle(1f, 1f);
            float x = (float) Math.random() * viewPort.getX();
            float y = (float) Math.random() * viewPort.getY();
            rectangle.moveTo(x, y);
            scene.addChild(rectangle);
        }
        for (int i = 0; i < 1; i++) {
            float x = (float) Math.random() * viewPort.getX();
            float y = (float) Math.random() * viewPort.getY();
            Circle circle = new Circle(0.5f, 0xff99aa22);

            circle.moveTo(x, y);
            scene.addChild(circle);
        }
//        triangle.moveTo((float) Math.random() * viewPort.getX(), (float) Math.random() * viewPort.getY());
//        triangle2.moveTo((float) Math.random() * viewPort.getX(), (float) Math.random() * viewPort.getY());
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
        float speed = 7f;
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
        Vector2D direction = new Vector2D(dx, dy);
        direction.normaize();
        direction.mul(speed * dt);

        if (input.isButton(MouseEvent.BUTTON1)) {
            Vector2D position = new Vector2D(input.getMouseX(), input.getMouseY());
            clickable.clear();
            clickable.addAll(scene.checkPicked(position));
        }
        if (clickable.size() > 0) {
            for (Clickable cli : clickable) {
                Vector2D pos = new Vector2D();
                pos.setX(input.getMouseX());
                pos.setY(input.getMouseY());
                ((Body) cli).move(direction);
                if (cli instanceof ShowNormalTriangle s) {
                    s.setTheMousePosition(pos);
                } else if (cli instanceof Triangle t) {
                    t.setColor((t.getColor() + 4) | 0xff000000);
                } else if (cli instanceof NewRectangle t) {
                    int color = t.getColor() + 0x1000;
                    t.setColor(color | 0xff000000);
                } else if (cli instanceof Circle t) {
                    int color = t.getColor() + 0x1000;
                    t.setColor(color | 0xff000000);
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
        for (int i = 0; i < scene.getChild().size() - 1; i++) {
            Body shapeA = (Body) scene.getChild().get(i);
            for (int j = i + 1; j < scene.getChild().size(); j++) {

                Body shapeB = (Body) scene.getChild().get(j);

                if (shapeA.getShapeType() == Body.Shape.Polygon) {
                    if (shapeB.getShapeType() == Body.Shape.Polygon) {
                        CollisionInfo collide = sat.polygonCollide(shapeA.toWorldCoordinate(), shapeB.toWorldCoordinate());
                        if (collide.isCollide()) {
                            float len = collide.getDepth() / 2f;
                            Vector2D scaleA = collide.getNormal().scale(len);
                            Vector2D scaleB = collide.getNormal().scale(-len);
                            shapeA.move(scaleA);
                            shapeB.move(scaleB);
                        }
                    }
                    if (shapeB.getShapeType().equals(Body.Shape.Circle)) {
                        final Vector2D[] circleWorld = shapeB.toWorldCoordinate();
                        Vector2D[] worldCoordinate = shapeA.toWorldCoordinate();
                        final CollisionInfo collide = sat.circlePolygonCollide(worldCoordinate, circleWorld[0], circleWorld[1].getY());
                        if (collide.isCollide()) {
                            float len = collide.getDepth() / 2f;
                            Vector2D scaleA = collide.getNormal().scale(len);
                            Vector2D scaleB = collide.getNormal().scale(-len);
                            shapeA.move(scaleA);
                            shapeB.move(scaleB);
                        }
                    }
                }
                if (shapeA.getShapeType() == Body.Shape.Circle) {
                    if (shapeB.getShapeType() == Body.Shape.Circle) {
                        final Vector2D[] circleAWorld = shapeA.toWorldCoordinate();
                        final Vector2D[] circleBWorld = shapeB.toWorldCoordinate();
                        CollisionInfo collide = sat.circleCircleCollide(circleAWorld[0], circleAWorld[1].getY(),
                                circleBWorld[0], circleBWorld[1].getY());
                        if (collide.isCollide()) {
                            float len = collide.getDepth() / 2f;
                            Vector2D scaleA = collide.getNormal().scale(len);
                            Vector2D scaleB = collide.getNormal().scale(-len);
                            shapeA.move(scaleA);
                            shapeB.move(scaleB);
                        }
                    }
                    if (shapeB.getShapeType().equals(Body.Shape.Polygon)) {
                        final Vector2D[] circleWorld = shapeA.toWorldCoordinate();
                        final CollisionInfo collide = sat.circlePolygonCollide(shapeB.toWorldCoordinate(), circleWorld[0], circleWorld[1].getY());
                        if (collide.isCollide()) {
                            float len = collide.getDepth() / 2f;
                            Vector2D scaleA = collide.getNormal().scale(len);
                            Vector2D scaleB = collide.getNormal().scale(-len);
                            shapeA.move(scaleA);
                            shapeB.move(scaleB);
                        }
                    }
                }

//            if(shapeA instanceof Triangle a && shapeB instanceof Circle b ||
//                    shapeA instanceof Circle b1 && shapeB instanceof Triangle a1) {
//                Vector2D[] worldCoordinateA = a.toWorldCoordinate();
//                Vector2D[] worldCoordinateB = b.toWorldCoordinate();
//                CollisionInfo collide = sat.collide(worldCoordinateA, worldCoordinateB);
//                if(collide.isCollide()) {
//                    float len = collide.getDepth() / 2f;
//                    Vector2D scaleA = collide.getNormal().scale(len);
//                    Vector2D scaleB = collide.getNormal().scale(-len);
//                    a.move(scaleA);
//                    b.move(scaleB);
//                }
//            }
            }

        }
//        triangle.gety(rotate);
        scene.update(null, dt);
//        input.updata();

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

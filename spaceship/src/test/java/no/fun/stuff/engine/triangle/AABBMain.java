package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Input;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.SAT.ShowNormalTriangle;
import no.fun.stuff.engine.game.Camera2D;
import no.fun.stuff.engine.game.Clickable;
import no.fun.stuff.engine.game.objects.Triangle;
import no.fun.stuff.engine.game.objects.*;
import no.fun.stuff.engine.game.physics.collition.AABB;
import no.fun.stuff.engine.game.physics.collition.Collision;
import no.fun.stuff.engine.matrix.Vector2D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AABBMain extends AbstractGame {
    private final TriangleScene scene = new TriangleScene();
    private final Collision collision = new Collision();
    private Camera2D camera2D;
    private final List<Clickable> clickable = new ArrayList<>();

    @Override
    public void init(GameContainer gc) {
        Vector2D viewPort = new Vector2D(40f, 30f);
        camera2D = new Camera2D(new Vector2D(gc.getWith(), gc.getHeight()), viewPort);
        scene.setCamera(camera2D);
        NewRectangle rectangle = new NewRectangle(viewPort.getX() * 0.9f, viewPort.getY() * 0.1f);
        rectangle.setStatic(false);
        rectangle.moveTo(viewPort.getX() * 0.5f, viewPort.getY() * 0.9f);
        scene.addChild(rectangle);
        NewRectangle rec = new NewRectangle(1f, 1f);
        rec.moveTo(viewPort.getX() * 0.5f, viewPort.getY() * 0.5f);
        scene.addChild(rec);
        Triangle triangle = new DrawBoundingBoxTriangle(new Vector2D(0.0f, 0.0f),
                    new Vector2D(0.0f, 1.0f),
                    new Vector2D(1.0f, 0.0f), 0xff2222ee);
        triangle.moveTo(3f,3f);
        scene.addChild(triangle);
        DrawBoundingBoxCircle drawBoundingBoxCircle = new DrawBoundingBoxCircle(1f, 0xff2222ff);
        drawBoundingBoxCircle.moveTo(4f,4f);
        scene.addChild(drawBoundingBoxCircle);
        NewRectangle drawBoundingBoxCircle2 = new NewRectangle(2f, 2f);
        drawBoundingBoxCircle2.moveTo(8f,8f);
        drawBoundingBoxCircle2.rotateTo(3.14f/2f);
        scene.addChild(drawBoundingBoxCircle2);

    }

    @Override
    public void update(GameContainer gc, float dt) {
        Input input = gc.getInput();
        if(input.isKey(KeyEvent.VK_A)) {
            camera2D.setZoom( 0.11f);
        }
        if(input.isKey(KeyEvent.VK_S)) {
            camera2D.setZoom( -0.11f);
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
        float force = 0.1f;
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
                    t.move(acc);
                } else if (cli instanceof NewRectangle t) {
                    Vector2D acc = direction.mul(force);
                    t.move(acc);
                    boolean collide = AABB.collide((Body) scene.getChild().get(0), (Body) scene.getChild().get(1));
                    if(collide)
                        ((NewRectangle)scene.getChild().get(0)).setColor(0xffff2222);
                    else
                        ((NewRectangle)scene.getChild().get(0)).setColor(0xffeeeeee);
//                    t.getForce().setXY(acc);

                    int color = t.getColor() + 0x1000;
                    t.setColor(color | 0xff000000);
                } else if (cli instanceof Circle c) {
                    Vector2D acc = direction.mul(force);
                    c.getForce().setXY(acc);
//                    c.set(acc);
//                    c.setForce(direction.mul(10f));
                    int color = c.getColor() + 0x1000;
                    c.setColor(color | 0xff000000);
                }
            }
        }
        scene.update(null, dt);

    }
//    @Override
    public void update(SceneObject parent, float dt) {
//        translate.translate(worldCenter);
//        this.rotate(-0.001f);
//        translate.translate(pos);
//        scale.scale(6.0f);
    }
    @Override
    public void render(GameContainer gc, Renderer r) {
        for(int i=0;i<scene.getChild().size()-1;i++) {
            Body a = (Body) scene.getChild().get(i);
            for(int j=i+1;j<scene.getChild().size();j++) {
                Body b = (Body) scene.getChild().get(j);
                if(AABB.collide(a, b)) {
                    r.drawText("Kollisjon!", 400, 10, 0xffeeeeee);
                }
            }
        }
        scene.render(camera2D, gc.getRenderer());
    }
    public static void main(final String[] args) {
        GameContainer gc = new GameContainer(new AABBMain());
        gc.setWith(800);
        gc.setHeight(600);
        gc.setScale(1.0f);
        gc.start();
    }
}

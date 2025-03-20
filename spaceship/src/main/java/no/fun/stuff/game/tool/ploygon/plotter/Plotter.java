package no.fun.stuff.game.tool.ploygon.plotter;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Input;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.game.objects.Triangle;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.game.spaceship.TriangleScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Plotter extends AbstractGame {
    private LookAtCamera camera2D;
    //    private Camera2D camera2D;
    private Vector2D screenSize = new Vector2D(800f, 600f);
    private Vector2D viewPort = new Vector2D(400, 300);
    private static GameContainer gc;
    private PlotterInput input;
    no.fun.stuff.engine.game.objects.Grid grid = new no.fun.stuff.engine.game.objects.Grid(100, 100, 1f, 1f);
    private ClickedState clickedState = new ClickedState();
    private final TriangleScene scene = new TriangleScene();
    private int mouseScroll;
    private final EventPublisher eventPublisher;
    private final List<Vector2D> plottList;
    private final Subscriber sub;
    private final Subscriber saveToFile;
    private final Subscriber undo;
    private final List<Triangle> triangleList;
    private final List<Integer> undoList;


    public Plotter() {
//        camera2D = new Camera2D(screenSize, viewPort,);
        camera2D = new LookAtCamera(grid, screenSize, viewPort);
        scene.setCamera(camera2D);
        grid.moveTo(new Vector2D());
        grid.scale(10f);
        scene.addChild(grid);
        eventPublisher = new EventPublisher();
        plottList = new ArrayList<>();
        triangleList = new ArrayList<>();
        undoList = new ArrayList<>();
        undo = new Subscriber() {
            @Override
            public void update(String event, Object data) {
                if (event.equals("undo")) {
                    if (data instanceof String s) {
                        if ("undo".equals(s)) {
                            int size = plottList.size();
                            boolean lastElement = size > 0;
                            if(lastElement) {
                                if(clickedState.getState() == CState.MAKE) {
                                    undoList.add(scene.getChild().size() - 1);
                                    if(size == 2) {
                                        clickedState.reset();
                                    }
                                }
                                plottList.remove(size - 1);
//                                plottList.remove(size - 1);
//                                plottList.remove(size - 1);
                            }
                        }
                    }
                }
            }
        };
        eventPublisher.subscribe("undo", undo);
        saveToFile = new Subscriber() {
            @Override
            public void update(String event, Object data) {
                if (event.equals("save")) {
                    if (data instanceof String s) {
                        if ("save".equals(s)) {
                            final List<Vector2D> worldSpacePoints = new ArrayList<>();
                            Matrix3x3 matrix3x3 = camera2D.calculateInverseModel();
                            for(final Vector2D p : plottList) {
                                worldSpacePoints.add(matrix3x3.mul(p));
                            }
                            final WritePlottedPoints writePlottedPoints = new WritePlottedPoints();
                            writePlottedPoints.writePoints(worldSpacePoints);
                        }
                    }
                }
            }
        };
        eventPublisher.subscribe("save", saveToFile);

        sub = new Subscriber() {

            @Override
            public void update(String event, Object data) {
                if (event.equals("clicked")) {
                    if (data instanceof Vector2D v) {
                        plottList.add(new Vector2D(v));
                        clickedState.leftClick();
                        if (clickedState.getState() == CState.MAKE) {
                            Vector2D first = plottList.get(plottList.size() - 2);
                            Vector2D second = plottList.get(plottList.size() - 1);
                            Matrix3x3 matrix3x3 = camera2D.calculateInverseModel();
                            Vector2D firstWorldSpace = matrix3x3.mul(first);
                            Vector2D secondWorldSpace = matrix3x3.mul(second);
                            Vector2D direction = secondWorldSpace.minus(firstWorldSpace);
                            Vector2D perpendicular = new Vector2D(direction.getY(), -direction.getX());
//
                            Vector2D midPoint = new Vector2D();
                            Util.lerp(0.5f, firstWorldSpace, secondWorldSpace, midPoint);
                            Vector2D lastMiddelPoint = midPoint.add(perpendicular);
                            final Vector2D[] pos = {firstWorldSpace, secondWorldSpace, lastMiddelPoint};
                            Vector2D center = Triangle.getCenter(pos);
                            Triangle e = new Triangle(firstWorldSpace, secondWorldSpace, lastMiddelPoint, 0xff4499AA);
                            e.moveTo(center);
                            e.setStatic(true);
                            e.setWireFrame(true);
                            triangleList.add(e);
                        }
                    }
                }
            }
        };
        eventPublisher.subscribe("clicked", sub);



    }

    class PlotterInput extends Input {

        public PlotterInput(GameContainer container) {
            super(container);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            eventPublisher.publishEvent("clicked", new Vector2D(e.getX(), e.getY()));
        }
        @Override
        public void keyTyped(KeyEvent e) {
            if("s".charAt(0) == e.getKeyChar()) {
                eventPublisher.publishEvent("save", "save");
            }
            if("u".charAt(0) == e.getKeyChar()) {
                eventPublisher.publishEvent("undo", "undo");
            }
        }
    }

    @Override
    public void init(GameContainer gc) {
        input = new PlotterInput(gc);
    }

    @Override
    public void update(GameContainer gc, float dt) {
//        input.keyTyped();
        final String fileName = "save_#.txt";
        mouseScroll = input.getScroll();
        if (mouseScroll != 0) {
            Vector2D scale = camera2D.getScale().getScale();
            scale.pluss((float) mouseScroll, (float) mouseScroll);

            camera2D.getScale().scale(scale);
            System.out.println("Mouse Scroll : " + camera2D.getScale());
            System.out.println("Mouse Scroll : " + camera2D.getScale().getScale());
        }
        for (final Integer i : undoList) {
            scene.getChild().remove(i.intValue());
        }
        undoList.clear();
        for (final Triangle t : triangleList) {
            scene.addChild(t);
        }
        triangleList.clear();
        scene.update(camera2D, dt);
        input.updata();
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(camera2D, r);

    }

    public static void main(final String[] args) {
        gc = new GameContainer(new Plotter());
        gc.setWith(800);
        gc.setHeight(600);
        gc.setScale(1.0f);
        gc.start();
    }
}

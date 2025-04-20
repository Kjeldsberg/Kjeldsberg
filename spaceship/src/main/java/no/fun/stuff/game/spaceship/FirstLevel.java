package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Input;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.game.objects.*;
import no.fun.stuff.engine.game.physics.Integrate;
import no.fun.stuff.engine.game.physics.VerletWithVelocityIntegration;
import no.fun.stuff.engine.game.physics.collition.Collision;
import no.fun.stuff.engine.game.physics.collition.CollisionInfo;
import no.fun.stuff.engine.matrix.Vector2D;

import java.awt.event.KeyEvent;
import java.util.List;

public class FirstLevel extends AbstractGame {
    public static final int STEP_COUNT = 4;
    private final TriangleScene scene = new TriangleScene();
    //private final Collision collision = new Collision();
    private LookAtCamera camera2D;
    private static GameContainer gc;
    private Vector2D viewPort;
    private Integrate integrator = new VerletWithVelocityIntegration();
    no.fun.stuff.engine.game.objects.Grid grid = new no.fun.stuff.engine.game.objects.Grid(80, 60, 10f, 10f);
    private TheShip theShip = new TheShip(new Vector2D(0f, 0f),
            new Vector2D(-2.0f, 5f),
            new Vector2D(2.0f, 5f), 0xff00aabb);
    private Triangle startPlace = new Triangle(new Vector2D(-4f, 0f), new Vector2D(0f, 1f), new Vector2D(4f, 0f),  0xffffff99);
    private Triangle endPlace = new Triangle(new Vector2D(-4f, 0f), new Vector2D(0f, 1f), new Vector2D(4f, 0f),  0xffffff99);
    private final Collision collision = new ThePostCollisionTest(theShip, endPlace);
    private final Vector2D shipStartPosition = new Vector2D();
    private Level1 level1 = new Level1();
    private TimeManager timeManager = new TimeManager();
    private ScoreBoard scoreBoard = new ScoreBoard();
    private ScoreBoard workingScoreBoard = new ScoreBoard();
    class ScoreBoard{
        private int wallCrash;
        private int resets;
        public void addWallCrash() {
            wallCrash++;
        }
        public void addResets() {
            resets++;
        }
        public void clear() {
            wallCrash = 0;
            resets = 0;
        }
        public int getWallCrash() {
            return wallCrash;
        }

        public int getResets() {
            return resets;
        }
    }
    class ThePostCollisionTest extends Collision {
        private Body theShip;
        private Body theEndPlace;

        public ThePostCollisionTest(Body theShip, Body theEndPlace) {
            this.theShip = theShip;
            this.theEndPlace = theEndPlace;
        }
        @Override
        public void postCollision(List<CollisionInfo> collidedObjectsList) {
            boolean foundShip = false;
            boolean foundEndPlace = false;
            for (CollisionInfo info : collidedObjectsList) {
                boolean gotTheShip = info.getShapeA() == theShip || info.getShapeB() == theShip;
                boolean gotTheEndPlace = info.getShapeA() == theEndPlace || info.getShapeB() == theEndPlace;
                boolean endReached = gotTheEndPlace && gotTheShip;
                if(endReached) {
                    //System.out.println("Collision.");
                    timeManager.stop();
                }
                if(!endReached && gotTheShip) {
                    scoreBoard.addWallCrash();
                }
            }
        }
    }
    class TimeManager {
        private long time;
        private long startTime;
        private boolean started = false;
        public boolean isStarted() {
            return started;
        }
        public void start() {
            startTime = System.currentTimeMillis();
            started = true;
        }
        public void reSet() {
            if(!started) {
                time = 0L;
            }
        }
        public void stop() {
            started = false;
        }

        public long getTime() {
            if(started) {
                time = System.currentTimeMillis() - startTime;
            }
            return time;
        }
    }
    @Override
    public void init(GameContainer gc) {

        viewPort = new Vector2D(gc.getWith()/7f, gc.getHeight()/7f);
        endPlace.setWireFrame(false);
        endPlace.setRestitution(0.1f);
        endPlace.scale(5f);
        endPlace.setStatic(true);
        startPlace.setWireFrame(false);
        startPlace.setRestitution(0.1f);
        startPlace.scale(5f);
        startPlace.setStatic(true);
        Vector2D endPosition = new Vector2D(140f, 80);
        startPlace.moveTo(endPosition);
        scene.addChild(startPlace);
        camera2D = new LookAtCamera(theShip, new Vector2D(gc.getWith(), gc.getHeight()), viewPort);
        scene.setCamera(camera2D);
        //scene.addChild(grid);
        level1.scale(1.0f);
        level1.setStatic(true);
        scene.addChild(theShip);
        scene.addChild(endPlace);
        scene.addChild(startPlace);
        camera2D.moveTo(new Vector2D());
        theShip.rotateTo(0.1f);
        Vector2D position = new Vector2D(140f, -4f);
        shipStartPosition.setXY(endPosition.add(new Vector2D(0f, -5f)));
        theShip.moveTo(shipStartPosition);
        endPlace.moveTo(position.add(new Vector2D(0f, 4f)));

        theShip.setDensity(Body.WaterDensity);
        List<Triangle> triangleList = level1.getTriangleList();
        scene.getChild().addAll(triangleList);

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
            workingScoreBoard.addResets();
        }

        boolean keyup = input.isKey(KeyEvent.VK_UP);
        theShip.setUpPress(keyup);
        if(keyup && !timeManager.isStarted() ) {
            timeManager.start();
        }
        boolean keyR = input.isKey(KeyEvent.VK_R);
        if(keyR) {
            theShip.getVelocity().setXY(Vector2D.ZERO);
            theShip.setAngularVelocity(0f);
            theShip.moveTo(shipStartPosition);
        }
        boolean keyS = input.isKey(KeyEvent.VK_S);
        if(keyS) {
            workingScoreBoard.clear();
            theShip.getVelocity().setXY(Vector2D.ZERO);
            theShip.setAngularVelocity(0f);
            theShip.moveTo(shipStartPosition);
        }
        scoreBoard.clear();
        float deltaDT = dt/STEP_COUNT;
        for (int i = 0; i < STEP_COUNT; i++) {
            scene.update(null, deltaDT);

            for(SceneObject s : scene.getChild()) {
                if(s instanceof Body b) {
                    integrator.integrate(b, deltaDT);
                }
            }
            collision.collision(scene, deltaDT);
        }
        boolean hasShipCollideWithWall = scoreBoard.getWallCrash() > 0;
        boolean hasUserResetTheShip = scoreBoard.getResets() > 0;
        if(hasShipCollideWithWall) {
            workingScoreBoard.addWallCrash();
        }
        if(hasUserResetTheShip){
            workingScoreBoard.addResets();
        }

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        scene.render(camera2D, r);
        Float aLong = Float.valueOf(timeManager.getTime() / 1000f);
        r.drawText("Time: " + aLong, 0, 50, 0xffbbbbbb);
        Integer wallCrash = Integer.valueOf(workingScoreBoard.getWallCrash());
        r.drawText("Wall crash: " + wallCrash, 0, 70, 0xffbbbbbb);
        Integer resets = Integer.valueOf(workingScoreBoard.getResets());
        r.drawText("Resets: " + resets, 0, 90, 0xffbbbbbb);
    }
    public static void main(final String[] args) {
        gc = new GameContainer(new FirstLevel());
//        gc.setWith(900);
//        gc.setHeight(450);
        gc.setWith(1280);
        gc.setHeight(720);
        gc.setWith(1368);
        gc.setHeight(768);
        gc.setScale(1f);
        gc.start();
    }

}

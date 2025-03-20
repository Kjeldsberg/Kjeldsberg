package no.fun.stuff.engine.triangle;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.LookAtCamera;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.game.spaceship.TriangleScene;

import java.awt.event.KeyEvent;
import java.util.List;

public class SimpleSquareWithTriangle extends AbstractGame {
    private TriangleScene scene = new TriangleScene();
    private final int[] color = { 0x88556677, 0x88546677, 0x88526677, 0x88526677, 0x88526677};
    private boolean rotate = false;
    private Square e = null;
    class Square extends SceneObject {
        private Vector2D[] localCoordinate;
        private Vector2D[] worldCoordinate;
        public float rotation = 0.0f;
        public  Square(GameContainer gc) {
            gc.getRenderer().setAmbientcolor(-1);
            List<Vector2D> list = List.of(
                    new Vector2D(-10, -10),
                    new Vector2D(-10, 10),
                    new Vector2D(10, 10),
                    new Vector2D(-10, -10),
                    new Vector2D(10, 10),
                    new Vector2D(10, -10),
                    new Vector2D(-10, 10),
                    new Vector2D(0, 20),
                    new Vector2D(10, 10),
                    new Vector2D(-10, -10),
                    new Vector2D(-20, 0),
                    new Vector2D(-10, 10)

                    );

            localCoordinate = new Vector2D[list.size()];
            worldCoordinate = new Vector2D[list.size()];
            for (int i = 0; i < list.size(); i++) {
                localCoordinate[i] = new Vector2D();
                worldCoordinate[i] = new Vector2D();
            }
            final Vector2D org[] = new Vector2D[list.size()];
            list.toArray(org);
            final Matrix3x3 mod = new Matrix3x3();
            mod.scale(6.0f);
//            mod.rotate(0.001f);
//            mod.rotate(0.01f);
//            float radians = -6.2809f;
            float radians = -0.784f;
            this.rotation = radians;
//            float radians = -8.99f;
            mod.rotate(radians);
            mod.mul(org, localCoordinate);
            SimpleSquareWithTriangle.this.rotate = false;
        }
        @Override
        public void update(SceneObject parent, float dt) {
            if(SimpleSquareWithTriangle.this.rotate) {
//                float radians = -3.4709f;
                float radians2 = -0.001f;
                this.rotation += radians2;
                this.rotate.rotate(radians2);

            }
        }

        @Override
        public void render(SceneObject parent, Renderer r) {
            this.model.set(this.translate.fastMulCopy(this.rotate).fastMulCopy(this.scale).getCopy());
            Matrix3x3 viewModel = new Matrix3x3();
            if(parent != null) {
                viewModel.set(parent.getModel().fastMulCopy(model));
            } else {
                viewModel.set(model);
            }
            viewModel.mul(localCoordinate, worldCoordinate);
            int y = 0;
            int i = 0;
            for(;i<localCoordinate.length;i+=3) {
                r.fillTriangle(worldCoordinate[i], worldCoordinate[i+1], worldCoordinate[i+2], color[y++]);
            }
            String rotation = Float.toString(e.rotation);
            r.drawText("Rad: " + rotation, 150,0,0xefffffff);

        }
    }

    @Override
    public void init(GameContainer gc) {
        gc.getRenderer().setAmbientcolor(-1);
        e = new Square(gc);
        scene.addChild(e);
        LookAtCamera lookAtCamera = new LookAtCamera(e, new Vector2D(gc.getWith(), gc.getHeight()));
        scene.setCamera(lookAtCamera);
    }

    @Override
    public void update(GameContainer gc, float dt) {

        if(gc.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
            rotate = !rotate;
        }

        scene.update(null, dt);

    }

    @Override
    public void render(GameContainer gc, Renderer r) {

        scene.render(null, gc.getRenderer() );
    }
    public static void main(final String[] args) {
        GameContainer gc = new GameContainer(new SimpleSquareWithTriangle());
        gc.setWith(320);
        gc.setHeight(240);
//        gc.setWith(640);
//        gc.setHeight(480);
        gc.setScale(3.0f);
        gc.start();

    }

}

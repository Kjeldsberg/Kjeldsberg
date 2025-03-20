package no.fun.stuff.engine.game;

import no.fun.stuff.engine.game.objects.NewRectangle;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LookAtCameraTest {
    @Test
    void testMatrix() {

        NewRectangle theSquare = new NewRectangle(5f, 5f);
        LookAtCamera lookAtCamera = new LookAtCamera(theSquare, new Vector2D(800f, 600f), new Vector2D(40f, 30f));
        lookAtCamera.calculateModel();

        Vector2D theNullOne = lookAtCamera.getModel().mul(theSquare.getPos());
        assertTrue(Util.compare(theNullOne.getX(), 0f)&& Util.compare(theNullOne.getY(), 0f));

        theSquare.moveTo(20f, 15f);
        Vector2D middelOfTheBigScreen = lookAtCamera.getModel().mul(theSquare.getPos());
        assertTrue(Util.compare(middelOfTheBigScreen.getX(), 400f) && Util.compare(middelOfTheBigScreen.getY(), 300f));

        theSquare.moveTo(10f, 10f);
        theSquare.calculateModel();
        lookAtCamera.moveTo(new Vector2D(20f - theSquare.getPos().getX(), 15 - theSquare.getPos().getY()));
        Vector2D shouldBe400300 = lookAtCamera.getModel().mul(theSquare.getPos());


        Matrix3x3 model = new Matrix3x3();
        Matrix3x3 view = new Matrix3x3();
        model.translate(new Vector2D(2f,3f));
        Vector2D pos = new Vector2D(1f, 1f);
        Matrix3x3 result = view.mulCopy(model);
        Vector2D resultPos = result.mul(pos);
        assertTrue(Util.compare(3f, resultPos.getX()));
        assertTrue(Util.compare(4f, resultPos.getY()));


        model.translate(20f, 3f);
        view.translate(1f/20f, 1f/3);
        Matrix3x3 result2 = view.mulCopy(model);
        Vector2D mul = result2.mul(pos);

        int test = 0;
//        assertNotEquals();
    }


}
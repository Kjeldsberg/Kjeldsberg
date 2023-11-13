package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.game.util.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

    @Test
    void getAngleTest() {
        float v = (float)Math.PI * 2 + 0.001f;
        float minusv = v*-1.0f;
        float v1 = (float)Math.PI * 2;

        SpaceShip spaceShip = new SpaceShip(null, 0, 0);
        float angle = spaceShip.getAngle(v, v1);
        float angle1 = spaceShip.getAngle(minusv, v1);
        float testValue = (float)Math.PI * 2;
        float posValue = v1;
        for(int i=0;i<10;i++) {
            posValue += 0.01;
            float angle2 = spaceShip.getAngle(posValue, testValue);
            boolean compare = Util.compare(testValue, angle2, Util.epsilon);
            assertTrue(compare);
        }
        float negValue = testValue*-1.0f;
        float startValue = negValue;
        for(int i=0;i<10;i++) {
            startValue -= 0.01;
            float angle2 = spaceShip.getAngle(startValue, testValue);
            boolean compare = Util.compare(negValue, angle2, Util.epsilon);
            assertTrue(compare);
        }


        int test = 0;

    }
}
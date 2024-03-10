package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {
    @Test
    void test() {

        Circle circle = new Circle();
        circle.drawCircle(new Vector2D(0.0f,0.0f), 20);

    }

}
package no.fun.stuff.engine.matrix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {

    public static final int INT = 2000000;

    @Test
    void testAdditionTime() {
        float x, y;
        float x2, y2;

        x = 0.5f;
        y = 0.002f;
        x2 = 0.003f;
        y2 = 0.004f;
        long l = System.nanoTime();
        for(int i = 0; i< INT; i++) {
            x += x2;
            y += y2;
        }
        long l1 = System.nanoTime();
        long timeInNano = l1 - l;
        System.out.println("Time = " + timeInNano);

        Vector2D xen = new Vector2D(x, y);
        Vector2D vector2D = new Vector2D(x2, y2);
        long l2 = System.nanoTime();
        for(int i=0;i<INT;i++) {
            xen.pluss(vector2D);
        }
        long l3 = System.nanoTime();
        long timeInNano2 = l3 - l2;
        System.out.println("Time = " + timeInNano2);

        long start2 = System.nanoTime();
        for(int i=0;i<INT;i++) {
            xen.add(vector2D);
        }
        long end2 = System.nanoTime();
        long time2 = end2 - start2;
        System.out.println("Time = " + time2);

    }

}
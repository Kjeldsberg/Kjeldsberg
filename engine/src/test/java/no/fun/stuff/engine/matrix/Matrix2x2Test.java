package no.fun.stuff.engine.matrix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Matrix2x2Test {
    @Test
    public void inverseTest() {
        Matrix2x2 a = new Matrix2x2(1, 2, 3, 4);
        Matrix2x2 m3 = new Matrix2x2(1, 2, 3, 4);
        a.inverse();
        a.mul(m3);
        float[][] f = a.get();
        assertEquals(1.0f, f[0][0]);
        assertEquals(0.0f, f[0][1]);
        assertEquals(0.0f, f[1][0]);
        assertEquals(1.0f, f[1][1]);

        int est = 0;
    }

}
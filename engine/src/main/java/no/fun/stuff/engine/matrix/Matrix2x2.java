package no.fun.stuff.engine.matrix;

public class Matrix2x2 {
    private final float[][] m;

    public Matrix2x2() {
        m =  new float[2][2];
    }
    public Matrix2x2(float m00, float m01, float m10, float m11) {
        m =  new float[2][2];
        m[0][0] = m00;
        m[0][1] = m01;
        m[1][0] = m10;
        m[1][1] = m11;
    }
    public void mul(final Matrix2x2 in) {
        float[][] b = in.m;
        float m00 = m[0][0] * b[0][0] + m[0][1] * b[1][0];
        float m01 = m[0][0] * b[0][1] + m[0][1] * b[1][1];

        float m10 = m[1][0] * b[0][0] + m[1][1] * b[1][0];
        float m11 = m[1][0] * b[0][1] + m[1][1] * b[1][1];
        m[0][0] = m00;
        m[0][1] = m01;
        m[1][0] = m10;
        m[1][1] = m11;
    }
    public float determinant() {
        return m[0][0] * m[1][1] - m[1][0]*m[0][1];
    }
    public void inverse() {
        float val = 1/determinant();
        float tmp = m[0][0];
        m[0][0] = m[1][1];
        m[1][1] = tmp;
        m[1][0] = -m[1][0];
        m[0][1] = -m[0][1];
        multiply(val);
    }

    public void multiply(float val) {
        m[0][0] *= val;
        m[0][1] *= val;
        m[1][0] *= val;
        m[1][1] *= val;
    }
    public void set(float m00, float m01, float m10, float m11) {
        m[0][0] = m00;
        m[0][1] = m01;
        m[1][0] = m10;
        m[1][1] = m11;
    }
    public float[][] get() {
        return m;
    }
}

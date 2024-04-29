package no.fun.stuff.engine.matrix;


import no.fun.stuff.engine.game.util.Util;

public class Matrix3x3 {
    private float RAD = (float) (180.0 / Math.PI);
    private float[][] m = {{0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}};
    private float[][] tmp = {{0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}};

    private float[][] identity = {{1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}};

    private final Matrix2x2 rotateInverse = new Matrix2x2();
    public Matrix3x3() {
        setIdentity();
    }

    public Matrix3x3(final Matrix3x3 m) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.m[i][j] = m.m[i][j];
            }
        }
    }

    public void setIdentity() {
        clear();
        m[0][0] = 1.0f;
        m[1][1] = 1.0f;
        m[2][2] = 1.0f;

    }

    public void inverse(final Matrix3x3 result) {
        float determinant = determinant(this);
        float oneOverDet  = Util.compare(determinant, 0.0f) ? 1.0f : 1/determinant;
        result.set(this);
        result.transpose();
        adjugateMatrix(result);
		result.multiply(oneOverDet);


    }

    // 00 01 02 00 01 02
    // 10 11 12 10 11 12
    // 20 21 22 20 21 22
    public float determinant(final Matrix3x3 mat) {

        float l1 = m[0][0] * m[1][1] * m[2][2];
        float l2 = m[0][1] * m[1][2] * m[2][0];
        float l3 = m[0][2] * m[1][0] * m[2][1];

        float r1 = m[0][0] * m[1][2] * m[2][1];
        float r2 = m[0][1] * m[1][0] * m[2][2];
        float r3 = m[0][2] * m[1][1] * m[2][0];

        return l1 + l2 + l3 - r1 - r2 - r3;
    }

    // 00 01 02
    // 10 11 12
    // 20 21 22
    public void adjugateMatrix(Matrix3x3 adjugate) {
        float m00 = m[1][1] * m[2][2] - m[1][2] * m[2][1];
        float m01 = m[1][0] * m[2][2] - m[1][2] * m[2][0];
        float m02 = m[1][0] * m[2][1] - m[1][1] * m[2][0];

        float m10 = m[0][1] * m[2][2] - m[0][2] * m[2][1];
        float m11 = m[0][0] * m[2][2] - m[0][2] * m[2][0];
        float m12 = m[0][0] * m[2][1] - m[0][1] * m[2][0];

        float m20 = m[0][1] * m[1][2] - m[0][2] * m[1][1];
        float m21 = m[0][0] * m[1][2] - m[0][2] * m[1][0];
        float m22 = m[0][0] * m[1][1] - m[0][1] * m[1][0];
        float[][] m = adjugate.m;
        m[0][0] = m00;
        m[0][1] = -1f*m01;
        m[0][2] = m02;

        m[1][0] = -1f*m10;
        m[1][1] = m11;
        m[1][2] = -1f*m12;

        m[2][0] = m20;
        m[2][1] = -1f*m21;
        m[2][2] = m22;
    }

    public void multiply(float val) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] *= val;
            }
        }
    }

    public void setUpVector(final Vector2D up) {
        final Vector2D perp = new Vector2D(up.getY(), -up.getX());
        float[][] m = {{perp.getX(), up.getX(), 0},
                {perp.getY(), up.getY(), 0},
                {0, 0, 1}};
        set(m);
    }

    public Matrix3x3 invertTranslateCopy() {
        Matrix3x3 ret = new Matrix3x3(this);
        ret.m[0][2] = -ret.m[0][2];
        ret.m[1][2] = -ret.m[1][2];
        return ret;
    }

    public void invertTranslate() {
        m[0][2] = -m[0][2];
        m[1][2] = -m[1][2];
    }

    public void set(final float[][] mat) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(mat[i], 0, m[i], 0, 3);
        }
    }

    public void set(final Matrix3x3 mat) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(mat.m[i], 0, m[i], 0, 3);
        }
    }

    public void transpose(final Matrix3x3 u) {
        float[][] m = {{u.m[0][0], u.m[1][0], 0},
                {u.m[0][1], u.m[1][1], 0},
                {0, 0, 1}};
        set(m);
    }

    public void transpose() {
        tmp[0][0] = m[0][0];
        tmp[0][1] = m[0][1];
        tmp[0][2] = m[0][2];

        tmp[1][0] = m[1][0];
        tmp[1][1] = m[1][1];
        tmp[1][2] = m[1][2];

        tmp[2][0] = m[2][0];
        tmp[2][1] = m[2][1];
        tmp[2][2] = m[2][2];

        m[0][0] = tmp[0][0];
        m[0][1] = tmp[1][0];
        m[0][2] = tmp[2][0];

        m[1][0] = tmp[0][1];
        m[1][1] = tmp[1][1];
        m[1][2] = tmp[2][1];

        m[2][0] = tmp[0][2];
        m[2][1] = tmp[1][2];
        m[2][2] = tmp[2][2];
    }


    public void rotate(float radians) {
        m[0][0] = (float) Math.cos(radians); m[0][1] = -(float) Math.sin(radians);
        m[1][0] = (float) Math.sin(radians); m[1][1] = (float) Math.cos(radians);
    }
//    public void rotateTo(float radians) {
//        m[0][0] = (float) Math.cos(radians); m[0][1] = -(float) Math.sin(radians);
//        m[1][0] = (float) Math.sin(radians); m[1][1] = (float) Math.cos(radians);
//    }
    public void inverseRotate() {
        rotateInverse.set(m[0][0], m[0][1], m[1][0], m[1][1]);
        rotateInverse.inverse();
        float[][] f = rotateInverse.get();
        m[0][0] = f[0][0];
        m[0][1] = f[0][1];
        m[1][0] = f[1][0];
        m[1][1] = f[1][1];
    }

    public void translate(final Point p) {
        setIdentity();
        m[0][2] = p.getX();
        m[1][2] = p.getY();
//		mul(i);

    }

    public void translate(final Vector2D p) {
        setIdentity();
        m[0][2] = p.getX();
        m[1][2] = p.getY();
    }
    public void move(final Vector2D p) {
        m[0][2] += p.getX();
        m[1][2] += p.getY();
    }

    public void translate(float x, float y) {
        m[0][2] = x;
        m[1][2] = y;
    }

    public Vector2D getMotionVector() {
        return new Vector2D(m[0][2], m[1][2]);
    }

    public void clear() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = 0f;
            }
        }
    }

    public void scale(float scale) {
        setIdentity();
        m[0][0] = scale;
        m[1][1] = scale;
        m[2][2] = 1.0f;
    }

    public Vector2D getScale() {
        return new Vector2D(m[0][0], m[1][1]);
    }

    public void scale(final Vector2D scale) {
        setIdentity();
        m[0][0] = scale.getX();
        m[1][1] = scale.getY();
        m[2][2] = 1.0f;
    }

    public void invertScale() {
        m[0][0] = 1.0f / m[0][0];
        m[1][1] = 1.0f / m[1][1];
        m[2][2] = 1.0f;
    }

    public Matrix3x3 invertScaleCopy() {
        Matrix3x3 ret = new Matrix3x3(this);
        ret.m[0][0] = 1.0f / m[0][0];
        ret.m[1][1] = 1.0f / m[1][1];
        ret.m[2][2] = 1.0f;
        return ret;
    }

    public Point[] mul(final Point[] points) {
        Point[] ret = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            final Point p = points[i];
            ret[i] = new Point(p.getX() * m[0][0] + p.getY() * m[0][1] + m[0][2],
                    p.getX() * m[1][0] + p.getY() * m[1][1] + m[1][2]);
        }
        return ret;
    }

    public Point mul(final Point points) {
        final Point p = points;
        return new Point(p.getX() * m[0][0] + p.getY() * m[0][1] + m[0][2],
                p.getX() * m[1][0] + p.getY() * m[1][1] + m[1][2]);
    }

    public Vector2D mul(final Vector2D p) {
        return new Vector2D(p.getX() * m[0][0] + p.getY() * m[0][1] + m[0][2],
                p.getX() * m[1][0] + p.getY() * m[1][1] + m[1][2]);
    }

    public Point[] mul(final Point[] points, Point[] ret) {

        for (int i = 0; i < points.length; i++) {
            final Point p = points[i];
            ret[i] = new Point(p.getX() * m[0][0] + p.getY() * m[0][1] + m[0][2],
                    p.getX() * m[1][0] + p.getY() * m[1][1] + m[1][2]);
        }
        return ret;
    }

    public Vector2D[] mul(final Vector2D[] localSpace, final Vector2D[] ret) {

        for (int i = 0; i < localSpace.length; i++) {
            final Vector2D p = localSpace[i];
            final Vector2D r = ret[i];
            r.setX(p.getX() * m[0][0] + p.getY() * m[0][1] + m[0][2]);
            r.setY(p.getX() * m[1][0] + p.getY() * m[1][1] + m[1][2]);
        }
        return ret;
    }

    public Matrix3x3 mulCopy(final Matrix3x3 matrix) {
        float[][] u = matrix.m;
        Matrix3x3 ret = new Matrix3x3();
        ret.clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    ret.m[i][j] += m[i][k] * u[k][j];
                }
            }
        }
        return ret;
    }

    public void mul(final float u[][]) {
        float[][] tmp = new float[3][3];
//		clearTmp();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    tmp[i][j] += m[i][k] * u[k][j];
                }
            }
        }
        m = tmp;
    }

    public Matrix3x3 multiply(final Matrix3x3 matrix) {
        Matrix3x3 ret = new Matrix3x3();
        float[][] u = matrix.m;
        float[][] tmp = ret.m;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    tmp[i][j] += m[i][k] * u[k][j];
                }
            }
        }
        return ret;
    }

    public Matrix3x3 fastMulCopy(final Matrix3x3 matrix) {
        Matrix3x3 ret = new Matrix3x3();
        float[][] u = matrix.m;
        float[][] tmp = ret.m;
        tmp[0][0] = m[0][0] * u[0][0] + m[0][1] * u[1][0];// + m[0][2]*u[2][0];
        tmp[0][1] = m[0][0] * u[0][1] + m[0][1] * u[1][1];// + m[0][2]*u[2][1];
        tmp[0][2] = m[0][0] * u[0][2] + m[0][1] * u[1][2] + m[0][2];//*u[2][2];

        tmp[1][0] = m[1][0] * u[0][0] + m[1][1] * u[1][0];// + m[1][2]*u[2][0];
        tmp[1][1] = m[1][0] * u[0][1] + m[1][1] * u[1][1];// + m[1][2]*u[2][1];
        tmp[1][2] = m[1][0] * u[0][2] + m[1][1] * u[1][2] + m[1][2];//*u[2][2];
        return ret;
    }

    public void fastMul(final Matrix3x3 matrix) {
        float[][] u = matrix.m;
        tmp[2][2] = 1f;
        tmp[0][0] = m[0][0] * u[0][0] + m[0][1] * u[1][0];// + m[0][2]*u[2][0];
        tmp[0][1] = m[0][0] * u[0][1] + m[0][1] * u[1][1];// + m[0][2]*u[2][1];
        tmp[0][2] = m[0][0] * u[0][2] + m[0][1] * u[1][2] + m[0][2];//*u[2][2];

        tmp[1][0] = m[1][0] * u[0][0] + m[1][1] * u[1][0];// + m[1][2]*u[2][0];
        tmp[1][1] = m[1][0] * u[0][1] + m[1][1] * u[1][1];// + m[1][2]*u[2][1];
        tmp[1][2] = m[1][0] * u[0][2] + m[1][1] * u[1][2] + m[1][2];//*u[2][2];
        float[][] t = m;
        m = tmp;
        tmp = t;
    }
    public void fullFastMul(final Matrix3x3 matrix) {
        float[][] u = matrix.m;
        tmp[0][0] = m[0][0] * u[0][0] + m[0][1] * u[1][0] + m[0][2]*u[2][0];
        tmp[0][1] = m[0][0] * u[0][1] + m[0][1] * u[1][1] + m[0][2]*u[2][1];
        tmp[0][2] = m[0][0] * u[0][2] + m[0][1] * u[1][2] + m[0][2]*u[2][2];

        tmp[1][0] = m[1][0] * u[0][0] + m[1][1] * u[1][0] + m[1][2]*u[2][0];
        tmp[1][1] = m[1][0] * u[0][1] + m[1][1] * u[1][1] + m[1][2]*u[2][1];
        tmp[1][2] = m[1][0] * u[0][2] + m[1][1] * u[1][2] + m[1][2]*u[2][2];

        tmp[2][0] = m[2][0] * u[0][0] + m[2][1] * u[1][0] + m[2][2]*u[2][0];
        tmp[2][1] = m[2][0] * u[0][1] + m[2][1] * u[1][1] + m[2][2]*u[2][1];
        tmp[2][2] = m[2][0] * u[0][2] + m[2][1] * u[1][2] + m[2][2]*u[2][2];
        float[][] t = m;
        m = tmp;
        tmp = t;
    }
    public Matrix3x3 thaMulCopy(final Matrix3x3 matrix) {
        float[][] u = matrix.m;
        Matrix3x3 ret = new Matrix3x3();
        float[][] tmp = ret.m;
//        setIdentity(tmp);
        tmp[0][0] = m[0][0] * u[0][0] + m[0][1] * u[1][0] + m[0][2]*u[2][0];
        tmp[0][1] = m[0][0] * u[0][1] + m[0][1] * u[1][1] + m[0][2]*u[2][1];
        tmp[0][2] = m[0][0] * u[0][2] + m[0][1] * u[1][2] + m[0][2]*u[2][2];

        tmp[1][0] = m[1][0] * u[0][0] + m[1][1] * u[1][0] + m[1][2]*u[2][0];
        tmp[1][1] = m[1][0] * u[0][1] + m[1][1] * u[1][1] + m[1][2]*u[2][1];
        tmp[1][2] = m[1][0] * u[0][2] + m[1][1] * u[1][2] + m[1][2]*u[2][2];

        tmp[2][0] = m[2][0] * u[0][0] + m[2][1] * u[1][0] + m[2][2]*u[2][0];
        tmp[2][1] = m[2][0] * u[0][1] + m[2][1] * u[1][1] + m[2][2]*u[2][1];
        tmp[2][2] = m[2][0] * u[0][2] + m[2][1] * u[1][2] + m[2][2]*u[2][2];
        return ret;
    }

    public void fastMul(final Matrix3x3... mat) {
        set(mat[0]);
        if(mat.length == 1) {
            return;
        }
        for (int i = 1;i< mat.length; i++) {
            fastMul(mat[i]);
        }
    }

    private void setIdentity(final float[][] m) {
        m[0][0] = 1f; m[0][1] = m[0][2] = 0f;
        m[1][0] = 0f; m[1][1] = 1f; m[1][2] = 0f;
        m[2][0] = 0f; m[2][1] = 0f; m[2][2] = 1f;

    }
    public float[][] getCopy() {
        float[][] ret = {{m[0][0], m[0][1], m[0][2]},
                {m[1][0], m[1][1], m[1][2]},
                {m[2][0], m[2][1], m[2][2]}};
        return ret;
    }
}

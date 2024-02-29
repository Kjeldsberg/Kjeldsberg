package no.fun.stuff.engine.game.geo.triangle;

import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextureSideScanTest {
    @Test
    void barycentricCartesianCoordinateUV() {
        Vector2D A = new Vector2D(0.0f, 0.1f);
        Vector2D B = new Vector2D(0.1f, 0.0f);
        Vector2D C = new Vector2D(0.0f, 0.0f);
        float u = 0.5f;
        float v = 0.5f;

        Vector2D result = getVector(A, B, C, u, v);
        System.out.println(result);
        int test = 0;


    }
    @Test
    void barycentricCartesianCoordinateUV2() {
        Vector2D A = new Vector2D(1.0f, 1.0f);
        Vector2D B = new Vector2D(4.0f, 2.0f);
        Vector2D C = new Vector2D(4.0f, 5.0f);
        float u = 1.0f;
        float v = 0.0f;

        Vector2D result = getVector(A, B, C, u, v);
        System.out.println(A);
        System.out.println(result);
        assertTrue(Util.compare(A.getX(), result.getX()));

        int test = 0;


    }

    private static Vector2D getVector(Vector2D A, Vector2D B, Vector2D C, float u, float v) {
        Vector2D AC = C.minus(A);
        Vector2D AB = B.minus(A);

        Vector2D scaleAB = AB.scale(u);
        Vector2D scaleAC = AC.scale(v);
        Vector2D result = A.add(scaleAB).add(scaleAC);
        return result;
    }

    @Test
    void barycentricCoordinate() {
        Vector2D p0 = new Vector2D(0.0f, 0.0f);
        Vector2D p1 = new Vector2D(0.0f, 1.0f);
        Vector2D p2 = new Vector2D(1.0f, 0.0f);


        float a = 0.5f;
        float b = 0.5f;
        float c = 1 - a - b;

        p0.mul(a);
        p1.mul(b);
        p2.mul(c);
        Vector2D result = new Vector2D();
        result.setXY(p0);
        result.pluss(p1);
        result.pluss(p2);
        System.out.println(result);
        int test = 0;

    }
    @Test
    void barycentricCoordinate2() {
        Vector2D p0 = new Vector2D(1.0f, 1.0f);
        Vector2D p1 = new Vector2D(4.0f, 5.0f);
        Vector2D p2 = new Vector2D(4.0f, 2.0f);


        float u = 0.0f;
        float v = 1.0f;
        float c = 1 - (u + v);

        p0.mul(u);
        p1.mul(v);
        p2.mul(c);
        Vector2D result = new Vector2D();
        result.setXY(p0);
        result.pluss(p1);
        result.pluss(p2);
            System.out.println(result);
        int test = 0;

    }
    @Test
    void barycentricCoordinate3() {
        Vector2D p0 = new Vector2D(1.0f, 1.0f);
        Vector2D p1 = new Vector2D(4.0f, 5.0f);
        Vector2D p2 = new Vector2D(4.0f, 2.0f);


        float u = 1.0f;
        float v = 0.0f;
        float w = 1 - (u + v);

        TextureSideScan textureSideScan = new TextureSideScan(p0, p1, p1, p1);
        Vector2D result = new Vector2D();
        textureSideScan.barycentricCooridnate(p0, p1, p2, w, u, v, result);
        System.out.println(result);
        int test = 0;

    }
    @Test
    void lerp() {
//        Image image = new Image("/pitrizzo-SpaceShip-gpl3-opengameart-24x24.png");
        TextureSideScan textureSideScan = new TextureSideScan(new Vector2D(10.0f, 0.0f), new Vector2D(0.0f, 10.0f),
                new Vector2D(1.0f, 0.0f), new Vector2D(0.0f, 1.0f));

        Vector2D result = new Vector2D();
        Vector2D result1 = new Vector2D();
        Vector2D result2 = new Vector2D();
        Vector2D result3 = new Vector2D();
        Vector2D result4 = new Vector2D();
        Vector2D result5 = new Vector2D();

        textureSideScan.lerp(1.0f, result);
        textureSideScan.lerp(0.9f, result1);
        textureSideScan.lerp(0.8f, result2);
        textureSideScan.lerp(0.7f, result3);
        textureSideScan.lerp(0.6f, result4);
        textureSideScan.lerp(0.5f, result5);
        int test = 0;
    }
    @Test
    void lerp2() {
//        Image image = new Image("/pitrizzo-SpaceShip-gpl3-opengameart-24x24.png");
        TextureSideScan textureSideScan = new TextureSideScan(new Vector2D(10.0f, 0.0f), new Vector2D(0.0f, 10.0f),
                new Vector2D(1.0f, 0.0f), new Vector2D(0.0f, 1.0f));

        Vector2D result = new Vector2D();
        Vector2D result1 = new Vector2D();
        Vector2D result2 = new Vector2D();
        Vector2D result3 = new Vector2D();
        Vector2D result4 = new Vector2D();
        Vector2D result5 = new Vector2D();
        final Vector2D p0 = new Vector2D(2.0f, 4.0f);
        final Vector2D p1 = new Vector2D(3.0f, 3.0f);

        textureSideScan.lerp(1.0f, p0, p1, result);
        textureSideScan.lerp(0.9f, p0, p1, result1);
        textureSideScan.lerp(0.8f, p0, p1, result2);
        textureSideScan.lerp(0.7f, p0, p1, result3);
        textureSideScan.lerp(0.6f, p0, p1, result4);
        textureSideScan.lerp(0.5f, p0, p1, result5);
        textureSideScan.lerp(0.0f, p0, p1, result5);
        int test = 0;
    }
}
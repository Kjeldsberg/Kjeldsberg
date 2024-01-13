package no.fun.stuff.engine;

import no.fun.stuff.engine.game.geo.SideScan;
import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestSlope {
    @Test
    public void test() {
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(16, 15);
        Vector2D p3 = new Vector2D(16, 0);
        Vector2D p4 = new Vector2D(0, 0);
        Vector2D p5 = new Vector2D(0, 15);
        Vector2D p6 = new Vector2D(16, 15);

        TestSlope testSlope = new TestSlope();

//        testSlope.print(p1, p2);
        List<APoint> leftSide = testSlope.leftSide(p1, p2);
        List<APoint> side = testSlope.side(p1, p2);
        for(int i = 0;i<leftSide.size();i++) {
            System.out.println("diff: (" + (leftSide.get(i).x - side.get(i).x) + ")");
        }
    }

    private void print(final Vector2D p1, final Vector2D p2) {

        SideScan sideScan = new SideScan(p1, p2);
        float v = sideScan.xOfY(16.0f);
        int est = 0;
        float p1x = p1.getX();
        for (int y = 0; y <= (int) p2.getY(); y++) {
            float x = sideScan.xOfY(y);

            System.out.println("(" + x + ", " + y + ")\t slope: (" + p1x + ", " + y + ")\tdiff: " + (p1x - x));
            p1x += sideScan.dl;
        }
    }

    public List<APoint> leftSide(final Vector2D p1, Vector2D p2) {
        ArrayList<APoint> ret = new ArrayList<>();

        SideScan sideScan = new SideScan(p1, p2);
        float p1x = p1.getX();
        for (int y = 0; y <= (int) p2.getY(); y++) {

            int ceil = (int) Math.ceil(p1x+0.0001f);
            ret.add(new APoint(ceil, y));
            p1x += sideScan.dl;
        }
        return ret;
    }
    public List<APoint> side(final Vector2D p1, Vector2D p2) {
        ArrayList<APoint> ret = new ArrayList<>();

        SideScan sideScan = new SideScan(p1, p2);
        float p1x = p1.getX();
        for (int y = 0; y <= (int) p2.getY(); y++) {

            int p1x1 = (int) Math.floor(p1x);
            ret.add(new APoint(p1x1, y));
            p1x += sideScan.dl;
        }
        return ret;
    }
    public void setPixel(int x, int y) {

    }
    record APoint(int x, int y) {

    }
}

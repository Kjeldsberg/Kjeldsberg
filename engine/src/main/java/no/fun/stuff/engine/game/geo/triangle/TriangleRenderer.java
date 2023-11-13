package no.fun.stuff.engine.game.geo.triangle;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.geo.SideScan;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

import java.util.Comparator;

import static java.util.Arrays.*;

public class TriangleRenderer {
    private final Renderer renderer;
    private final Vector2D[] workingVectors = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
    private final Vector2D[] ordered = new Vector2D[3];

    public TriangleRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void drawTriangle2(final Vector2D v1, final Vector2D v2, final Vector2D v3, int color) {
        workingVectors[0].setXY(v1);
        workingVectors[1].setXY(v2);
        workingVectors[2].setXY(v3);
        Comparator<Vector2D> comparator1 = (o1, o2) -> {
            if (Util.compare(o1.getY(), o2.getY(), Util.epsilon)) {
                return 0;
            }
            if (o1.getY() < o2.getY()) {
                return -1;
            }
            return 1;
        };
        sort(workingVectors, comparator1);
        final Vector2D p0 = workingVectors[0];
        final Vector2D p1 = workingVectors[1];
        final Vector2D p2 = workingVectors[2];
        boolean flatTOp = Util.compare(p0.getY(), p1.getY(), Util.epsilon);
        boolean flatBottom = Util.compare(p1.getY(), p2.getY(), Util.epsilon);
        if (flatBottom) {
            fillFlatBottom(p0, p1, p2, color);
            return;
        }
        if (flatTOp) {
            fillFlatTop(p0, p1, p2, color);
            return;
        }
        SideScan leftSide = null;
        SideScan rightSide = null;
        Vector2D midtPoint = null;
        final boolean toLeft = p1.getX() < p2.getX();
        if (toLeft) {
//            leftSide = new SideScan(p1, p0);
            rightSide = new SideScan(p2, p0);
            midtPoint = new Vector2D(rightSide.xOfY(p1.getY()), p1.getY());
            fillFlatBottom(p0, p1, midtPoint, color);
            fillFlatTop(p1, p2, midtPoint, color);
        }else {
            leftSide = new SideScan(p2, p0);
//            rightSide = new SideScan(p1, p0);
            midtPoint = new Vector2D(leftSide.xOfY(p1.getY()), p1.getY());
            fillFlatBottom(p0, midtPoint, p1, color);
            fillFlatTop(midtPoint, p2, p1, color);
        }


//        Vector2D v4 = new Vector2D(
//                (int) (p0.getX() + ((float) (p1.getY() - p0.getY()) / (float) (p2.getY() - p0.getY())) * (p2.getX() - p0.getX())), p1.getY());
//        drawFlatBottomTriangle2(p0, p1, v4, color);
//        drawFlatTopTriangle2(p1, v4, p2, color);

    }

    public void drawTriangle(final Vector2D v1, Vector2D v2, Vector2D v3, int color) {
        workingVectors[0].setXY(v1);
        workingVectors[1].setXY(v2);
        workingVectors[2].setXY(v3);
//        sort(workingVectors, (o1, o2) -> (int)(o1.getY() - o2.getY()));
        Vector2D[] order = rotateToLowestY(workingVectors);
        final Vector2D p0 = order[0];
        final Vector2D p1 = order[1];
        final Vector2D p2 = order[2];
//        final Vector2D p0 = workingVectors[0];
//        final Vector2D p1 = workingVectors[1];
//        final Vector2D p2 = workingVectors[2];
        boolean flatTOp = Util.compare(p0.getY(), p2.getY(), Util.epsilon);
        boolean flatBottom = Util.compare(p1.getY(), p2.getY(), Util.epsilon);
        if (flatBottom) {
            fillFlatBottom(p0, p1, p2, color);
            return;
        }
        if (flatTOp) {
            fillFlatTop(p0, p1, p2, color);
            return;
        }

//        final SideScan left = new SideScan(p0, p2);
        float leftY = p1.getY() - p0.getY();
        float rightY = p2.getY() - p0.getY();

        boolean leftIsSmallest = leftY < rightY;
//        final SideScan scan =  leftIsSmallest ? new SideScan(p1, p0) : new SideScan(p2, p1);
        Vector2D v4 = new Vector2D(
                (int) (p0.getX() + ((float) (p1.getY() - p0.getY()) / (float) (p2.getY() - p0.getY())) * (p2.getX() - p0.getX())), p1.getY());
        if (leftIsSmallest) {
            final SideScan right = new SideScan(p2, p0);
            final Vector2D midPoint = new Vector2D((int) right.xOfY(p1.getY()), p1.getY());
//            fillFlatBottom(p0, p1, midPoint, color);
//            fillFlatTop(p1, p2, midPoint, color);
            drawFlatBottomTriangle(p0, p1, midPoint, color);
            drawFlatTopTriangle(p1, p2, midPoint, color);

        } else {
            final SideScan left = new SideScan(p1, p0);
            final Vector2D midPoint = new Vector2D(left.xOfY(p2.getY()), p2.getY());
            fillFlatBottom(p0, midPoint, p2, color);
            fillFlatTop(midPoint, p1, p2, color);
//            drawFlatBottomTriangle(p0, midPoint, p2, color);
//            drawFlatTopTriangle(midPoint, p1, p2, color);

        }
    }

    public Vector2D[] rotateToLowestY(final Vector2D[] workingVectors) {
        int lowestY = 0;
        int i = 0;
        for (; i < workingVectors.length; i++) {
            if (workingVectors[i].getY() < workingVectors[lowestY].getY()) {
                lowestY = i;
            }
        }

        if (lowestY > 0) {
            ordered[0] = workingVectors[lowestY++];
            ordered[1] = workingVectors[lowestY++ % 3];
            ordered[2] = workingVectors[lowestY % 3];
        } else {
            for (int j = 0; j < workingVectors.length; j++) {
                ordered[j] = workingVectors[j];
            }
        }

        return ordered;
    }

    private void drawFlatTopTriangle(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        float slopex = (p1.getX() - p0.getX()) / (p1.getY() - p0.getY());
        float slopey = (p1.getX() - p2.getX()) / (p1.getY() - p2.getY());

        float xl = p1.getX();
        float xr = xl;

        for (int y = (int) p1.getY(); y > (int) p0.getY(); y--) {
            for (int x = (int) xl; x <= (int) xr; x++) {
                renderer.setPixel(x, y, color);
            }
            xl -= slopex;
            xr -= slopey;
        }

    }

    private void drawFlatBottomTriangle(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        float slopex = (p1.getX() - p0.getX()) / (p1.getY() - p0.getY());
        float slopey = (p2.getX() - p0.getX()) / (p2.getY() - p0.getY());

        float xl = p0.getX();
        float xr = xl;

        for (int y = (int) p0.getY(); y < (int) p2.getY(); y++) {
            for (int x = (int) xl; x <= (int) xr; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += slopex;
            xr += slopey;
        }

    }

    private void drawFlatBottomTriangle2(final Vector2D v1, final Vector2D v2, final Vector2D v3, int color) {
        float invslope1 = (v2.getX() - v1.getX()) / (v2.getY() - v1.getY());
        float invslope2 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());
        float curx1 = v1.getX();
        float curx2 = v1.getX();

        for (int scanlineY = (int) v1.getY(); scanlineY <= v2.getY(); scanlineY++) {
            for (int x = (int) curx1; x < curx2; x++) {
                renderer.setPixel(x, scanlineY, color);
            }
            curx1 += invslope1;
            curx2 += invslope2;
        }
    }

    private void drawFlatTopTriangle2(final Vector2D v1, final Vector2D v2, final Vector2D v3, int color) {
        float invslope1 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());
        float invslope2 = (v3.getX() - v2.getX()) / (v3.getY() - v2.getY());

        float curx1 = v3.getX();
        float curx2 = v3.getX();

        for (int scanlineY = (int) v3.getY(); scanlineY > v1.getY(); scanlineY--) {
            for (int x = (int) curx1; x < curx2; x++) {
                renderer.setPixel(x, scanlineY, color);
            }
            curx1 -= invslope1;
            curx2 -= invslope2;
        }
    }

    private void fillFlatTop(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
//        final Vector2D p0 = workingVectors[0];
//        final Vector2D p1 = p1;
//        final Vector2D p2 = workingVectors[2];

        SideScan left = new SideScan(p0, p1);
        SideScan right = new SideScan(p2, p1);
        int yStart = (int) p0.getY();
        int yStop = (int) p1.getY();
        float dl = left.dl;
        float dr = right.dl;
        float xl = p0.getX();
        float xr = p2.getX();
        for (int y = yStart; y <= yStop; y++) {
            int startX = (int) xl;
            int stopX = (int) xr;
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += dl;
            xr += dr;
        }
    }

    private void fillFlatBottom(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
//        final Vector2D p0 = workingVectors[0];
//        final Vector2D p1 = p1;
//        final Vector2D p2 = workingVectors[2];
        final SideScan left = new SideScan(p1, p0);
        final SideScan right = new SideScan(p2, p0);

        int yStart = (int) p0.getY();
        int yStop = (int) p1.getY();
        float dl = left.dl;
        float dr = right.dl;
        float xl = p0.getX();
        float xr = p0.getX();
        for (int y = yStart; y <= yStop; y++) {
            int startX = (int) xl;
            int stopX = (int) xr;
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += dl;
            xr += dr;
        }
    }

    public void drawTriangle(final Vector2D[] vectors, int color) {
        sort(vectors, new Comparator<Vector2D>() {
            @Override
            public int compare(Vector2D o1, Vector2D o2) {
                return (int) (o1.getY() - o2.getY());
            }
        });

        boolean flatTOp = Util.compare(vectors[0].getY(), vectors[1].getY(), Util.epsilon);
        boolean flatBottom = Util.compare(vectors[1].getY(), vectors[2].getY(), Util.epsilon);

        if (flatTOp) {

        }
    }
}

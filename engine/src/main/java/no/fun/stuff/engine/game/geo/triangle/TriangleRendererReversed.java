package no.fun.stuff.engine.game.geo.triangle;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Vector2D;

public class TriangleRendererReversed {
    private final Renderer renderer;
    private final Vector2D[] workingVectors = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
    private final Vector2D[] ordered = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
    private final SideScan p1p0 = new SideScan(new Vector2D(), new Vector2D());
    private final SideScan p2p0 = new SideScan(new Vector2D(), new Vector2D());
    private final SideScan lowerLeft = new SideScan(new Vector2D(), new Vector2D());
    public TriangleRendererReversed(Renderer renderer) {
        this.renderer = renderer;
    }

    public void drawTriangle2(final Vector2D v1, final Vector2D v2, final Vector2D v3, int color) {
        workingVectors[0].setXY(v1);
        workingVectors[1].setXY(v2);
        workingVectors[2].setXY(v3);
        rotateToLowestY(workingVectors);
        float firstSide = ordered[1].getY() - ordered[0].getY();
        float secondSide = ordered[2].getY() - ordered[1].getY();
        float thirdSide = ordered[0].getY() - ordered[2].getY();
        boolean firstSideLefts = firstSide > 0.0f;
        boolean secondSideLefts = secondSide > 0.0f;
        boolean thirdSideLefts = thirdSide > 0.0f;
        final Vector2D p0 = ordered[0];
        final Vector2D p1 = ordered[1];
        final Vector2D p2 = ordered[2];
        SideScan longSide;
        SideScan upperLeft;
        p1p0.setNewEgde(p1, p0);
        p2p0.setNewEgde(p2, p0);

        if (p1.getY() >= p2.getY()) {
            longSide = p1p0;    //new SideScan(p1, p0);
            upperLeft = p2p0;//new SideScan(p2, p0);
            lowerLeft.setNewEgde(p1, p2);// = new SideScan(p1, p2);
            longSide.leftside = firstSideLefts;
            upperLeft.leftside = thirdSideLefts;
            lowerLeft.leftside = secondSideLefts;
            if (upperLeft.flatLine) {
                drawFlattTopOrBottom(longSide, lowerLeft, color);
            } else {
                drawRightSegments(upperLeft, lowerLeft, longSide, color);
            }
        } else {
            longSide = p2p0; //new SideScan(p2, p0);
            upperLeft = p1p0; //new SideScan(p1, p0);
            lowerLeft.setNewEgde(p2, p1); //= new SideScan(p2, p1);
            longSide.leftside = thirdSideLefts;
            upperLeft.leftside = firstSideLefts;
            lowerLeft.leftside = secondSideLefts;

            if (lowerLeft.flatLine) {
                drawFlattBottom(longSide, upperLeft, color);
            } else {
                drawLeftSegments(upperLeft, lowerLeft, longSide, color);
            }
        }
    }


    private void drawLeftSegments(final SideScan upperPart,
                                  final SideScan lowerPart,
                                  final SideScan longPart,
                                  int color) {


        int yStart = (int) Math.floor(longPart.p0.getY());
        int yStop = (int) Math.floor(lowerPart.p1.getY());
        float dl = lowerPart.dl;
        float dr = longPart.dl;
        float xl = lowerPart.p0.getX();
        float xr = longPart.p0.getX();
        int y = yStart;
        for (; y > yStop; y--) {
            int startX = lowerPart.leftside ? (int) Math.ceil(xl) : (int) Math.floor(xl);
            int stopX = (int) Math.floor(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl -= dl;
            xr -= dr;
        }

        yStop = (int) Math.floor(longPart.p1.getY());
        xl = upperPart.p0.getX();
        dl = upperPart.dl;
        for (; y > yStop; y--) {
            int startX = upperPart.leftside ? (int) Math.ceil(xl) : (int) Math.floor(xl);
            int stopX = (int) Math.floor(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl -= dl;
            xr -= dr;
        }
    }

    private void drawRightSegments(final SideScan upperPart,
                                   final SideScan lowerPart,
                                   final SideScan longPart,
                                   int color) {


        int yStart = (int) Math.floor(longPart.p0.getY());
        int yStop = (int) Math.floor(upperPart.p0.getY());
        float dl = longPart.dl;
        float dr = lowerPart.dl;
        float xl = lowerPart.p0.getX();
        float xr = lowerPart.p0.getX();
        int y;
        for (y = yStart; y > yStop; y--) {
            int startX = longPart.leftside ? (int) Math.ceil(xl) : (int) Math.floor(xl);
            int stopX = (int) Math.floor(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl -= dl;
            xr -= dr;
        }

        yStop = (int) Math.floor(longPart.p1.getY());
        dr = upperPart.dl;
        xr = upperPart.p0.getX();
        for (; y > yStop; y--) {
            int stopX = (int) Math.floor(xr);
            int startX = longPart.leftside ? (int) Math.ceil(xl) : (int) Math.floor(xl);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl -= dl;
            xr -= dr;
        }
    }

    public void drawFlattTopOrBottom(final SideScan leftPart, final SideScan rightPart, int color) {

        int yStart = (int) Math.floor(leftPart.p0.getY());
        float yStop = (int) Math.floor(leftPart.p1.getY());
        float xl = leftPart.p0.getX();
        float xr = rightPart.p0.getX();
        float dl = leftPart.dl;
        float dr = rightPart.dl;
        for (int y = yStart; y > yStop; y--) {
            int start = (int) xl;
            int stop = (int) xr;
            for (int x = start; x < stop; x++) {
                renderer.setPixel(x, y, color);
            }
            xr -= dr;
            xl -= dl;
        }
    }

    public void drawFlattBottom(final SideScan leftPart, final SideScan rightPart, int color) {

        int yStart = (int) Math.floor(leftPart.p0.getY());
        float yStop = (int) Math.floor(leftPart.p1.getY());
        float xl = rightPart.p0.getX();
        float xr = leftPart.p1.getX();
        float dl = leftPart.dl;
        float dr = rightPart.dl;
        for (int y = yStart; y > yStop; y--) {
            int start = (int) xl;
            int stop = (int) xr;
            for (int x = start; x < stop; x++) {
                renderer.setPixel(x, y, color);
            }
            xr -= dl;
            xl -= dr;
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
            ordered[0].setXY(workingVectors[lowestY++]);
            ordered[1].setXY(workingVectors[lowestY++ % 3]);
            ordered[2].setXY(workingVectors[lowestY % 3]);
        } else {
            for (int j = 0; j < workingVectors.length; j++) {
                ordered[j].setXY(workingVectors[j]);
            }
        }

        return ordered;
    }

}

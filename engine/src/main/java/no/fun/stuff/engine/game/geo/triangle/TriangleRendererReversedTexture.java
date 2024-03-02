package no.fun.stuff.engine.game.geo.triangle;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Vector2D;

public class TriangleRendererReversedTexture {
    private final Renderer renderer;
    private final Vector2D[] workingVectors = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
    private final Vector2D[] workingtexture = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
    private final Vector2D[] orderedTexture = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
    private final Vector2D[] ordered = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
    private final Vector2D[] texture = new Vector2D[3];
    private final TextureSideScan p1p0 = new TextureSideScan(new Vector2D(), new Vector2D(), new Vector2D(), new Vector2D());
    private final TextureSideScan p2p0 = new TextureSideScan(new Vector2D(), new Vector2D(), new Vector2D(), new Vector2D());
    private final TextureSideScan lowerLeft = new TextureSideScan(new Vector2D(), new Vector2D(), new Vector2D(), new Vector2D());
    public TriangleRendererReversedTexture(final Renderer renderer) {
        this.renderer = renderer;
    }
    private RotateToLowestY rotateToLowestY = new RotateToLowestY();
    public void drawTriangle(final Vector2D v1, final Vector2D v2, final Vector2D v3,
                             final Vector2D uv1, final Vector2D uv2, final Vector2D uv3, final Image theImage) {
        workingVectors[0].setXY(v1);
        workingVectors[1].setXY(v2);
        workingVectors[2].setXY(v3);
        workingtexture[0].setXY(uv1);
        workingtexture[1].setXY(uv2);
        workingtexture[2].setXY(uv3);
        final int[] orderedIndex = rotateToLowestY.rotateToLowestY(v1, v2, v3);
        ordered[0].setXY(workingVectors[orderedIndex[0]]);
        ordered[1].setXY(workingVectors[orderedIndex[1]]);
        ordered[2].setXY(workingVectors[orderedIndex[2]]);
        orderedTexture[0].setXY(workingtexture[orderedIndex[0]]);
        orderedTexture[1].setXY(workingtexture[orderedIndex[1]]);
        orderedTexture[2].setXY(workingtexture[orderedIndex[2]]);
        // find top-left
        boolean firstSideLefts = ordered[1].getY() - ordered[0].getY() > 0.0f;
        boolean secondSideLefts = ordered[2].getY() - ordered[1].getY() > 0.0f;
        boolean thirdSideLefts = ordered[0].getY() - ordered[2].getY() > 0.0f;
        final Vector2D p0 = ordered[0];
        final Vector2D p1 = ordered[1];
        final Vector2D p2 = ordered[2];
        final Vector2D UV0 = orderedTexture[0];
        final Vector2D UV1 = orderedTexture[1];
        final Vector2D UV2 = orderedTexture[2];
        final boolean toLeft = p1.getX() <= p2.getX();
        TextureSideScan longSide;
        TextureSideScan upperLeft;
        p1p0.setNewEgde(p1, p0, UV1, UV0);
        p2p0.setNewEgde(p2, p0, UV2, UV0);

        if (toLeft) {
            if (p1.getY() > p2.getY()) {
                longSide = p1p0;    //new SideScan(p1, p0);
                upperLeft = p2p0;//new SideScan(p2, p0);
                lowerLeft.setNewEgde(p1, p2, UV1, UV2);// = new SideScan(p1, p2);
                longSide.leftside = firstSideLefts;
                upperLeft.leftside = thirdSideLefts;
                lowerLeft.leftside = secondSideLefts;
                if (upperLeft.flatLine) {
                    drawFlattTop(longSide, lowerLeft, upperLeft, theImage);
                } else {
                    drawRightSegments(upperLeft, lowerLeft, longSide, theImage);
                }
            } else {
                longSide = p2p0; //new SideScan(p2, p0);
                upperLeft = p1p0; //new SideScan(p1, p0);
                lowerLeft.setNewEgde(p2, p1, UV2, UV1); //= new SideScan(p2, p1);
                longSide.leftside = thirdSideLefts;
                upperLeft.leftside = firstSideLefts;
                lowerLeft.leftside = secondSideLefts;

                if (lowerLeft.flatLine) {
                    drawFlattBottom(longSide, upperLeft,lowerLeft,theImage);
                } else {
                    drawLeftSegments(upperLeft, lowerLeft, longSide, theImage);
                }
            }
        }
    }


    private void drawLeftSegments(final TextureSideScan upperPart,
                                  final TextureSideScan lowerPart,
                                  final TextureSideScan longPart,
                                  Image texture) {


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
                renderer.setPixel(x, y, 0xffddeeee);
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
                renderer.setPixel(x, y, 0xffddeeee);
            }
            xl -= dl;
            xr -= dr;
        }
    }

    private void drawRightSegments(final TextureSideScan upperPart,
                                   final TextureSideScan lowerPart,
                                   final TextureSideScan longPart,
                                   final Image color) {

        // Puv = uv0*u0 + uv1*u1 + uv2*u2;

        int yStart = (int) Math.floor(longPart.p0.getY());
        int yStop = (int) Math.floor(upperPart.p0.getY());
        float dl = longPart.dl;
        float dr = lowerPart.dl;
        float xl = lowerPart.p0.getX();
        float xr = lowerPart.p0.getX();
        float uv0l = lowerPart.uv0.getX();
        float uv0r = lowerPart.uv1.getX();
        float uv1l = lowerPart.uv0.getX();
        float uv1r = lowerPart.uv1.getX();
        float uv2l = lowerPart.uv0.getX();
        float uv3r = lowerPart.uv1.getX();
        int y;
        for (y = yStart; y > yStop; y--) {
            int startX = longPart.leftside ? (int) Math.ceil(xl) : (int) Math.floor(xl);
            int stopX = (int) Math.floor(xr);

            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, 0xffddeeee/*color*/);
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
                renderer.setPixel(x, y, 0xffddeeee);
            }
            xl -= dl;
            xr -= dr;
        }
    }
    final Vector2D ABuv = new Vector2D();
    final Vector2D CBuv = new Vector2D();
    final Vector2D dAB = new Vector2D();
    final Vector2D dCB = new Vector2D();
    final Vector2D result = new Vector2D();
    final Vector2D add = new Vector2D();

    public void drawFlattTop(final TextureSideScan leftPart,
                                     final TextureSideScan rightPart,
                                     final TextureSideScan upperpart, Image color) {

        int yStart = (int) Math.floor(leftPart.p0.getY());
        float yStop = (int) Math.floor(leftPart.p1.getY());
        float xl = leftPart.p0.getX();
        float xr = rightPart.p0.getX();
        float dl = leftPart.dl;
        float dr = rightPart.dl;
        ABuv.setXY(leftPart.uv1); ABuv.sub(leftPart.uv0);
        CBuv.setXY(rightPart.uv1); CBuv.sub(rightPart.uv0);
        dAB.setXY(leftPart.p1); dAB.sub(leftPart.p0);
        dCB.setXY(rightPart.p1); dCB.sub(rightPart.p0);
        float dABlu = Util.compare2(dAB.getX(), 0.0f) ? 0 : 1.0f/Math.abs(dAB.getX());
        float dABlv = Util.compare2(dAB.getY(), 0.0f) ? 0 : 1.0f/Math.abs(dAB.getY());
        float dCBlu = Util.compare2(dCB.getX(), 0.0f) ? 0 : 1.0f/Math.abs(dCB.getX());
        float dCBlv = Util.compare2(dCB.getY(), 0.0f) ? 0 : 1.0f/Math.abs(dCB.getY());
        float ABduvx = dABlu * ABuv.getX();
        float ABduvy = dABlv * ABuv.getY();
        float CBduvx = dCBlu * CBuv.getX();
        float CBduvy = dCBlv * CBuv.getY();
        Vector2D rightDelta = new Vector2D(rightPart.uv0);
        Vector2D leftDelta = new Vector2D(leftPart.uv0);
        for (int y = yStart; y > yStop; y--) {
            int start = (int)Math.floor(xl);
            int stop = (int) Math.floor(xr);
            float deltaX = stop - start;
            float oneOverDeltaX = Util.compare(deltaX, 0.0f) ? 0.0f : 1.0f/ deltaX;
            float oneOverDeltaXInc = oneOverDeltaX;
            for (int x = start; x < stop; x++) {
                leftPart.lerp(oneOverDeltaXInc, leftDelta, rightDelta, result);
                float u = result.getX();
                float v = result.getY();
                if(u >= 1.0f) {
                    u = Util.ONE_MINUS_THRESHOLD;
                }
                if(u < 0.0f) {
                    u = Util.epsilon;
                }
                if(v >= 1.0f) {
                    v = Util.ONE_MINUS_THRESHOLD;
                }
                if(v < 0.0f) {
                    v = Util.epsilon;
                }
                int w = color.getW();
                float a = w * u;
                int tx = (int)Math.floor(a);
                int h = color.getH();
                int ty = (int)Math.floor(h * v);
                int index = ty * w + tx;
                if(index >= color.getP().length) {
                    int yersy =0;
                }
                if(index >= color.getP().length) {
                    index = color.getP().length - 1;
                }
                int i = color.getP()[index];
                renderer.setPixel(x, y, i);
                oneOverDeltaXInc += oneOverDeltaX;
            }
            leftDelta.setX(leftDelta.getX() + ABduvx);
            leftDelta.setY(leftDelta.getY() + ABduvy);
            rightDelta.setX(rightDelta.getX() + CBduvx);
            rightDelta.setY(rightDelta.getY() + CBduvy);

            xr -= dr;
            xl -= dl;
        }
    }

    public void drawFlattBottom(final TextureSideScan leftPart,
                                final TextureSideScan rightPart,
                                final TextureSideScan upperpart, Image color) {

        int yStart = (int) Math.floor(leftPart.p0.getY());
        float yStop = (int) Math.floor(leftPart.p1.getY());
        float xl = rightPart.p0.getX();
        float xr = leftPart.p1.getX();
        float dl = leftPart.dl;
        float dr = rightPart.dl;
        float deltaLeftPart = leftPart.oneOverDy;
        float deltaRightPart = rightPart.oneOverDy;
        float startLeftPart = 0.0f;
        float startRightPart = 0.0f;

        for (int y = yStart; y > yStop; y--) {
            int start = (int) xl;
            int stop = (int) xr;
            for (int x = start; x < stop; x++) {
                float upperPartValue = 1.0f - (startLeftPart + startRightPart);
                leftPart.barycentricCooridnate(rightPart.uv0, rightPart.uv1, leftPart.uv0, startLeftPart, startRightPart, upperPartValue, add);
                if(add.getX() > 1.0f) {
                    add.setX(Util.ONE_MINUS_THRESHOLD);
                }
                if(add.getX() < 0.0f) {
                    add.setX(Util.epsilon);
                }
                if(add.getY() > 1.0f) {
                    add.setY(Util.ONE_MINUS_THRESHOLD);
                }
                if(add.getY() < 0.0f) {
                    add.setY(Util.epsilon);
                }
                int w = color.getW();
                float a = w * add.getX();
                int tx = (int)Math.floor(a);
                int h = color.getH();
                int ty = (int)Math.floor(h * add.getY());
                int index = ty * w + tx;
                if(index >= color.getP().length) {
                    index = color.getP().length - 1;
                }
                int i = color.getP()[index];

                renderer.setPixel(x, y, i);
                startLeftPart += deltaLeftPart;
                startRightPart += deltaRightPart;
            }
            xr -= dl;
            xl -= dr;
        }
    }
}

package no.fun.stuff.engine.game.geo.triangle;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.geo.SideScan;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;

public class TriangleRendererReversed {
    private final Renderer renderer;
    private final Vector2D[] workingVectors = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
    private final Vector2D[] ordered = new Vector2D[]{new Vector2D(), new Vector2D(), new Vector2D()};
    public TriangleRendererReversed(Renderer renderer) {
        this.renderer = renderer;
    }

    public void drawTriangle2(final Vector2D v1, final Vector2D v2, final Vector2D v3, int color) {
        workingVectors[0].setXY(v1);
        workingVectors[1].setXY(v2);
        workingVectors[2].setXY(v3);
        rotateToLowestY(workingVectors);
        Vector2D firstSide = ordered[1].minus(ordered[0]);
        Vector2D secondSide = ordered[2].minus(ordered[1]);
        Vector2D thirdSide = ordered[0].minus(ordered[2]);
        boolean b = firstSide.getY() > 0.0f;
        boolean b1 = secondSide.getY() > 0.0f;
        boolean b2 = thirdSide.getY() > 0.0f;
        if (color == 0x88556677) {
            renderer.drawText("p0", (int) v1.getX(), (int) v1.getY(), 0xffffffff);
            renderer.drawText("p1", (int) v2.getX(), (int) v2.getY(), 0xffffffff);
            renderer.drawText("p2", (int) v3.getX(), (int) v3.getY(), 0xffffffff);

        }
        final Vector2D p0 = ordered[0];
        final Vector2D p1 = ordered[1];
        final Vector2D p2 = ordered[2];
        final boolean toLeft = p1.getX() <= p2.getX();
        boolean flatTOp = toLeft
                ? ((int) Math.floor(p0.getY()) - (int) Math.floor(p1.getY()) == 0)
                : ((int) Math.floor(p0.getY()) - (int) Math.floor(p2.getY()) == 0);
//                ? Util.compare2(p0.getY(), p1.getY())
//                : Util.compare2(p0.getY(), p2.getY());
        boolean flatBottom = Util.compare(p1.getY(), p2.getY(), Util.epsilon);
//        if (flatBottom) {
//            fillFlatBottomV2(p0, p1, p2, color);
//            return;
//        }
//        if (flatTOp) {
//            fillFlatTopV2(p0, p1, p2, color);
//            return;
//        }
        SideScan longSide;
        SideScan upperLeft;
        SideScan lowerLeft;
        if (toLeft) {
            if (p1.getY() > p2.getY()) {
                longSide = new SideScan(p1, p0);
                upperLeft = new SideScan(p2, p0);
                lowerLeft = new SideScan(p1, p2);
//                upperLeft.flatLine = (p0.getY() - p2.getY()) < 1.0f;
                upperLeft.flatLine =((int) Math.floor(p0.getY()) - (int) Math.floor(p2.getY()) == 0);
                longSide.leftside = b;
                upperLeft.leftside = b2;
                lowerLeft.leftside = b1;
                drawRightSegments(upperLeft, lowerLeft, longSide, color);
            } else {
                longSide = new SideScan(p2, p0);
                upperLeft = new SideScan(p1, p0);
                lowerLeft = new SideScan(p2, p1);
                longSide.leftside = b2;
                upperLeft.leftside = b;
                lowerLeft.leftside = b1;
//                upperLeft.flatLine = (p2.getY() - p1.getY()) < 1.0f;
                upperLeft.flatLine = ((int) Math.floor(p2.getY()) - (int) Math.floor(p1.getY()) == 0);
                drawLeftSegments(upperLeft, lowerLeft, longSide, color);
            }
        }
    }


    private void drawLeftSegments(final SideScan upperPart,
                                  final SideScan lowerPart,
                                  final SideScan longPart,
                                  int color) {
        int yStart = Math.round(upperPart.p1.getY());
        int yStop = Math.round(upperPart.p0.getY());
        float dl = upperPart.dl;
        float dr = longPart.dl;
        float xl = upperPart.p1.getX();
        float xr = longPart.p1.getX();
        if(upperPart.flatLine) {
            yStart++;
            xl += dl + (upperPart.leftside ? 1.0f : 0.0f);
            xr += dr;
        }
        int y;
        for (y = yStart; y < yStop; y++) {
            int startX = upperPart.leftside ? (int) Math.ceil(xl) : (int) Math.floor(xl);
            int stopX = (int) Math.floor(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += dl;
            xr += dr;
        }

        yStop = Math.round(lowerPart.p0.getY());
        dl = lowerPart.dl;
        xl = lowerPart.p1.getX();
        for (; y <= yStop; y++) {
            int ceil = (int) Math.ceil(xl);
            int floor = (int) Math.floor(xl);
            int startX = lowerPart.leftside ? ceil : floor;
            int stopX = (int) Math.floor(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += dl;
            xr += dr;
        }
    }

    private void drawRightSegments(final SideScan upperPart,
                                   final SideScan lowerPart,
                                   final SideScan longPart,
                                   int color) {


        int yStart = Math.round(upperPart.p1.getY());
        int yStop = Math.round(upperPart.p0.getY());
        float dl = longPart.dl;
        float dr = upperPart.dl;
        float xl = upperPart.p1.getX();
        float xr = longPart.p1.getX();
        int y;
        for (y = yStart; y < yStop; y++) {

            int startX = longPart.leftside ? (int) Math.ceil(xl) : (int) Math.floor(xl);
            int stopX = (int) Math.floor(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += dl;
            xr += dr;
        }

        yStop = Math.round(lowerPart.p0.getY());
        dr = lowerPart.dl;
        xr = lowerPart.p1.getX();
        if(upperPart.flatLine) {
            y++;
            xr += dr;
            xl += dl + (upperPart.flatLine ? 1.0f : 0.0f);
        }
        for (; y <= yStop; y++) {
            int stopX = (int) Math.floor(xr);
            int startX = longPart.leftside ? (int) Math.ceil(xl) : (int) Math.floor(xl);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += dl;
            xr += dr;
        }
    }


    private void fillFlatTop(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        SideScan left = new SideScan(p0, p1);
        SideScan right = new SideScan(p2, p1);
        int yStart = Math.round(p0.getY());
        int yStop = (int) Math.ceil(p1.getY());
//        int yStart = (int) Math.ceil(p0.getY());
//        int yStop = Math.round(p1.getY());

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
        final SideScan left = new SideScan(p1, p0);
        final SideScan right = new SideScan(p2, p0);

        int yStart = (int) p0.getY();
        int yStop = (int) p1.getY();
        float dl = left.dl;
        float dr = right.dl;
        float xl = p0.getX();
        float xr = p0.getX();
        for (int y = yStart; y <= yStop; y++) {
            int startX = (int) Math.floor(xl);
            int stopX = (int) Math.ceil(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += dl;
            xr += dr;
        }
    }

    private void fillFlatTopV2(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        final SideScan left = new SideScan(p0, p1);
        final SideScan right = new SideScan(p2, p1);
        int yStart = (int) Math.ceil(p0.getY());
        int yStop = Math.round(p1.getY());
        float dl = left.dl;
        float dr = right.dl;
        float xl = p0.getX() + dl;
        float xr = p2.getX() + dr;
        for (int y = yStart + 1; y <= yStop; y++) {
            int startX = Math.round(xl);
            int stopX = Math.round(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += dl;
            xr += dr;
        }
    }

    private void fillTopLeftFlatTopV2(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        final SideScan left = new SideScan(p0, p1);
        final SideScan right = new SideScan(p2, p1);

        final SideScan left2 = new SideScan(p1, p0);
        int yStart = (int) Math.ceil(p0.getY());
        int yStop = Math.round(p1.getY());
//        if(left2.dy < 0.0f) {
//            int etst = 0;
//        }else {
//            if(Math.abs(yStop - yStart) > 0) {
//                System.out.println("OH NO!");
//            }
//        }
        float dl = left.dl;
        float dr = right.dl;
        float xl = p0.getX() + dl;
        float xr = p2.getX() + dr;
        for (int y = yStart + 1; y <= yStop; y++) {
//            int startX = Math.round(xl) + 1;
//            int stopX = Math.round(xr);
            if (Util.compare(0.0f, xl)) {
                xl += Util.epsilon;
            }
            int startX = (int) Math.ceil(xl);
            int stopX = (int) Math.floor(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl += dl;
            xr += dr;
        }
    }

    private void fillTopLeftFlatBottomV2(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        final SideScan left = new SideScan(p1, p0);
        final SideScan right = new SideScan(p2, p0);

        final SideScan lefttest = new SideScan(p0, p1);
//        if(lefttest.dy < 0.0f) {
//            int test = 0;
//        }else {
//            System.out.println("NO!");
//        }
        int yStart = (int) Math.ceil(p1.getY());
        ;
        int yStop = Math.round(p0.getY());
        float xl = p1.getX() + 0.0001f;
        float xr = p2.getX();
        float dl = left.dl;
        float dr = right.dl;
        for (int y = yStart; y >= yStop; y--) {
//            int startX = Math.round(xl) + 1;
            int startX = (int) Math.ceil(xl);
            int stopX = (int) Math.floor(xr);
//            int stopX = Math.round(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl -= dl;
            xr -= dr;
        }
    }

    private void fillFlatBottomV2(final Vector2D p0, final Vector2D p1, final Vector2D p2, int color) {
        final SideScan left = new SideScan(p1, p0);
        final SideScan right = new SideScan(p2, p0);

        int yStart = (int) Math.ceil(p1.getY());
        ;
        int yStop = Math.round(p0.getY());
        float xl = p1.getX();
        float xr = p2.getX();
        float dl = left.dl;
        float dr = right.dl;
        for (int y = yStart; y >= yStop; y--) {
            int startX = Math.round(xl);
            int stopX = Math.round(xr);
            for (int x = startX; x <= stopX; x++) {
                renderer.setPixel(x, y, color);
            }
            xl -= dl;
            xr -= dr;
        }
    }

    public int higestY(final Vector2D[] workingVectors) {
        int highest = 0;
        float y = workingVectors[0].getY();
        for (int i = 1; i < workingVectors.length; i++) {
            if (workingVectors[i].getY() > y) {
                highest = i;
                y = workingVectors[i].getY();
            }
        }
        return highest;
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

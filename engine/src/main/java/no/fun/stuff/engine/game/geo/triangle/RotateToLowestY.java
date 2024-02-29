package no.fun.stuff.engine.game.geo.triangle;

import no.fun.stuff.engine.matrix.Vector2D;

public class RotateToLowestY {
    private final Vector2D[] working = new Vector2D[3];
    private final int[] orderedIndex = new int[3];
    public int[] rotateToLowestY(final Vector2D v0, final Vector2D v1, final Vector2D v2) {
        working[0] = v0;
        working[1] = v1;
        working[2] = v2;
        int lowestY = 0;
        int i = 0;
        for (; i < working.length; i++) {
            if (working[i].getY() < working[lowestY].getY()) {
                lowestY = i;
            }
        }

        if (lowestY > 0) {
            orderedIndex[0] = lowestY++;
            orderedIndex[1] = lowestY++ % 3;
            orderedIndex[2] = lowestY % 3;
        } else {
            orderedIndex[0] = 0;
            orderedIndex[1] = 1;
            orderedIndex[2] = 2;
        }

        return orderedIndex;
    }

}

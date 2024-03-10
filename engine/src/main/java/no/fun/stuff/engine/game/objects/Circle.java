package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.matrix.Vector2D;

public class Circle {

    public void drawCircle(Vector2D center, float radius) {

        float diamer = radius*2;
        final Vector2D up = new Vector2D(0.0f, 1.0f);
        final Vector2D workingUp = new Vector2D(up).scale(radius);
        float ySlope = 1.0f/radius;
        Vector2D workVector = new Vector2D(center);
//        workVector.pluss(workingUp);
        workVector.setXY(0.0f, -radius);
        for(int y = 0;y<(int)diamer;y++) {
            float y1 = workVector.getY() * workVector.getY();
            float x = (float)Math.sqrt(radius*radius - y1);
            int start = (int)-x;
            int stop = (int)x;
            System.out.println("Start: " + start + "\t stop: " + stop);
//            for(int i = start;i<stop;i++) {
//            }
            workVector.pluss(-ySlope, ySlope);
        }

    }
}

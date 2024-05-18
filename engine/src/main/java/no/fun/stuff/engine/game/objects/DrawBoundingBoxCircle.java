package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Vector2D;

public class DrawBoundingBoxCircle extends Circle {

    public DrawBoundingBoxCircle(Vector2D position, float radius, int color) {
        super(position, radius, color);
    }

    public DrawBoundingBoxCircle(float radius, int color) {
        super(radius, color);
    }
    @Override
    public void render(SceneObject parent, Renderer r) {
        super.render(parent, r);
        DrawBoundingBoxTriangle.drawBox(this, parent, r, getColor());
    }

}

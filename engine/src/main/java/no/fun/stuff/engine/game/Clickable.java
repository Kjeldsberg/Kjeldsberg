package no.fun.stuff.engine.game;

import no.fun.stuff.engine.game.objects.Circle;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public interface Clickable {
    boolean clickedOn(final Vector2D position, final Matrix3x3 cameraMatrix);
}

package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.matrix.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Integrator {
    private final List<Vector2D> forces;
    private final Vector2D oldPos;
    private final Vector2D pos;
    public Integrator() {
        forces = new ArrayList<>();
        oldPos = new Vector2D();
        pos = new Vector2D();
    }
    public void integrate(final Vector2D[] forces,
                          final Vector2D[] oldPos,
                          final Vector2D[] pos) {


    }
    public void addForce(final Vector2D force) {
        forces.add(force);
    }

}

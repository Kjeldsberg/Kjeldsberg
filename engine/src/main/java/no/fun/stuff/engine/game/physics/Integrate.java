package no.fun.stuff.engine.game.physics;

import no.fun.stuff.engine.game.objects.Body;

public interface Integrate {
    void integrate(final Body body, float dt);
}

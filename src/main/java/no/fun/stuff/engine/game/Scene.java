package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Renderer;

public interface Scene {
	
	public void update(float dt);
	public void render(final Renderer r);

}

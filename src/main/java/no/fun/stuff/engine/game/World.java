package no.fun.stuff.engine.game;

import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;

public class World extends SceneObject {

	public World(final GameContainer gc) {
		final SpaceShip spaceShip = new SpaceShip(gc, 48, 48);
		final Camera2D camera2 = new Camera2D();
		camera2.lookAt(spaceShip);
		this.child.add(spaceShip);
		this.child.add(camera2);
		this.child.add(new ImageLevel("/level3.png"));
	}
	@Override
	public void update(SceneObject parent, float dt) {
		for(SceneObject obj: this.child) {
			obj.update(this, dt);
		}

	}

	@Override
	public void render(SceneObject parent, Renderer r) {
		for(SceneObject obj: this.child) {
			obj.render(this, r);
		}
	}

}

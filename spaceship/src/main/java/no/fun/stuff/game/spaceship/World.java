package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.Camera2D;
import no.fun.stuff.engine.game.ImageLevel;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Vector2D;

public class World extends SceneObject {
	final Camera2D camera2;

	public World(final GameContainer gc) {
		camera2 = new Camera2D(new Vector2D(gc.getWith(), gc.getHeight()), new Vector2D(8, 6));
		final SpaceShip spaceShip = new SpaceShip(gc, 48, 48);
		camera2.lookAt(spaceShip);
		this.child.add(spaceShip);
		this.child.add(new ImageLevel("/level3.png"));
		this.child.add(camera2);
//		this.child.add(new Grid(40,40, 0xffffffff, gc.getWith(), gc.getHeight()));
	}
	@Override
	public void update(SceneObject parent, float dt) {
		for (int i = 0; i < child.size(); i++) {
			SceneObject sceneObject = child.get(i);
			sceneObject.update(this, dt);
			if (sceneObject.isDead()) {
				child.remove(i);
				i--;
			}
		}
	}

	@Override
	public void render(SceneObject parent, Renderer r) {
		camera2.render(null, r);
		for(SceneObject obj: this.child) {
			obj.render(camera2, r);
		}
	}
}

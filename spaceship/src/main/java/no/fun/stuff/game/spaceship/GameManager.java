package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.GameObject;
import no.fun.stuff.engine.game.ImageLevel;

import java.util.ArrayList;
import java.util.List;


public class GameManager extends AbstractGame {
	public static final int TS = 16; 
	private List<GameObject> objects = new ArrayList<>();
//	private Camera2D camera;
//	private GameScene scene = new GameScene();
	private boolean[] collition;
	private int levelW, levelH;
	private World world = null;
	public GameManager() {
//		objects.add(new Player(3, 3));
//		scene.setCamera2D(camera);
	}

	@Override
	public void init(final GameContainer gc) {
		gc.getRenderer().setAmbientcolor(-1);
		world = new World(gc);
	}
	@Override
	public void update(GameContainer gc, float dt) {
		world.update(null, dt);
	}

	float temp = 0;

	@Override
	public void render(GameContainer gc, Renderer r) {
		world.render(null, r);
	}

	public void addObject(final GameObject gameObject) {
		objects.add(gameObject);
	}
	
	public GameObject getObject(String tag) {
		for(int i=0;i<objects.size();i++) {
			final GameObject gameObject = objects.get(i);
			if(gameObject.getTag().equals(tag)) {
				return gameObject;
			}
		}
		return null;
	}
	
	public boolean getCollision(int x, int y) {
		if(x < 0 || x >=levelW || y<0 || y >= levelH) {
			return true;
		}
		return collition[x + y * levelW];
	}
	public static void main(final String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWith(320);
		gc.setHeight(240);
		gc.setScale(3.0f);	
		gc.start();
		
	}

}

package no.fun.stuff.engine.game;

import java.util.ArrayList;
import java.util.List;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Vector2D;

public class GameManager extends AbstractGame {
	public static final int TS = 16; 
	private List<GameObject> objects = new ArrayList<>();
//	private Camera2D camera;
	private GameScene scene = new GameScene();
	private boolean[] collition;
	private int levelW, levelH;
	public GameManager() {
//		objects.add(new Player(3, 3));
//		scene.setCamera2D(camera);
	}

	@Override
	public void init(GameContainer gc) {
		gc.getRenderer().setAmbientcolor(-1);
		final World world = new World(gc);
		scene.setRenderer(gc.getRenderer());
		scene.getObjects().add(world);
		scene.getObjects().add(new SpaceShip(gc, 48, 48));
		scene.getObjects().add(new ImageLevel("/level3.png"));
		final SceneObject lookat = scene.findGameObjectByName("Spaceship");
		scene.getCamera().lookAt(lookat);
	}
	@Override
	public void update(GameContainer gc, float dt) {
		scene.update(dt);
	}

	float temp = 0;

	@Override
	public void render(GameContainer gc, Renderer r) {
		scene.render(r);
//		for(GameObject object : objects) {
//			object.render(gc, r);
//		}
	}
	public Camera2D getCamera() {
		return scene.getCamera();
	}
	public void loadLevel(String path) {
		Image level = new Image(path);
		levelH = level.getH();
		levelW = level.getW();
 		collition = new boolean[levelH * levelW];
 		for(int y=0;y<level.getH();y++) {
			for(int x =0;x<level.getW();x++) {
				if(level.getP()[x + y *level.getW()] == 0xff000000) {
					collition[x + y * level.getW()] = true;
				}else {
 	 				collition[x + y*level.getW()] = false;
	 			}
			}
		}
 		final float w = GameManager.TS*0.5f;
 		final float h = GameManager.TS*0.5f;

 		for(int y=0;y<level.getH();y++) {
			for(int x =0;x<level.getW();x++) {
				if(collition[x + y * level.getW()]) {
					final Rectangle rec = new Rectangle(new Vector2D((x*2+1)*w, (y*2+1)*h));
					rec.setColor(0xff444444);
					objects.add(rec);
				}
			}
		}
 		
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

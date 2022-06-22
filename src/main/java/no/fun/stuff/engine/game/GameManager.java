package no.fun.stuff.engine.game;

import java.util.ArrayList;
import java.util.List;

import no.fun.stuff.engine.AbstractGame;
import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.gfx.Image;

public class GameManager extends AbstractGame {
	public static final int TS = 16; 
	private List<GameObject> objects = new ArrayList<>();
	private Camera camera;
	
	private boolean[] collition;
	private int levelW, levelH;
	public GameManager() {
		objects.add(new Player(3, 3));
		objects.add(new SpaceShip(100, 100));
		loadLevel("/level.png");
		camera = new Camera("player");
	}

	@Override
	public void init(GameContainer gc) {
		gc.getRenderer().setAmbientcolor(-1);
	}
	@Override
	public void update(GameContainer gc, float dt) {
		for(int i=0;i<objects.size();i++) {
			objects.get(i).update(gc,this, dt);
			if(objects.get(i).dead) {
				objects.remove(i);
				i--;
			}
		}
		camera.update(gc,this, dt);
	}

	float temp = 0;

	@Override
	public void render(GameContainer gc, Renderer r) {
		camera.render(r);
		for(int y=0;y<levelH;y++) {
			for(int x =0;x<levelW;x++) {
				if(collition[x +y * levelW]) {
					r.drawFillRec(x*TS, y *TS, TS, TS, 0xff0f0f0f);
					
				}else {
					r.drawFillRec(x*TS, y *TS, TS, TS, 0xfff9f9f9);
					
				}
			}
		}
		for(GameObject object : objects) {
			object.render(gc, r);
		}
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

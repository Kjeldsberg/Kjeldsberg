package no.fun.stuff.spaceship.game;

import java.awt.event.KeyEvent;

import no.fun.stuff.spaceship.AbstractGame;
import no.fun.stuff.spaceship.GameContainer;
import no.fun.stuff.spaceship.Renderer;
import no.fun.stuff.spaceship.audio.SoundClip;
import no.fun.stuff.spaceship.gfx.Image;
import no.fun.stuff.spaceship.gfx.ImageTile;
import no.fun.stuff.spaceship.gfx.Light;

public class GameManager extends AbstractGame {
	
	private ImageTile image;
	private Image mouse;
	private Image backScreen;
	private Image leopard;
	private Image driedMudA64;;
	private Light light;
	private SoundClip clip;
	private Image pine;
	public GameManager() {
		pine = new Image("/pine.png");
		pine.setAlfa(true);
		image = new ImageTile("/test2.png", 16, 16);
		backScreen = new Image("/backscreen.png");
//		bachScreen.setLightBlock(Light.FULL);
		backScreen.setAlfa(false);
		leopard = new Image("/leopardA21.png");
		leopard.setAlfa(true);
		driedMudA64 = new Image("/driedMudA65.png");
		driedMudA64.setAlfa(true);
	
		mouse = new Image("/test.png");
//		mouse.setLightBlock(Light.FULL);
		mouse.setAlfa(false);
		clip = new SoundClip("/laser.wav");
		clip.volume(-20);
		light = new Light(100, 0xff00ffff);
	}

	@Override
	public void update(GameContainer gc, float dt) {
		if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
			clip.play();
			System.out.println("A is down.");
		}
		temp += dt * 10;
		if (temp > 3) {
			temp = 0;
		}
	}

	float temp = 0;

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.setzDept(0);
//		r.drawImage(backScreen, 0, 0);
//		r.setzDept(1);
//		r.drawImage(mouse, 80, 80);
//		r.setzDept(2);
//		r.drawImage(leopard, 120, 120);
//		r.setzDept(3);
		r.drawImage(mouse,80, 80);
		r.setzDept(1);
		r.drawImage(leopard, gc.getInput().getMouseX() - 32 , gc.getInput().getMouseY() - 32);
		r.setzDept(2);
		r.drawImage(driedMudA64 ,50, 50);
		
//		r.drawLight(light, gc.getInput().getMouseX(), gc.getInput().getMouseY());
//		r.drawImage(mouse, gc.getInput().getMouseX() - 32 , gc.getInput().getMouseY() - 32);
//		r.drawFillRec(100, 100, 40, 40, 0xffff10ff);
//		r.drawImage(lightMap, 150, 150);
//		r.drawImageTile(image, gc.getInput().getMouseX() - 8 , gc.getInput().getMouseY() - 16, (int)temp, 0);
	}

	public static void main(final String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWith(320);
		gc.setHeight(240);
		gc.setScale(3.0f);
		gc.start();
	}
}

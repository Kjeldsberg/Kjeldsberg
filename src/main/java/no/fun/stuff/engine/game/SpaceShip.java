package no.fun.stuff.engine.game;



import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.texturemap.Texturemap4Points;
import no.fun.stuff.engine.game.texturemap.Face2d;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;

public class SpaceShip extends GameObject {
	private final Image spaceship;
	private Point p[] = {new Point(-10, -10), new Point(10, -10), new Point(10, 10), new Point(-10, 10)};
	private final Point[] originaluv = { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, 1)}; 
	private Point world[];
	private Point newOrder[];
	private Face2d face = new Face2d(p, originaluv);
	private Point center = new Point(320/2, 240/2);
	private float angle = 0f;
	private Matrix3x3 m = new Matrix3x3();
	private Matrix3x3 translate = new Matrix3x3();
	private Matrix3x3 scale = new Matrix3x3();
	private Matrix3x3 rotate = new Matrix3x3();
	float totAngle = 0;
	public SpaceShip(int posX, int posY) {
		spaceship = new Image("/pitrizzo-SpaceShip-gpl3-opengameart-24x24.png");
		this.tag = "spaceship";
		this.posX = posX;
		this.posY = posY;
		this.height = spaceship.getH();
		this.width = spaceship.getW();
		this.dead = false;
		this.world = new Point[4];
		this.newOrder = new Point[4];

		m.scale(8.5f);
		m.translate(center);
		rotate.rotate(0.523598f);
		m.mul(rotate);
		world = m.mul(p);
		face.pos = world;

	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		float angleAdd = 0.001f;
		if(gc.getInput().isKeyDown(KeyEvent.VK_Z)) {
			angle += angleAdd;
		}
		if(gc.getInput().isKeyDown(KeyEvent.VK_X)) {
			angle -= angleAdd;
		}
		totAngle += angle;
		m.scale(8.5f);
		m.translate(center);
		rotate.rotate(angle);
//		System.out.println("Angle: " + totAngle);
		m.mul(rotate);
		world = m.mul(p);
		face.pos = world;
		m.clear();
	}
	
	private void swap(int i, int y) {
		Point temp= world[i];
		world[i] = world[y];
		world[y] = temp;
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.setCamX(0);
		r.setCamY(0);
		Texturemap4Points texture =  new Texturemap4Points(r);
		texture.texturemap(face, spaceship);
	}
	
	public void drawBresenhamLineh(int x0, int y0, int x1, int y1, Image image, Renderer r, float slopey) {
		int dx = Math.abs(x1-x0);
		int dy = Math.abs(y1 -y0);
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		int err = dx -dy;
		int e2;
		float imageslope = (float)image.getW()/(x1 - x0);
		imageslope *= Math.signum(imageslope);
		float y = 0;
		float x = 0;
		while(true) {
			int screenX = x0;
			int screenY = y0;
//			setLightMap(screenX,  screenY,  lightColor);
			int test3 = (int)x;
			int test4 = (int)y;
			int sisss = image.getP().length;
			int pixel = (int)x + (int)y*image.getW();
			if(pixel < sisss) {
				pixel = sisss -1;
				r.setPixel(screenX, screenY, image.getP()[pixel]);
			}
			x += imageslope;
			y += slopey;
			if(x0 == x1 && y0 == y1) {
				break;
			}
			e2 = 2 *err;
			if(e2 > -1 * dy) {
				err -= dy;
				x0 += sx;
			}
			if(e2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
	}
	public  List<Point> drawBresenhamLine(int x0, int y0, int x1, int y1, int color) {
		final List<Point> ret = new ArrayList<>();
		int dx = Math.abs(x1-x0);
		int dy = Math.abs(y1 -y0);
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		int err = dx -dy;
		int e2;
		while(true) {
			int screenX = x0;
			int screenY = y0;
//			setLightMap(screenX,  screenY,  lightColor);
			ret.add(new Point(screenX, screenY));
//			r.setPixel(screenX, screenY, color);
			if(x0 == x1 && y0 == y1) {
				break;
			}
			e2 = 2 *err;
			if(e2 > -1 * dy) {
				err -= dy;
				x0 += sx;
			}
			if(e2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
		return ret;
	}

}

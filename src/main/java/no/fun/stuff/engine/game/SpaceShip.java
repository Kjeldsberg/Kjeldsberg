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
import no.fun.stuff.engine.matrix.Vector2D;

public class SpaceShip extends GameObject {
	private final Image spaceship;
	private Point p[] = {new Point(-10, -10), new Point(10, -10), new Point(10, 10), new Point(-10, 10)};
	private final Point[] originaluv = { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, 1)}; 
	private Point world[];
	private Face2d face = new Face2d(p, originaluv);
	private Point center = new Point(320/2, 240/2);
	private float angle = 0f;
	private Matrix3x3 m = new Matrix3x3();
	private Matrix3x3 rotate = new Matrix3x3();
	private Vector2D gravity = new Vector2D(0, 9.81f);
	private Vector2D direction = new Vector2D(0, -1.0f);
	private Vector2D rotatedDirection = new Vector2D(direction);
	private Vector2D velocity = new Vector2D(); 
	private float mass = 30.0f;
	private final Point pos;
	private boolean powerOn = false;
	float totAngle = 0;
	final Point dp0 = new Point(0.0f, -0.5f);
	final Point dp1 = new Point(0.0f, 0.5f);
	public SpaceShip(int posX, int posY) {
		this.tag = "Spaceship";
		spaceship = new Image("/pitrizzo-SpaceShip-gpl3-opengameart-24x24.png");
//		spaceship = new Image("/png-transparent-spider-man-heroes-download-with-transparent-background-free-thumbnail.png");
			
		this.tag = "spaceship";
		this.posX = posX;
		this.posY = posY;
		pos = new Point(posX, posY);
		this.height = spaceship.getH();
		this.width = spaceship.getW();
		this.dead = false;
		this.world = new Point[4];

		m.translate(pos);
		m.mul(rotate);
		world = m.mul(p);
		face.pos = world;

	}
	private void drawDirection(final Vector2D v,final Renderer r) {
		Matrix3x3 m = new Matrix3x3();
//		m.translate(new Point(0.0f, -0.5f));
		m.scale(10);
//		m.translate(center);
//		m.mul(rotate);
		Vector2D h00 = new Vector2D(v).scale(10);
//		Vector2D h01 = new Vector2D(v1);
//		Vector2D h0 = h00.minus(h01);
//		Vector2D h1 = h0.scale(15);
//		m.translate(center);
//		Point o0 = m.mul(h0.toPoint());
//		Point o1 = m.mul(h1.toPoint());
//		Point o0 = m.mul(h0.toPoint());
//		Point o1 = m.mul(h1.toPoint());
		drawLine(v, h00, r);
//		drawBresenhamLine((int)h0.getX(), (int)h0.getY(), (int)h1.getX(), (int)h1.getY(), 0xffffffff, r);

	}
	void drawLine(final Point h0, final Point h1, final Renderer r) {
		drawBresenhamLine((int)h0.getX(), (int)h0.getY(), (int)h1.getX(), (int)h1.getY(), 0xffffffff, r);
	}
	void drawLine(final Vector2D h0, final Vector2D h1, final Renderer r) {
		drawBresenhamLine((int)h0.getX(), (int)h0.getY(), (int)h1.getX(), (int)h1.getY(), 0xffffffff, r);
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
//		float angleAdd = 0.001f;
		float angleAdd = dt/10;
		powerOn = gc.getInput().isKeyDown(KeyEvent.VK_SPACE);
		if(gc.getInput().isKey(KeyEvent.VK_Z)) {
			angle -= angleAdd;
		}	
		if(gc.getInput().isKey(KeyEvent.VK_X)) {
			angle += angleAdd;
		}
		totAngle += angle;
		rotate.rotate(angle);
		rotatedDirection = rotate.mul(direction);
		
		final Vector2D sumOfForces = new Vector2D(gravity);
		if(powerOn) {

//			Vector2D rocketForce = new Vector2D(dp0).minus(new Vector2D(dp1));
//			Vector2D rocketForce = new Vector2D(rotatedDirection);
			sumOfForces.pluss(rotatedDirection.scale(30));
//			sumOfForces.pluss(direction.scale(20));
		}
//		sumOfForces.add(new Vector2D(0, -11.0f));
		final Vector2D v0 = new Vector2D(velocity);
		velocity = v0.add(sumOfForces.scale(dt));
		final Vector2D s0 = new Vector2D(pos);
		final Vector2D st = s0.add(v0.add(velocity).scale(dt*0.5f));
		
		final Vector2D nextpos = s0.add(velocity.scale(dt).add(sumOfForces.scale(mass * dt*dt*0.5f)));
		pos.setX(nextpos.getX());
		pos.setY(nextpos.getY());
		m.translate(pos);
		m.mul(rotate);
		world = m.mul(p);
		face.pos = world;
		m.clear();
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {
//		drawDirection(dp0, dp1, gc.getRenderer());
		drawDirection(rotatedDirection, r);
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
	public  List<Point> drawBresenhamLine(int x0, int y0, int x1, int y1, int color, final Renderer r) {
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
			r.setPixel(screenX, screenY, color);
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

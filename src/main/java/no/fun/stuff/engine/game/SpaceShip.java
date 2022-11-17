package no.fun.stuff.engine.game;



import java.awt.event.KeyEvent;

import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.texturemap.Face2d;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;
import no.fun.stuff.engine.matrix.Vector2D;

public class SpaceShip extends GameObject {
	private final Image spaceship;

	private final int w = GameManager.TS/2;
	private final int h = GameManager.TS/2;
	private Point p[] = {new Point(-w, -h), new Point(w, -h), new Point(w, h), new Point(-w, h)};
	private final Point[] originaluv = { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, 1)}; 
	private Point world[];
	private Face2d face = new Face2d(p, originaluv);
	private float angle = 0f;
	private Matrix3x3 m = new Matrix3x3();
	private Matrix3x3 rotate = new Matrix3x3();
	private Vector2D gravity = new Vector2D(0, 9.81f);
	private Vector2D direction = new Vector2D(0, -1.0f);
	private Vector2D rotatedDirection = new Vector2D(direction);
	private Vector2D velocity = new Vector2D(); 
	private final Point pos;
	private boolean powerOn = false;
	private int tileX, tileY;
	private float offX, offY;
	float totAngle = 0;
	final Vector2D motionVector = new Vector2D();
	private boolean xcollition = false, ycollition = false;
	private boolean mousePositioning = true;
	public SpaceShip(int posX, int posY) {
		this.tag = "Spaceship";
		spaceship = new Image("/pitrizzo-SpaceShip-gpl3-opengameart-24x24.png");
//		spaceship = new Image("/png-transparent-spider-man-heroes-download-with-transparent-background-free-thumbnail.png");
			
		this.posX = posX * GameManager.TS;
		this.posY = posY * GameManager.TS;
		this.tileX = posX;
		this.tileY = posY;
		this.offX = 0.0f;
		this.offY = 0.0f;
		
		this.height = GameManager.TS;
		this.width = GameManager.TS;
		this.dead = false;
		
		pos = new Point(this.posX, this.posY);
		motionVector.setXY(pos);
		this.world = new Point[4];

		m.translate(motionVector);
		m.mul(rotate);
		world = m.mul(p);
		face.pos = world;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		float angleAdd = dt/10;
		final boolean theY = gm.getCollision(tileX , tileY + (int)Math.signum((int)offY));
		final boolean theX = gm.getCollision(tileX, tileY);
		System.out.println("theX: " + theX + "\ttheY: " + theY + "\ttilex:" + tileX + "\ttiley: " + tileY);
		if(theX || theY) {
			xcollition = true;
		} else {
			xcollition = false;
		}
		if(gm.getCollision(tileX-1 , tileY) || gm.getCollision(tileX-1 , tileY + (int)Math.signum((int)offY)) ) {
			ycollition = true;
		}else {
			ycollition = false;
		}

		powerOn = gc.getInput().isKeyDown(KeyEvent.VK_UP);
		if(gc.getInput().isKey(KeyEvent.VK_LEFT)) {
			angle -= angleAdd;
		}	
		if(gc.getInput().isKey(KeyEvent.VK_RIGHT)) {
			angle += angleAdd;
		}
		
		totAngle += angle;
		rotate.rotate(angle);
		rotatedDirection = rotate.mul(direction);
		
		final Vector2D sumOfForces = new Vector2D(gravity);
		if(powerOn) {
			sumOfForces.pluss(rotatedDirection.scale(40));
		}
		final Vector2D v0 = new Vector2D(velocity);
		velocity = v0.add(sumOfForces.scale(dt));
		final Vector2D s0 = new Vector2D(offX, offY);
		final Vector2D s1 = s0.add(v0.add(velocity).scale(dt*0.5f));
//		final Vector2D s1 = s0.add(velocity.scale(dt)).add(velocity).scale(dt*dt*0.5f));
		
		offX = s1.getX();
		offY = s1.getY();
		if(mousePositioning) {
			int x = gc.getInput().getMouseX();
			int y = gc.getInput().getMouseY();
			System.out.println("mouseX: " + x + "\tmouseY: " + y);
;			boolean notInitialized = x == 0 && y==0;
			if(!notInitialized) {
				int ts = GameManager.TS;
				tileX = x / GameManager.TS;
				tileY = y / GameManager.TS;
				offX = (float)(x % GameManager.TS);
				offY = (float)(y % GameManager.TS);
			}
		} else {
			
		}
		final int HALF_INT = GameManager.TS /2;
		if(offY > HALF_INT ) {
			tileY++;
			offY -= GameManager.TS;
		}
		if(offY < -HALF_INT ) {
			tileY--;
			offY += GameManager.TS;	
		}
		if(offX > HALF_INT ) {
			tileX++;
			offX -= GameManager.TS;
		}
		if(offX < -HALF_INT ) {
			tileX--;
			offX += GameManager.TS;
		}
		this.posX = tileX * GameManager.TS + offX;
		this.posY = tileY * GameManager.TS + offY;
		Vector2D tmp = new Vector2D(posX, posY).minus(motionVector);
		motionVector.pluss(tmp);

		m.translate(motionVector);
		m.mul(rotate);
		world = m.mul(p);
		face.pos = world;
		m.clear();
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		drawLine(this.face.pos, r);
		final Face2d d = new Face2d(this.face);
		r.drawTexture(d, spaceship);
//		r.drawRect((int)(posX-2.5f) , (int)(posY-2.5), 5, 5, 0xff000000);
		this.drawDirection(rotatedDirection, r);
		if(xcollition) {
			drawRec(new Vector2D(),0xffff0000, r);
		}
		if(ycollition) {
			drawRec(new Vector2D(16, 0), 0xffffff00, r);
		}
	}
	private void drawDirection(final Vector2D v,final Renderer r) {
		Vector2D h00 = new Vector2D(v).scale(10);
		drawLine(v, h00, r);

	}
	void drawLine(final Point h0, final Point h1, final Renderer r) {

		r.drawBresenhamLine((int)h0.getX(), (int)h0.getY(), (int)h1.getX(), (int)h1.getY(), 0xffff0000);
	}
	void drawLine(final Point[] h0, final Renderer r) {
		for(int i=0, y = 1;i<h0.length - 1;i++, y++) {
			r.drawBresenhamLine((int)h0[i].getX(), (int)h0[i].getY(), (int)h0[y].getX(), (int)h0[y].getY(), 0xffff0000);
		}
		r.drawBresenhamLine((int)h0[0].getX(), (int)h0[0].getY(), (int)h0[h0.length -1].getX(), (int)h0[h0.length - 1].getY(), 0xffff0000);
	}

	void drawLine(final Vector2D h0, final Vector2D h1, final Renderer r) {
		int tempcamX = r.getCamX();
		int tempcamY = r.getCamY();
		r.setCamX(0);
		r.setCamY(0);
		r.drawBresenhamLine((int)h0.getX(), (int)h0.getY(), (int)h1.getX(), (int)h1.getY(), 0xffff0000);
		r.setCamX(tempcamX);
		r.setCamY(tempcamY);
	}
	void drawRec(final Vector2D h0, int color, final Renderer r) {
		int tempcamX = r.getCamX();
		int tempcamY = r.getCamY();
		r.setCamX(0);
		r.setCamY(0);
		r.drawFillRec((int)h0.getX(), (int)h0.getY(), 16, 16, color);
		r.setCamX(tempcamX);
		r.setCamY(tempcamY);
	}
	void drawRec2(final Vector2D h0, int color, final Renderer r) {
//		int tempcamX = r.getCamX();
//		int tempcamY = r.getCamY();
//		r.setCamX(0);
//		r.setCamY(0);
		r.drawFillRec((int)h0.getX(), (int)h0.getY(), 16, 16, color);
//		r.setCamX(tempcamX);
//		r.setCamY(tempcamY);
	}

}

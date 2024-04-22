package no.fun.stuff.game.spaceship;




import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.game.texturemap.Texture2d;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;
import no.fun.stuff.engine.matrix.Vector2D;

import java.awt.event.KeyEvent;

public class SpaceShip extends SceneObject {
	private final Image spaceship;
	private final int w = GameManager.TS/2;
	private final int h = GameManager.TS/2;
	private Point p[] = {new Point(-w, -h), new Point(w, -h), new Point(w, h), new Point(-w, h)};
	private final Point[] originaluv = { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, 1)};
	private Point world[];
	private Texture2d face = new Texture2d(p, originaluv);
	private float angle = 0f;
	private Matrix3x3 rotate = new Matrix3x3();
	private Vector2D gravity = new Vector2D(0, 9.81f);
	private Vector2D direction = new Vector2D(0, -1.0f);
	private Vector2D rotatedDirection = new Vector2D(direction);
	private Vector2D velocity = new Vector2D();
	private boolean powerOn = false;
	float totAngle = 0;
	float oldTotAngle = 0;
	final Vector2D motionVector = new Vector2D();
	private boolean xcollition = false, ycollition = false;
	private boolean mousePositioning = true;
	private GameManager gm;
	private final GameContainer gc;

	public SpaceShip(final GameContainer gc, int posX, int posY) {
		this.gc = gc;
		this.target = "Spaceship";
		spaceship = new Image("/pitrizzo-SpaceShip-gpl3-opengameart-24x24.png");
//		spaceship = new Image("/png-transparent-spider-man-heroes-download-with-transparent-background-free-thumbnail.png");
//		this.height = GameManager.TS;
//		this.width = GameManager.TS;
		this.dead = false;
		pos.setX(posX);
		pos.setY(posY);
		oldPos.setXY(pos);
		motionVector.setXY(pos);
		this.world = new Point[4];

		model.translate(motionVector);
		model.mulCopy(rotate);
		world = model.mul(p);
		face.pos = world;
	}
	public float getAngle(float totAngle, float testValue) {
		float signum = Math.signum(totAngle);
		if(signum < 0.0f) {
			if(totAngle < -testValue) {
				totAngle = -testValue;
			}
		}
		Float.floatToIntBits(testValue);
		if(totAngle > testValue) {
			totAngle = testValue;
		}
		return totAngle;
	}
	@Override
	public void update(SceneObject parent, float dt) {
//		int x = gc.getInput().getMouseX();
//		int y = gc.getInput().getMouseY();
//
//		Vector2D tmp = new Vector2D(x, y);//.minus(motionVector);
//		translate.translate(tmp);
//		this.pos.setX(x);
//		this.pos.setY(y);
//		System.out.println("x: " +model.getMotionVector().getX() + " y: " + model.getMotionVector().getY());
		float angleAdd = dt/10;
		powerOn = gc.getInput().isKeyDown(KeyEvent.VK_UP);
		if(gc.getInput().isKey(KeyEvent.VK_LEFT)) {
			angle -= angleAdd;
		}
		if(gc.getInput().isKey(KeyEvent.VK_RIGHT)) {
			angle += angleAdd;
		}

		rotate.rotate(getAngle(angle, (float)Math.PI/4));
		rotatedDirection = rotate.mul(direction);

		final Vector2D sumOfForces = new Vector2D(gravity);
		if(powerOn) {
			sumOfForces.pluss(rotatedDirection.scale(40));
		}
		Vector2D velocity = pos.minus(oldPos);
		Vector2D nextPos = pos.add(velocity);
		nextPos.pluss(sumOfForces.scale(dt*dt));
		oldPos.setXY(pos);
		pos.setXY(nextPos);
		this.translate.translate(pos);
	}

	@Override
	public void render(SceneObject parent, Renderer r) {

//		System.out.println("\t x: " +this.translate.getMotionVector().getX() + " y: " + this.translate.getMotionVector().getY());
		this.model.set(this.translate.fastMulCopy(this.rotate).fastMulCopy(this.scale).getCopy());
		Matrix3x3 viewModel = parent.getModel().fastMulCopy(model);
		world = viewModel.mul(p);
		face.pos = world;
		drawLine(this.face.pos, r);
		r.drawTexture(face, spaceship);
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

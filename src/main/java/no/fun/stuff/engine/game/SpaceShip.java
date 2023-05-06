package no.fun.stuff.engine.game;




import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.texturemap.Texture2d;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;
import no.fun.stuff.engine.matrix.Vector2D;

public class SpaceShip extends SceneObject {
	private final Image spaceship;

	private final int w = GameManager.TS/2;
	private final int h = GameManager.TS/2;
	private Point p[] = {new Point(-w, -h), new Point(w, -h), new Point(w, h), new Point(-w, h)};
	private final Point[] originaluv = { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, 1)}; 
	private Point world[];
	private Texture2d face = new Texture2d(p, originaluv);
	private float angle = 0f;
//	private Matrix3x3 localSpace = new Matrix3x3();
	private Matrix3x3 rotate = new Matrix3x3();
	private Vector2D gravity = new Vector2D(0, 9.81f);
	private Vector2D direction = new Vector2D(0, -1.0f);
	private Vector2D rotatedDirection = new Vector2D(direction);
	private Vector2D velocity = new Vector2D(); 
//	private final Point pos;
	private boolean powerOn = false;
	private int tileX, tileY;
	private float offX, offY;	
	float totAngle = 0;
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
		
		motionVector.setXY(pos);
		this.world = new Point[4];

		localSpace.translate(motionVector);
		localSpace.mulCopy(rotate);
		world = localSpace.mul(p);
		face.pos = world;
	}
	@Override
	public void update(SceneObject parent, float dt) {
		int x = gc.getInput().getMouseX();
		int y = gc.getInput().getMouseY();
	
		Vector2D tmp = new Vector2D(x, y);//.minus(motionVector);
//		motionVector.pluss(tmp);
		localSpace.translate(tmp);
		this.pos.setX(x);
		this.pos.setY(y);
		System.out.printf("x: " +localSpace.getMotionVector().getX() + " y: " + localSpace.getMotionVector().getY());
	}

	@Override
	public void render(SceneObject parent, Renderer r) {
		final Matrix3x3 cameraMatrixc = parent.getLocalSpace();
		final Matrix3x3 cameraSpace = new Matrix3x3();
		cameraSpace.set(cameraMatrixc.getCopy());
//		final Matrix3x3 cameraMatrixc = gm.getCamera().getToCameraSpace();
//		final Matrix3x3 cameraMatrixc = gm.getCamera().getToWorldSpace();
//		final Matrix3x3 cameraSpace = cameraMatrixc.mulCopy(localSpace);
//		final Matrix3x3 cameraMatrixc = new Matrix3x3();
//		final Matrix3x3 cameraSpace = localSpace.mulCopy(cameraMatrixc);
//		final Matrix3x3 cameraSpace = cameraMatrixc.mulCopy(localSpace);
		
		System.out.println("\t cx: " +cameraSpace.getMotionVector().getX() + " y: " + cameraSpace.getMotionVector().getY());
		
		world = cameraSpace.mul(p);
//		Vector2D m = cameraSpace.getMotionVector();
//		System.out.println("x: " +m.getX() + " y: " + m.getY() + "\t mouse x: " + x + " Mouse y:" + y);
//		world = localSpace.mul(p);
		face.pos = world;
//		cameraMatrixc.mul(p, face.pos);
		drawLine(this.face.pos, r);
//		final Texture2d d = new Texture2d(this.face);
		r.drawTexture(face, spaceship);
//		r.drawRect((int)(posX-2.5f) , (int)(posY-2.5), 5, 5, 0xff000000);
		this.drawDirection(rotatedDirection, r);
		if(xcollition) {
			drawRec(new Vector2D(),0xffff0000, r);
		}
		if(ycollition) {
			drawRec(new Vector2D(16, 0), 0xffffff00, r);
		}
		
	}
	
	public void update(GameContainer gc, GameManager gm, float dt) {
		this.gm = gm;
//		float angleAdd = dt/10;
//		final boolean theY = gm.getCollision(tileX , tileY + (int)Math.signum((int)offY));
//		final boolean theX = gm.getCollision(tileX, tileY);
////		System.out.println("theX: " + theX + "\ttheY: " + theY + "\ttilex:" + tileX + "\ttiley: " + tileY);
//		if(theX || theY) {
//			xcollition = true;
//		} else {
//			xcollition = false;
//		}
//		if(gm.getCollision(tileX-1 , tileY) || gm.getCollision(tileX-1 , tileY + (int)Math.signum((int)offY)) ) {
//			ycollition = true;
//		}else {
//			ycollition = false;
//		}
//
//		powerOn = gc.getInput().isKeyDown(KeyEvent.VK_UP);
//		if(gc.getInput().isKey(KeyEvent.VK_LEFT)) {
//			angle -= angleAdd;
//		}	
//		if(gc.getInput().isKey(KeyEvent.VK_RIGHT)) {
//			angle += angleAdd;
//		}
//		
//		totAngle += angle;
//		rotate.rotate(angle);
//		rotatedDirection = rotate.mul(direction);
//		
//		final Vector2D sumOfForces = new Vector2D(gravity);
//		if(powerOn) {
//			sumOfForces.pluss(rotatedDirection.scale(40));
//		}
//		final Vector2D v0 = new Vector2D(velocity);
//		velocity = v0.add(sumOfForces.scale(dt));
//		final Vector2D s0 = new Vector2D(offX, offY);
//		final Vector2D s1 = s0.add(v0.add(velocity).scale(dt*0.5f));
////		final Vector2D s1 = s0.add(velocity.scale(dt)).add(velocity).scale(dt*dt*0.5f));
//		
//		offX = s1.getX();
//		offY = s1.getY();
//		if(mousePositioning) {
//			int x = gc.getInput().getMouseX();
//			int y = gc.getInput().getMouseY();
////			System.out.println("mouseX: " + x + "\tmouseY: " + y);
//			boolean notInitialized = x == 0 && y==0;
//			if(!notInitialized) {
//				int ts = GameManager.TS;
//				tileX = x / GameManager.TS;
//				tileY = y / GameManager.TS;
//				offX = (float)(x % GameManager.TS);
//				offY = (float)(y % GameManager.TS);
//			}
//		} 
//		final int HALF_INT = GameManager.TS /2;
//		if(offY > HALF_INT ) {
//			tileY++;
//			offY -= GameManager.TS;
//		}
//		if(offY < -HALF_INT ) {
//			tileY--;
//			offY += GameManager.TS;	
//		}
//		if(offX > HALF_INT ) {
//			tileX++;
//			offX -= GameManager.TS;
//		}
//		if(offX < -HALF_INT ) {
//			tileX--;
//			offX += GameManager.TS;
//		}
//		this.posX = tileX * GameManager.TS + offX;
//		this.posY = tileY * GameManager.TS + offY;
//		System.out.println("x: " +posX + " posy: " + posY);
		int x = gc.getInput().getMouseX();
		int y = gc.getInput().getMouseY();
	
		Vector2D tmp = new Vector2D(x, y);//.minus(motionVector);
//		motionVector.pluss(tmp);
		localSpace.translate(tmp);
		this.pos.setX(x);
		this.pos.setY(y);
//		posX = localSpace.getMotionVector().getX();
//		posY = localSpace.getMotionVector().getY();
		System.out.printf("x: " +localSpace.getMotionVector().getX() + " y: " + localSpace.getMotionVector().getY());
//		localSpace.mul(rotate.getCopy());
		
//		final Matrix3x3 cameraMatrixc = gm.getCamera().getToCameraSpace();
//		final Matrix3x3 cameraMatrixc = gm.getCamera().getToWorldSpace();
//		final Matrix3x3 cameraSpace = cameraMatrixc.mulCopy(localSpace);
//		final Matrix3x3 cameraMatrixc = new Matrix3x3();
//		final Matrix3x3 cameraSpace = localSpace.mulCopy(cameraMatrixc);
//		final Matrix3x3 cameraSpace = cameraMatrixc.mulCopy(localSpace);
		
//		System.out.println("x: " +m.getX() + " y: " + m.getY());
		
//		world = cameraSpace.mul(p);
//		Vector2D m = cameraSpace.getMotionVector();
//		System.out.println("x: " +m.getX() + " y: " + m.getY() + "\t mouse x: " + x + " Mouse y:" + y);
//		world = localSpace.mul(p);
//		face.pos = world;
//		localSpace.setIdentity();
	}
	
	public void render(GameContainer gc, Renderer r) {
		final Matrix3x3 cameraMatrixc = gm.getCamera().getToCameraSpace();
//		final Matrix3x3 cameraMatrixc = gm.getCamera().getToWorldSpace();
//		final Matrix3x3 cameraSpace = cameraMatrixc.mulCopy(localSpace);
//		final Matrix3x3 cameraMatrixc = new Matrix3x3();
		final Matrix3x3 cameraSpace = localSpace.mulCopy(cameraMatrixc);
//		final Matrix3x3 cameraSpace = cameraMatrixc.mulCopy(localSpace);
		
		System.out.println("\t cx: " +cameraSpace.getMotionVector().getX() + " y: " + cameraSpace.getMotionVector().getY());
		
		world = cameraSpace.mul(p);
//		Vector2D m = cameraSpace.getMotionVector();
//		System.out.println("x: " +m.getX() + " y: " + m.getY() + "\t mouse x: " + x + " Mouse y:" + y);
//		world = localSpace.mul(p);
		
		drawLine(this.face.pos, r);
		final Texture2d d = new Texture2d(this.face);
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

package no.fun.stuff.engine.game;

import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.Rect4PointRenderer;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;
import no.fun.stuff.engine.matrix.Vector2D;

public class Rectangle extends GameObject {
	private final int w = GameManager.TS/2;
	private final int h = GameManager.TS/2;
	private Point[] p = {new Point(-w, -h), new Point(w, -h), new Point(w, h), new Point(-w, h)};
	private Point[] word = new Point[p.length];
	private final Vector2D position = new Vector2D();
	private Matrix3x3 localSpace = new Matrix3x3();
	private Matrix3x3 translate = new Matrix3x3();
	private int color = 0;
	public Rectangle(final Vector2D pos) {
		position.setXY(pos);
		for(int i=0;i<p.length;i++) {
			word[i] = p[i];
		}
	}
	public Rectangle(final Vector2D pos, int color) {
		position.setXY(pos);
		this.color = color;
		for(int i=0;i<p.length;i++) {
			word[i] = p[i];
		}
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
//		Vector2D pos = position
		localSpace.translate(position);
		final Matrix3x3 camera = gm.getCamera().getToCameraSpace();
		final Matrix3x3 matrix = camera.fastMulCopy(localSpace);
//		final Matrix3x3 matrix = localSpace.fastMulCopy(camera);
		
		matrix.mul(p, word);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		final Rect4PointRenderer p = new Rect4PointRenderer();
		p.setColor(color);
		p.drawRect(word, r);
	}

	public void setColor(int color) {
		this.color = color;
	}
}

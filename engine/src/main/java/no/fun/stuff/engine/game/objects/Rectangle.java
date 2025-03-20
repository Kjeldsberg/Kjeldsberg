package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;
import no.fun.stuff.engine.matrix.Vector2D;

public class Rectangle extends Body {
	public static final int TS = 16;
	private final int w = TS/2;
	private final int h = TS/2;
	private Point[] p = {new Point(-w, -h), new Point(w, -h), new Point(w, h), new Point(-w, h)};
	private Point[] word = new Point[p.length];
	private final Vector2D position = new Vector2D();
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
	


	public void setColor(int color) {
		this.color = color;
	}

	public void update(SceneObject parent, float dt) {
		translate.translate(position);
		final Matrix3x3 camera = parent.getModel();
		final Matrix3x3 matrix = camera.fastMulCopy(model);
		matrix.mul(p, word);

	}

	public void render(SceneObject parent, Renderer r) {
		final Rect4PointRenderer p = new Rect4PointRenderer();
		p.setColor(color);
		p.drawRect(word, r);

	}

	@Override
	public Vector2D applyForces() {
		return Vector2D.ZERO;
	}
}

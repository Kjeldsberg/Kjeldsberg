package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.Rect4PointRenderer;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;
import no.fun.stuff.engine.matrix.Vector2D;

public class Rect extends SceneObject {
	private final int w = GameManager.TS/2;
	private final int h = GameManager.TS/2;
	private Point[] p = {new Point(-w, -h), new Point(w, -h), new Point(w, h), new Point(-w, h)};
	private Point[] word = new Point[p.length];
	private int color = 0;

	public Rect(final Vector2D pos) {
		this.pos.setXY(pos);
		for(int i=0;i<p.length;i++) {
			word[i] = p[i];
		}
	}
	public Rect(final Vector2D pos, int color) {
		pos.setXY(pos);
		this.color = color;
		for(int i=0;i<p.length;i++) {
			word[i] = p[i];
		}
	}

	@Override
	public void update(SceneObject parent, float dt) {
//		Vector2D pos = position
		this.localSpace.translate(pos);
//		final Matrix3x3 camera = gm.getCamera().getToCameraSpace();
//		final Matrix3x3 matrix = camera.fastMulCopy(localSpace);
//		final Matrix3x3 matrix = localSpace.fastMulCopy(camera);
		
//		matrix.mul(p, word);

	}

	@Override
	public void render(SceneObject parent, Renderer r) {
		final Matrix3x3 work = new Matrix3x3();
		if(parent.getLocalSpace() != null) {
			work.set(parent.localSpace.fastMulCopy(this.localSpace).getCopy());
		}else {
			work.set(this.localSpace.getCopy());
		}
		work.mul(p, word);
		r.drawRect4(this);
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	public Point[] getWordCordinate() {
		return word;
	}
}

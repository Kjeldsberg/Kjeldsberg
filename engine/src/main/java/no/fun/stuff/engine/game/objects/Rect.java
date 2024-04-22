package no.fun.stuff.engine.game.objects;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;
import no.fun.stuff.engine.matrix.Vector2D;

public class Rect extends SceneObject {
	private float halfWidth;
	private float halfHeight;
	private Point[] p;
	private Point[] word;
	private int color = 0;
	private boolean dirty = true;

	public Rect(Vector2D size, final Vector2D pos) {
		halfWidth = size.getX();
		halfHeight = size.getY();
		p = new Point[] {new Point(-halfWidth, -halfHeight), new Point(halfWidth, -halfHeight), new Point(halfWidth, halfHeight), new Point(-halfWidth, halfHeight)};
		word = new Point[p.length];
		this.pos.setXY(pos);
		translate.translate(pos);
		this.model.set(translate.fastMulCopy(rotate).fastMulCopy(scale).getCopy());
		dirty = false;
		for(int i=0;i<p.length;i++) {
			word[i] = p[i];
		}
	}
	public void setPosition(final Vector2D pos) {
		this.pos.setXY(pos);
		dirty = true;
	}
	@Override
	public void update(SceneObject parent, float dt) {
//		Vector2D pos = position
		this.model.translate(pos);
//		final Matrix3x3 camera = gm.getCamera().getToCameraSpace();
//		final Matrix3x3 matrix = camera.fastMulCopy(model);
//		final Matrix3x3 matrix = model.fastMulCopy(camera);
		
//		matrix.mul(p, word);

	}

	@Override
	public void render(SceneObject parent, Renderer r) {
		if(dirty) {
			this.model.set(translate.fastMulCopy(rotate).fastMulCopy(scale).getCopy());
			dirty = false;
		}
		final Matrix3x3 work = new Matrix3x3();
		if(parent.getModel() != null) {
			work.set(parent.model.fastMulCopy(this.model).getCopy());
		}else {
			work.set(this.model.getCopy());
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

	public float getHalfWidth() {
		return halfWidth;
	}

	public void setHalfWidth(float halfWidth) {
		this.halfWidth = halfWidth;
	}

	public float getHalfHeight() {
		return halfHeight;
	}

	public void setHalfHeight(float halfHeight) {
		this.halfHeight = halfHeight;
	}
}

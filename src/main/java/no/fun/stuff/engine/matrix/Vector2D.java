package no.fun.stuff.engine.matrix;

public class Vector2D {
	private float x, y;

	public Vector2D(final Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}
	
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D() {
	}
	
	public Vector2D minus(final Vector2D v) {
		return new Vector2D(this.x - v.x, this.y - v.y);
	}
	
	public Vector2D scale(float scale) {
		return new Vector2D(this.x * scale, this.y * scale);
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
}

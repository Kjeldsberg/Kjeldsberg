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
	public Vector2D(final Vector2D o) {
		this.x = o.x;
		this.y = o.y;
	}
	
	public Vector2D() {
	}
	public void normaize() {
		float len = 1/(float)Math.sqrt(x*x + y*y);
		x *= len;
		y *= len;
	}
	public Point toPoint() {
		return new Point(x, y);
	}
	
	public Vector2D minus(final Vector2D v) {
		return new Vector2D(this.x - v.x, this.y - v.y);
	}
	public Vector2D add(final Vector2D v) {
		return new Vector2D(this.x + v.x, this.y + v.y);
	}
	
	public void pluss(final Vector2D v) {
		this.x += v.x; 
		this.y += v.y;
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
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return "Vector2D [x=" + x + ", y=" + y + "]";
	}

}

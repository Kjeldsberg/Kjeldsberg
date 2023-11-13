package no.fun.stuff.engine.matrix;

public class Point {
	private float x, y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Point(final Point p) {
		this.x = p.x;
		this.y =p.y;
	}

	public Point() {
	}

	public void setVector(final Vector2D v) {
		this.x = v.getX();
		this.y = v.getY();
	}

	public void setPoint(final Point p) {
		this.x = p.x;
		this.y =p.y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}


}

package no.fun.stuff.engine.matrix;

import no.fun.stuff.engine.game.util.Util;

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
	public float cross(final Vector2D v) {
		return x * v.y - y * v.x;
	}

	public void setXY(final Point p) {
		x = p.getX();
		y = p.getY();
	}
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void setXY(final Vector2D p) {
		x = p.x;
		y = p.y;
	}
	public float dot(final Vector2D v) {
		return x * v.x + y*v.y;
	}
	public void normaize() {
		float sqrt = (float) Math.sqrt(x * x + y * y);
		if(Math.abs(sqrt) > Util.epsilon) {
			float len = 1/ sqrt;
			x *= len;
			y *= len;
		}
	}
	public float length() {
		return (float)Math.sqrt(x*x + y*y);
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
	public void sub(final Vector2D v) {
		this.x -= v.x; 
		this.y -= v.y;
	}
	
	public void pluss(final Vector2D v) {
		this.x += v.x; 
		this.y += v.y;
	}
	public Vector2D scale(float scale) {
		return new Vector2D(this.x * scale, this.y * scale);
	}
	public Vector2D mul(float scale) {
		this.x *= scale;
		this.y *= scale;
		return this;
	}
	public Vector2D mul(float scaleX, float scaleY) {
		this.x *= scaleX;
		this.y *= scaleY;
		return this;
	}

	public Vector2D pluss(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
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

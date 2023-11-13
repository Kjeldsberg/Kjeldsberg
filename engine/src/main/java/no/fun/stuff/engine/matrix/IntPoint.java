package no.fun.stuff.engine.matrix;

public class IntPoint {

	private int x, y;

	public IntPoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public IntPoint(Point p) {
		super();
		this.x = (int)Math.floor(p.getX());
		this.y = (int)Math.floor(p.getY());
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}

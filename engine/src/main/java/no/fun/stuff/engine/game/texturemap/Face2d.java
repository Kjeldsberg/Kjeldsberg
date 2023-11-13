package no.fun.stuff.engine.game.texturemap;


import no.fun.stuff.engine.matrix.Point;

public class Face2d {
	public Point[] pos;
	public Point[] uv;
	public String[] point = {"p0", "p1", "p2", "p3"};
	public Face2d(final Face2d d) {
		
		pos = new Point[d.pos.length];
		uv = new Point[d.uv.length];
		for(int i = 0;i<d.pos.length;i++) {
			pos[i] = new Point(d.pos[i]);
			uv[i] = new Point(d.uv[i]);
		}
	}
	public Face2d(final Point[] pos, final Point[] uv) {
		this.pos = pos;
		this.uv = uv;
	}
	private int topPoint(Point[] points) {
		Point p = points[0];
		int ret = 0;
		for (int i = 1; i < points.length; i++) {
			boolean yen = points[i].getY() < p.getY();

			if (yen) {
				p = points[i];
				ret = i;
			}
		}
		return ret;
	}

}

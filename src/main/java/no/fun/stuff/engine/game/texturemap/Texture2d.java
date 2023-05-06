package no.fun.stuff.engine.game.texturemap;

import no.fun.stuff.engine.matrix.Point;

public class Texture2d {
	public Point[] pos;
	public Point[] uv;
	public String[] point = {"p0", "p1", "p2", "p3"};
	public Texture2d(final Texture2d d) {
		
		pos = new Point[d.pos.length];
		uv = new Point[d.uv.length];
		for(int i = 0;i<d.pos.length;i++) {
			pos[i] = new Point(d.pos[i]);
			uv[i] = new Point(d.uv[i]);
		}
	}
	public Texture2d(final Point[] pos, final Point[] uv) {
		this.pos = pos;
		this.uv = uv;
	}

}

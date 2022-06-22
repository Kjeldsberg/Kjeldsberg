package no.fun.stuff.engine.game.texturemap;

import no.fun.stuff.engine.matrix.Point;

public class Face2d {
	public Point[] pos;
	public Point[] uv;
	public String[] point = {"p0", "p1", "p2", "p3"};
	public Face2d(final Point[] pos, final Point[] uv) {
		this.pos = pos;
		this.uv = uv;
	}

}

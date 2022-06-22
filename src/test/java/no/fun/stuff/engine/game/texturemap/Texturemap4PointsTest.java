package no.fun.stuff.engine.game.texturemap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;

class Texturemap4PointsTest {
	private Point p[] = {new Point(-10, -10), new Point(10, -10), new Point(10, 10), new Point(-10, 10)};
	private Point p2[] = {new Point(-10, -10), new Point(10, -10), new Point(10, 10), new Point(-10, 10)};
	private Point center = new Point(320/2, 240/2);
	private float angle = 30f;
	private Matrix3x3 m = new Matrix3x3();
	private Matrix3x3 translate = new Matrix3x3();
	private Matrix3x3 scale = new Matrix3x3();
	private Matrix3x3 rotate = new Matrix3x3();

	@Test
	void testTexturemap() {
		angle = 30;
		m.scale(8.5f);
		m.translate(center);
		rotate.rotate(angle);
		m.mul(rotate);
		Point[] world = m.mul(p);
		m.clear();
		final Face2d get = new Face2d(world , p2);
		Texturemap4Points test = new Texturemap4Points(null);
		test.texturemap(get, null);
		int t = 0;
	}

}

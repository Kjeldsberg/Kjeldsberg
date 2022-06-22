package no.fun.stuff.engine.matrix;

import org.junit.jupiter.api.Test;

public class Matrix3x3Test {
	@Test
	void test() {
		Matrix3x3 t = new Matrix3x3();
		Point p = new Point(100, 100);
		Point f = t.mul(p);
		t.rotate(1);
		Point g = t.mul(p);
		int test = 0;
	}

}

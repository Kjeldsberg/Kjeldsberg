package no.fun.stuff.engine.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Matrix3x3Test {
	
	@Test
	void scaleTest() {
		final Matrix3x3 test = new Matrix3x3();
		test.scale(2.0f);
		final float[][] rest = test.getCopy();
		assertEquals(rest[0][0], 2.0f);
		assertEquals(rest[1][0], 0.0f);
		assertEquals(rest[2][0], 0.0f);

		assertEquals(rest[0][1], 0.0f);
		assertEquals(rest[1][1], 2.0f);
		assertEquals(rest[2][1], 0.0f);

		assertEquals(rest[0][2], 0.0f);
		assertEquals(rest[1][2], 0.0f);
		assertEquals(rest[2][2], 1.0f);
		test.invertScale();
		final float[][] fest = test.getCopy();
		assertEquals(fest[0][0], 1.0f/2.0f);
		assertEquals(fest[1][0], 0.0f);
		assertEquals(fest[2][0], 0.0f);

		assertEquals(fest[0][1], 0.0f);
		assertEquals(fest[1][1], 1.0f/2.0f);
		assertEquals(fest[2][1], 0.0f);

		assertEquals(fest[0][2], 0.0f);
		assertEquals(fest[1][2], 0.0f);
		assertEquals(fest[2][2], 1.0f);
	}
	@Test
	void invertTranslateTest() {
		final Matrix3x3 test = new Matrix3x3();
		test.setIdentity();
		final Vector2D p = new Vector2D(10.0f, 20.0f);
		test.translate(p);
		float[][] rest = test.getCopy();
		assertEquals(rest[0][0], 1.0f);
		assertEquals(rest[1][0], 0.0f);
		assertEquals(rest[2][0], 0.0f);

		assertEquals(rest[0][1], 0.0f);
		assertEquals(rest[1][1], 1.0f);
		assertEquals(rest[2][1], 0.0f);

		assertEquals(rest[0][2], p.getX());
		assertEquals(rest[1][2], p.getY());
		assertEquals(rest[2][2], 1.0f);
		
		float[][] inv = test.invertTranslateCopy().getCopy();
		assertEquals(inv[0][0], 1.0f);
		assertEquals(inv[1][0], 0.0f);
		assertEquals(inv[2][0], 0.0f);

		assertEquals(inv[0][1], 0.0f);
		assertEquals(inv[1][1], 1.0f);
		assertEquals(inv[2][1], 0.0f);

		assertEquals(inv[0][2], p.getX() *-1);
		assertEquals(inv[1][2], p.getY() *-1);
		assertEquals(inv[2][2], 1.0f);
		
	}
	
	@Test
	void setIdentityTest() {
		final Matrix3x3 test = new Matrix3x3();
		test.setIdentity();
		float[][] t = test.getCopy();
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(i==j) {
					assertTrue(t[i][j] == 1.0f);
					continue;
				}
				assertTrue(t[i][j] == 0.0f);
			}
		}
	}
	
	@Test
	void test() {
		Matrix3x3 t = new Matrix3x3();
		Point p = new Point(100, 100);
		Point f = t.mul(p);
		t.rotate(1);
		Point g = t.mul(p);
		int test = 0;
	}

	@Test
	void testMultipify() {
		final Matrix3x3 base = new Matrix3x3();
		base.setUpVector(new Vector2D(0.0f, 1.0f));
		final Matrix3x3 scale = new Matrix3x3();
		scale.scale(2);
		final Matrix3x3 translate = new Matrix3x3();
		translate.translate(new Vector2D(10.0f, 20.0f));
		
		Matrix3x3 test1 = base.multiply(scale).multiply(translate);
		Matrix3x3 test12 = base.fastMulCopy(scale).fastMulCopy(translate);
		Matrix3x3 test2 = base.multiply(scale);
		Matrix3x3 test3 = test2.multiply(translate);
		int test = 0;
		test +=1;
	}
}

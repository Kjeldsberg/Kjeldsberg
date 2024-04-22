package no.fun.stuff.engine.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import no.fun.stuff.engine.game.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Matrix3x3Test {
	private Matrix3x3 model = new Matrix3x3();
	private Matrix3x3 modelInverse = new Matrix3x3();
	private Matrix3x3 camera = new Matrix3x3();
	private Matrix3x3 cameraInverse = new Matrix3x3();
	@Test
	void testNormal() {
		Matrix3x3 trans = new Matrix3x3();
		Matrix3x3 scale = new Matrix3x3();
		Matrix3x3 rotate = new Matrix3x3();
		Vector2D p = new Vector2D(1.0f, 2.0f);
		trans.translate(p);
		scale.scale(12);
		rotate.rotate(0.01f);
		model.fastMul(trans, rotate, scale);
		Vector2D pos = new Vector2D(3.5f, 4.5f);
		Vector2D pos2 = new Vector2D(1.5f, 1.5f);
		Vector2D vpos = new Vector2D(pos).minus(pos2);

		Vector2D world = model.mul(pos);
		Vector2D world2 = model.mul(pos2);
		Vector2D vWorld = new Vector2D(world).minus(world2);
		Vector2D mul1 = model.mul(vpos);
		vpos.normaize();
		Vector2D mul3 = model.mul(vpos);
		trans.invertTranslate();
		scale.invertScale();
		rotate.transpose();
		modelInverse.fastMul(scale, rotate, trans);
		Vector2D mul = modelInverse.mul(world);
		Vector2D mul2 = modelInverse.mul(vWorld);
		Vector2D mul4 = modelInverse.mul(mul3);
		assertTrue(Util.compare(mul.getX(), pos.getX()));
		assertTrue(Util.compare(mul.getY(), pos.getY()));

	}
	@Test
	void modelTest() {
		Matrix3x3 trans = new Matrix3x3();
		Matrix3x3 scale = new Matrix3x3();
		Matrix3x3 rotate = new Matrix3x3();
		Vector2D p = new Vector2D(1.0f, 2.0f);
		trans.translate(p);
		scale.scale(12);
		rotate.rotate(0.01f);
		model.fastMul(trans, rotate, scale);
		Vector2D pos = new Vector2D(3.5f, 4.5f);
		Vector2D world = model.mul(pos);
		trans.invertTranslate();
		scale.invertScale();
		rotate.transpose();
		modelInverse.fastMul(scale, rotate, trans);
		Vector2D mul = modelInverse.mul(world);
		assertTrue(Util.compare(mul.getX(), pos.getX()));
		assertTrue(Util.compare(mul.getY(), pos.getY()));


		Matrix3x3 camTrans = new Matrix3x3();
		Matrix3x3 camScale = new Matrix3x3();
		Matrix3x3 camRotate = new Matrix3x3();
		camScale.scale(new Vector2D(3.6f, 4.6f));
		camRotate.rotate(-0.3f);
		camTrans.translate(new Vector2D(10f, 20f));
		camera.fastMul(camTrans, camRotate, camScale);
		Matrix3x3 view = new Matrix3x3(camera);
		view.fastMul(model);
		Vector2D mul1 = view.mul(pos);
		camTrans.invertTranslate();
		camRotate.transpose();
		camScale.invertScale();
		cameraInverse.fastMul(camScale, camRotate, camTrans);
		Matrix3x3 viewInvert = new Matrix3x3(modelInverse);
		viewInvert.fastMul(cameraInverse);
		Vector2D mul2 = viewInvert.mul(mul1);
		assertTrue(Util.compare(mul2.getX(), pos.getX()));
		assertTrue(Util.compare(mul2.getY(), pos.getY()));
	}

	@Test
	void inverseRotateTest() {
		Matrix3x3 rotate = new Matrix3x3();
		rotate.rotate(2f);
		Vector2D p = new Vector2D(1f, 1f);
		Vector2D mul = rotate.mul(p);
		Matrix3x3 rotateInv = new Matrix3x3(rotate);
//		rotateInv.inverseRotate();
		rotateInv.transpose();
		Vector2D mul1 = rotateInv.mul(mul);
		rotate.fullFastMul(rotateInv);
		boolean compareX = Util.compare(p.getX(), mul1.getX());
		boolean compareY = Util.compare(p.getY(), mul1.getY());
		assertTrue(compareX && compareY);
	}

	@Test
	void inverseTranslateTest() {
		Matrix3x3 transe = new Matrix3x3();
		transe.translate(2f, 3f);
		Vector2D p = new Vector2D(1f, 1f);
		Vector2D mul = transe.mul(p);
		Matrix3x3 transeInv = new Matrix3x3(transe);
		transeInv.invertTranslate();
		Vector2D mul1 = transeInv.mul(mul);
		transe.fullFastMul(transeInv);
		boolean compareX = Util.compare(p.getX(), mul1.getX());
		boolean compareY = Util.compare(p.getY(), mul1.getY());
		assertTrue(compareX && compareY);
	}

	@Test
	void inverse3x3Test() {
		float[][] test = {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}
		};
		Matrix3x3 m = new Matrix3x3();
		m.set(test);
		Matrix3x3 inverse = new Matrix3x3();
		m.inverse(inverse);
		Matrix3x3 multiply = inverse.multiply(m);
		Matrix3x3 multiply1 = m.multiply(inverse);
		Matrix3x3 transpose = new Matrix3x3();
		transpose.translate(2f, 3f);
		Matrix3x3 transposeInvers = new Matrix3x3(transpose);
		transposeInvers.invertTranslate();
		transposeInvers.fullFastMul(transpose);
		int test2 = 0;
	}

	@Test
	void invere3x3Test() {
		Matrix3x3 scale = new Matrix3x3();
		Matrix3x3 translate = new Matrix3x3();
		Matrix3x3 rotate = new Matrix3x3();
		scale.scale(2.0f);
		translate.translate(2.5f, 3.5f);
		rotate.rotate(0.1f);
		model.fastMul(translate, rotate, scale);
		Vector2D local = new Vector2D(1f, 1f);
		Vector2D world = model.mul(local);

		scale.invertScale();
		translate.invertTranslate();
		rotate.transpose();

		modelInverse.fastMul(scale, rotate, translate);
		Vector2D mul = modelInverse.mul(world);
		boolean cX = Util.compare(local.getX(), mul.getX());
		boolean cY = Util.compare(local.getY(), mul.getY());
		assertTrue(cX && cY);
	}

	@Test
	void testInverse() {
		Matrix3x3 scale = new Matrix3x3();
		Matrix3x3 translate = new Matrix3x3();
		Matrix3x3 rotate = new Matrix3x3();
		scale.scale(2.0f);
		translate.translate(2.5f, 3.5f);
		rotate.rotate(0.1f);
		model.fastMul(translate, rotate, scale);
		Vector2D local = new Vector2D(1f, 1f);
		Vector2D world = model.mul(local);
		scale.invertScale();
		translate.invertTranslate();
		rotate.inverseRotate();
		modelInverse.fastMul(translate, rotate, scale);
		Vector2D mul = modelInverse.mul(world);
		Matrix3x3 matrix3x3 = model.thaMulCopy(modelInverse);
		Matrix3x3 inverse = new Matrix3x3();

		modelInverse.fastMul(scale, rotate, translate);
		Vector2D mul1 = modelInverse.mul(local);
		rotate.inverse(inverse);
		float[][] c = rotate.getCopy();
		Matrix2x2 m2 = new Matrix2x2(c[0][0], c[0][1], c[1][0], c[1][1]);
		Matrix2x2 m3 = new Matrix2x2(c[0][0], c[0][1], c[1][0], c[1][1]);
		m3.inverse();
		m2.mul(m3);
		int test = 0;
//		Matrix3x3 m = new Matrix3x3(translate);
//		m.fastMul(rotate, scale);
//		Vector2D v = new Vector2D(1.0f, 1.0f);
//		Vector2D world = m.mul(v);
//		Matrix3x3 inverse = new Matrix3x3();
//		m.inverse(inverse);
//		Vector2D mul = inverse.mul(world);
//		boolean b = Util.compare2(mul.getX(), v.getX());
//		boolean b1 = Util.compare2(mul.getY(), v.getY());
//		assertTrue(b);
//		assertTrue(b1);

	}

	@Test
	void inversRotateTest() {
		Matrix3x3 rotate = new Matrix3x3();
		rotate.rotate(0.1f);
		Matrix3x3 invRotate = new Matrix3x3(rotate);
		invRotate.inverseRotate();
		rotate.fastMul(invRotate);
		float[][] c = rotate.getCopy();
		boolean b0 = c[0][0] == 1f;
		boolean b1 = c[1][1] == 1f;
		boolean b2 = c[2][2] == 1f;
		assertTrue(b0 && b1 && b2);
	}

	@Test
	void testTranspose() {
		float[][] test = {{1, 2, 3},
				{0, 1, 2},
				{5, 6, 0}};
		Matrix3x3 matrix3x3 = new Matrix3x3();
		matrix3x3.set(test);
		matrix3x3.transpose();
		float[][] copy = matrix3x3.getCopy();
		assertEquals(test[0][2], copy[2][0]);
		assertEquals(test[0][1], copy[1][0]);
		assertEquals(test[0][0], copy[0][0]);

		assertEquals(test[1][2], copy[2][1]);
		assertEquals(test[1][1], copy[1][1]);
		assertEquals(test[1][0], copy[0][1]);

		assertEquals(test[2][2], copy[2][2]);
		assertEquals(test[2][1], copy[1][2]);
		assertEquals(test[2][0], copy[0][2]);
	}

	@Test
	void testAdjugated() {
		float[][] test = {{1, 2, 3},
				{0, 1, 4},
				{5, 6, 0}};
		Matrix3x3 mat = new Matrix3x3();
		mat.set(test);
		mat.transpose();
		Matrix3x3 adjugated = new Matrix3x3();
		mat.adjugateMatrix(adjugated);
		float[][] test2 = {{-24, 18, 5},
				{20, -15, -4},
				{-5, 4, 1}};
		float[][] copy = adjugated.getCopy();
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				assertEquals(test2[i][j], copy[i][j]);
			}
		}


	}

	@Test
	void deterinatTest() {
		float[][] test = {{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}};

		Matrix3x3 matrix3x3 = new Matrix3x3();
		matrix3x3.set(test);
		Assertions.assertEquals(matrix3x3.determinant(matrix3x3), 0);
	}

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

	@Test
	void scaleTestMul() {
		Matrix3x3 scale = new Matrix3x3();
		Vector2D pos = new Vector2D(1f, 1f);
		Vector2D oneFactor = scale.mul(pos);
		System.out.println("oneFactor: " +  oneFactor);
		scale.scale(2f);
		Vector2D twoFactor = scale.mul(pos);
		System.out.println("twoFactor: " +  twoFactor);
		scale.scale(0.5f);
		Vector2D halfFactor = scale.mul(pos);
		System.out.println("halfFactor: " +  halfFactor);
		int test = 0;

	}
}

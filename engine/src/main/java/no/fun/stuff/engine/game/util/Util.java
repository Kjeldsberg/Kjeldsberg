package no.fun.stuff.engine.game.util;

import no.fun.stuff.engine.matrix.Vector2D;

public class Util {
	public static float epsilon = 0.00001f;
	public static final float ONE_MINUS_THRESHOLD = 1.0f - epsilon;

	public static boolean compare(float a, float b, float epsilon) {
		return Math.abs(a-b) <= epsilon * Math.max(1.0f,  Math.max(Math.abs(a), Math.abs(b)));
	}	
	public static boolean compare(float a, float b) {
		return Math.abs(a-b) <= epsilon * Math.max(1.0f,  Math.max(Math.abs(a), Math.abs(b)));
	}
	public static boolean compare2(float a, float b) {
		float abs = Math.abs(a - b);
		boolean b1 = abs <= 0.01f;
		if(b1) {
			int tesy = 0;
		}
		return abs <= 0.01000f;
	}
	private static final Vector2D b = new Vector2D();
	public static void lerp(float delta, final Vector2D p0, Vector2D p1, final Vector2D result) {
		Vector2D a = result;
		if(delta <= epsilon) {
			a.setXY(p0);
			return;
		}
		if(delta >= 1.0f) {
			a.setXY(p1);
			return;
		}
		b.setXY(p1);
		a.setXY(p0);
		b.sub(a);
		b.mul(delta);
		a.pluss(b);
	}

}

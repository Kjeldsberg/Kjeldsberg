package no.fun.stuff.engine.game.util;

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

}

package no.fun.stuff.engine.game.util;

public class Util {
	public static float epsilon = 0.00001f;
	public static boolean compare(float a, float b, float epsilon) {
		return Math.abs(a-b) <= epsilon * Math.max(1.0f,  Math.max(Math.abs(a), Math.abs(b)));
	}	

}

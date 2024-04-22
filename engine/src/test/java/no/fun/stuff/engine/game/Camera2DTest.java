package no.fun.stuff.engine.game;

import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Camera2DTest {
	@Test
	void test() {
		Vector2D viewPort = new Vector2D(8f, 6f);
		Vector2D screenSize = new Vector2D(320, 200);
		Camera2D cam = new Camera2D(screenSize, viewPort);
		cam.calculateModel();
		Vector2D mul = cam.getModel().mul(viewPort.scale(0.5f));
		boolean comparex = Util.compare(mul.getX(), screenSize.getX() * 0.5f);
		boolean comparey = Util.compare(mul.getY(), screenSize.getY() * 0.5f);
		assertTrue(comparex && comparey);
	}

}

package no.fun.stuff.engine.game;

import org.junit.jupiter.api.Test;

public class Camera2DTest {
	
	@Test
	void test() {
		Camera2D cam = new Camera2D();
		cam.recalculateCameraSpace();
		cam.recalculateWorldSpace();
	}

}

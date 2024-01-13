package no.fun.stuff.engine.game;

import no.fun.stuff.engine.matrix.Vector2D;
import org.junit.jupiter.api.Test;

public class Camera2DTest {
	@Test
	void test() {
		Camera2D cam = new Camera2D(new Vector2D(320, 200));
		cam.recalculateCameraSpace();
		cam.recalculateWorldSpace();
	}

}

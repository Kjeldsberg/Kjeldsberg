//package no.fun.stuff.engine.game;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import no.fun.stuff.engine.Renderer;
//
//public class GameScene implements Scene {
//	private List<SceneObject> objects = new ArrayList<SceneObject>();
//	private Renderer renderer;
//
//	public GameScene() {
//	}
//
//	@Override
//	public void update(float dt) {
//		for (int i = 0; i < objects.size(); i++) {
////			objects.get(i).update(camera, dt);
//			if (objects.get(i).dead) {
//				objects.remove(i);
//				i--;
//			}
//		}
////		camera.update(null, dt);
//	}
//
//	@Override
//	public void render(Renderer r) {
////		camera.render(camera, renderer);
//		for(SceneObject s:objects) {
//			s.render(null, r);
//		}
//	}
//
//	public SceneObject findGameObjectByName(String tag) {
//		for (int i = 0; i < objects.size(); i++) {
//			final SceneObject gameObject = objects.get(i);
//			if (tag.equals(gameObject.getTarget())) {
//				return gameObject;
//			}
//		}
//		return null;
//	}
//
//	public void setRenderer(final Renderer renderer) {
//		this.renderer = renderer;
//	}
//
//	public List<SceneObject> getObjects() {
//		return objects;
//	}
//
////	public Camera2D getCamera() {
////		return camera;
////	}
////
////	public void setCamera(Camera2D camera) {
////		this.camera = camera;
////	}
//
//}

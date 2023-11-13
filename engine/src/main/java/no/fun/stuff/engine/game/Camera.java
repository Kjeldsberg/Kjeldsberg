//package no.fun.stuff.engine.game;
//
//import no.fun.stuff.engine.GameContainer;
//import no.fun.stuff.engine.Renderer;
//
//public class Camera {
//	public static int counter = 0;
//	float offX, offY;
//	private GameObject target;
//	private String targetTag = null;
//	public Camera(String tag) {
//		this.targetTag = tag;
//	}
//	public void update(GameContainer gc, final GameManager gm, float dt) {
//		if(target == null) {
//			target = gm.getObject(targetTag);
//		}
//		if(target == null) {
//			return;
//		}
////		float targetX = (target.getPosX() + target.getWidth() / 2) - gc.getWith() /2;
////		float targetY = (target.getPosY() + target.getHeight() / 2)- gc.getWith() /2;
////		offX -= dt * (offX - targetX) * 5;
////		offY -= dt * (offY - targetY) * 5;
//		final int HALF_WIDTH = (int)((float)gc.getWith() * 0.5f);
//		offX = (target.getPosX() + target.getWidth() / 2) - HALF_WIDTH;
//		offY = (target.getPosY() + target.getHeight() / 2) - gc.getWith() /2;
//		
//		
//	}
//	public void render(Renderer r) {
//			r.setCamX((int)offX);
//			r.setCamY((int)offY);
//	}
//	public float getOffX() {
//		return offX;
//	}
//	public void setOffX(float offX) {
//		this.offX = offX;
//	}
//	public float getOffY() {
//		return offY;
//	}
//	public void setOffY(float offY) {
//		this.offY = offY;
//	}
//	public GameObject getTarget() {
//		return target;
//	}
//	public void setTarget(GameObject target) {
//		this.target = target;
//	}
//	public String getTargetTag() {
//		return targetTag;
//	}
//	public void setTargetTag(String targetTag) {
//		this.targetTag = targetTag;
//	}
//	
//}

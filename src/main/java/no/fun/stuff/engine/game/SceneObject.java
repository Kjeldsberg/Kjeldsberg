package no.fun.stuff.engine.game;

import java.util.ArrayList;
import java.util.List;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public abstract class SceneObject {
	protected String target;
	protected Matrix3x3 localSpace = new Matrix3x3();
	protected Matrix3x3 rotate = new Matrix3x3();
	protected Matrix3x3 scale = new Matrix3x3();
	protected Matrix3x3 translate = new Matrix3x3();
	protected Matrix3x3 modelView = new Matrix3x3();
	protected List<SceneObject> child = new ArrayList<SceneObject>();
	protected SceneObject parent;
	protected Vector2D pos = new Vector2D();
	protected boolean dead = false;
	public abstract void update(final SceneObject parent, float dt);
	public abstract void render(final SceneObject parent, final Renderer r);
	
	public void addChild(final SceneObject e) {
		child.add(e);
	}
	public List<SceneObject> getChildren() {
		return child;
	}
	public Vector2D getPos() {
		return pos;
	}
	public void setPos(Vector2D pos) {
		this.pos = pos;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public Matrix3x3 getLocalSpace() {
		return localSpace;
	}
	public void setLocalSpace(Matrix3x3 localSpace) {
		this.localSpace = localSpace;
	}
	public Matrix3x3 getRotate() {
		return rotate;
	}
	public void setRotate(Matrix3x3 rotate) {
		this.rotate = rotate;
	}
	public Matrix3x3 getScale() {
		return scale;
	}
	public void setScale(Matrix3x3 scale) {
		this.scale = scale;
	}
	public Matrix3x3 getTranslate() {
		return translate;
	}
	public void setTranslate(Matrix3x3 translate) {
		this.translate = translate;
	}
	public List<SceneObject> getChild() {
		return child;
	}
	public void setChild(List<SceneObject> child) {
		this.child = child;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public SceneObject getParent() {
		return parent;
	}
	public void setParent(SceneObject parent) {
		this.parent = parent;
	}
	
}

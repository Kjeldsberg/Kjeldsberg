package no.fun.stuff.engine.game;

import no.fun.stuff.engine.GameContainer;
import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Matrix3x3;

public abstract class GameObject {
	protected String tag;
	protected float posX, posY;
	protected int width, height;
	protected boolean dead = false;
	protected Matrix3x3 localMatrix = new Matrix3x3();
	
	public abstract void update(GameContainer gc, GameManager gm, float dt);
	public abstract void render(GameContainer gc, Renderer r);
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public float getPosX() {
		return posX;
	}
	public void setPosX(float posX) {
		this.posX = posX;
	}
	public float getPosY() {
		return posY;
	}
	public void setPosY(float posY) {
		this.posY = posY;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public Matrix3x3 getLocalMatrix() {
		return localMatrix;
	}
	public void setLocalMatrix(Matrix3x3 localMatrix) {
		this.localMatrix = localMatrix;
	}

}

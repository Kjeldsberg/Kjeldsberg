package no.fun.stuff.spaceship.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	private int w, h;
	private int[] p;
	private boolean alfa = false;
	private int lightBlock = Light.NONE;
	public Image(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Image.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		w = image.getWidth();
		h = image.getHeight();
		p = image.getRGB(0, 0,  w,  h,  null,  0,  w);
		image.flush();
	}
	
	public Image(int p[], int w, int h) {
		this.w = w;
		this.h = h;
		this.p = p;
	}
	
	public int getW() {
		return w;
	}
	public int getH() {
		return h;
	}
	public int[] getP() {
		return p;
	}
	public boolean isAlfa() {
		return alfa;
	}
	public void setAlfa(boolean alfa) {
		this.alfa = alfa;
	}
	public int getLightBlock() {
		return lightBlock;
	}
	public void setLightBlock(int lightBlock) {
		this.lightBlock = lightBlock;
	}
	

}

package no.fun.stuff.spaceship.gfx;

public class Light {
	public static final int NONE = 0;
	public static final int FULL = 1;
	
	private int radius;
	private int color;
	private int diameter;
	private int[] lightMap;
	
	public Light(int radius, int color) {
		this.radius = radius;
		this.diameter = radius * 2;
		this.color = color;
		this.lightMap = new int[diameter * diameter];
		for(int y=0;y<diameter;y++) {
			for(int x=0;x<diameter;x++) {
				int xPlussRadius = x-radius;
				int yPlussRadius = y-radius;
				double distanse = Math.sqrt(xPlussRadius * xPlussRadius + yPlussRadius * yPlussRadius);
				if(distanse < radius) {
					double power = 1 - (distanse / radius);
					lightMap[x + y * diameter] = ((int) (((color >> 16) & 0xff) * power) << 16 | (int) (((color >> 8) & 0xff) * power) << 8 | (int) ((color & 0xff) * power));;
				}else {
					lightMap[x + y * diameter] = 0;
				}
				
			}
		}
	}
	public int getLightValue(int x, int y ) {
		if(x < 0 || x >= diameter || y < 0 || y>= diameter) {
			return 0;
		}
		return lightMap[x +  y * diameter];
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public int[] getLightMap() {
		return lightMap;
	}

	public void setLightMap(int[] lightMap) {
		this.lightMap = lightMap;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
	

}

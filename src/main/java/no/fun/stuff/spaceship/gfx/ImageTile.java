package no.fun.stuff.spaceship.gfx;

public class ImageTile extends Image {

	private int tileW, tileH;
	public ImageTile(String path, int tileW, int tileH) {
		super(path);
		this.tileW = tileW;
		this.tileH = tileH;
	}
	public Image getTileImage(int tilex, int tiley) {
		int[] p = new int[tilex * tiley];
		for(int y=0;y<tileH;y++) {
			for(int x=0;x<tileW;x++) {
				p[x+y*tileW]=this.getP()[(x+tilex*tileW)+ (y + tiley *tileH)*this.getH()];
				
			}
		}
		return new Image(p, tileW, tileH);
	}
	public int getTileW() {
		return tileW;
	}
	public void setTileW(int tileW) {
		this.tileW = tileW;
	}
	public int getTileH() {
		return tileH;
	}
	public void setTileH(int tileH) {
		this.tileH = tileH;
	}
	

}

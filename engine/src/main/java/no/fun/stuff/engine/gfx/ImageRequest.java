package no.fun.stuff.engine.gfx;

public class ImageRequest {
	public Image image;
	public int zDepth, offx, offy;

	public ImageRequest(final Image image, int zDepth, int offx, int offy) {
		this.offx = offx;
		this.offy = offy;
		this.zDepth = zDepth;
		this.image = image;
	}
}

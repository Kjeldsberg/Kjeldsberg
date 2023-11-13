package no.fun.stuff.engine.game.texturemap;

import java.util.ArrayList;
import java.util.List;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Point;

public class Texturemap4Points {
	private Point[] ordered;
	private final Point[] uv = new Point[4];
	private final String[] text = new String[4];
	public final float THRESHOLD = 0.0001f;
	public final float ONE_MINUS_THRESHOLD = 1.0f - THRESHOLD;
	final List<NextSwitch> switches = new ArrayList<>();

	public Texturemap4Points() {
		ordered = new Point[4];
	}

	public void texturemap(final Texture2d face, final Image image, final Renderer r) {
		Point[] world = face.pos;
		int index = topPoint(world);
		for (int i = 0; i < world.length; i++) {
			int test = (index + i) % 4;
			ordered[i] = world[test];
			uv[i] = face.uv[test];
			text[i] = face.point[test];
		}

		// x = x1 + (y-y1)* ((x2-x1)/(y2-y1))
		Point p0 = ordered[0];
		Point p1 = ordered[1];
		Point p2 = ordered[2];
		Point p3 = ordered[3];

		final SideScan firstLeft = new SideScan(p3, p0, uv[3], uv[0]);
		final SideScan secondLeft = new SideScan(p2, p3, uv[2], uv[3]);
		final SideScan firstRight = new SideScan(p1, p0, uv[1], uv[0]);
		final SideScan secondRight = new SideScan(p2, p1, uv[2], uv[1]);


		if (firstLeft.flatLine || secondLeft.flatLine || firstRight.flatLine || secondRight.flatLine) {
			if (firstLeft.flatLine) {
				switches.add(new NextSwitch(secondLeft.getYStep(), secondLeft, firstRight));
			} else {
				switches.add(new NextSwitch(firstLeft.getYStep(), firstLeft, secondRight));
			}
		} else if (firstLeft.getYStep() > firstRight.getYStep()) {
			float y = firstRight.p0.getY();
			final SideScan startLeft = firstLeft.toY(y);
			switches.add(new NextSwitch(startLeft.getYStep(), startLeft, firstRight));

			final SideScan endLeft = firstLeft.fromPoint(startLeft);
			final SideScan startRight = secondRight.toY(firstLeft.p0.getY());
			switches.add(new NextSwitch(startRight.getYStep(), endLeft, startRight));

			final SideScan endRight = secondRight.fromPoint(startRight);
			switches.add(new NextSwitch(secondLeft.getYStep(), secondLeft, endRight));

		} else if (firstLeft.getYStep() < firstRight.getYStep()) {

			float yr = firstLeft.p0.getY();
			final SideScan startRight = firstRight.toY(yr);

			switches.add(new NextSwitch(startRight.getYStep(), firstLeft, startRight));

			final SideScan startLeft = secondLeft.toY(firstRight.p0.getY());
			final SideScan endRight = firstRight.fromPoint(startRight);
			switches.add(new NextSwitch(startLeft.getYStep(), startLeft, endRight));

			final SideScan endLeft = secondLeft.fromPoint(startLeft);
			switches.add(new NextSwitch(endLeft.getYStep(), endLeft, secondRight));

		} else {
			switches.add(new NextSwitch(firstLeft.getYStep(), firstLeft, firstRight));
			switches.add(new NextSwitch(secondLeft.getYStep(), secondLeft, secondRight));
		}

		for (final NextSwitch s : switches) {
			SideScan leftScan = s.left;
			SideScan rightScan = s.right;

			float dl = leftScan.dl;
			float dr = rightScan.dl;

			float xl = leftScan.p1.getX();
			float xr = rightScan.p1.getX();
			int y = (int) leftScan.p1.getY();
// textureing.
			float ul = leftScan.uv1.getX();
			float vl = leftScan.uv1.getY();
			float ur = rightScan.uv1.getX();
			float vr = rightScan.uv1.getY();
			float u = leftScan.uv1.getX();
			float v = leftScan.uv1.getY();
			int imageX, imageY;

			for (int step = 0; step < s.steps; y++, step++) {

				int rightx = (int) Math.floor(xr);
				int leftx = (int) Math.floor(xl);
				int xdiff = rightx - leftx;
				float xrMinusXl = (xdiff == 0) ? 0f : 1.0f / xdiff;
				float du = xrMinusXl < THRESHOLD ? THRESHOLD : (ur - ul) * xrMinusXl;
				float dv = xrMinusXl < THRESHOLD ? THRESHOLD : (vr - vl) * xrMinusXl;
				for (int x = leftx; x < rightx; x++) {
					if (u >= 1.0f)
						u = ONE_MINUS_THRESHOLD;
					if (u < THRESHOLD)
						u = THRESHOLD;

					if (v >= 1.0f)
						v = ONE_MINUS_THRESHOLD;
					if (v < THRESHOLD)
						v = THRESHOLD;
					imageX = (int) (u * image.getW());
					imageY = (int) (v * image.getH());
					int color = image.getP()[imageX + imageY * image.getW()];
					r.setPixel(x, y, color);
					u += du;
					v += dv;
				}
				ul += leftScan.uDelta;
				vl += leftScan.vDelta;
				ur += rightScan.uDelta;
				vr += rightScan.vDelta;
				u = ul;
				v = vl;
				xr += dr;
				xl += dl;
			}
		}
		switches.clear();
	}

	private int topPoint(Point[] points) {
		Point p = points[0];
		int ret = 0;
		for (int i = 1; i < points.length; i++) {
			boolean yen = points[i].getY() < p.getY();

			if (yen) {
				p = points[i];
				ret = i;
			}
		}
		return ret;
	}

	class NextSwitch {
		public SideScan left;
		public SideScan right;
		public int steps;

		public NextSwitch(final int steps, final SideScan left, final SideScan right) {
			this.left = left;
			this.right = right;
			this.steps = steps;
		}
	}

	private class SideScan {
		public final float dx, dy, dl;
		public float uDelta, vDelta;
		public Point p0, p1;
		public Point uv0, uv1;

		public boolean flatLine = false;

		public SideScan(final Point p0, final Point p1, final Point uv0, final Point uv1) {
			this.p0 = p0;
			this.p1 = p1;
			this.uv0 = uv0;
			this.uv1 = uv1;
			dx = p0.getX() - p1.getX();
			dy = p0.getY() - p1.getY();
			flatLine = dy < THRESHOLD;
			float oneOverDy = 1 / dy;
			dl = flatLine ? 0 : dx * oneOverDy;
			uDelta = flatLine ? 0 : (uv0.getX() - uv1.getX()) * oneOverDy;
			vDelta = flatLine ? 0 : (uv0.getY() - uv1.getY()) * oneOverDy;
		}
		
		public SideScan toY(final float y) {
			final Point interSectionPoint = new Point(this.xOfY(y), y);
			final Point interSectionUV = this.interpolateUV(interSectionPoint, this.p1);
			return new SideScan(interSectionPoint, this.p1, interSectionUV, this.uv1);
		}

		public SideScan fromPoint(final SideScan in) {
			return new SideScan(this.p0, in.p0, this.uv0, in.uv0);
		}

		private final Point retOfInterpolate = new Point();

		public Point interpolateUV(final Point p0, final Point p1) {
			float u = uv0.getX(), v = uv0.getY();
			float deltax = p0.getX() - p1.getX();
			float deltay = p0.getY() - p1.getY();
			float thisDeltax = this.p0.getX() - this.p1.getX();
			float thisDeltay = this.p0.getY() - this.p1.getY();
			float dx = Math.abs(thisDeltax) < THRESHOLD ? THRESHOLD : deltax / thisDeltax;
			float dy = Math.abs(thisDeltay) < THRESHOLD ? THRESHOLD : deltay / thisDeltay;
			float deltaU = uv0.getX() - uv1.getX();
			float deltaV = uv0.getY() - uv1.getY();

			if (Math.abs(deltaU) > THRESHOLD) {
				float theU = dx * deltaU;
				if (theU < 0.0f) {
					theU = 1 + theU;
				}
				u = Math.abs(theU);
			}
			if (Math.abs(deltaV) > THRESHOLD) {
				float theV = dy * deltaV;
				if (theV < 0.0f) {
					theV = 1 + theV;
				}
				v = Math.abs(theV);
			}
			retOfInterpolate.setX(u);
			retOfInterpolate.setY(v);
			return retOfInterpolate;
		}

		public int getYStep() {
			int top = (int) p0.getY();// Math.round(p0.getY());
			int bottom = (int) p1.getY();// Math.floor(p1.getY());
			return top - bottom;
//			return (int)Math.floor(p0.getY() - p1.getY()) + 1;
		}

		public float xOfY(float y) {
			// x = x1 + (y-y1)*((x2-x1)/(y2-y1))

			return p1.getX() + (y - p1.getY()) * (dl);
		}
	}

//	private void drawLines(final List<Point> p) {
//		final Matrix3x3 scale = new Matrix3x3();
//		final Matrix3x3 translate = new Matrix3x3();
//		translate.translate(new Point(15, 15));
//		scale.mul(translate);
//		scale.scale(60);
//		Point[] scaled = scale.mul(p.toArray(new Point[p.size()]));
//		int[] color = { 0x7fffffff, 0x7fff0000, 0x7f00ff00, 0x7f0000ff, 0x7fff00ff };
//		for (int j = 0, i = 0; i < p.size(); i += 2, j++) {
//			drawBresenhamLineInternal((int) scaled[i].getX(), (int) scaled[i].getY(), (int) scaled[i + 1].getX(),
//					(int) scaled[i + 1].getY(), color[j % 4]);
//		}
//
//	}

//	public void drawBresenhamLineInternal(int x0, int y0, int x1, int y1, int color) {
//		int dx = Math.abs(x1 - x0);
//		int dy = Math.abs(y1 - y0);
//		int sx = x0 < x1 ? 1 : -1;
//		int sy = y0 < y1 ? 1 : -1;
//		int err = dx - dy;
//		int e2;
//		while (true) {
//			int screenX = x0;
//			int screenY = y0;
//			r.setPixel(screenX, screenY, color);
//			if (x0 == x1 && y0 == y1) {
//				break;
//			}
//			e2 = 2 * err;
//			if (e2 > -1 * dy) {
//				err -= dy;
//				x0 += sx;
//			}
//			if (e2 < dx) {
//				err += dx;
//				y0 += sy;
//			}
//		}
////		return ret;
//	}
}

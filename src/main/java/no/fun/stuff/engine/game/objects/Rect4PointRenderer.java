package no.fun.stuff.engine.game.objects;

import java.util.ArrayList;
import java.util.List;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Point;

public class Rect4PointRenderer  {
	private Point[] ordered;
	public final float THRESHOLD = 0.0001f;
	final List<NextSwitch> switches = new ArrayList<>();
	private int color = 0xff444444;
	public Rect4PointRenderer() {
		super();
		ordered = new Point[4];
	}

	public void drawRect(final Point[] face, final Renderer r) {
		Point[] world = face;
		int index = topPoint(world);
		for (int i = 0; i < world.length; i++) {
			int test = (index + i) % 4;
			ordered[i] = world[test];
		}

		// x = x1 + (y-y1)* ((x2-x1)/(y2-y1))
		Point p0 = ordered[0];
		Point p1 = ordered[1];
		Point p2 = ordered[2];
		Point p3 = ordered[3];

		final SideScan firstLeft = new SideScan(p3, p0);
		final SideScan secondLeft = new SideScan(p2, p3);
		final SideScan firstRight = new SideScan(p1, p0);
		final SideScan secondRight = new SideScan(p2, p1);


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

			for (int step = 0; step < s.steps; y++, step++) {

				int rightx = (int) Math.floor(xr);
				int leftx = (int) Math.floor(xl);
				for (int x = leftx; x < rightx; x++) {
					r.setPixel(x, y, color);
				}
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
		public Point p0, p1;

		public boolean flatLine = false;

		public SideScan(final Point p0, final Point p1) {
			this.p0 = p0;
			this.p1 = p1;
			dx = p0.getX() - p1.getX();
			dy = p0.getY() - p1.getY();
			flatLine = dy < THRESHOLD;
			float oneOverDy = 1 / dy;
			dl = flatLine ? 0 : dx * oneOverDy;
		}
		
		public SideScan toY(final float y) {
			final Point interSectionPoint = new Point(this.xOfY(y), y);
			return new SideScan(interSectionPoint, this.p1);
		}

		public SideScan fromPoint(final SideScan in) {
			return new SideScan(this.p0, in.p0);
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
	public void setColor(int color) {
		this.color = color;
	}
}

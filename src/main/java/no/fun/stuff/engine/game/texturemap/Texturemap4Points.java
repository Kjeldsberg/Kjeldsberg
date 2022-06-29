package no.fun.stuff.engine.game.texturemap;

import java.util.ArrayList;
import java.util.List;


import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;
import no.fun.stuff.engine.matrix.Vector2D;

public class Texturemap4Points {
	private Point[] ordered;
	private final Renderer r;
	private final Point[] uv = new Point[4];
	private final String[] text = new String[4];
	public final float THRESHOLD = 0.0001f;
	public final float ONE_MINUS_THRESHOLD = 1.0f - THRESHOLD;
	public Texturemap4Points(final Renderer r) {
		this.r = r;
		ordered = new Point[4];
	}

	public void texturemap(final Face2d face, final Image image) {
		Point[] world = face.pos;
		int index = topPoint(world);
		for (int i = 0; i < world.length; i++) {
			int test = (index+i)%4;
			ordered[i] = world[test]; 
			uv[i] = face.uv[test];
			text[i] = face.point[test];
		}
		
		//x = x1 + (y-y1)* ((x2-x1)/(y2-y1))
		Point p0 = ordered[0];
		Point p1 = ordered[1];
		Point p2 = ordered[2];
		Point p3 = ordered[3];
		List<SideScan> left = new ArrayList<>();
		List<SideScan> right = new ArrayList<>();
		
		final SideScan firstLeft = new SideScan(p3, p0, uv[3], uv[0]);
		final SideScan secondLeft = new SideScan(p2, p3, uv[2], uv[3]);
		final SideScan firstRight = new SideScan(p1, p0, uv[1], uv[0]);
		final SideScan secondRight = new SideScan(p2, p1, uv[2], uv[1]);
		
//		if(!firstLeft.aFlatLine()) {
//			left.add(firstLeft);
//		}
//		if(!secondLeft.aFlatLine()) {
//			left.add(secondLeft);
//		}
//		if(!firstRight.aFlatLine()) {
//			right.add(firstRight);
//		}
//		if(!secondRight.aFlatLine()) {
//			right.add(secondRight);
//		}
		List<NextSwitch> switches = new ArrayList<>();
//		if(left.size() == right.size() && left.size() == 1) {
//			final SideScan sideScan = left.get(0);
//			final SideScan sideScan2 = right.get(0);
//			switches.add(new NextSwitch(sideScan.getYStep(), sideScan, sideScan2));
//		} else {
			
			if(firstLeft.p0.getY() > firstRight.p0.getY()) {
				float y = firstRight.p0.getY();
				final SideScan startLeft  = firstLeft.toY(y);
				switches.add(new NextSwitch(startLeft.getYStep(),  startLeft, firstRight));
				
				final SideScan endLeft = firstLeft.fromPoint(startLeft);
				final SideScan startRight = secondRight.toY(firstLeft.p0.getY());
				switches.add(new NextSwitch(startRight.getYStep(), endLeft, startRight));
				
				final SideScan endRight = secondRight.fromPoint(startRight);
				switches.add(new NextSwitch(secondLeft.getYStep(), secondLeft, endRight));
				NextSwitch s = switches.get(0);
					
			} else if(firstLeft.p0.getY() < firstRight.p0.getY()) {
				
				float yr = firstLeft.p0.getY();//p3.getY();
				final SideScan startRight = firstRight.toY(yr);
				
				switches.add(new NextSwitch(startRight.getYStep(), firstLeft, startRight));
				
				final SideScan startLeft = secondLeft.toY(firstRight.p0.getY());
				final SideScan endRight = firstRight.fromPoint(startRight);
				switches.add(new NextSwitch(startLeft.getYStep(), startLeft, endRight));
				
				final SideScan endLeft = secondLeft.fromPoint(startLeft);
				switches.add(new NextSwitch(endLeft.getYStep(), endLeft, secondRight));
				
			} 
//			else {
//				switches.add(new NextSwitch(firstLeft.getYStep(), firstLeft, firstRight));
//				switches.add(new NextSwitch(secondLeft.getYStep(), secondLeft, secondRight));
//			}
//		}		

		for(final NextSwitch s : switches) {
			SideScan leftScan = s.left; 
			SideScan rightScan = s.right; 
			
			float dl = leftScan.dl;
			float dr = rightScan.dl;
			 
			float xl = leftScan.p1.getX();
			float xr = rightScan.p1.getX();
			int y = (int)leftScan.p1.getY();
// textureing.
			float ul = leftScan.uv1.getX();
			float vl = leftScan.uv1.getY();
			float ur = rightScan.uv1.getX();
			float vr = rightScan.uv1.getY();
			float u = leftScan.uv1.getX();
			float v = leftScan.uv1.getY();
			int imageX, imageY;
			
			for(int step = 0; step<s.steps; y++, step++) {
				
				int rightx = (int)xr; //Math.floor(xr);
				int leftx = (int)xl; //Math.floor(xl);
				int xdiff = rightx - leftx;
				float xrMinusXl = (rightx == leftx) ? 1.0f : 1.0f/(xdiff);
				float du = xrMinusXl < THRESHOLD ? THRESHOLD : (ur - ul)*xrMinusXl;
				float dv = xrMinusXl < THRESHOLD ? THRESHOLD : (vr - vl)*xrMinusXl;
				for(int x = leftx; x <= rightx; x++) {
					if(u >= 1.0f)
						u = ONE_MINUS_THRESHOLD;
					if(u < THRESHOLD) 
						u = THRESHOLD;
					
					if(v >= 1.0f)
						v = ONE_MINUS_THRESHOLD;
					if(v < THRESHOLD) v = THRESHOLD;
					imageX = (int)(u * image.getW());
					imageY = (int) (v * image.getH());
					int color = image.getP()[imageX + imageY*image.getW()];
					r.setPixel(x,  y, color);
//					r.setPixel(x,  y, 0x3fff0000);
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
	}
	private void drawLines(final List<Point> p) {
		final Matrix3x3 scale = new Matrix3x3();
		final Matrix3x3 translate = new Matrix3x3();
		translate.translate(new Point(15,15));
		scale.mul(translate);
		scale.scale(60);
		Point[] scaled = scale.mul(p.toArray(new Point[p.size()]));
		int[] color = {0x7fffffff, 0x7fff0000, 0x7f00ff00, 0x7f0000ff, 0x7fff00ff};
		for(int j = 0, i = 0; i< p.size();i+=2, j++) {
			drawBresenhamLineInternal((int)scaled[i].getX(), (int)scaled[i].getY(), (int)scaled[i+1].getX(), (int)scaled[i+1].getY(), color[j%4]);
		}

	}
	public  void drawBresenhamLineInternal(int x0, int y0, int x1, int y1, int color) {
//		final List<Point> ret = new ArrayList<>();
		int dx = Math.abs(x1-x0);
		int dy = Math.abs(y1 -y0);
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		int err = dx -dy;
		int e2;
		while(true) {
			int screenX = x0;
			int screenY = y0;
//			setLightMap(screenX,  screenY,  lightColor);
//			ret.add(new Point(screenX, screenY));
			r.setPixel(screenX, screenY, color);
			if(x0 == x1 && y0 == y1) {
				break;
			}
			e2 = 2 *err;
			if(e2 > -1 * dy) {
				err -= dy;
				x0 += sx;
			}
			if(e2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
//		return ret;
	}

	private int topPoint(Point[] points) {
		Point p = points[0];
		int ret = 0;
		for(int i=1;i<points.length;i++) {
			boolean yen = points[i].getY() < p.getY();
			
			if(yen) {
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

	class SideScan {
		public final float dx, dy, dl;
		public float uDelta, vDelta;
		public Point p0,p1;
		public Point uv0, uv1;
		public Point switchPoint;
		public boolean negativeU = false;
		public boolean negativeV = false;

		public boolean flatLine = false;
		public SideScan(final Point p0, final Point p1) {
			dx = p0.getX() - p1.getX();
			dy = p0.getY() - p1.getY();
			flatLine = dy < 0.001;
			dl = flatLine ? 0 : (dx*(1/dy));
			this.p0 = p0;
			this.p1 = p1;
		}
		public SideScan(final SideScan o) {
			this.dl = o.dl;
			this.dx = o.dx;
			this.dy = o.dy;
			this.uDelta = o.uDelta;
			this.vDelta = o.vDelta;
			this.p1 = new Point(o.p1);
			this.p0 = new Point(o.p0);
			this.uv0 = new Point(o.uv0);
			this.uv1 = new Point(o.uv1);
		}
		public SideScan(final Point p0, final Point p1, final Point uv0, final Point uv1) {
			this.p0 = p0;
			this.p1 = p1;
			this.uv0 = uv0;
			this.uv1 = uv1;
			dx = p0.getX() - p1.getX();
			dy = p0.getY() - p1.getY();
			flatLine = dy < THRESHOLD;
			float oneOverDy = 1/dy;
			dl = flatLine ? 0 : dx*oneOverDy;
			uDelta = flatLine ? 0 : (uv0.getX() - uv1.getX())*oneOverDy;
			vDelta = flatLine ? 0 : (uv0.getY() - uv1.getY())*oneOverDy;
		}
		public SideScan toY(final float y) {
			final Point interSectionPoint = new Point(this.xOfY(y), y);
			final Point interSectionUV = this.interpolateUV(interSectionPoint, this.p1);
			final SideScan test = new SideScan(interSectionPoint, this.p1, interSectionUV, this.uv1);
			return test;
		}
		public SideScan fromPoint(final Point p) {
			final float y = (float) Math.floor(p.getY() + dl);
			final Point temp = new Point(p);
			temp.setY(y);
			final Point interSectionUV = this.interpolateUV(this.p0, temp);
			final SideScan scanside = new SideScan(this.p0, temp, this.uv0, interSectionUV);
			return scanside;
		}
		public SideScan fromPoint(final SideScan in) {
			return new SideScan(this.p0, in.p0, this.uv0, in.uv0);
		}
		
		public Point interpolateUV(final Point p0, final Point p1) {
			float u = uv0.getX(),v = uv0.getY();
			float deltax = p0.getX() - p1.getX();
			float deltay = p0.getY() - p1.getY();
			float thisDeltax = this.p0.getX() - this.p1.getX();
			float thisDeltay = this.p0.getY() - this.p1.getY();
			float dx = Math.abs(thisDeltax) < THRESHOLD ? THRESHOLD : deltax/thisDeltax;//*(1.0f/thisDeltax);
			float dy = Math.abs(thisDeltay) < THRESHOLD ? THRESHOLD : deltay/thisDeltay;//*(1.0f/thisDeltay);
			float deltaU = uv0.getX() - uv1.getX();
			float deltaV = uv0.getY() - uv1.getY();
			
			if(Math.abs(deltaU) > THRESHOLD) {
				float theU = dx*deltaU;
				negativeU = theU < 0.0f;
				if(negativeU) {
					theU = 1+theU;
				}
				u = Math.abs(theU);
			}
			if(Math.abs(deltaV) > THRESHOLD) {
				float theV = dy*deltaV;
				negativeV = theV < 0.0f;
				if(negativeV) {
					theV = 1+theV;
				}
				v = Math.abs(theV);
			}
			return new Point(u, v);
		}
		
		public int getYStep() {
			int top = (int)Math.floor(p0.getY());
			int bottom = (int)Math.floor(p1.getY());
			return top - bottom;
//			return (int)Math.floor(p0.getY() - p1.getY()) + 1;
		}
		public boolean aFlatLine() {
			return dy < THRESHOLD;
		}
		public float xOfY(float y) {
			// x = x1 + (y-y1)*((x2-x1)/(y2-y1))
			
			return p1.getX() + (y - p1.getY()) * (dl);
		}
	}
}

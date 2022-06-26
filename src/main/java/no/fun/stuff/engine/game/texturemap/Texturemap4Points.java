package no.fun.stuff.engine.game.texturemap;

import java.util.ArrayList;
import java.util.List;


import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Point;

public class Texturemap4Points {
	private Image texture;
	private Point[] ordered;
	private final Renderer r;
	private final Point[] uv = new Point[4];
	private final String[] text = new String[4];
	public final float THRESHOLD = 0.0001f;
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
		
		if(!firstLeft.aFlatLine()) {
			left.add(firstLeft);
		}
		if(!secondLeft.aFlatLine()) {
			left.add(secondLeft);
		}
		if(!firstRight.aFlatLine()) {
			right.add(firstRight);
		}
		if(!secondRight.aFlatLine()) {
			right.add(secondRight);
		}
		List<NextSwitch> switches = new ArrayList<>();
		if(left.size() == right.size() && left.size() == 1) {
			final SideScan sideScan = left.get(0);
			final SideScan sideScan2 = right.get(0);
			switches.add(new NextSwitch(sideScan.getYStep(), sideScan, sideScan2));
		} else {
			
			if(firstLeft.getYStep() > firstRight.getYStep()) {
				float y = firstRight.p0.getY();
				final SideScan startLeft  = firstLeft.toY(y);
				switches.add(new NextSwitch(startLeft.getYStep(),  startLeft, firstRight));
				
				final SideScan endLeft = firstLeft.fromPoint(startLeft.p0);
				endLeft.uv1 = new Point(startLeft.uv0);
				final SideScan startRight = secondRight.toY(firstLeft.p0.getY());
				switches.add(new NextSwitch(startRight.getYStep(), endLeft, startRight));
				
				final SideScan endRight = secondRight.fromPoint(startRight.p0);
				endRight.uv1 = new Point(startRight.uv0);
				switches.add(new NextSwitch(secondLeft.getYStep(), secondLeft, endRight));

//				Point pl1Point = new Point(firstLeft.xOfY(y),  y);
//				SideScan pl1 = new SideScan(pl1Point, firstLeft.p1, firstLeft.uv0, firstLeft.uv1);
////				pl1.uv0 = firstLeft.interpolateUV(pl1);
////				pl1.uDelta = firstLeft.uDelta;
////				pl1.vDelta = firstLeft.vDelta;
//				switches.add(new NextSwitch(pl1.getYStep(),  pl1, firstRight));
//				
//				SideScan nextLeft = new SideScan(firstLeft.p0, pl1Point, firstLeft.uv0, firstLeft.uv1/*pl1.uv0*/);
////				nextLeft.uDelta = firstLeft.uDelta;
////				nextLeft.vDelta = firstLeft.vDelta;
//				float righty = firstLeft.p0.getY();
//				Point pr1 = new Point(secondRight.xOfY(righty), righty);
//				SideScan nextRight = new SideScan(pr1, firstRight.p0, secondRight.uv0, secondRight.uv1);
////				nextRight.uv0 = secondRight.interpolateUV(nextRight);
////				nextRight.uDelta = secondRight.uDelta;
////				nextRight.vDelta = secondRight.vDelta;
//				switches.add(new NextSwitch(nextRight.getYStep(), nextLeft, nextRight));
////				System.out.println("left > right: " + pl1.uv0 + "\t" + pl1.uv1 + "\tindex: " + index);
//				
//				SideScan lastRight = new SideScan(secondRight.p0, pr1, secondRight.uv0, secondRight.uv1/*nextRight.uv0*/);
////				lastRight.uDelta = secondRight.uDelta;
////				lastRight.vDelta = secondRight.vDelta;
//				switches.add(new NextSwitch(secondLeft.getYStep(), secondLeft, lastRight));
				NextSwitch s = switches.get(0);
				List<Point> pLeft = new ArrayList<>();
				pLeft.add(s.left.uv0);
				pLeft.add(s.left.uv1);
				NextSwitch s1 = switches.get(1);
				pLeft.add(s1.left.uv0);
				pLeft.add(s1.left.uv1);
				NextSwitch s2 = switches.get(2);
				pLeft.add(s2.left.uv0);
				pLeft.add(s2.left.uv1);
				
				drawLines(pLeft);
				List<Point> pRightt = new ArrayList<>();
				pRightt.add(s.right.uv0);
				pRightt.add(s.right.uv1);
				pRightt.add(s1.right.uv0);
				pRightt.add(s1.right.uv1);
				pRightt.add(s2.right.uv0);
				pRightt.add(s2.right.uv1);
				drawLines(pRightt);
					
			} else if(firstLeft.getYStep() < firstRight.getYStep()) {
				
				float yr = firstLeft.p0.getY();//p3.getY();
				final SideScan startRight = firstRight.toY(yr);
				
				switches.add(new NextSwitch(startRight.getYStep(), firstLeft, startRight));
				
				final SideScan startLeft = secondLeft.toY(firstRight.p0.getY());
				final SideScan endRight = firstRight.fromPoint(startRight.p0);
				endRight.uv1 = new Point(startRight.uv0);
				switches.add(new NextSwitch(startLeft.getYStep(), startLeft, endRight));
				
				final SideScan endLeft = secondLeft.fromPoint(startLeft.p0);
				endLeft.uv1 = new Point(startLeft.uv0);
				switches.add(new NextSwitch(endLeft.getYStep(), endLeft, secondRight));
				
//				float xr = firstRight.xOfY(yr);
//				Point pr = new Point(xr, yr);
//				SideScan prr = new SideScan(pr, firstRight.p1, firstRight.uv0, firstRight.uv1);
//				prr.uv0 = firstRight.interpolateUV(prr);
//				prr.uDelta = firstRight.uDelta;
//				prr.vDelta = firstRight.vDelta;
//				switches.add(new NextSwitch(prr.getYStep(), firstLeft, prr));
//				
//				float yl = firstRight.p0.getY();
//				float xl = secondLeft.xOfY(yl);
//				Point pll = new Point(xl, yl);
//				SideScan plll = new SideScan(pll, secondLeft.p1, secondLeft.uv0, secondLeft.uv1);
//				plll.uv0 = secondLeft.interpolateUV(plll);
//				plll.uDelta = secondLeft.uDelta;
//				plll.vDelta = secondLeft.vDelta;
//				
//				SideScan prr2 = new SideScan(firstRight.p0, prr.p0, firstRight.uv0, prr.uv0);
//				prr2.uDelta = firstRight.uDelta;
//				prr2.vDelta = firstRight.vDelta;
//				switches.add(new NextSwitch(plll.getYStep(), plll, prr2));
//				
//				SideScan lastLeft = new SideScan(secondLeft.p0, pll, secondLeft.uv0, plll.uv0);
//				lastLeft.uDelta = secondLeft.uDelta;
//				lastLeft.vDelta = secondLeft.vDelta;
//				switches.add(new NextSwitch(lastLeft.getYStep(), lastLeft, secondRight));
//				final NextSwitch s = switches.get(0);	
////				System.out.println("left < right: " + s.right.uv0 + "\t" + s.right.uv1  );
				List<Point> p = new ArrayList<>();
				final NextSwitch first = switches.get(0);
				p.add(first.left.uv0);
				p.add(first.left.uv1);
				p.add(first.right.uv0);
				p.add(first.right.uv1);
				final NextSwitch second = switches.get(1);
				p.add(second.left.uv0);
				p.add(second.left.uv1);
				p.add(second.right.uv0);
				p.add(second.right.uv1);
				final NextSwitch third = switches.get(2);
				p.add(third.left.uv0);
				p.add(third.left.uv1);
				p.add(third.right.uv0);
				p.add(third.right.uv1);
				drawLines(p);

			} else {
				switches.add(new NextSwitch(firstLeft.getYStep(), firstLeft, firstRight));
				switches.add(new NextSwitch(secondLeft.getYStep(), secondLeft, secondRight));
			}
		}		

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
			
			for(int step = 0; step<s.steps; y++, step++) {
				
//				System.out.println("yen: " + y);
				int rightx = ((int)xr) + 1;
				int leftx = ((int)xl);
				float xrMinusXl = 1f/(rightx - leftx);
				float vrMinusUr = (vr - vl);
//				if(vr == 1.0 && ul == 1.0) {
//					vrMinusUr = 1.0f;
//				}
				float du = xrMinusXl < THRESHOLD ? THRESHOLD : (ur - ul)*xrMinusXl;
				float dv = xrMinusXl < THRESHOLD ? THRESHOLD : (vrMinusUr)*xrMinusXl;
//				System.out.println("du: " + du + "\tdv: " +dv);
				int imageX, imageY;
				imageX = imageY = 0;
				for(int x = leftx; x <= rightx; x++) {
					if(u > 1.0f)
						u = 1.0f;
					if(u < 0.0f) u = 0.0f;
					
					if(v >1.0f)
						v = 1.0f;
					if(v <0.0f) v = 0.0f;
					imageX = (int)(u * image.getW());
					imageY = (int) (v * image.getH());
					if(imageY > 23) imageY = 23;
					if(imageX > 23) imageX = 23;
//					System.out.println("imagex: " + imageX + "\timagey: " + imageY);
					int color = image.getP()[imageX + imageY*image.getW()];
					int tempColor = 0x30ff0000;
					r.setPixel(x,  y, color);
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
		for(int i=0;i<text.length;i++) {
			Point p = ordered[i];
			r.drawText(text[i], (int)p.getX(), (int)p.getY(), 0xffffffff);
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

	private SideScan findSide(final List<SideScan> side, float y) {
		if(side.size() == 1) {
			return side.get(0);
		}else if(side.size() > 1) {
			for(final SideScan s : side) {
				if(y <= s.p0.getY() && y >= s.p1.getY()) {
					return s;
				}
			}
		}
		return null;
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
	class TextureScan {
		private final Point uv0, uv1;
		public TextureScan(final Point uv0, Point uv1) {
			this.uv0 = uv0;
			this.uv1 = uv1;
			
		}
	}
	class SideScan {
		public final float dx, dy, dl;
		public float uDelta, vDelta;
		public Point p0,p1;
		public Point uv0, uv1;
		public Point switchPoint;

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
			flatLine = dy < 0.0001f;
			float oneOverDy = 1/dy;
			dl = flatLine ? 0 : dx*oneOverDy;
			uDelta = flatLine ? 0 : (uv0.getX() - uv1.getX())*oneOverDy;
			vDelta = flatLine ? 0 : (uv0.getY() - uv1.getY())*oneOverDy;
		}
		public SideScan toY(final float y) {
			final SideScan ret = new SideScan(this);
			final Point interSectionPoint = new Point(this.xOfY(y), y);
			final Point interSectionUV = this.interpolateUV(interSectionPoint, this.p1);
			ret.p0 = interSectionPoint;
			ret.uv0 = interSectionUV;
			return ret;
		}
		public SideScan fromPoint(final Point p) {
			final SideScan ret = new SideScan(this);
			final float y = p.getY();// + dl;
			final Point interSectionPoint = new Point(this.xOfY(y), y);
			final Point interSectionUV = this.interpolateUV(this.p0, interSectionPoint);
			ret.p1 = interSectionPoint;
			ret.uv1 = interSectionUV;
			return ret;
		}
		
		public Point interpolateUV(final Point p0, final Point p1) {
			float u = uv0.getX(),v = uv0.getY();
			float deltax = p0.getX() - p1.getX();
			float deltay = p0.getY() - p1.getY();
			float thisDeltax = this.p0.getX() - this.p1.getX();
			float thisDeltay = this.p0.getY() - this.p1.getY();
			float dx = Math.abs(thisDeltax) < THRESHOLD ? THRESHOLD : deltax/(/*1/*/thisDeltax);
			float dy = Math.abs(thisDeltay) < THRESHOLD ? THRESHOLD : deltay*(1/thisDeltay);
			float deltaU = uv0.getX() - uv1.getX();
			float deltaV = uv0.getY() - uv1.getY();
			
			if(Math.abs(deltaU) > THRESHOLD) {
				u = Math.abs(dx*deltaU);
			}
			if(Math.abs(deltaV) > THRESHOLD) {
				v = Math.abs(dy*deltaV);
			}
			return new Point(u, v);
		}
		public Point interpolateUV(final SideScan other) {
			float u = uv0.getX(),v = uv0.getY();
			float deltax = other.p0.getX() - other.p1.getX();
			float deltay = other.p0.getY() - other.p1.getY();
			float thisDeltax = p0.getX() - p1.getX();
			float thisDeltay = p0.getY() - p1.getY();
			float dx = Math.abs(thisDeltax) < 0.001 ? 0 : deltax/(/*1/*/thisDeltax);
			float dy = Math.abs(thisDeltay) < 0.001 ? 0 : deltay*(1/thisDeltay);
			float deltaU = Math.abs(uv0.getX() - uv1.getX());
			float deltaV = Math.abs(uv0.getY() - uv1.getY());
			
			if(deltaU > THRESHOLD) {
				u = Math.abs(dx*deltaU);
			}
			if(deltaV > THRESHOLD) {
				v = Math.abs(dy*deltaV);
			}
//			float thisDeltau = (uv0.getX() == uv1.getX()) ? uv0.getX() : uv0.getX() - uv1.getX(); 
//			float thisDeltav = (uv0.getY() == uv1.getY()) ? uv0.getY() : uv0.getY() - uv1.getY(); 
//			float thisDeltau = uv0.getX() - uv1.getX();
//			float thisDeltav = uv0.getY() - uv1.getY();
		
			if(Math.abs(dy - dl)< THRESHOLD) {
				int test = 0;
			}
//			u = Math.abs(thisDeltau*dx);
//			v = Math.abs(thisDeltav*dy);
			return new Point(u, v);
		}
		
		public int getYStep() {
			int top = (int)Math.floor(p0.getY());
			int bottom = (int)Math.floor(p1.getY());
			return top - bottom;
//			return (int)Math.floor(p0.getY() - p1.getY()) + 1;
		}
		public boolean aFlatLine() {
			return dy < 0.001;
		}
		public float xOfY(float y) {
			// x = x1 + (y-y1)*((x2-x1)/(y2-y1))
			
			return p1.getX() + (y - p1.getY()) * (dl);
		}
	}
}

package no.fun.stuff.engine.matrix;

public class Matrix3x3 {
	private float RAD = (float) (180.0/Math.PI);
	private float[][] m = {	{1, 0, 0},
							{0, 1, 0},
							{0,0,1}};
	
	private Point translate = new Point();
	private float[][] identity = {	{1, 0, 0},
									{0, 1, 0},
									{0,0,1}};
	public void rotate(float radians) {
//		clear();
		final float rotate[][] = { 	{(float)Math.cos(radians), -(float)Math.sin(radians), 0f},
									{(float)Math.sin(radians), (float)Math.cos(radians), 0f},
									{0f, 0f, 1}};
//		this.rotate(radians);
		this.mul(rotate);
		int i=0;
		
	}
	public void translate(final Point p) {
		float[][] i = {	{1, 0, 0},
				{0, 1, 0},
				{0,0,1}};
		translate = p;
		m[0][2] = p.getX();
		m[1][2] = p.getY();
//		mul(i);
		
	}
	public void clear() {
		for(int i=0;i<3;i++) {
			for(int j = 0; j<3;j++) {
				for(int k=0;k<3;k++) {
					m[i][j] = 0f; 
				}
			}
		}
		m[0][0] = 1.0f;
		m[1][1] = 1.0f;
		m[2][2] = 1.0f;

	}
	public void scale(float scale) {
		float[][] i = {	{scale, 0, 0},
						{0, scale, 0},
							{0,0,1}};
		mul(i);
		int id=0;
		
	}
	public Point[] mul(final Point[] points) {
		Point[] ret = new Point[points.length];
		
		for(int i=0;i<points.length;i++) {
			final Point p = points[i];
			ret[i] = new Point(p.getX() *  m[0][0] + p.getY() * m[0][1] + m[0][2],
					p.getX() *  m[1][0] + p.getY() * m[1][1] + m[1][2]);
		}
		return ret;
	}
	public Point mul(final Point points) {
			final Point p = points;
			return new Point(p.getX() *  m[0][0] + p.getY() * m[1][0] + m[2][0],
					p.getX() *  m[0][1] + p.getY() * m[1][1] + m[1][2]);
	}

	public Matrix3x3 mul(final Matrix3x3 matrix) {
		float[][] u = matrix.m;
		float[][] tmp = new float[3][3];
		for(int i=0;i<3;i++) {
			for(int j = 0; j<3;j++) {
				for(int k=0;k<3;k++) {
					tmp[i][j] += m[i][k]*u[k][j]; 
				}
			}
		}

//		tmp[0][0] = m[0][0]*u[0][0] + m[0][1]*u[1][0] + m[0][2]*u[2][0];
//		tmp[0][1] = m[0][0]*u[0][1] + m[0][1]*u[1][1] + m[0][2]*u[2][1];
//		tmp[0][2] = m[0][0]*u[0][2] + m[0][1]*u[1][2] + m[0][2]*u[2][2];
//		
//		tmp[1][0] = m[1][0]*u[0][0] + m[1][1]*u[1][0] + m[1][2]*u[2][0];
//		tmp[1][1] = m[1][0]*u[0][1] + m[1][1]*u[1][1] + m[1][2]*u[2][1];
//		tmp[1][2] = m[1][0]*u[0][2] + m[1][1]*u[1][2] + m[1][2]*u[2][2];
//
//		tmp[2][0] = m[2][0]*u[0][0] + m[2][1]*u[1][0] + m[2][2]*u[2][0];
//		tmp[2][1] = m[2][0]*u[0][1] + m[2][1]*u[1][1] + m[2][2]*u[2][1];
//		tmp[2][2] = m[2][0]*u[0][2] + m[2][1]*u[1][2] + m[2][2]*u[2][2];
		m = tmp;
		return this;
	}
	
	public Matrix3x3 mul(final float u[][]) {
		float[][] tmp = new float[3][3];
		for(int i=0;i<3;i++) {
			for(int j = 0; j<3;j++) {
				for(int k=0;k<3;k++) {
					tmp[i][j] += m[i][k]*u[k][j]; 
				}
			}
		}
//		tmp[0][0] = m[0][0]*u[0][0] + m[0][1]*u[1][0] + m[0][2]*u[2][0];
//		tmp[0][1] = m[0][0]*u[0][1] + m[0][1]*u[1][1] + m[0][2]*u[2][1];
//		tmp[0][2] = m[0][0]*u[0][2] + m[0][1]*u[1][2] + m[0][2]*u[2][2];
//		
//		tmp[1][0] = m[1][0]*u[0][0] + m[1][1]*u[1][0] + m[1][2]*u[2][0];
//		tmp[1][1] = m[1][0]*u[0][1] + m[1][1]*u[1][1] + m[1][2]*u[2][1];
//		tmp[1][2] = m[1][0]*u[0][2] + m[1][1]*u[1][2] + m[1][2]*u[2][2];
//
//		tmp[2][0] = m[2][0]*u[0][0] + m[2][1]*u[1][0] + m[2][2]*u[2][0];
//		tmp[2][1] = m[2][0]*u[0][1] + m[2][1]*u[1][1] + m[2][2]*u[2][1];
//		tmp[2][2] = m[2][0]*u[0][2] + m[2][1]*u[1][2] + m[2][2]*u[2][2];
		m = tmp;
		return this;
	}
}

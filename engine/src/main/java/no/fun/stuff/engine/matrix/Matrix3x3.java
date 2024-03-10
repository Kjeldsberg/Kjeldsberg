package no.fun.stuff.engine.matrix;


public class Matrix3x3 {
	private float RAD = (float) (180.0/Math.PI);
	private float[][] m = {	{0, 0, 0},
							{0, 0, 0},
							{0, 0, 0}};
	private float[][] tmp = {	{0, 0, 0},
							{0, 0, 0},
							{0, 0, 0}};

	private Point translate = new Point();
	private float[][] identity = {	{1, 0, 0},
									{0, 1, 0},
									{0,0,1}};
	
	public Matrix3x3() {
		setIdentity();
	}
	
	public Matrix3x3(final Matrix3x3 m) {
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				this.m[i][j] = m.m[i][j];
			}
		}
	}
	
	public void setIdentity() {
		clear();
		m[0][0] = 1.0f;
		m[1][1] = 1.0f;
		m[2][2] = 1.0f;

	}

	public void setUpVector(final Vector2D up) { 	
		final Vector2D perp = new Vector2D(up.getY(), -up.getX());
		float[][] m = {	{perp.getX(), up.getX(), 0},
						{perp.getY(), up.getY(), 0},
						{0, 0, 1}};
		set(m);
	}
	
	public Matrix3x3 invertTranslateCopy() {
		Matrix3x3 ret = new Matrix3x3(this);
		ret.m[0][2] = -ret.m[0][2]; 
		ret.m[1][2] = -ret.m[1][2]; 
		return ret;
	}
	
	public void invertTranslate() {
		m[0][2] = -m[0][2]; 
		m[1][2] = -m[1][2]; 
	}
	
	public void set(final float[][] mat) {
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				m[i][j] = mat[i][j];
			}
		}
	}
	public void set(final Matrix3x3 mat) {
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				m[i][j] = mat.m[i][j];
			}
		}
	}

	public void transpose(final Matrix3x3 u) {
		float[][] m = {	{u.m[0][0], u.m[1][0], 0},
						{u.m[0][1 ], u.m[1][1], 0},
						{0, 0, 1}};
		set(m);
	}
	
	public void invert(final Matrix3x3 u) {
		float[][] m = {	{u.m[0][0], -u.m[0][1], 0},
						{-u.m[1][0], u.m[1][1], 0},
						{0, 0, 1}};
		
	}
	public void rotate(float radians) {
//		clear();
//		Math.toRadians(radians)
		final float rotate[][] = { 	{(float)Math.cos(radians), -(float)Math.sin(radians), 0f},
									{(float)Math.sin(radians), (float)Math.cos(radians), 0f},
									{0f, 0f, 1}};
//		this.rotate(radians);
		this.mul(rotate);
		int i=0;
		
	}
	public void translate(final Point p) {
		setIdentity();
		translate = p;
		m[0][2] = p.getX();
		m[1][2] = p.getY();
//		mul(i);
		
	}
	public void translate(final Vector2D p) {
		setIdentity();
		translate = p.toPoint();
		m[0][2] = p.getX();
		m[1][2] = p.getY();
	}
	public Vector2D getMotionVector() {
		return new Vector2D(m[0][2], m[1][2]);
	}
	
	public void clear() {
		for(int i=0;i<3;i++) {
			for(int j = 0; j<3;j++) {
					m[i][j] = 0f; 
			}
		}
	}
	public void scale(float scale) {
		setIdentity();
		m[0][0] = scale;
		m[1][1] = scale;
		m[2][2] = 1.0f;
	}

	public void invertScale() {
		m[0][0] = 1.0f/m[0][0];
		m[1][1] = 1.0f/m[1][1];
		m[2][2] = 1.0f;
	}
	
	public Matrix3x3 invertScaleCopy() {
		Matrix3x3 ret = new Matrix3x3(this);
		ret.m[0][0] = 1.0f/m[0][0];
		ret.m[1][1] = 1.0f/m[1][1];
		ret.m[2][2] = 1.0f;
		return ret;
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
			return new Point(p.getX() *  m[0][0] + p.getY() * m[0][1] + m[0][2],
					p.getX() *  m[1][0] + p.getY() * m[1][1] + m[1][2]);
	}
	
	public Vector2D mul(final Vector2D p) {
		return new Vector2D(p.getX() *  m[0][0] + p.getY() * m[0][1] + m[0][2],
				p.getX() *  m[1][0] + p.getY() * m[1][1] + m[1][2]);
	}

	public Point[] mul(final Point[] points, Point[] ret) {
		
		for(int i=0;i<points.length;i++) {
			final Point p = points[i];
			ret[i] = new Point(p.getX() *  m[0][0] + p.getY() * m[0][1] + m[0][2],
					p.getX() *  m[1][0] + p.getY() * m[1][1] + m[1][2]);
		}
		return ret;
	}
	public Vector2D[] mul(final Vector2D[] localSpace, final Vector2D[] ret) {

		for(int i=0;i<localSpace.length;i++) {
			final Vector2D p = localSpace[i];
			final Vector2D r = ret[i];
			r.setX(p.getX() *  m[0][0] + p.getY() * m[0][1] + m[0][2]);
			r.setY(p.getX() *  m[1][0] + p.getY() * m[1][1] + m[1][2]);
		}
		return ret;
	}

	public Matrix3x3 mulCopy(final Matrix3x3 matrix) {
		float[][] u = matrix.m;
		Matrix3x3 ret = new Matrix3x3();
		ret.clear();
		for(int i=0;i<3;i++) {
			for(int j = 0; j<3;j++) {
				for(int k=0;k<3;k++) {
					ret.m[i][j] += m[i][k]*u[k][j]; 
				}
			}
		}
		return ret;
	}
	
	public void mul(final float u[][]) {
		float[][] tmp = new float[3][3];
//		clearTmp();
		for(int i=0;i<3;i++) {
			for(int j = 0; j<3;j++) {
				for(int k=0;k<3;k++) {
					tmp[i][j] += m[i][k]*u[k][j]; 
				}
			}
		}
		m = tmp;
	}
	public Matrix3x3 multiply(final Matrix3x3 matrix) {
		Matrix3x3 ret = new  Matrix3x3();
		float[][] u = matrix.m;
		float[][] tmp = ret.m;	
		for(int i=0;i<3;i++) {
			for(int j = 0; j<3;j++) {
				for(int k=0;k<3;k++) {
					tmp[i][j] += m[i][k]*u[k][j]; 
				}
			}
		}
		return ret;
	}
	public Matrix3x3 fastMulCopy(final Matrix3x3 matrix) {
		Matrix3x3 ret = new  Matrix3x3();
		float[][] u = matrix.m;
		float[][] tmp = ret.m;
		tmp[0][0] = m[0][0]*u[0][0] + m[0][1]*u[1][0];// + m[0][2]*u[2][0];
		tmp[0][1] = m[0][0]*u[0][1] + m[0][1]*u[1][1];// + m[0][2]*u[2][1];
		tmp[0][2] = m[0][0]*u[0][2] + m[0][1]*u[1][2] + m[0][2];//*u[2][2];
		
		tmp[1][0] = m[1][0]*u[0][0] + m[1][1]*u[1][0];// + m[1][2]*u[2][0];
		tmp[1][1] = m[1][0]*u[0][1] + m[1][1]*u[1][1];// + m[1][2]*u[2][1];
		tmp[1][2] = m[1][0]*u[0][2] + m[1][1]*u[1][2] + m[1][2];//*u[2][2];
		return ret;
	}
	public void fastMul(final Matrix3x3 matrix) {
		float[][] u = matrix.m;
		tmp[0][0] = m[0][0]*u[0][0] + m[0][1]*u[1][0];// + m[0][2]*u[2][0];
		tmp[0][1] = m[0][0]*u[0][1] + m[0][1]*u[1][1];// + m[0][2]*u[2][1];
		tmp[0][2] = m[0][0]*u[0][2] + m[0][1]*u[1][2] + m[0][2];//*u[2][2];

		tmp[1][0] = m[1][0]*u[0][0] + m[1][1]*u[1][0];// + m[1][2]*u[2][0];
		tmp[1][1] = m[1][0]*u[0][1] + m[1][1]*u[1][1];// + m[1][2]*u[2][1];
		tmp[1][2] = m[1][0]*u[0][2] + m[1][1]*u[1][2] + m[1][2];//*u[2][2];
		float[][] t = m;
		m = tmp;
		tmp = t;
	}

	public float[][] getCopy() {
		float[][] ret = {	{m[0][0], m[0][1], m[0][2]},
							{m[1][0], m[1][1], m[1][2]},
							{m[2][0], m[2][1], m[2][2]}};
		return ret;
	}
}

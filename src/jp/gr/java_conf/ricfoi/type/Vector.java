package jp.gr.java_conf.ricfoi.type;

import java.text.NumberFormat;

import jp.gr.java_conf.ricfoi.Constant;

/**
 * 空間座標（３次元ベクトル）
 *
 */
public class Vector {

	/**
	 * X軸
	 */
	public final static Vector X_AXIS = new Vector(1,0,0);

	/**
	 * Y軸
	 */
	public final static Vector Y_AXIS = new Vector(0,1,0);

	/**
	 * Z軸
	 */
	public final static Vector Z_AXIS = new Vector(0,0,1);

	/**
	 * 空間座標(X,Y,Z)
	 */
	private double[] _coord = {};

	/**
	 * 空間座標(0,0,0) - 原点
	 */
	public Vector() {
		_coord = new double[]{0,0,0};
	}

	/**
	 * 空間座標(x,y,z)
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector(double x, double y, double z) {
		_coord = new double[]{x,y,z};
	}

	/**
	 * X座標の値を返します。
	 * @return
	 */
	public double getX() {
		return _coord[0];
	}

	/**
	 * Y座標の値を返します。
	 * @return
	 */
	public double getY() {
		return _coord[1];
	}

	/**
	 * Z座標の値を返します。
	 * @return
	 */
	public double getZ() {
		return _coord[2];
	}

	/**
	 * X座標に値を設定します。
	 * @param x
	 */
	public void setX(double x) {
		_coord[0] = x;
	}

	/**
	 * Y座標に値を設定します。
	 * @param y
	 */
	public void setY(double y) {
		_coord[1] = y;
	}

	/**
	 * Z座標に値を設定します。
	 * @param z
	 */
	public void setZ(double z) {
		_coord[2] = z;
	}

	/**
	 * @param v
	 */
	public void setMaxCoord(Vector v) {
		if (v.getX() > _coord[0]) _coord[0] = v.getX();
		if (v.getY() > _coord[1]) _coord[1] = v.getY();
		if (v.getZ() > _coord[2]) _coord[2] = v.getZ();
	}

	/**
	 * @param v
	 */
	public void setMinCoord(Vector v) {
		if (v.getX() < _coord[0]) _coord[0] = v.getX();
		if (v.getY() < _coord[1]) _coord[1] = v.getY();
		if (v.getZ() < _coord[2]) _coord[2] = v.getZ();
	}
	
	/**
	 * ベクトルの長さを返します。
	 * 
	 * @return the length of the vector
	 */
	public double length() {
		return Math.sqrt(_coord[0]*_coord[0] + _coord[1]*_coord[1] + _coord[2]*_coord[2]);
	}

	/**
	 * ベクトルの加算
	 * 
	 * (a1,a2,a3) + (b1,b2,b3) = (a1+b1,a2+b2,a3+b3)
	 * 
	 * @param v - (x,y,z)
	 * @return Vector
	 */
	public Vector add(Vector v) {
		return new Vector(_coord[0]+v.getX(), _coord[1]+v.getY(), _coord[2]+v.getZ());
	}

	/**
	 * ベクトルの減算
	 * 
	 * (a1,a2,a3) - (b1,b2,b3) = (a1-b1,a2-b2,a3-b3)
	 * 
	 * @param v - (x,y,z)
	 * @return
	 */
	public Vector subtract(Vector v) {
		return this.add(v.multiply(-1));
	}

	/**
	 * ベクトルの乗算
	 * 
	 * (a1,a2,a3) * b = (a1*b,a2*b,a3*b)
	 * 
	 * @param factor スカラー
	 * @return scales the vector
	 */
	public Vector multiply(double factor) {
		return new Vector(_coord[0]*factor,_coord[1]*factor,_coord[2]*factor);
	} 

	/**
	 * ベクトルの除算
	 * 
	 * (a1,a2,a3) / b = (a1/b,a2/b,a3/b)
	 * 
	 * @param factor スカラー
	 * @return
	 */
	public Vector divide(double factor) {
		return this.multiply(1/factor);
	}

	/**
	 * ベクトルの内積
	 * 
	 * (a1,a2,a3)･(b1,b2,b3) = a1*b1+a2*b2+a3*b3
	 * 
	 * @param v - (x,y,z)
	 * @return inner product of two vectors
	 */
	public double product(Vector v) {
		return _coord[0]*v.getX() + _coord[1]*v.getY() + _coord[2]*v.getZ();
	}

	/**
	 * u軸と2次元ベクトル（v,u）との角度
	 *
	 * @param v
	 * @param u
	 * @return a value from (-180..180)
	 */
	static public double atan2(double v, double u)  {
		if (u==0) {
			if (v>=0) return 90;
			else return -90;
		} 
		if (u>0)  return Math.atan(v/u)*180/Math.PI;
		if (v>=0) return 180 + Math.atan(v/u)*180/Math.PI;
		return Math.atan(v/u)*180/Math.PI-180;
	}

	/**
	 * 正規化
	 * 
	 * @return
	 */
	public Vector normalize() {
		double len = this.length();
		return new Vector(_coord[0]/len,_coord[1]/len,_coord[2]/len);
	}

	/**
	 * @param v
	 * @return
	 */
	public boolean equals(Vector v) {
		return this.subtract(v).length() < 0.0000001; 
	}

	
	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		NumberFormat f = FloatFormat.getInstance();
		return Constant.LT+f.format(_coord[0])+Constant.COMMA+f.format(_coord[1])+Constant.COMMA+f.format(_coord[2])+Constant.GT;
	}
}

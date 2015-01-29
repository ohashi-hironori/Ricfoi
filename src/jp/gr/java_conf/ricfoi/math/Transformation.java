/**
 * 
 */
package jp.gr.java_conf.ricfoi.math;

import jp.gr.java_conf.ricfoi.type.Matrix;
import jp.gr.java_conf.ricfoi.type.Vector;

/**
 * @author
 *
 */
public class Transformation {

	/**
	 * X座標
	 */
	public final static int X = 0;

	/**
	 * Y座標
	 */
	public final static int Y = 1;

	/**
	 * Z座標
	 */
	public final static int Z = 2;

	/**
	 * 
	 */
	public final static int T = 3;

	/**
	 * 回転行列
	 */
	private Matrix _matrix;

	/**
	 * 並進ベクトル
	 */
	private Vector _vector;
	
	/**
	 * 3×3の単位行列（3次単位行列）
	 *     |1 0 0|
	 * E = |0 1 0|
	 *     |0 0 1|
	 * 
	 * 空間座標 - 原点
	 * P = (0,0,0)
	 */
	public Transformation() {
		_matrix = new Matrix();
		_vector = new Vector();
	}

	/**
	 * @param m 回転行列
	 * @param v 並進ベクトル
	 */
	public Transformation(Matrix m, Vector v) {
		_matrix = m;
		_vector = v;
	}

	/**
	 * @return
	 */
	public Matrix getMatrix() {
		return _matrix;
	}
	
	/**
	 * @return
	 */
	public Vector getVector() {
		return _vector;
	}

	/**
	 * X軸上に投影(回転行列のX列)を返します。
	 * @return 回転行列のX列を返します。
	 */
	public Vector getX() {
		return _matrix.col(X);
	}

	/**
	 * Y軸上に投影(回転行列のY列)を返します。
	 * @return 回転行列のY列を返します。
	 */
	public Vector getY() {
		return _matrix.col(Y);
	}

	/**
	 * Z軸上に投影(回転行列のZ列)を返します。
	 * @return 回転行列のZ列を返します。
	 */
	public Vector getZ() {
		return _matrix.col(Z);
	}

	/**
	 * 並進ベクトルを返します。
	 * @return 並進ベクトル
	 */
	public Vector getT() { 
		return _vector;
	}

	/**
	 * @param v
	 * @return
	 */
	public Vector apply(Vector v) {
		return _matrix.product(v).add(_vector);
	}

	/**
	 * local rotation about z-axis
	 * @param angle
	 * @return
	 */
	public Transformation rotationZ(double angle) {
		double radAngle = angle*Math.PI/180;
		Matrix rm = new Matrix(
				Math.cos(radAngle) ,-Math.sin(radAngle) ,0 ,
				Math.sin(radAngle) , Math.cos(radAngle) ,0 ,
				0                  ,0                   ,1
		);
		return new Transformation(_matrix.product(rm),_vector);
	}

	/**
	 * local rotation about y-axis
	 * @param angle
	 * @return
	 */
	public Transformation rotationY(double angle) {
		double radAngle = angle*Math.PI/180;
		Matrix rm = new Matrix(
				Math.cos(radAngle) ,0 ,-Math.sin(radAngle) ,
				0                  ,1 ,                  0 ,
				Math.sin(radAngle) ,0 , Math.cos(radAngle)
		);
		return new Transformation(_matrix.product(rm),_vector);
	}

	/**
	 * local rotation about the x axis
	 * @param angle
	 * @return
	 */
	public Transformation rotationX(double angle) {
		double radAngle = angle*Math.PI/180;
		Matrix rm = new Matrix(
				1 ,                 0 ,                  0 ,
				0 ,Math.cos(radAngle) ,-Math.sin(radAngle) ,
				0 ,Math.sin(radAngle) , Math.cos(radAngle)
		);
		return new Transformation(_matrix.product(rm),_vector);
	}

	/**
	 * local rotation about the x and z axees - for the substems
	 * @param delta
	 * @param rho
	 * @return
	 */
	public Transformation rotationXZ(double delta, double rho) {
		double radDelta = delta*Math.PI/180;
		double radRho = rho*Math.PI/180;
		double sir = Math.sin(radRho);
		double cor = Math.cos(radRho);
		double sid = Math.sin(radDelta);
		double cod = Math.cos(radDelta);
		Matrix rm = new Matrix(
				cor ,-sir*cod , sir*sid ,
				sir , cor*cod ,-cor*sid ,
				0   ,     sid ,     cod
		);
		return new Transformation(_matrix.product(rm),_vector);
	}

	/**
	 * local rotation away from the local z-axis about an angle delta using an axis given by rho
	 * - used for splitting and random rotations
	 * @param delta
	 * @param rho
	 * @return
	 */
	public Transformation rotationAxisZ(double delta, double rho) {
		double radDelta = delta*Math.PI/180;
		double radRho = rho*Math.PI/180;
		double a = Math.cos(radRho);
		double b = Math.sin(radRho);
		double si = Math.sin(radDelta);
		double co = Math.cos(radDelta);
		Matrix rm = new Matrix(
				co+a*a*(1-co) ,b*a*(1-co)    , b*si ,
				a*b*(1-co)    ,co+b*b*(1-co) ,-a*si ,
				-b*si         ,a*si          , co
		);
		return new Transformation(_matrix.product(rm),_vector);
	}

	/**
	 * rotation about an axis
	 * @param angle
	 * @param axis
	 * @return
	 */
	public Transformation rotationAxis(double angle, Vector axis) {
		double radAngle = angle*Math.PI/180;
		Vector normAxis=axis.normalize();
		double a = normAxis.getX();
		double b = normAxis.getY();
		double c = normAxis.getZ();
		double si = Math.sin(radAngle);
		double co = Math.cos(radAngle);
		Matrix rm = new Matrix(
				 co+a*a*(1-co)   ,-c*si+b*a*(1-co) , b*si+c*a*(1-co) ,
				 c*si+a*b*(1-co) , co+b*b*(1-co)   ,-a*si+c*b*(1-co) ,
				-b*si+a*c*(1-co) , a*si+b*c*(1-co) , co+c*c*(1-co)
		);
		return new Transformation(rm.product(_matrix),_vector);
	}

	/**
	 * @param
	 * @return
	 */
	public Transformation product(Transformation T1) {
		return new Transformation(_matrix.product(T1.getMatrix()),_matrix.product(T1.getVector()).add(_vector));
	}

	/**
	 * @param v
	 * @return
	 */
	public Transformation translate(Vector v) {
		return new Transformation(_matrix,_vector.add(v));
	}

	/**
	 * get inverse transformation M+t -> M'-M'*t"
	 * @return
	 */
	public Transformation inverse() {
		Matrix T1 = _matrix.transpose();
		return new Transformation(T1,T1.product(_vector.multiply(-1)));
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "x: "+getX()+", y: "+getY()+", z: "+getZ()+", t: "+getT(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

}

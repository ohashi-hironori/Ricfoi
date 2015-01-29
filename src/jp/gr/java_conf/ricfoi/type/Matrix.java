/**
 * 
 */
package jp.gr.java_conf.ricfoi.type;

/**
 * 3×3の行列
 * 
 * 
 * @author
 *
 */
public final class Matrix {

	private double[][] _data;

	/**
	 * 3×3の単位行列（3次単位行列）
	 *     |1 0 0|
	 * E = |0 1 0|
	 *     |0 0 1|
	 */
	public Matrix() {
		_data = new double[][] {
				{1,0,0},
				{0,1,0},
				{0,0,1}
		};
	}

	/**
	 * 3×3の行列
	 *     |xx xy xz|
	 * E = |yx yy yz|
	 *     |zx zy zz|
	 */
	public Matrix(double xx, double xy, double xz,
			double yx, double yy, double yz,
			double zx, double zy, double zz) {
		_data = new double[][]{
				{xx,xy,xz},
				{yx,yy,yz},
				{zx,zy,zz}
		};
	}

	public Vector row(int r) {
		return new Vector(_data[r][0],_data[r][1],_data[r][2]);
	}

	public Vector col(int c) {
		return new Vector(_data[0][c],_data[1][c],_data[2][c]);
	}

	public double get(int r, int c) {
		return _data[r][c];
	}

	public void set(int r, int c, double value)  {
		_data[r][c] = value;
	}

	/**
	 * 
	 * 加算
	 * 
	 * @param M the matrix to be added
	 * @return the sum of the two matrices
	 */
	public Matrix add(Matrix M) {
		return new Matrix(
			_data[0][0]+M.get(0,0),_data[0][1]+M.get(0,1),_data[0][2]+M.get(0,2),
			_data[1][0]+M.get(1,0),_data[1][1]+M.get(1,1),_data[1][2]+M.get(1,2),
			_data[2][0]+M.get(2,0),_data[2][1]+M.get(2,1),_data[2][2]+M.get(2,2)
		);
	}

	/**
	 * 減算
	 * 
	 * @param M the matrix to be subtracted
	 * @return The result of subtracting another matrix
	 */
	public Matrix subtract(Matrix M) {
		return add(M.multiply(-1));
	}

	/**
	 * 
	 * 乗算
	 * 
	 * @param M the matrix to be added
	 * @return the sum of the two matrices
	 */
	public Matrix multiply(double factor) {
		return new Matrix(
			_data[0][0]*factor,_data[0][1]*factor,_data[0][2]*factor,
			_data[1][0]*factor,_data[1][1]*factor,_data[1][2]*factor,
			_data[2][0]*factor,_data[2][1]*factor,_data[2][2]*factor
		);
	}

	
	/**
	 * 除算
	 * 
	 * @param factor the divisor
	 * @return The matrix divided by the value
	 */
	public Matrix divide(double factor) {
		return multiply(1/factor);
	}

	/**
	 * 
	 * 行列積
	 * 
	 * @param M the matrix to be product
	 * @return The result of producting another matrix
	 */
	public Matrix product(Matrix M) {
		Matrix R = new Matrix();
		for (int r=0; r<=2; r++) {
			for (int c=0; c<=2; c++) {
				R.set(r,c,row(r).product(M.col(c)));
			}
		}
		return R;
	}

	/**
	 * Multiplies the matrix with a vector
	 * 
	 * @param v the vector
	 * @return The product of the matrix and the vector
	 */
	public Vector product(Vector v) {
		return new Vector(row(0).product(v),row(1).product(v),row(2).product(v));
	}

	/**
	 * @return
	 */
	public Matrix transpose() {
		return new Matrix(
			_data[0][0],_data[1][0],_data[2][0],
			_data[0][1],_data[1][1],_data[2][1],
			_data[0][2],_data[1][2],_data[2][2]
		);
	}

	/**
	 * 
	 * 
	 * @return 
	 */
	@Override
	public String toString() {
		return "x: "+row(0)+" y: "+row(1)+" z: "+row(2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}

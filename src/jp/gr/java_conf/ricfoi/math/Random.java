/**
 * 
 */
package jp.gr.java_conf.ricfoi.math;

/**
 *
 */
public class Random extends java.util.Random {
	private static final long serialVersionUID = 1L;

	/* (非 Javadoc)
	 * @see java.util.Random#Random(long seed)
	 */
	public Random(long seed) {
		super(seed);
	}
	
	/**
	 * @param low
	 * @param high
	 * @return
	 */
	public double uniform(double low, double high) {
		return low+nextDouble()*(high-low);
	}
	
	/* (非 Javadoc)
	 * @see java.util.Random#nextLong()
	 */
	public long getState() {
		long state = nextLong();
		setSeed(state);
		return state;
	}
	
	/* (非 Javadoc)
	 * @see java.util.Random#setSeed(long seed)
	 */
	public void setState(long state) {
		setSeed(state);
	}

}

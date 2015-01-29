package jp.gr.java_conf.ricfoi.tree;

import jp.gr.java_conf.ricfoi.math.Transformation;
import jp.gr.java_conf.ricfoi.type.Vector;

public interface StemSection {

	/**
	 * 
	 * @return the midpoint of the section
	 */
	public Vector getPosition();

	/**
	 * 
	 * @return the radius of the section (point distance from
	 * midpoint can vary when lobes are used)
	 */
	public double getRadius();

	/**
	 * @return relative distance from stem origin
	 */
	public double getDistance();
	
	/**
	 * @return the transformation for the section, giving it's
	 * position vector and rotation matrix
	 */
	public Transformation getTransformation();

	/**
	 * 
	 * @return the z-direction vector, it is orthogonal to the
	 * section layer
	 */
	public Vector getZ(); 
	
	/**
	 * 
	 * @return the vertex points of the section. It's number
	 * is influenced by the stem level and smooth value. It's distance
	 * from midpoint can vary about the radius (when lobes are used).
	 */
	public Vector[] getSectionPoints();

}

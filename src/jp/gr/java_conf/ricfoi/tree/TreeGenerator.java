package jp.gr.java_conf.ricfoi.tree;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.TreeMap;

import jp.gr.java_conf.ricfoi.params.AbstractParam;
import jp.gr.java_conf.ricfoi.params.ParamException;
import jp.gr.java_conf.ricfoi.params.Params;

public interface TreeGenerator {

	public abstract Tree makeTree();

	public abstract void setSeed(int seed);

	public abstract int getSeed();

	public abstract Params getParams();

	public abstract void setParam(String param, String value);

	// TODO: not used at the moment, may be the GUI
	// should get a TreeGenerator as a ParamContainer
	// and tree maker, and not work directly with Params
	// class
	public abstract AbstractParam getParam(String param);

	/**
	 * Returns a parameter group
	 * 
	 * @param level The branch level (0..3)
	 * @param group The parameter group name
	 * @return A hash table with the parameters
	 */
	// TODO: not used at the moment, may be the GUI
	// should get a TreeGenerator as a ParamContainer
	// and tree maker, and not work directly with Params
	// class
	public abstract TreeMap<Integer, AbstractParam> getParamGroup(int level, String group);

	/**
	 * Writes out the parameters to an XML definition file
	 * 
	 * @param out The output stream
	 * @throws ParamException
	 */
	// TODO: not used at the moment, may be the GUI
	// should get a TreeGenerator as a ParamContainer
	// and tree maker, and not work directly with Params
	// class
	public abstract void writeParamsToXML(PrintWriter out);

	/**
	 * Clear all parameter values of the tree.
	 */
	public abstract void clearParams();

	/**
	 * Read parameter values from an XML definition file
	 * 
	 * @param is The input XML stream
	 * @throws ParamException
	 */
	public abstract void readParamsFromXML(InputStream is);

	/**
	 * Read parameter values from an Config style definition file
	 * 
	 * @param is The input text stream
	 * @throws ParamException
	 */
	public abstract void readParamsFromCfg(InputStream is);

}

package jp.gr.java_conf.ricfoi.tree;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.TreeMap;

import jp.gr.java_conf.ricfoi.params.AbstractParam;
import jp.gr.java_conf.ricfoi.params.Params;

public class TreeGeneratorImpl implements TreeGenerator {

	Params params;
	private int seed = 13;

	public TreeGeneratorImpl() {
		params = new Params();
	}

	public TreeGeneratorImpl(Params params) {
		this.params = params;
	}

	@Override
	public void clearParams() {
		params.clearParams();
	}

	@Override
	public AbstractParam getParam(String param) {
		return params.getParam(param);
	}

	@Override
	public TreeMap<Integer, AbstractParam> getParamGroup(int level, String group) {
		return params.getParamGroup(level,group);
	}

	@Override
	public Params getParams() {
		return params;
	}

	@Override
	public int getSeed() {
		return seed;
	}

	@Override
	public Tree makeTree() {
		TreeImpl tree = new TreeImpl(seed, params);
		tree.make();
		return tree;
	}

	@Override
	public void readParamsFromCfg(InputStream is) {
		params.readFromConfig(is);
	}

	@Override
	public void readParamsFromXML(InputStream is) {
		params.readFromXML(is);
	}

	@Override
	public void setParam(String param, String value) {
		params.setParam(param,value);
	}

	@Override
	public void setSeed(int seed) {
		this.seed = seed;
	}

	@Override
	public void writeParamsToXML(PrintWriter out) {
		params.toXML(out);
	}

}

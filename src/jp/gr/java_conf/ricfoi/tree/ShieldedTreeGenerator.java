package jp.gr.java_conf.ricfoi.tree;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.logging.Level;

import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.params.AbstractParam;
import jp.gr.java_conf.ricfoi.params.Params;

public class ShieldedTreeGenerator  implements TreeGenerator {
	
	TreeGenerator treeGenerator;
	
	public ShieldedTreeGenerator(TreeGenerator treeGenerator) {
		this.treeGenerator = treeGenerator;
	}

	@Override
	public void clearParams() {
		try {
			treeGenerator.clearParams();
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
		}
	}
	
	@Override
	public AbstractParam getParam(String param) {
		try {
			return treeGenerator.getParam(param);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
			return null;
		}
	}
	
	@Override
	public TreeMap<Integer, AbstractParam> getParamGroup(int level, String group) {
		try {
			return treeGenerator.getParamGroup(level,group);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
			return null;
		}
	}
	
	@Override
	public Params getParams() {
		try {
			return treeGenerator.getParams();
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
			return null;
		}
	}
	
	@Override
	public int getSeed() {
		try {
			return treeGenerator.getSeed();
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
			return 13;
		}
	}
	
	@Override
	public Tree makeTree() {
		try {
			return treeGenerator.makeTree();
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
			return null;
		}
	}
	
	@Override
	public void readParamsFromCfg(InputStream is) {
		try {
			treeGenerator.readParamsFromCfg(is);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
		}
	}
	
	@Override
	public void readParamsFromXML(InputStream is) {
		try {
			treeGenerator.readParamsFromXML(is);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
		}
	}
	
	@Override
	public void setParam(String param, String value) {
		try {
			treeGenerator.setParam(param,value);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
		}
	}
	
	@Override
	public void setSeed(int seed) {
		try {
			treeGenerator.setSeed(seed);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
		}
	}
	
	@Override
	public void writeParamsToXML(PrintWriter out) {
		try {
			treeGenerator.writeParamsToXML(out);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in tree generator:", e); //$NON-NLS-1$
		}
	}

}

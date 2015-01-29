package jp.gr.java_conf.ricfoi.export;

import java.io.PrintWriter;

public abstract class Exporter {

	public static final int LEAF_PROGRESS_STEP = 500;
	public static final int STEM_PROGRESS_STEP = 100;
	public static final int MESH_PROGRESS_STEP = 500;

	private boolean _outputStemUVs;
	private boolean _outputLeafUVs;

	public abstract void write(PrintWriter w);

	public abstract PrintWriter getWriter();

	public void setOutputStemUVs(boolean outputStemUVs) {
		_outputStemUVs = outputStemUVs;
	}

	/**
	 * @return _outputStemUVs
	 */
	public boolean isOutputStemUVs() {
		return _outputStemUVs;
	}

	public void setOutputLeafUVs(boolean outputLeafUVs) {
		_outputLeafUVs = outputLeafUVs;
	}

	/**
	 * @return _outputLeafUVs
	 */
	public boolean isOutputLeafUVs() {
		return _outputLeafUVs;
	}

}

package jp.gr.java_conf.ricfoi.tree;

import jp.gr.java_conf.ricfoi.params.Params;

public class TreeGeneratorFactory {

	static public TreeGenerator createTreeGenerator() {
		return new TreeGeneratorImpl();
	}

	static public TreeGenerator createTreeGenerator(Params params) {
		return new TreeGeneratorImpl(params);
	}

	static public TreeGenerator createShieldedTreeGenerator() {
		return new ShieldedTreeGenerator(new TreeGeneratorImpl());
	}

	static public TreeGenerator createShieldedTreeGenerator(Params params) {
		return new ShieldedTreeGenerator(new TreeGeneratorImpl(params));
	}

}

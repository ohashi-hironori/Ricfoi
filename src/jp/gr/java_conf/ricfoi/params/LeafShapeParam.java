package jp.gr.java_conf.ricfoi.params;

public class LeafShapeParam extends StringParam {

	private final static String[] items = {
		"disc",
		"disc1",
		"disc2",
		"disc3",
		"disc4",
		"disc5",
		"disc6",
		"disc7",
		"disc8",
		"disc9",
		"square",
		"sphere"
	}; 

	public LeafShapeParam(String name, String def, String group, int level,
			int order, String shortDesc, String longDesc) {
		super(name, def, group, level, order, shortDesc, longDesc);
	}

	public static String[] getValues() {
		return items;
	}

}

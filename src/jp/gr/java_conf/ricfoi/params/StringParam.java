package jp.gr.java_conf.ricfoi.params;

import jp.gr.java_conf.ricfoi.Constant;

public class StringParam extends AbstractParam {

    private String _default;
    private  String _value;

	public StringParam(String name, String def, String group, int level, int order,
			String shortDesc, String longDesc) {
		super(name, group, level, order, shortDesc, longDesc);
		_default = def;
		_value = Constant.ZS;
	}

	@Override
	public void setValue(String value) throws ParamException {
		_value = value;
		fireStateChanged();
	}

	@Override
	public String getValue() {
		if (isEmpty()) {
		    // set value to default, t.e. don't warn again
		    _value=_default;
		    fireStateChanged();
		}
		return _value;
	}

	@Override
	public String getDefaultValue() {
		return _default;
	}

	@Override
	public void clear() {
		_value = Constant.ZS;
		fireStateChanged();
	}

	@Override
	public boolean isEmpty() {
		return _value.equals(Constant.ZS);
	}

}

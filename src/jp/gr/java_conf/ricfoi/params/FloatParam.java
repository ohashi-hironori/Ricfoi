package jp.gr.java_conf.ricfoi.params;

import jp.gr.java_conf.ricfoi.Constant;

public class FloatParam extends AbstractParam {

	private double _min;
	private double _max;
	private double _default;
	private double _value;

	public FloatParam(
			String name,
			double min,
			double max,
			double def,
			String group,
			int level,
			int order,
			String shortDesc,
			String longDesc
	) {
		super(name, group, level, order, shortDesc, longDesc);
		_min = min;
		_max = max;
		_default = def;
		_value = Double.NaN;
	}

    public double getDoubleValue() {
    	if (isEmpty()) {
    		// set value to default, t.e. don't warn again
    		_value=_default;
    		fireStateChanged();
    	}
    	return _value;
    }

    @Override
	public String getLongDesc() {
    	String desc = super.getLongDesc();
    	desc += "<br><br>";								 //$NON-NLS-1$
    	if (! Double.isNaN(_min)) {
    		desc += "Minimum: "+_min+Constant.LS;		 //$NON-NLS-1$
    	}
    	if (! Double.isNaN(_max)) {
    		desc += "Maximum: "+_max+Constant.LS;		 //$NON-NLS-1$
    	}
    	if (! Double.isNaN(_default)) {
    		desc += "Default: "+_default+Constant.LS;	 //$NON-NLS-1$
    	}
    	return desc;
    }

	@Override
	public void setValue(String value) throws ParamException {
    	double d;
    	try {
    		d = Double.parseDouble(value);
    	} catch (NumberFormatException e) {
    		throw new ParamException("Error setting value of "+getName()+". \""+value+"\" isn't a valid number.");
    	}
    	
    	if (d<_min) {
    		throw new ParamException("Value of "+getName()+" should be greater then or equal to "+_min); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    	if (d>_max) {
    		throw new ParamException("Value of "+getName()+" should be less then or equal to "+_max); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    	_value = d;
    	fireStateChanged();
	}

	@Override
	public String getValue() {
		return Double.toString(_value);
	}

	@Override
	public String getDefaultValue() {
		return Double.toString(_default);
	}

	@Override
	public void clear() {
		_value = Double.NaN;
		fireStateChanged();
	}

	@Override
	public boolean isEmpty() {
		return Double.isNaN(_value);
	}

}

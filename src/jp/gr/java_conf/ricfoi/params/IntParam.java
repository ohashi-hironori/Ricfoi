package jp.gr.java_conf.ricfoi.params;

public class IntParam extends AbstractParam{

	private int _min;
	private int _max;
	private int _default;
	private int _value;

	public IntParam(String name, int min, int max, int def,
			String group, int level, int order, String shortDesc, String longDesc) {
		super(name, group, level, order, shortDesc, longDesc);
		_min = min;
		_max = max;
		_default = def;
		_value = Integer.MIN_VALUE;
	}

	public int getIntValue() {
		if (isEmpty()) {
			// set value to default, i.e. don't warn again
			_value=_default;
			fireStateChanged();
		}
		return _value;
	}

	@Override
	public void setValue(String value) throws ParamException {
		int i;
		try {
			i = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new ParamException("Error setting value of "+getName()+". \""+value+"\" isn't a valid integer."); //$NON-NLS-1$
		}
		
		if (i<_min) {
			throw new ParamException("Value of "+getName()+" should be greater or equal to "+_min); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		if (i>_max) {
			throw new ParamException("Value of "+getName()+" should be greater or equal to "+_max); //$NON-NLS-1$ //$NON-NLS-2$
		}
		_value = i;
		fireStateChanged();
	}

	@Override
	public String getValue() {
		return Integer.toString(_value);
	}

	@Override
	public String getDefaultValue() {
		return Integer.toString(_default);
	}

	@Override
	public void clear() {
		_value = Integer.MIN_VALUE;
		fireStateChanged();
	}

	@Override
	public boolean isEmpty() {
		return _value == Integer.MIN_VALUE;
	}

	@Override
	public String getLongDesc() {
		String desc = super.getLongDesc();
		desc += "<br><br>";						 //$NON-NLS-1$
		if (_min != Integer.MIN_VALUE) {
			desc += "Minimum: "+_min+'\n';		 //$NON-NLS-1$
		}
		if (_max != Integer.MAX_VALUE) {
			desc += "Maximum: "+_max+'\n';		 //$NON-NLS-1$
		}
		if (_default != Integer.MIN_VALUE) {
			desc += "Default: "+_default+'\n';	 //$NON-NLS-1$
		}
		return desc;
	}

}

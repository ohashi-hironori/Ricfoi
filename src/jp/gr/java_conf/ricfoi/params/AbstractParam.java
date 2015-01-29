package jp.gr.java_conf.ricfoi.params;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public abstract class AbstractParam {
	
	private String _name;
	private String _group;
	private int _level;
	private int _order;
	private String _shortDesc;
	private String _longDesc;
	private boolean _enabled;
	private EventListenerList _listenerList = new EventListenerList();
	private ChangeEvent _changeEvent = null;

	public AbstractParam(String name, String group, int level, int order, String shortDesc, String longDesc) {
		setName(name);
		setGroup(group);
		setLevel(level);
		setOrder(order);
		setShortDesc(shortDesc);
		setLongDesc(longDesc);
		_enabled=true;
	}

	public abstract void setValue(String val) throws ParamException;
	
	public abstract String getValue();
	
	public abstract String getDefaultValue();
	
	public abstract void clear();
	
	public abstract boolean isEmpty();

	public void setEnabled(boolean enabled) {
		_enabled = enabled;
		fireStateChanged();
	}
	
	public boolean isEnabled() {
		return _enabled;
	}

	public void addChangeListener(ChangeListener listener) {
		_listenerList.add(ChangeListener.class, listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		_listenerList.remove(ChangeListener.class, listener);
	}

	public void fireStateChanged() {
		Object [] listeners = _listenerList.getListenerList();
		for (int i = listeners.length -2; i>=0; i-=2) {
			if (listeners[i] == ChangeListener.class) {
				if (_changeEvent == null) {
					_changeEvent = new ChangeEvent(this);
				}
				((ChangeListener)listeners[i+1]).stateChanged(_changeEvent);
			}
		}
	}

	@Override
	public String toString() { 
		if (! isEmpty()) {
			return getValue();
		} 
		// else 
		return getDefaultValue();
	}

	/**
	 * @return _name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @param _name セットする _name
	 */
	public void setName(String _name) {
		this._name = _name;
	}

	/**
	 * @return _group
	 */
	public String getGroup() {
		return _group;
	}

	/**
	 * @param _group セットする _group
	 */
	public void setGroup(String _group) {
		this._group = _group;
	}

	/**
	 * @return _level
	 */
	public int getLevel() {
		return _level;
	}

	/**
	 * @param _level セットする _level
	 */
	public void setLevel(int _level) {
		this._level = _level;
	}

	/**
	 * @return _order
	 */
	public int getOrder() {
		return _order;
	}

	/**
	 * @param _order セットする _order
	 */
	public void setOrder(int _order) {
		this._order = _order;
	}

	/**
	 * @return _shortDesc
	 */
	public String getShortDesc() {
		return _shortDesc;
	}

	/**
	 * @param _shortDesc セットする _shortDesc
	 */
	public void setShortDesc(String _shortDesc) {
		this._shortDesc = _shortDesc;
	}

	/**
	 * @return _longDesc
	 */
	public String getLongDesc() {
		return _longDesc;
	}

	/**
	 * @param _longDesc セットする _longDesc
	 */
	public void setLongDesc(String _longDesc) {
		this._longDesc = _longDesc;
	}

}

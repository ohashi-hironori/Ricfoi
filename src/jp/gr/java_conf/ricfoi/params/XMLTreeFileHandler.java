package jp.gr.java_conf.ricfoi.params;

import jp.gr.java_conf.ricfoi.Constant;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLTreeFileHandler extends DefaultHandler {

	private static final String ATTR_NAME_SPECIES = "species";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_PARAM = "param";
	private static final String ATTR_VALUE = "value";
	private Params _params;
	private String _errors;
	
	public XMLTreeFileHandler(Params params) {
		_params = params;
		_errors = Constant.ZS;
	}
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		
		try {
			
			if (qName.equals(ATTR_NAME_SPECIES)) {
				_params.setParam(Params.NAME_SPECIES, atts.getValue(ATTR_NAME));
			} else if (qName.equals(ATTR_PARAM)) {
				_params.setParam(atts.getValue(ATTR_NAME),atts.getValue(ATTR_VALUE));
			}
		} catch (ParamException e) {
			_errors += e.getMessage()+Constant.LS;
			// throw new SAXException(e.getMessage());
		}
		
	}

	public boolean isErrors() {
		return (!_errors.isEmpty());
	}

	public String getErrors() {
		return _errors;
	}
}

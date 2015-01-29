package jp.gr.java_conf.ricfoi.params;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLTreeParser {

	private SAXParser parser;
	
	public XMLTreeParser() throws ParserConfigurationException, SAXException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		parser = spf.newSAXParser();
	}
	
	public void parse(InputSource is, Params params) throws SAXException, IOException, ParamException {
		XMLTreeFileHandler xml_handler = new XMLTreeFileHandler(params);
		parser.parse(is,xml_handler);
		if (xml_handler.isErrors()) {
			throw new ParamException(xml_handler.getErrors());
		}
	}

}

package jp.gr.java_conf.ricfoi.params;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class ConfigTreeParser {

	private static final String ATTR_NAME_SPECIES = "species";
	
	@SuppressWarnings("resource")
	public static void parse(String fileName, Params params) throws Exception {
		File inputFile = new File(fileName);
		LineNumberReader r = new LineNumberReader(new FileReader(inputFile));
		parse(r,params);
	}
	
	public static void parse(InputStream is, Params params) throws Exception {
		LineNumberReader r = new LineNumberReader(new InputStreamReader(is));
		parse(r,params);
	}
	
	public static void parse(LineNumberReader r, Params params) throws Exception {
		String line = r.readLine().trim();
		String param;
		String value;
		while (line != null) {
			if (! line.isEmpty() && line.charAt(0) != '#') {
				int equ = line.indexOf('=');
				param = line.substring(0,equ).trim();
				value = line.substring(equ+1).trim();
				if (param.equals(ATTR_NAME_SPECIES)) {
					params.setParam(Params.NAME_SPECIES,value);
				} else {
					params.setParam(param,value);
				}
				line = r.readLine();
			}
		}
	}

}

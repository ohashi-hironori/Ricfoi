package jp.gr.java_conf.ricfoi.export;

import java.io.PrintWriter;

public class ShieldedExporter extends Exporter {

	private Exporter exporter;

	public ShieldedExporter(Exporter exporter) {
		this.exporter = exporter;
	}

	@Override
	public PrintWriter getWriter() {
		return exporter.getWriter();
	}

	@Override
	public void write(PrintWriter w) {
		exporter.write(w);
	}

}

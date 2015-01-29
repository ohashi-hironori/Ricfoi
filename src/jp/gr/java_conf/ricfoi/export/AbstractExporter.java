package jp.gr.java_conf.ricfoi.export;

import java.io.PrintWriter;

public abstract class AbstractExporter extends Exporter {

	protected PrintWriter w;

	@Override
	public void write(PrintWriter w) {
		this.w=w;
		doWrite();
	}

	@Override
	public PrintWriter getWriter() {
		return w;
	}

	protected abstract void doWrite();

}

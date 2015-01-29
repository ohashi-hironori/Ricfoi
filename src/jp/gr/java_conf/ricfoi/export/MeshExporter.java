package jp.gr.java_conf.ricfoi.export;

import jp.gr.java_conf.ricfoi.mesh.MeshGenerator;

abstract class MeshExporter extends AbstractExporter {

	protected MeshGenerator meshGenerator;

	public MeshExporter(MeshGenerator meshGenerator) {
		this.meshGenerator = meshGenerator;
	}

}

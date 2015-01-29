package jp.gr.java_conf.ricfoi.mesh;

public class MeshGeneratorFactory {

	static public MeshGenerator createMeshGenerator(boolean useQuads) {
		return new MeshGeneratorImpl(useQuads);
	}

	static public MeshGenerator createShieldedMeshGenerator(boolean useQuads) {
		return new ShieldedMeshGenerator(
				new MeshGeneratorImpl(useQuads));
	}
}

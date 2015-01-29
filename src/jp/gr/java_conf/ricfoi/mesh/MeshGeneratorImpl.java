package jp.gr.java_conf.ricfoi.mesh;

import jp.gr.java_conf.ricfoi.tree.Tree;
import jp.gr.java_conf.ricfoi.type.LeafMesh;

public class MeshGeneratorImpl implements MeshGenerator {

	public boolean useQuads;

	public MeshGeneratorImpl(boolean useQuads) {
		super();
		this.useQuads = useQuads;
	}

	@Override
	public LeafMesh createLeafMesh(Tree tree, boolean useQuads) {
		return new LeafMesh(tree.getLeafShape(),
				tree.getLeafLength(),tree.getLeafWidth(),
				tree.getLeafStemLength(),useQuads);
	}

	@Override
	public Mesh createStemMesh(Tree tree) {
		Mesh mesh = new Mesh(tree.getLevels());
		MeshCreator meshCreator = new MeshCreator(mesh, -1, useQuads);
		tree.traverseTree(meshCreator);
		return mesh;
	}

	@Override
	public Mesh createStemMeshByLevel(Tree tree) {
		Mesh mesh = new Mesh(tree.getLevels());
		for (int level=0; level < tree.getLevels(); level++) {
			MeshCreator meshCreator = new MeshCreator(mesh, level, useQuads);
			tree.traverseTree(meshCreator);
		}
		return mesh;
	}

	@Override
	public boolean getUseQuads() {
		return useQuads;
	}

}

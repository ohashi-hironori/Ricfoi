package jp.gr.java_conf.ricfoi.mesh;

import jp.gr.java_conf.ricfoi.tree.Tree;
import jp.gr.java_conf.ricfoi.type.LeafMesh;

public interface MeshGenerator {

	public abstract Mesh createStemMesh(Tree tree);

	public abstract Mesh createStemMeshByLevel(Tree tree);

	public abstract LeafMesh createLeafMesh(Tree tree, boolean useQuads);

	public boolean getUseQuads();

}

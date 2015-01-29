package jp.gr.java_conf.ricfoi.mesh;

import java.util.logging.Level;

import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.tree.Tree;
import jp.gr.java_conf.ricfoi.type.LeafMesh;

public class ShieldedMeshGenerator implements MeshGenerator {

	private MeshGenerator meshGenerator;
	
	public ShieldedMeshGenerator(MeshGenerator meshGenerator) {
		this.meshGenerator = meshGenerator;
	}

	@Override
	public LeafMesh createLeafMesh(Tree tree, boolean useQuads) {
		try {
			return meshGenerator.createLeafMesh(tree, useQuads);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in mesh generator:", e); //$NON-NLS-1$
			return null;
		}
	}

	@Override
	public Mesh createStemMesh(Tree tree) {
		try {
			return meshGenerator.createStemMesh(tree);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in mesh generator:", e); //$NON-NLS-1$
			return null;
		}
	}

	@Override
	public Mesh createStemMeshByLevel(Tree tree) {
		try {
			return meshGenerator.createStemMeshByLevel(tree);
		} catch (Exception e) {
			Ricfoi.logger.log(Level.WARNING, "Error in mesh generator:", e); //$NON-NLS-1$
			return null;
		}
	}
	
	@Override
	public boolean getUseQuads() {
		return meshGenerator.getUseQuads();
	}

}

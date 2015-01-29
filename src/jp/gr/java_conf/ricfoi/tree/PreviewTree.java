package jp.gr.java_conf.ricfoi.tree;

import java.io.PrintWriter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import jp.gr.java_conf.ricfoi.mesh.Mesh;
import jp.gr.java_conf.ricfoi.mesh.MeshGenerator;
import jp.gr.java_conf.ricfoi.mesh.MeshGeneratorFactory;
import jp.gr.java_conf.ricfoi.params.IntParam;
import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.type.LeafMesh;
import jp.gr.java_conf.ricfoi.type.Vector;

public final class PreviewTree implements Tree {

	int showLevel=1;
	Params originalParams;
	Mesh mesh;
	LeafMesh leafMesh;
	MeshGenerator meshGenerator;
	
	Tree tree;
	
	protected ChangeEvent changeEvent = null;
	protected EventListenerList listenerList = new EventListenerList();

	/**
	 * @param params tree parameters
	 */
	public PreviewTree(Params params) {
		this.originalParams=params;
	}
	
	public Params getParams() { return originalParams; }

	public void setParams(Params params) { this.originalParams = params; }

	// delegate interface methods to the tree
	@Override
	public boolean traverseTree(TreeTraversal traversal)
	{
		return tree.traverseTree(traversal);
	}

	@Override
	public long getStemCount() { return tree.getStemCount(); }

	@Override
	public long getLeafCount() { return tree.getLeafCount(); }

	@Override
	public Vector getMaxPoint() { return tree.getMaxPoint(); }

	@Override
	public Vector getMinPoint() { return tree.getMinPoint(); }

	@Override
	public int getSeed() { return tree.getSeed(); }

	@Override
	public double getHeight() { return tree.getHeight(); }
	
	@Override
	public double getWidth() { return tree.getWidth(); }
	
	@Override
	public void paramsToXML(PrintWriter w) {
		throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
	}
	
	@Override
	public String getSpecies() { return tree.getSpecies(); }
	
	@Override
	public int getLevels() { return tree.getLevels(); }
	
	@Override
	public String getLeafShape() { return tree.getLeafShape(); }
	
	@Override
	public double getLeafWidth() { return tree.getLeafWidth(); }
	
	@Override
	public double getLeafLength() { return tree.getLeafLength(); }
	
	@Override
	public double getLeafStemLength() { return tree.getLeafStemLength(); };
	
	@Override
	public String getVertexInfo(int level) { return tree.getVertexInfo(level); };
	
	public void setShowLevel(int l) {
		int Levels = ((IntParam)(originalParams.getParam(Params.NAME_LEVELS))).getIntValue(); 
		if (l>Levels) showLevel=Levels;
		else showLevel=l;
	}
	
	public int getShowLevel() {
		return showLevel;
	}

	public void remake(boolean doFireStateChanged) {
			Params params = new Params(originalParams);
			params.setPreview(true);
			
			// manipulate params to avoid making the whole tree
			// FIXME: previewTree.Levels <= tree.Levels
			int Levels = ((IntParam)(originalParams.getParam(Params.NAME_LEVELS))).getIntValue(); 
			if (Levels>showLevel+1) {
				params.setParam(Params.NAME_LEVELS,Integer.toString(showLevel+1));
				params.setParam(Params.NAME_LEAVES,"0");
			} 
			for (int i=0; i<showLevel; i++) {
				params.setParam(Integer.toString(i)+"Branches",Integer.toString(1));
				params.setParam(Integer.toString(i)+"DownAngleV",Integer.toString(0));
			}

			TreeGenerator treeGenerator = TreeGeneratorFactory.createShieldedTreeGenerator(params);
		    tree = treeGenerator.makeTree();
		    
		    MeshGenerator meshGenerator = MeshGeneratorFactory.createShieldedMeshGenerator(true);
			mesh = meshGenerator.createStemMesh(tree);
			leafMesh = meshGenerator.createLeafMesh(tree,true);
	
			if (doFireStateChanged)	fireStateChanged();
	}
	
	public Mesh getMesh() {
		return mesh;
	}

	public LeafMesh getLeafMesh() {
		return leafMesh;
	}

	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}
	
	protected void fireStateChanged() {
		Object [] listeners = listenerList.getListenerList();
		for (int i = listeners.length -2; i>=0; i-=2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
			}
		}
	}

}

package jp.gr.java_conf.ricfoi.tree;

import java.io.PrintWriter;
import java.util.Enumeration;

import jp.gr.java_conf.ricfoi.math.Transformation;
import jp.gr.java_conf.ricfoi.params.LevelParams;
import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.type.Vector;

class TreeImpl implements Tree {
	
	public Params params;
	int seed = 13;
	long stemCount;
	long leafCount;
	// the trunks (one for trees, many for bushes)
	java.util.Vector<StemImpl> trunks;
	double trunk_rotangle = 0;
	Vector maxPoint;
	Vector minPoint;
	
	public TreeImpl(int seed, Params params) {
		this.params = params;
		this.seed = seed;
		trunks = new java.util.Vector<>();
	}

	public TreeImpl(TreeImpl other) {
		params = new Params(other.params);
		trunks = new java.util.Vector<>();
	}
	public void clear() {
		trunks = new java.util.Vector<>();
	}
	
	@Override
	public double getHeight() { return maxPoint.getZ(); }
	
	@Override
	public long getLeafCount() { return leafCount; }
	
	@Override
	public double getLeafLength() { return params.getLeafScale()/Math.sqrt(params.getLeafQuality()); }
	
	@Override
	public String getLeafShape() { return params.getLeafShape(); }
	
	@Override
	public double getLeafStemLength() { return params.getLeafStemLength(); }
	
	@Override
	public double getLeafWidth() { return params.getLeafScale()*params.getLeafScaleX()/Math.sqrt(params.getLeafQuality()); }
	
	@Override
	public int getLevels() { return params.getLevels();}
	
	@Override
	public Vector getMaxPoint() { return maxPoint; }
	
	@Override
	public Vector getMinPoint() { return minPoint; }
	
	@Override
	public int getSeed() { return seed; }
	
	@Override
	public String getSpecies() { return params.getSpecies(); }
	
	@Override
	public long getStemCount() { return stemCount; }

	@Override
	public String getVertexInfo(int level) {
		return "vertices/section: "											//$NON-NLS-1$
			+ params.getLevelParams(level).getMeshPoints() + ", smooth: " 	//$NON-NLS-1$
			+ (params.getSmoothMeshLevel()>=level? "yes" : "no");			//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public double getWidth() {
		return Math.sqrt(Math.max(
				 minPoint.getX()*minPoint.getX()+minPoint.getY()*minPoint.getY(),
				 maxPoint.getX()*maxPoint.getX()+maxPoint.getY()*maxPoint.getY())); 
	}
	
	public void make() {
		params.prepare(seed);
		maxPoint = new Vector(-Double.MAX_VALUE,-Double.MAX_VALUE,-Double.MAX_VALUE);
		minPoint = new Vector(Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE);
		
		// create the trunk and all its stems and leaves
		Transformation transf = new Transformation();
		Transformation trf;
		double angle;
		double dist;
		LevelParams lpar = params.getLevelParams(0);
		for (int i=0; i<lpar.getNBranches(); i++) {
			trf = trunkDirection(transf,lpar);
			angle = lpar.var(360);
			dist = lpar.var(lpar.getNBranchDist());
			trf = trf.translate(new Vector(dist*Math.sin(angle), dist*Math.cos(angle),0));
			StemImpl trunk = new StemImpl(this,null,0,trf,0);
			trunks.addElement(trunk);
			trunk.index=0;
			trunk.make();
		}
		
		// set leafCount and stemCount for the tree
		if (params.getLeaves()==0) setLeafCount(0);
		else {
			LeafCounter leafCounter = new LeafCounter();
			traverseTree(leafCounter);
			setLeafCount(leafCounter.getLeafCount());
		}
		StemCounter stemCounter = new StemCounter();
		traverseTree(stemCounter);
		setStemCount(stemCounter.getStemCount());
	}
	
	public void minMaxTest(Vector pt) {
		maxPoint.setMaxCoord(pt);
		minPoint.setMinCoord(pt);
	}
	
	@Override
	public void paramsToXML(PrintWriter out)  {
		params.toXML(out);
	}
	
	public void setLeafCount(long cnt) { leafCount=cnt; }
	
	public void setStemCount(long cnt) { stemCount=cnt; }
	
	@Override
	public boolean traverseTree(TreeTraversal traversal) {
	    if (traversal.enterTree(this))  // enter this tree?
        {
             Enumeration<StemImpl> stems = trunks.elements();
             while (stems.hasMoreElements())
                if (! ((Stem)stems.nextElement()).traverseTree(traversal))
                        break;
        }
        return traversal.leaveTree(this);
	}
	
	Transformation trunkDirection(Transformation trf, LevelParams lpar) {
		// get rotation angle
		double rotangle;
		if (lpar.getNRotate()>=0) { // rotating trunk
			trunk_rotangle = (trunk_rotangle + lpar.getNRotate()+lpar.var(lpar.getNRotateV())+360) % 360;
			rotangle = trunk_rotangle;
		} else { // alternating trunks
			if (Math.abs(trunk_rotangle) != 1) trunk_rotangle = 1;
			trunk_rotangle = -trunk_rotangle;
			rotangle = trunk_rotangle * (180+lpar.getNRotate()+lpar.var(lpar.getNRotateV()));
		}
		
		// get downangle
		double downangle;
		downangle = lpar.getNDownAngle()+lpar.var(lpar.getNDownAngleV());
		
		return trf.rotationXZ(downangle,rotangle);
	}

}

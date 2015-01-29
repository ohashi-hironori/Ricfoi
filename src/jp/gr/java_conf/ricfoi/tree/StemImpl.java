package jp.gr.java_conf.ricfoi.tree;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.math.Transformation;
import jp.gr.java_conf.ricfoi.params.LevelParams;
import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.type.Vector;

public class StemImpl implements Stem {

	private class SectionsEnumerator implements Enumeration<Object> {
		private Enumeration<SegmentImpl> segments;
		private Enumeration<?> subsegments;
		
		public SectionsEnumerator(StemImpl stem) {
			segments = stem.segments.elements();
		}
		
		@Override
		public boolean hasMoreElements() {
			return (subsegments != null && subsegments.hasMoreElements()) || (segments.hasMoreElements());
		}
		
		@Override
		public Object nextElement() {
			if (subsegments == null) {
				// first segment, return it as base section
				SegmentImpl s = segments.nextElement();
				subsegments = s.subsegments.elements();
				return s;
			} else {
				if (subsegments.hasMoreElements())
					return subsegments.nextElement();
				else if (segments.hasMoreElements()) {
					SegmentImpl s = segments.nextElement();
					subsegments = s.subsegments.elements();
					return subsegments.nextElement();
				} else {
					throw new NoSuchElementException("SectionsEnumerator"); //$NON-NLS-1$
				}
			}
		}
	}
	
	TreeImpl tree;
	Params par;
	LevelParams lpar;
	StemImpl parent;
	StemImpl clonedFrom=null;
	Transformation transf;
	
	Vector minPoint;
	Vector maxPoint;
	
	final static double MIN_STEM_LEN=0.0005;
	final static double MIN_STEM_RADIUS=MIN_STEM_LEN/10;
	
	public int stemlevel; // the branch level, could be > 4 
	double offset; // how far from the parent's base 
	
	java.util.Vector<SegmentImpl> segments; // the segments forming the stem
	java.util.Vector<StemImpl> clones;      // the stem clones (for splitting)
	
	java.util.Vector<StemImpl> substems;    // the substems
	java.util.Vector<LeafImpl> leaves;     // the leaves
	double length;
	double segmentLength;
	
	int segmentCount; 
	double baseRadius;
	double lengthChildMax;
	double substemsPerSegment;
	double substemRotangle;
	
	double leavesPerSegment;
	double splitCorrection;
	boolean pruneTest; // flag for pruning cycles
	
	int index; // substem number
	java.util.Vector<Integer> cloneIndex; // clone number (Integers)
	
	public StemImpl(TreeImpl tr, StemImpl growsOutOf, int stlev, Transformation trf, double offs) {
		tree = tr;
		stemlevel = stlev;
		transf = trf; 
		offset = offs;
		
		if (growsOutOf != null) {
			if (growsOutOf.stemlevel<stemlevel)
				parent = growsOutOf;
			else {
				clonedFrom = growsOutOf;
				parent = growsOutOf.parent;
			}
		}

		par = tree.params;
		lpar = par.getLevelParams(stemlevel);

		// initialize lists
		segments = new java.util.Vector<>(lpar.getNCurveRes());
	
		if (lpar.getNSegSplits() > 0 || par.get0BaseSplits()>0) {
			clones = new java.util.Vector<>();
		}
		
		if (stemlevel < par.getLevels()-1) {
			LevelParams lpar_1 = par.getLevelParams(lpar.getLevel()+1);
			substems = new java.util.Vector<>(lpar_1.getNBranches());
		}
		
		if (stemlevel == par.getLevels()-1 && par.getLeaves() != 0) {
			leaves = new java.util.Vector<>(Math.abs(par.getLeaves()));
		}
		
		// inialize other variables
		leavesPerSegment = 0;
		splitCorrection = 0;

		index=0; // substem number
		
		cloneIndex = new java.util.Vector<>();
		pruneTest = false; // flag used for pruning

		//...
		maxPoint = new Vector(-Double.MAX_VALUE,-Double.MAX_VALUE,-Double.MAX_VALUE);
		minPoint = new Vector(Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE);

	}
	
//	StemImpl clone(Transformation trf, int start_segm) {
	StemImpl clone(Transformation trf) {
		// creates a clone stem with same atributes as this stem
		StemImpl clone = new StemImpl(tree,this,stemlevel,trf,offset);
		clone.segmentLength = segmentLength;
		clone.segmentCount = segmentCount;
		clone.length = length;
		clone.baseRadius = baseRadius;
		clone.splitCorrection = splitCorrection; 
		clone.pruneTest = pruneTest;
		clone.index = index;
		
		clone.cloneIndex.addAll(cloneIndex);
		
		clone.cloneIndex.addElement(new Integer(clones.size()));
		if (! pruneTest) {
			clone.lengthChildMax = lengthChildMax;
			clone.substemsPerSegment = substemsPerSegment;
			// FIXME: for more then one clone this angle should somehow
			// correspond to the rotation angle of the clone
			clone.substemRotangle=substemRotangle+180;
			clone.leavesPerSegment=leavesPerSegment;
		}
		return clone;
	}
	@Override
	public double getBaseRadius() {
		return segments.elementAt(0).rad1;
	}

	@Override
	public int getCloneSectionOffset() {
		if (! isClone()) return 0;
		else {
			// find out how many sections the parent stem
			// has below the first segment of this stem
			int segInx = segments.elementAt(0).index;
			
			return clonedFrom.getSectionCountBelow(segInx);
		}
	}
	
	@Override
	public long getLeafCount() {
		if (leaves != null)
			return leaves.size();
		else
			return 0;
	}
	
	@Override
	public double getLength() { return length; }
	
	@Override
	public int getLevel() { return stemlevel; }
	
	@Override
	public Vector getMaxPoint() { return maxPoint; }
	
	@Override
	public Vector getMinPoint() { return minPoint; }
	
	@Override
	public double getPeakRadius() {
		return segments.elementAt(segments.size()-1).rad2;
	}
	
	protected int getSectionCountBelow(int index) {
		int count=1; // first segments base section
	
		Enumeration<SegmentImpl> segs = segments.elements();
		while (segs.hasMoreElements()) {
		
			SegmentImpl s = segs.nextElement();
			if (s.index < index) count += s.subsegments.size();
			else return count-1;
		}
		
		return count-1;
	}
	
	@Override
	public Transformation getTransformation() { return transf; }
	
	@Override
	public String getTreePosition() {
		// returns the position of the stem in the tree as a string, e.g. 0c0.1
		// for the second substem of the first clone of the trunk
		StemImpl stem = this;
		int lev = stemlevel;
		String pos = Constant.ZS;
		while (lev>=0) {
			if (stem.cloneIndex.size()>0) { 
				String clonestr = Constant.ZS;
				for (int i=0; i<stem.cloneIndex.size(); i++) {
					clonestr += "c"+stem.cloneIndex.elementAt(i).toString();
				}
				pos = Integer.toString(stem.index)+clonestr+Constant.FS+pos;
			} else {
				pos = Integer.toString(stem.index)+Constant.FS+pos;
			}
			if (lev>0) stem = stem.parent;
			lev--;
		} 	
		if (pos.charAt(pos.length()-1) == '.') pos = pos.substring(0,pos.length()-1);
		return pos;
	}
	
	@Override
	public boolean isClone(){
		return cloneIndex.size()>0;
	}
	
	boolean isInsideEnvelope(Vector vector) {
		double r = Math.sqrt(vector.getX()*vector.getX() + vector.getY()*vector.getY());
		double ratio = (par.getScaleTree() - vector.getZ())/(par.getScaleTree()*(1-par.getBaseSize()));
		return (r/par.getScaleTree()) < (par.getPruneWidth() * par.getShapeRatio(ratio,8));
	}
	
	@Override
	public boolean isSmooth() {
		return stemlevel<=par.getSmoothMeshLevel();
	}
	
	double leavesPerBranch() {
		// calcs the number of leaves for a stem
		if (par.getLeaves()==0) return 0;
		if (stemlevel == 0) {
			// FIXME: maybe set Leaves=0 when Levels=1 in Params.prepare()
			System.err.println("WARNING: trunk cannot have leaves, no leaves are created"); //$NON-NLS-1$
			return 0;
		}
		
		return (Math.abs(par.getLeaves()) 
				* par.getShapeRatio(offset/parent.length,par.getLeafDistrib()) 
				* par.getLeafQuality());
	}
	
	public boolean make() {
		
		// makes the stem with all its segments, substems, clones and leaves
		segmentCount = lpar.getNCurveRes();
		length = stemLength();
		segmentLength = length/lpar.getNCurveRes();
		baseRadius = stemBaseRadius();
		if (stemlevel==0) {
			double baseWidth = Math.max(baseRadius,stemRadius(0));
			minMaxTest(new Vector(baseWidth,baseWidth,0));
		}
		
		// FIXME: should pruning occur for the trunk too?
		if (stemlevel>0 && par.getPruneRatio() > 0) {
			pruning();
		}
		
		// FIXME: if length<=MIN_STEM_LEN the stem object persists here but without any segments
		// alternatively make could return an error value, the invoking function
		// then had to delete this stem
		if (length > MIN_STEM_LEN && baseRadius > MIN_STEM_RADIUS)
		{
			prepareSubstemParams();
			makeSegments(0,segmentCount);
			return true;
		} else {
			return false;
		}
	}
	
	int makeClones(Transformation trnsform, int nseg) {
		Transformation trf = trnsform;
		// splitting
		// FIXME: maybe move this calculation to LevelParams
		// but pay attention to saving errorValues and restoring when making prune tests
		int seg_splits_eff;
		if (stemlevel==0 && nseg==0 && par.get0BaseSplits()>0) {
			seg_splits_eff = par.get0BaseSplits();
		}
		else {
			// how many clones?
			double seg_splits = lpar.getNSegSplits();
			seg_splits_eff = (int)(seg_splits+lpar.getSplitErrorValue()+0.5);
			
			// adapt error value
//			lpar.splitErrorValue -= (seg_splits_eff - seg_splits);
			lpar.setSplitErrorValue(lpar.getSplitErrorValue() - (seg_splits_eff - seg_splits));
		}
		
		if (seg_splits_eff<1) return -1;
		
		double s_angle = 360/(seg_splits_eff+1);
		
		// make clones
		for (int i=0; i<seg_splits_eff; i++) { 
			
			// copy params
//			StemImpl clone = clone(trf,nseg+1);
			StemImpl clone = clone(trf);
			
			clone.transf = clone.split(trf,s_angle*(1+i),nseg,seg_splits_eff);
			
			// make segments etc. for the clone
			int segm = clone.makeSegments(nseg+1,clone.segmentCount);
			if (segm>=0) { // prune test - clone not inside envelope
				return segm;
			}
			// add clone to the list
			clones.addElement(clone);
		}
		// get another direction for the original stem too   
		trf = split(trf,0,nseg,seg_splits_eff);
		return -1;
	}
	
	void makeLeaves(SegmentImpl segment) {
		// creates leaves for the current segment
		
		if (par.getLeaves() > 0) { // ### NORMAL MODE, leaves along the stem
			// how many leaves in this segment
			double leaves_eff = (int)(leavesPerSegment + par.getLeavesErrorValue()+0.5);
			
			// adapt error value
//			par.leavesErrorValue -= (leaves_eff - leavesPerSegment);
			par.setLeavesErrorValue(par.getLeavesErrorValue() - (leaves_eff - leavesPerSegment));
			
			if (leaves_eff <= 0) return;
			
			double offs;
			if (segment.index==0) {
				offs = parent.stemRadius(offset)/segmentLength;
			} else {
				offs = 0;
			}
			
			// what distance between the leaves
			double dist = (1.0-offs)/leaves_eff;
			
			for (int s=0; s<leaves_eff; s++) {
				// where on the segment add the leaf
				
				// FIXME: may be use the same distribution method (BranchDist) as for substems?
				double where = offs+dist/2+s*dist+lpar.var(dist/2);
				
				// offset from stembase
				double loffs = (segment.index+where)*segmentLength;
				// get a new direction for the leaf
				Transformation trf = substemDirection(segment.transf,loffs);
				// translate it to its position on the stem
				trf = trf.translate(segment.transf.getZ().multiply(where*segmentLength));
				
				// create new leaf
				LeafImpl leaf = new LeafImpl(trf); // ,loffs);
				leaf.make(par);
				leaves.addElement(leaf);
				
			}
		}
		
		// ##### FAN MOD, leaves placed in a fan at stem end
		else if (par.getLeaves()<0 && segment.index == segmentCount-1) {
			
			LevelParams lpar_1 = par.getLevelParams(stemlevel+1);
			int cnt = (int)(leavesPerBranch()+0.5);
			
			Transformation trf = segment.transf.translate(segment.transf.getZ().multiply(segmentLength));
			double distangle = lpar_1.getNRotate()/cnt;
			double varangle = lpar_1.getNRotateV()/cnt;
			double downangle = lpar_1.getNDownAngle();
			double vardown = lpar_1.getNDownAngleV();
			double offsetangle = 0;
			// use different method for odd and even number
			if (cnt%2 == 1) {
				// create one leaf in the middle
				LeafImpl leaf = new LeafImpl(trf); //,segmentCount*segmentLength);
				leaf.make(par);
				leaves.addElement(leaf);
				offsetangle = distangle;
			} else {
				offsetangle = distangle/2;
			}
			// create leaves left and right of the middle
			for (int s=0; s<cnt/2; s++) {
				for (int rot=1; rot >=-1; rot-=2) {
					Transformation transf1 = trf.rotationY(rot*(offsetangle+s*distangle
							+lpar_1.var(varangle)));
					transf1 = transf1.rotationX(downangle+lpar_1.var(vardown));
					LeafImpl leaf = new LeafImpl(transf1); //,segmentCount*segmentLength);
					leaf.make(par);
					leaves.addElement(leaf);
				}
			}
		}
	}
	
	// makes the segments of the stem
	int makeSegments(int start_seg,int end_seg) {
//		
//		if (stemlevel==1) tree.updateGenProgress();
//		
//		if (! pruneTest) {
//			if (stemlevel==0) Console.progressChar('=');
//			else if (stemlevel==1 && start_seg==0) Console.progressChar('/');
//			else if (stemlevel==2 && start_seg==0) Console.progressChar();
//		}
//		
		Transformation trf = transf;
		
		for (int s=start_seg; s<end_seg; s++) {
//			if (stemlevel==0) tree.updateGenProgress();
//			
//			if (! pruneTest) {// && par.verbose) {
//				if (stemlevel==0) Console.progressChar('|');
//			}
//			
			// curving
			trf=newDirection(trf,s);
			
			// segment radius
			double rad1 = stemRadius(s*segmentLength);
			double rad2 = stemRadius((s+1)*segmentLength);
			
			// create new segment
			SegmentImpl segment = new SegmentImpl(this,s,trf,rad1,rad2);
			segment.make();
			segments.addElement(segment);
			
			// create substems
			if (! pruneTest && lpar.getLevel()<par.getLevels()-1) {
				makeSubstems(segment);
			}
			
			if (! pruneTest && lpar.getLevel()==par.getLevels()-1 && par.getLeaves()!=0) {
				makeLeaves(segment);
			}
			
			// shift to next position
			trf = trf.translate(trf.getZ().multiply(segmentLength));
			
			// test if too long
			if (pruneTest && ! isInsideEnvelope(trf.getT())) {
				return s;
			}
			
			// splitting (create clones)
			if (s<end_seg-1) {
				int segm = makeClones(trf,s);
				// trf is changed by make_clones
				// prune test - clone not inside envelope 
				if (segm>=0) {
					return segm;
				}
			}
		}
		
		return -1;
	}
	
	void makeSubstems(SegmentImpl segment) {
		// creates substems for the current segment
		LevelParams lpar_1 = par.getLevelParams(stemlevel+1);
		
		double subst_per_segm;
		double offs;
		
		if (stemlevel>0) {
			// full length of stem can have substems
			subst_per_segm = substemsPerSegment;
			
			if (segment.index==0) {
				offs = parent.stemRadius(offset)/segmentLength;
			} else { offs = 0; }
			
		} else if (segment.index*segmentLength > par.getBaseSize()*length) {
			// segment is fully out of the bare trunk region => normal nb of substems
			subst_per_segm = substemsPerSegment;
			offs = 0;
		} else if ((segment.index+1)*segmentLength <= par.getBaseSize()*length) {
			// segment is fully part of the bare trunk region => no substems
			return;
		} else {
			// segment has substems in the upper part only
			offs = (par.getBaseSize()*length - segment.index*segmentLength)/segmentLength;
			subst_per_segm = substemsPerSegment*(1-offs);
		}	
		
		// how many substems in this segment
		int substems_eff = (int)(subst_per_segm + lpar.getSubstemErrorValue()+0.5);
		
		// adapt error value
//		lpar.substemErrorValue -= (substems_eff - subst_per_segm);
		lpar.setSubstemErrorValue(lpar.getSubstemErrorValue() - (substems_eff - subst_per_segm));
		
		if (substems_eff <= 0) return;
		
		// what distance between the segements substems
		double dist = (1.0-offs)/substems_eff*lpar_1.getNBranchDist();
		double distv = dist*0.25; // lpar_1.nBranchDistV/2;
		
		for (int s=0; s<substems_eff; s++) {
			// where on the segment add the substem
			double where = offs+dist/2+s*dist+lpar_1.var(distv);
			
			//offset from stembase
			double offset = (segment.index + where) * segmentLength;
			
			Transformation trf = substemDirection(segment.transf,offset);
			trf = segment.substemPosition(trf,where);
			
			// create new substem
			StemImpl substem = new StemImpl(tree,this,stemlevel+1,trf,offset);
			substem.index=substems.size();
			if (substem.make()) {
				substems.addElement(substem);
			}			
		}
	}
	
	/**
	 * For every stem there is a box (as minPoint, maxPoint), 
	 * within the stem with all it's substems of any level
	 * should be contained. The box is calculated by invoking
	 * this method for every point limiting a stem segment.
	 * If such point of a stems segment of substems segment
	 * is outside of the box, the box is adapted.
	 *
	 * @param pt The point which should be inside of the containing box
	 */
	
	public void minMaxTest(Vector pt) {
		maxPoint.setMaxCoord(pt);
		minPoint.setMinCoord(pt);
		
		if (clonedFrom!=null) clonedFrom.minMaxTest(pt);
		if (parent!=null) {
			parent.minMaxTest(pt);
		} else {
			// no parent, this must be a trunk
			tree.minMaxTest(pt);
		}
	}
	
	Transformation newDirection(Transformation transform, int nsegm) {
		Transformation trf = transform;
		// next segments direction
		
		// The first segment shouldn't get another direction 
		// down and rotation angle shouldn't be falsified 
		if (nsegm == 0) return trf;
		
		// get curving angle
		double delta;
		if (lpar.getNCurveBack()==0) {
			delta = lpar.getNCurve() / lpar.getNCurveRes();
			
		} else {
			if (nsegm < (lpar.getNCurveRes()+1)/2) {
				delta = lpar.getNCurve()*2 / lpar.getNCurveRes();
			} else {
				delta = lpar.getNCurveBack()*2 / lpar.getNCurveRes();
			}
		}
		delta += splitCorrection;
		
		trf = trf.rotationX(delta);
		
		
		// With Weber/Penn the orientation of the x- and y-axis 
		// shouldn't be disturbed (maybe, because proper curving relies on this)
		// so may be such random rotations shouldn't be used, instead nCurveV should
		// add random rotation to rotx, and rotate nCurveV about the tree's z-axis too?
		
		// add random rotation about z-axis
		if (lpar.getNCurveV() > 0) {
			delta = lpar.var(lpar.getNCurveV())/lpar.getNCurveRes();
			double rho = 180+lpar.var(180);
			trf = trf.rotationAxisZ(delta,rho);
		}  
		
		// attraction up/down
		if (par.getAttractionUp() != 0 && stemlevel>=2) {
			
			double declination = Math.acos(trf.getZ().getZ());
			
			// 			I don't see, why we need orientation here, may be this avoids
			//          attraction of branches with the x-Axis up and thus avoids
			//			twisting (see below), but why branches in one direction should
			//			be attracted, those with another direction not, this is unnaturally:
			//    		double orient = Math.acos(trf.getY().getZ());
			//    		double curve_up_orig = par.AttractionUp * declination * Math.cos(orient)/lpar.nCurveRes; 
			
			// FIXME: devide by (lpar.nCurveRes-nsegm) if last segment
			// should actually be vertical 
			double curve_up = par.getAttractionUp() * 
			Math.abs(declination * Math.sin(declination)) / lpar.getNCurveRes();
			
			Vector z = trf.getZ();
			// FIXME: the mesh is twisted for high values of AttractionUp
			trf = trf.rotationAxis(-curve_up*180/Math.PI,new Vector(-z.getY(),z.getX(),0));
		}
		return trf;
	}
	
	void prepareSubstemParams() {
		//int level = min(stemlevel+1,3);
		LevelParams lpar_1 = par.getLevelParams(stemlevel+1);
		
		// maximum length of a substem
		lengthChildMax = lpar_1.getNLength()+lpar_1.var(lpar_1.getNLengthV());
		
		// maximum number of substems
		double stems_max = lpar_1.getNBranches();
		
		// actual number of substems and substems per segment
		double substem_cnt;
		if (stemlevel==0) {
			substem_cnt = stems_max;
			substemsPerSegment = substem_cnt / (float)segmentCount / (1-par.getBaseSize());
			
		} else if (par.isPreview()) {
			substem_cnt = stems_max;
			substemsPerSegment = substem_cnt / (float)segmentCount;
		} else if (stemlevel==1) {
			substem_cnt = (int)(stems_max * 
					(0.2 + 0.8*length/parent.length/parent.lengthChildMax));
			substemsPerSegment = substem_cnt / (float)segmentCount;
		} else {
			substem_cnt = (int)(stems_max * (1.0 - 0.5 * offset/parent.length));
			substemsPerSegment = substem_cnt / (float)segmentCount;
		}
		substemRotangle = 0;
		
		// how much leaves for this stem - not really a substem parameter
		if (lpar.getLevel() == par.getLevels()-1) {
			leavesPerSegment = leavesPerBranch() / segmentCount;
		}
	}
	
	void pruning() {
		
		// save random state, split and len values
		lpar.saveState();
		double splitcorr = splitCorrection;
		double origlen = length;
		
		// start pruning
		pruneTest = true;
		
		// test length
		int segm = makeSegments(0,segmentCount);
		
		while (segm >= 0 && length > 0.001*par.getScaleTree()) {
			
			// restore random state and split values
			lpar.restoreState();
			splitCorrection = splitcorr;
			
			// delete segments and clones
			if (clones != null) clones.clear();
			segments.clear();
			
			// get new length
			double minlen = length/2; // shorten max. half of length
			double maxlen = length-origlen/15; // shorten min of 1/15 of orig. len
			length = Math.min(Math.max(segmentLength*segm,minlen),maxlen);
			
			// calc new values dependent from length
			segmentLength = length/lpar.getNCurveRes();
			baseRadius = stemBaseRadius();
			
			if (length>MIN_STEM_LEN && baseRadius < MIN_STEM_RADIUS)
				Ricfoi.logger.warning("WARNING: stem radius ("+baseRadius+") too small for stem "+getTreePosition()); //$NON-NLS-1$ //$NON-NLS-2$
			
			// test once more
			if (length > MIN_STEM_LEN) segm = makeSegments(0,segmentCount);
		}
		// this length fits the envelope, 
		// diminish the effect corresp. to PruneRatio
		length = origlen - (origlen-length)*par.getPruneRatio();
		
		// restore random state and split values
		lpar.restoreState();
		splitCorrection = splitcorr;
		// delete segments and clones
		if (clones != null) clones.clear();
		segments.clear();
		pruneTest = false;
	}
	
	@Override
	public Enumeration<Object> sections() {
		return new SectionsEnumerator(this);
	}
	
	
	Transformation split(Transformation transform, double s_angle, int nseg, int nsplits) {
		Transformation trf = transform;
		// applies a split angle to the stem - the Weber/Penn method
		int remaining_seg = segmentCount-nseg-1;
		
		// the splitangle
		// FIXME: don't know if it should be nSplitAngle or nSplitAngle/2
		double declination = Math.acos(trf.getZ().getZ())*180/Math.PI;
		double split_angle = Math.max(0,(lpar.getNSplitAngle() + lpar.var(lpar.getNSplitAngleV()) - declination));
		
		// FIXME: first works better for level 0, second for further levels
		trf = trf.rotationX(split_angle);
		
		// adapt split correction
		splitCorrection -=  split_angle/remaining_seg;
		
		double split_diverge;
		if (s_angle>0) { // original stem has s_angle==0    
			if (par.get0BaseSplits()>0 && stemlevel==0 && nseg==0) {
				split_diverge = s_angle + lpar.var(lpar.getNSplitAngleV());
			}	else {
				split_diverge = 20 + 0.75 * (30 + Math.abs(declination-90)) 
				* Math.pow((lpar.var(1)+1)/2.0,2);
				if (lpar.var(1) >= 0) split_diverge = - split_diverge;
			}
			
			trf = trf.rotationAxis(split_diverge,Vector.Z_AXIS);
			
		} else split_diverge = 0; // for debugging only
		
		// lower substem prospensity
		if (! pruneTest) {
			substemsPerSegment /= (float)(nsplits+1);
			// FIXME: same reduction for leaves_per_segment?
		}
		return trf;
	}
	
	double stemBaseRadius() {
		
		if (stemlevel == 0) { // trunk
			// radius at the base of the stem
			// I think nScale+-nScaleV should applied to the stem radius but not to base radius(?)
			return length * par.getRatio(); // * par._0Scale; 
		} else {
			// max radius is the radius of the parent at offset
			double max_radius = parent.stemRadius(offset);
			
			// FIXME: RatioPower=0 seems not to work here
			double radius = parent.baseRadius * Math.pow(length/parent.length,par.getRatioPower());
			return Math.min(radius,max_radius);
		}
	}
	
	public Enumeration<LeafImpl> stemLeaves() {
		return leaves.elements();
	}
	
	double stemLength() {
		if (stemlevel == 0) { // trunk
			return (lpar.getNLength() + lpar.var(lpar.getNLengthV())) * par.getScaleTree();
		} else if (stemlevel == 1) {
			double parlen = parent.length;
			double baselen = par.getBaseSize()*par.getScaleTree();
			double ratio  = (parlen-offset)/(parlen-baselen);
			return parlen * parent.lengthChildMax * par.getShapeRatio(ratio);
		} else { // higher levels
			return parent.lengthChildMax*(parent.length-0.6*offset);
		}
	}
	
	public double stemRadius(double h) {
		double angle = 0; //FIXME: add an argument "angle" for Lobes, 
		// but at the moment Lobes are calculated later in mesh creation
		
		// gets the stem width at a given position within the stem
		double Z = Math.min(h/length,1.0); // min, to avoid rounding errors
		double taper = lpar.getNTaper();
		
		double unit_taper=0;
		if (taper <= 1) {
			unit_taper = taper;
		} else if (taper <=2) {
			unit_taper = 2 - taper;
		}
		
		double radius = baseRadius * (1 - unit_taper * Z);
		
		// spherical end or periodic tapering
		double depth;
		if (taper>1) {
			double Z2 = (1-Z)*length;
			if (taper<2 || Z2<radius) {
				depth = 1;
			} else {
				depth=taper-2;
			}
			double Z3;
			if (taper<2) {
				Z3=Z2;
			} else {
				Z3=Math.abs(Z2-2*radius*(int)(Z2/2/radius+0.5));
			}
			if (taper>2 || Z3<radius) {
				radius=(1-depth)*radius+depth*Math.sqrt(radius*radius-(Z3-radius)*(Z3-radius));
			}	  
		}	    
		if (stemlevel==0) { 
			// add flaring (thicker stem base)
			if (par.getFlare() != 0) {
				double y = Math.max(0,1-8*Z);
				double flare = 1 + par.getFlare() * (Math.pow(100,y) - 1) / 100.0;
				radius = radius*flare;
			}
			// add lobes - this is done in mesh creation not here at the moment
			if (par.getLobes()>0 && angle!=0) {
				// FIXME: use the formular from Segment.create_mesh_section() instead
				radius = radius*(1.0+par.getLobeDepth()*Math.sin(par.getLobes()*angle*Math.PI/180));
			}
			
			// multiply with 0Scale;
			// 0ScaleV is applied only in mesh creation (Segment.create_section_meshpoints)
			radius = radius*par.get0Scale();
		}
		
		return radius;
	}
	
	public Enumeration<SegmentImpl> stemSegments() {
		return segments.elements();
	}

	Transformation substemDirection(Transformation trf, double offset) {
		LevelParams lpar_1 = par.getLevelParams(stemlevel+1);
		//lev = min(level+1,3);
		
		// get rotation angle
		double rotangle;
		if (lpar_1.getNRotate()>=0) { // rotating substems
			substemRotangle = (substemRotangle + lpar_1.getNRotate()+lpar_1.var(lpar_1.getNRotateV())+360) % 360;
			rotangle = substemRotangle;
		} else { // alternating substems
			if (Math.abs(substemRotangle) != 1) substemRotangle = 1;
			substemRotangle = -substemRotangle;
			rotangle = substemRotangle * (180+lpar_1.getNRotate()+lpar_1.var(lpar_1.getNRotateV()));
		}
		
		// get downangle
		double downangle;
		if (lpar_1.getNDownAngleV()>=0) {
			downangle = lpar_1.getNDownAngle()+lpar_1.var(lpar_1.getNDownAngleV());
		} else {
			double len = (stemlevel==0)? length*(1-par.getBaseSize()) : length;
			downangle = lpar_1.getNDownAngle() +
			lpar_1.getNDownAngleV()*(1 - 2 * par.getShapeRatio((length-offset)/len,0));
		}  
		
		return trf.rotationXZ(downangle,rotangle);
	}

	long substemTotal() {
		if (substems == null) return 0;
		
		long sum = substems.size();
		for (int i=0; i<substems.size(); i++) {
			// FIXME: what about clones?
			sum += substems.elementAt(i).substemTotal();
		}
		return sum;
	}
	
	@Override
	public boolean traverseTree(TreeTraversal traversal) {
	    if (traversal.enterStem(this))  // enter this tree?
        {
	    
            if (leaves != null) {
            		Enumeration<LeafImpl> l = leaves.elements();
            		while (l.hasMoreElements())
            			if (! l.nextElement().traverseTree(traversal))
            				break;
            }
            
            if (substems != null) {
            		Enumeration<StemImpl> s = substems.elements();
            		while (s.hasMoreElements())
            			if (! s.nextElement().traverseTree(traversal))
            				break;	
            }
            
            if (clones != null) {
            		Enumeration<StemImpl> s = clones.elements();
            		while (s.hasMoreElements())
            			if (! s.nextElement().traverseTree(traversal))
            				break;
            }
        }

        return traversal.leaveStem(this);
	}

}

package jp.gr.java_conf.ricfoi.tree;

import jp.gr.java_conf.ricfoi.math.Transformation;
import jp.gr.java_conf.ricfoi.params.LevelParams;
import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.type.Vector;

public class SegmentImpl implements StemSection {

	public int index;
	Transformation transf;
	double rad1;
	public double rad2;
	StemImpl stem;
	Params par;
	public LevelParams lpar;
	double length;
	java.util.Vector<SubsegmentImpl> subsegments;

	public SegmentImpl(StemImpl stm, int inx, Transformation trf, double r1, double r2) {
		index = inx;
		transf = trf; 
		rad1 = r1;
		rad2 = r2;
		stem = stm;

		par = stem.par;
		lpar = stem.lpar;
		length = stem.segmentLength;
		
		// FIXME: rad1 and rad2 could be calculated only when output occurs (?) or here in the constructor ?
		// FIXME: inialize subsegs with a better estimation of size
		subsegments = new java.util.Vector<>(10);
	}

	@Override
	public double getDistance() {
		return index*length;
	}

	@Override
	public Vector getPosition() {
		return transf.getT();
	}

	@Override
	public double getRadius() {
		return rad1;
	}

	@Override
	public Vector[] getSectionPoints() {
		int pt_cnt = lpar.getMeshPoints();
		Vector[] points;
		Transformation trf = getTransformation();
		double rad = this.rad1;
		
		if (rad<0.000001) {
			points = new Vector[1];
			points[0] = trf.apply(new Vector(0,0,0));
		} else { //create pt_cnt points
			points = new Vector[pt_cnt];
			
			for (int i=0; i<pt_cnt; i++) {
				double angle = i*360.0/pt_cnt;
				// for Lobes ensure that points are near lobes extrema, but not exactly there
				// otherwise there are to sharp corners at the extrema
				if (lpar.getLevel()==0 && par.getLobes() != 0) {
					angle -= 10.0/par.getLobes();
				}
				
				// create some point on the unit circle
				Vector pt = new Vector(Math.cos(angle*Math.PI/180),Math.sin(angle*Math.PI/180),0);

				// scale it to stem radius
				if (lpar.getLevel()==0 && (par.getLobes() != 0 || par.get0ScaleV() !=0)) {
					double rad1 = rad * (1 + 
							par.getRandom().uniform(-par.get0ScaleV(),par.get0ScaleV())/
							getSubsegmentCount());
					pt = pt.multiply(rad1*(1.0+par.getLobeDepth()*Math.cos(par.getLobes()*angle*Math.PI/180.0))); 
				} else {
					pt = pt.multiply(rad); // faster - no radius calculations
				}
				// apply transformation to it
				// (for the first trunk segment transformation shouldn't be applied to
				// the lower meshpoints, otherwise there would be a gap between 
				// ground and trunk)
				// FIXME: for helical stems may be/may be not a random rotation 
				// should applied additionally?
				
				pt = trf.apply(pt);
				points[i] = pt;
			}
		}
		
		return points;
	}

	public double getSubsegmentCount() {
		return subsegments.size();
	}

	@Override
	public Transformation getTransformation() {
		return transf;
	}

	@Override
	public Vector getZ() {
		return transf.getZ();
	}

	public double getLength() { return length; }

	public void addSubsegment(SubsegmentImpl ss) {
		if (subsegments.size() > 0) {
			SubsegmentImpl p = ((SubsegmentImpl)subsegments.elementAt(subsegments.size()-1)); 
			p.next = ss;
			ss.prev = p; 
		}
		subsegments.add(ss);
	}
	
	void minMaxTest() {
		stem.minMaxTest(getUpperPosition());
		stem.minMaxTest(getLowerPosition());
	}
	
	public void make() {
		// FIXME: numbers for cnt should correspond to Smooth value
		// helical stem
		if (lpar.getNCurveV()<0) { 
			makeHelix(10);
		}
		
		// spherical end
		else if (lpar.getNTaper() > 1 && lpar.getNTaper() <=2 && isLastStemSegment()) {
			makeSphericalEnd(10);
		}
		
		// periodic tapering
		else if (lpar.getNTaper()>2) {
			makeSubsegments(20);
		}
		
		// trunk flare
		// FIXME: if nCurveRes[0] > 10 this division into several
		// subsegs should be extended over more then one segments?
		else if (lpar.getLevel()==0 && par.getFlare()!=0 && index==0) {
			
			makeFlare(10);
			
		} else {
			makeSubsegments(1);
		}
		
		// FIXME: for helical stems maybe this test
		// should be made for all subsegments
		minMaxTest();
	}
	
	private void makeSubsegments(int cnt) {
		Vector dir = getUpperPosition().subtract(getLowerPosition());
		for (int i=1; i<cnt+1; i++) {
			double pos = i*length/cnt;
			double rad = stem.stemRadius(index*length + pos);
			
			addSubsegment(new SubsegmentImpl(getLowerPosition().add(dir.multiply(pos/length)),rad, pos, this));
		}
	}
	
	private void makeSphericalEnd(int cnt) {
		Vector dir = getUpperPosition().subtract(getLowerPosition());
		for (int i=1; i<cnt; i++) {
			double pos = length-length/Math.pow(2,i);
			double rad = stem.stemRadius(index*length + pos);
			addSubsegment(new SubsegmentImpl(getLowerPosition().add(dir.multiply(pos/length)),rad, pos, this));
		}
		addSubsegment(new SubsegmentImpl(getUpperPosition(),rad2,length, this));
	}
	
	private void makeFlare(int cnt) {
		Vector dir = getUpperPosition().subtract(getLowerPosition());
		for (int i=cnt-1; i>=0; i--) {
			double pos = length/Math.pow(2,i);
			double rad = stem.stemRadius(index*length+pos);
			addSubsegment(new SubsegmentImpl(getLowerPosition().add(dir.multiply(pos/length)),rad, pos,this));
		}
	}
	
	private void makeHelix(int cnt) {
		double angle = Math.abs(lpar.getNCurveV())/180*Math.PI;
		// this is the radius of the helix
		double rad = Math.sqrt(1.0/(Math.cos(angle)*Math.cos(angle)) - 1)*length/Math.PI/2.0;
		
		for (int i=1; i<cnt+1; i++) {
			Vector pos = new Vector(rad*Math.cos(2*Math.PI*i/cnt)-rad,
					rad*Math.sin(2*Math.PI*i/cnt),
					i*length/cnt);
			// this is the stem radius
			double srad = stem.stemRadius(index*length + i*length/cnt);
			addSubsegment(new SubsegmentImpl(transf.apply(pos), srad, i*length/cnt,this));
		}
	}
	
	public Transformation substemPosition(Transformation trf,double where) {
		if (lpar.getNCurveV()>=0) { // normal segment 
			return trf.translate(transf.getZ().multiply(where*length));
		} else { // helix
			// get index of the subsegment
			int i = (int)(where*(subsegments.size()-1));
			// interpolate position
			Vector p1 = ((SubsegmentImpl)subsegments.elementAt(i)).pos;
			Vector p2 = ((SubsegmentImpl)subsegments.elementAt(i+1)).pos;
			Vector pos = p1.add(p2.subtract(p1).multiply(where - i/(subsegments.size()-1)));
			return trf.translate(pos.subtract(getLowerPosition()));
		}
	}
	
	public Vector getLowerPosition() {
		return transf.getT();
	}
	
	public Vector getUpperPosition() {
		return transf.getT().add(transf.getZ().multiply(length));
	}
	
	public int getIndex() {
		return index;
	}
	
	public double getLowerRadius() {
		return rad1;
	}
	
	public double getUpperRadius() {
		return rad2;
	}

	public boolean isLastStemSegment() {
		// use segmentCount, not segments.size, because clones
		// has less segments, but index starts from where the
		// clone grows out and ends with segmentCount
		return index == stem.segmentCount-1;
	}

}

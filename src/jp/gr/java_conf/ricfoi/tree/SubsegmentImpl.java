package jp.gr.java_conf.ricfoi.tree;

import jp.gr.java_conf.ricfoi.math.Transformation;
import jp.gr.java_conf.ricfoi.params.LevelParams;
import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.type.Vector;

public class SubsegmentImpl implements StemSection { 

	public Vector pos;
	public double dist;
	public double rad;
	SegmentImpl segment;
	public SubsegmentImpl prev=null;
	public SubsegmentImpl next=null;
	
	public SubsegmentImpl(Vector p, double r, double h, SegmentImpl segment) {
		pos = p;
		rad = r;
		dist = h;
		this.segment = segment;
	}
	
	@Override
	public double getDistance() {
		return segment.index * segment.length + dist;
	}
	
	@Override
	public Vector getPosition() {
		return pos;
	}
	
	@Override
	public double getRadius() {
		return rad;
	}
	
	@Override
	public Vector[] getSectionPoints() {
		Params par = segment.par;
		LevelParams lpar = segment.lpar;
		int pt_cnt = lpar.getMeshPoints();
		Vector[] points;
		Transformation trf = getTransformation();
		
		// if radius = 0 create only one point
		if (rad<0.000001) {
			points = new Vector[1];
			points[0] = trf.apply(new Vector(0,0,0));
		} else { //create pt_cnt points
			points = new Vector[pt_cnt];
			for (int i=0; i<pt_cnt; i++) {
				double angle = i*360.0/pt_cnt;
				if (lpar.getLevel()==0 && par.getLobes() != 0) {
					angle -= 10.0/par.getLobes();
				}
				
				Vector pt = new Vector(Math.cos(angle*Math.PI/180),Math.sin(angle*Math.PI/180),0);
				if (lpar.getLevel()==0 && (par.getLobes() != 0 || par.get0ScaleV() !=0)) {
					double rad1 = rad * (1 + 
							par.getRandom().uniform(-par.get0ScaleV(), par.get0ScaleV())/
							segment.getSubsegmentCount());
					pt = pt.multiply(rad1*(1.0+par.getLobeDepth()*Math.cos(par.getLobes()*angle*Math.PI/180.0))); 
				} else {
					pt = pt.multiply(rad); // faster - no radius calculations
				}
				pt = trf.apply(pt);
				points[i] = pt;
			}
		}
		return points;
	}
	
	@Override
	public Transformation getTransformation() { 
		return segment.transf.translate(pos.subtract(segment.getLowerPosition())); 
	}
	
	@Override
	public Vector getZ() {
		return segment.transf.getZ();
	}

}

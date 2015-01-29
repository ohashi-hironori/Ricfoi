package jp.gr.java_conf.ricfoi.mesh;

import java.util.Enumeration;

import jp.gr.java_conf.ricfoi.math.Transformation;
import jp.gr.java_conf.ricfoi.tree.Stem;
import jp.gr.java_conf.ricfoi.tree.StemSection;
import jp.gr.java_conf.ricfoi.type.Vector;

public class MeshPartCreator {

	MeshPart meshPart;
	boolean useQuads;
	Stem stem;
	
	public MeshPartCreator(Stem stem, boolean useQuads) {
		super();
		this.useQuads = useQuads;
		this.stem = stem;
	}

	
	public MeshPart createMeshPart() {
		meshPart = new MeshPart(stem, stem.isSmooth(), useQuads);
		double vLength = stem.getLength()+stem.getBaseRadius()+stem.getPeakRadius();
		
		// first section
		Enumeration<?> sections = stem.sections();
		StemSection section = (StemSection)sections.nextElement();
		
		// first section - create lower meshpoints
		// one point at the stem origin, with normal in reverse z-direction
		createMidPoint(section,0);
		((MeshSection)meshPart.firstElement()).setNormalsToVector(section.getZ().multiply(-1));
	
		while (true) {
			// create meshpoints at each section
			createSectionMeshpoints(section, section.getDistance()/vLength);

			if (! sections.hasMoreElements()) {
				// last section - close mesh with normal in z-direction
				
				if (section.getRadius()>0.000001) {
					createMidPoint(section,1);
				}
				
				((MeshSection)meshPart.lastElement()).setNormalsToVector(section.getZ());
			}

			// next section
			if (sections.hasMoreElements())
				section = (StemSection)sections.nextElement();
			else
				break;
		}
		
		if (meshPart.size()>0)
			return meshPart;
		else
			return null;
	}

	private void createMidPoint(StemSection sec, double vMap) {
		// create only one point in the middle of the sections
		MeshSection section = new MeshSection(1,vMap);
		Transformation trf = sec.getTransformation(); 
		section.addPoint(trf.apply(new Vector(0,0,0)),0.5);
		meshPart.addSection(section);
	}

	private void createSectionMeshpoints(StemSection sec, double vMap) {
		Vector[] points = sec.getSectionPoints();
		
		MeshSection section = new MeshSection(points.length,vMap);
			
		if (points.length == 1)
			section.addPoint(points[0],0.5);
		else { 
			for (int i=0; i<points.length; i++) {
				double angle = i*360.0/points.length;
				section.addPoint(points[i],angle/360.0);
			}
		}	
			//add section to the mesh part
		meshPart.addSection(section);
	}

}

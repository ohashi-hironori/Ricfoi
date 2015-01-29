package jp.gr.java_conf.ricfoi.type;

public class DiscShape extends LeafShape {

	public DiscShape(int triangleCount, double length, double width, double stemLen, boolean useQuads) {
		super(length, width, stemLen, useQuads);
		setVertices(new Vertex[triangleCount+2]);
		if (useQuads) {
			setFaces(new Face[triangleCount / 2 + triangleCount % 2]);
		} else {
			setFaces(new Face[triangleCount]);
		}
		
		setCirclePoints();
		setFaces();
	}
	
	private void setCirclePoints() {
		double angle;
		double x;
		double z;
		int cnt = getVertexCount();
		// set vertices along a circular curve
		for (int i=0; i<cnt; i++) {
			angle = i * 2.0 * Math.PI / cnt;
			x = Math.sin(angle);
			z = Math.cos(angle);
			
			// add a peak to the leaf
			if (angle < Math.PI) {
				x -= leaffunc(angle);
			} else if (angle > Math.PI) {
				x += leaffunc(2*Math.PI-angle);
			}
			setPoint(i, 0.5*x, 0, 0.5*z + 0.5);
		}
	}
	
	private static double leaffunc(double angle) {
		return leaffuncaux(angle)
		- angle*leaffuncaux(Math.PI)/Math.PI;
	}
	
	private static double leaffuncaux(double x) {
		return 0.8 * Math.log(x+1)/Math.log(1.2) - 1.0*Math.sin(x);
	}
	
	private void setFaces() {
		int left = 0;
		int right = getVertexCount()-1;
		//boolean alternate = false;
		// add triangles with an edge on alternating sides
		// of the leaf
		for (int i=0; i<getFaceCount(); i++) {
			if (isUseQuads()) {
				if (left+1<right-1) {
					setFace(i, new Face(left,left+1,right-1,right));
					left++; right--;
				} else {
					// last face is a triangle for odd vertex numbers
					setFace(i, new Face(left,left+1,right));
					left++;
				}
				
			} else {
				if (i % 2 == 0) {
					setFace(i, new Face(left,left+1,right));
					left++;
				} else {
					setFace(i, new Face(left,right-1,right));
					right--;
				}
			}
		}
	}

}

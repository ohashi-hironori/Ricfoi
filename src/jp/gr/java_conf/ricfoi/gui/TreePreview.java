package jp.gr.java_conf.ricfoi.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Enumeration;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.math.Transformation;
import jp.gr.java_conf.ricfoi.mesh.MeshPart;
import jp.gr.java_conf.ricfoi.mesh.MeshSection;
//import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.tree.DefaultTreeTraversal;
import jp.gr.java_conf.ricfoi.tree.Leaf;
import jp.gr.java_conf.ricfoi.tree.PreviewTree;
import jp.gr.java_conf.ricfoi.tree.Stem;
import jp.gr.java_conf.ricfoi.tree.StemSection;
import jp.gr.java_conf.ricfoi.tree.Tree;
import jp.gr.java_conf.ricfoi.tree.TreeTraversal;
import jp.gr.java_conf.ricfoi.type.Face;
import jp.gr.java_conf.ricfoi.type.LeafMesh;
import jp.gr.java_conf.ricfoi.type.Vector;

public class TreePreview extends JComponent {
	private static final long serialVersionUID = 1L;

	PreviewTree previewTree;
	int perspective;
	Config config;

	boolean draft=false;
	final static int PERSPECTIVE_FRONT=0;
	final static int PERSPECTIVE_TOP=90;

	static final Color thisLevelColor = new Color(0.3f,0.2f,0.2f);
	static final Color otherLevelColor = new Color(0.6f,0.6f,0.6f);
	static final Color leafColor = new Color(0.1f,0.6f,0.1f);
	static final Color bgClr = new Color(250,250,245);
	
	AffineTransform transform;
	Transformation rotation;
	Vector origin = new Vector();
	
	public TreePreview(PreviewTree prvTree, int perspect, Config config) {
		super();
		this.config = config;
		setMinimumSize(new Dimension(100,100));
		setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		setOpaque(true);
		setBackground(Color.WHITE);
		
		previewTree = prvTree;
		perspective = perspect;

		initRotation();
		
		previewTree.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				repaint();
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		if (previewTree.getMesh() == null) {
			try {
				previewTree.remake(false);
			} catch (Exception e) {
				Ricfoi.logger.log(Level.WARNING, Constant.ZS, e);
			}
		}
		
		Graphics2D g2 = (Graphics2D)g;
		
		// turn antialiasing on or off
		RenderingHints rh; 
		if (config.getProperty(Config.KEY_PREVIEW_ANTIALIAS).equals(Config.PREVIEW_ANTIALIAS_ON)) {
			rh = new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			rh = new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		g2.addRenderingHints(rh);
		
		try {
			g.clearRect(0,0,getWidth(),getHeight());
			g.setColor(bgClr);
			if (perspective==PERSPECTIVE_FRONT) 
				g.fillRect(0,0,getWidth()-1,getHeight());
			else
				g.fillRect(0,0,getWidth(),getHeight()-1);
			initTransform(/*g*/);
			if (draft) previewTree.traverseTree(new StemDrawer(g)); //drawStems(g);
			else {
				drawMesh(g);
//				Params params = previewTree.getParams();
//				previewTree.traverseTree(new LeafDrawer(g, params,previewTree));
				previewTree.traverseTree(new LeafDrawer(g, previewTree));
			}

		} catch (Exception e) {
			Ricfoi.logger.warning(e.toString());;
			// do nothing, don't draw
		}
	}
	
	public void setDraft(boolean d) {
		draft=d;
	}
	
	protected void initRotation() {
		rotation = new Transformation();
		if (perspective == PERSPECTIVE_TOP) 
			rotation = rotation.rotationX(90);
	}
	
	public void setRotation(double zangle) {
		initRotation();
		rotation = rotation.rotationZ(zangle);
		repaint();
	}
	
	public void setOrigin(Vector orig) {
		origin=orig;
	}
	
	private void initTransform() throws Exception {
		transform = new AffineTransform();
		double dw=1;
		double minw=0;
		double dh=0;
		double minh=0;
		double scale;
		double x;
		double y;
		final int margin=5;
	
		int showLevel = previewTree.getShowLevel();
		
		class FindAStem extends DefaultTreeTraversal {
			Stem found = null;
			int level;
			
			public FindAStem(int level) { this.level=level; }
			public Stem getFound() { return found; }
			@Override
			public boolean enterStem(Stem stem) {
				if (found == null && stem.getLevel() < level)
					return true; // look further
				else if (found != null || stem.getLevel() > level)
					return false; // found a stem or too deep
				else if (stem.getLevel() == level)
					found = stem;
				
				return true;
			}
			@Override
			public boolean leaveTree(Tree tree) {
				return (found != null);
			}
		}
		
		if (showLevel < 1) {
			setOrigin(new Vector());

			//////////// FRONT view
			if (perspective==PERSPECTIVE_FRONT) {
				// get width and height of the tree
				dw = previewTree.getWidth()*2;
				dh = previewTree.getHeight();
				minh = 0;
				minw = -dw/2;
			///////////////// TOP view
			} else {
				// get width of the tree
				dw = previewTree.getWidth()*2;
				minw = -dw/2;
			}
			
		} else {
				
			// find stem which to show
			Stem aStem = null;
			FindAStem stemFinder = new FindAStem(showLevel-1);
			if (previewTree.traverseTree(stemFinder)) {
				aStem = stemFinder.getFound();
			}

			if (aStem != null) {
				Vector diag = aStem.getMaxPoint().subtract(aStem.getMinPoint());
				Vector orig = aStem.getTransformation().getT();
				setOrigin(new Vector(orig.getX(),orig.getY(),0));
				Vector max = aStem.getMaxPoint();
				Vector min = aStem.getMinPoint();
				
				// get greatest distance from orig
				x = Math.max(Math.abs(min.getX()-orig.getX()),
							Math.abs(max.getX()-orig.getX()));
				y = Math.max(Math.abs(min.getY()-orig.getY()),
							Math.abs(max.getY()-orig.getY()));
	
				dw = Math.sqrt(x*x+y*y)*2;
				minw = -dw/2;
				
				dh = diag.getZ();
				minh = min.getZ();
			}
		}

		//////////// FRONT view
		if (perspective==PERSPECTIVE_FRONT) {
			
			// how much to scale for fitting into view?
			scale = Math.min((getHeight()-2*margin)/dh,(getWidth()-2*margin)/dw);
			// shift to mid point of the view
			transform.translate(getWidth()/2,getHeight()/2);
			// scale to image height
			transform.scale(scale,-scale);
			// shift mid of the tree to the origin of the image
			transform.translate(-minw-dw/2,-minh-dh/2);
			
	    ///////////////// TOP view
		} else {
			
			// how much to scale for fitting into view?
			scale = Math.min((getHeight()-2*margin)/dw,(getWidth()-2*margin)/dw);
			// shift to mid point of the view
			transform.translate(getWidth()/2,getHeight()/2);
			// scale to image height
			transform.scale(scale,-scale);
			// shift mid of the stem to the origin of the image
			transform.translate(-minw-dw/2,-minw-dw/2);
		}
		
		// DEBUG
//		Point p = new Point();
//		transform.transform(new Point2D.Double(0.0,0.0),p);
	}
	
	
	private void drawMesh(Graphics g) {
		try {
			for (Enumeration<MeshPart> parts=previewTree.getMesh().elements(); parts.hasMoreElements();) 
			{
				MeshPart m = (MeshPart)parts.nextElement();
				if (m.getLevel()==previewTree.getShowLevel()) {
					g.setColor(thisLevelColor);
				} else {
					g.setColor(otherLevelColor);
				}
				drawMeshPart(g,m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void drawMeshPart(Graphics g, MeshPart m) {
		if (m.size()>0) {
			MeshSection s = (MeshSection)m.elementAt(1);
			
			while (s.next != null) {
				if (s.size()>=s.next.size()) {
					for (int i=0; i<s.size(); i++) {
						drawLine(g,s.here(i),s.up(i));
						drawLine(g,s.here(i),s.right(i));
					} 
					s=s.next;
				} else {
					s=s.next; 
					for (int i=0; i<s.size(); i++) {
						drawLine(g,s.here(i),s.down(i));
						drawLine(g,s.here(i),s.right(i));
					} 
				}
			}
		}
	}
	
	private class LeafDrawer implements TreeTraversal {
		LeafMesh m;
		Graphics g;
		
//		public LeafDrawer(Graphics g, Params params, PreviewTree pTree) {
		public LeafDrawer(Graphics g, PreviewTree pTree) {
			this.g = g;
			this.m = pTree.getLeafMesh();
			g.setColor(leafColor);
		}
		@Override
		public boolean enterTree(Tree tree) { return true; }
		@Override
		public boolean leaveTree(Tree tree) { return true; }
		@Override
		public boolean enterStem(Stem stem) { return true; }
		@Override
		public boolean leaveStem(Stem stem) { return true; }
		@Override
		public boolean visitLeaf(Leaf leaf) {
			if (m.isFlat()) {
				Vector p = leaf.getTransformation().apply(m.shapeVertexAt(m.getShapeVertexCount()-1).getPoint());
			
				for (int i=0; i<m.getShapeVertexCount(); i++) {
					Vector q = leaf.getTransformation().apply(m.shapeVertexAt(i).getPoint());
					drawLine(g,p,q);
					p=q;
				}
			} else {
				for (int i=0; i<m.getShapeFaceCount(); i++) {
					Face f = m.shapeFaceAt(i);
					Vector p = leaf.getTransformation().apply(m.shapeVertexAt((int)f.getPoint(0)).getPoint());
					Vector q = leaf.getTransformation().apply(m.shapeVertexAt((int)f.getPoint(1)).getPoint());
					Vector r = leaf.getTransformation().apply(m.shapeVertexAt((int)f.getPoint(2)).getPoint());
					drawLine(g,p,q);
					drawLine(g,p,r);
					drawLine(g,r,q);
				}
				
			}
			return true;
		}
	}
	
	
	void drawLine(Graphics g,Vector p, Vector q) {
		// FIXME: maybe eliminate class instantiations
		// from this method for performance reasons:
		// use static point arrays for transformations
		
		Vector u = rotation.apply(p.subtract(origin));
		Vector v = rotation.apply(q.subtract(origin));

		Point2D.Double from = new Point2D.Double(u.getX(),u.getZ());
		Point2D.Double to = new Point2D.Double(v.getX(),v.getZ());
		
		Point ifrom = new Point();
		Point ito = new Point();
		
		transform.transform(from,ifrom);
		transform.transform(to,ito);
		
		g.drawLine(ifrom.x,ifrom.y,ito.x,ito.y);
	}
	
	private class StemDrawer implements TreeTraversal {
//		Stem stem;
		Graphics g;

		public StemDrawer(Graphics g) {
			this.g = g;
			g.setColor(otherLevelColor);
		}

		@Override
		public boolean enterTree(Tree tree) { return true; }
		@Override
		public boolean leaveTree(Tree tree) { return true; }
		@Override
		public boolean enterStem(Stem stem) {
			Enumeration<?> sections = stem.sections();
			
			if (sections.hasMoreElements()) {
				StemSection from; StemSection to;
				from = (StemSection)sections.nextElement();
			
				while (sections.hasMoreElements()) {
					to = (StemSection)sections.nextElement();
					// FIXME: maybe draw rectangles instead of thin lines
					drawLine(g,from.getPosition(),to.getPosition());
					from = to;
				}
			}
			return true;
		}
		@Override
		public boolean leaveStem(Stem stem) { return true; }
		@Override
		public boolean visitLeaf(Leaf l) { return true; }
	}
}

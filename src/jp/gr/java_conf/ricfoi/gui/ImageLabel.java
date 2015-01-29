package jp.gr.java_conf.ricfoi.gui;

import java.awt.Color;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.Ricfoi;

public class ImageLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	public final static ImageIcon ICON_SHAPE   = createImageIcon("images/shape.png","Tree shape");
	public final static ImageIcon ICON_RADIUS  = createImageIcon("images/radius.png","Trunk radius");
	public final static ImageIcon ICON_LEAVES  = createImageIcon("images/leaves.png","Leaves");
	public final static ImageIcon ICON_PRUNE   = createImageIcon("images/pruning.png","Pruning/Envelope");
	public final static ImageIcon ICON_MISC    = createImageIcon("images/misc.png","Miscellaneous parameters");
	public final static ImageIcon ICON_LEN_TAP = createImageIcon("images/len_tapr.png","Length and taper");
	public final static ImageIcon ICON_CURVE   = createImageIcon("images/curve.png","Curvature");
	public final static ImageIcon ICON_SPLIT   = createImageIcon("images/splitting.png","Splitting");
	public final static ImageIcon ICON_SUBSTEM = createImageIcon("images/substem.png","Branching");

	private static final Color _bgClr = new Color(242,242,229);

	public ImageLabel() {
		super(Constant.ZS, ICON_SHAPE, SwingConstants.CENTER);
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(2,2,2,2), "Tree shape", TitledBorder.CENTER, TitledBorder.BOTTOM));
		setOpaque(true);
		setBackground(_bgClr);
	}

    private static ImageIcon createImageIcon(String path, String description) {
    	URL url = Ricfoi.class.getResource(path);
    	if (url != null) {
    		return new ImageIcon(url, description);
    	} else {
    		return null;
    	}
    }


}

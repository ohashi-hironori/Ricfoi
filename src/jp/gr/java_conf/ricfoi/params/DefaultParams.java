package jp.gr.java_conf.ricfoi.params;

import java.util.Hashtable;

import jp.gr.java_conf.ricfoi.gui.GroupNode;

public class DefaultParams extends Hashtable<String, AbstractParam> {
	private static final long serialVersionUID = 1L;

	private int order;

	public DefaultParams() {
		super();
		initialize();
	}

	private void strParam(String name, String deflt,
			String group, String short_desc, String long_desc) {
		put(name,new StringParam(name,deflt,group,GroupNode.LEVEL_GENERAL,
				order++,short_desc,long_desc));
	}

	private void intParam(String name, int min, int max, int deflt,
			String group, String short_desc, String long_desc) {
		put(name,new IntParam(name,min,max,deflt,group,GroupNode.LEVEL_GENERAL,
				order++,short_desc,long_desc));
	}
	
	private void shapeParam(String name, int min, int max, int deflt,
			String group, String short_desc, String long_desc) {
		put(name,new ShapeParam(name,min,max,deflt,group,GroupNode.LEVEL_GENERAL,
				order++,short_desc,long_desc));
	}	
	
	private void dblParam(String name, double min, double max, double deflt,
			String group, String short_desc, String long_desc) {
		put(name,new FloatParam(name,min,max,deflt,group,GroupNode.LEVEL_GENERAL,
				order++,short_desc,long_desc));
	}

	private void lshParam(String name, String deflt,
			String group, String short_desc, String long_desc) {
		put(name,new LeafShapeParam(name,deflt,group,GroupNode.LEVEL_GENERAL,
				order++,short_desc,long_desc));
	}

	private void dbl4Param(String name, double min, double max, 
			double deflt0, double deflt1, double deflt2, double deflt3,
			String group, String short_desc, String long_desc) {
		double [] deflt = {deflt0,deflt1,deflt2,deflt3};
		order++;
		for (int i=0; i<4; i++) {
			String fullname = Integer.toString(i) + name.substring(1);
			put(fullname,new FloatParam(fullname,min,max,deflt[i],group,i,
					order,short_desc,long_desc));
		}
	}
	
	private void int4Param(String name, int min, int max, 
			int deflt0,int deflt1, int deflt2, int deflt3,
			String group, String short_desc, String long_desc) {
		int [] deflt = {deflt0,deflt1,deflt2,deflt3};
		order++;
		for (int i=0; i<4; i++) {
			String fullname = Integer.toString(i) + name.substring(1);
			put(fullname,new IntParam(fullname,min,max,deflt[i],group,i,
					order,short_desc,long_desc));
		}
	}

	@SuppressWarnings("nls")
	private void initialize() {
		order=1;
		
		strParam(Params.NAME_SPECIES,Params.DEFAULT_SPECIES,
				GroupNode.NAME_SHAPE,"the tree's species",
				"<strong>Species</strong> is the kind of tree.<br>\n"+
				"It is used for declarations in the output file.<br>\n");

		shapeParam (Params.NAME_SHAPE,0,8,0,GroupNode.NAME_SHAPE,"general tree shape id",
				"The <strong>Shape</strong> can be one of:<ul>\n"+
				"<li>0 - conical</li>\n"+
				"<li>1 - spherical</li>\n"+
				"<li>2 - hemispherical</li>\n"+
				"<li>3 - cylindrical</li>\n"+
				"<li>4 - tapered cylindrical</li>\n"+
				"<li>5 - flame</li>\n"+
				"<li>6 - inverse conical</li>\n"+
				"<li>7 - tend flame</li>\n"+
				"<li>8 - envelope - uses pruning envelope<br>\n"+
				"(see PruneWidth, PruneWidthPeak, PrunePowerLow, PrunePowerHigh)</li></ul>\n"
		);
		
		intParam(Params.NAME_LEVELS,0,9,3,GroupNode.NAME_SHAPE,"levels of recursion",
				"<strong>Levels</strong> are the levels of recursion when creating the\n"+
				"stems of the tree.<ul>\n" +
				"<li>Levels=1 means the tree consist only of the (may be splitting) trunk</li>\n"+
				"<li>Levels=2 the tree consist of the trunk with one level of branches</li>\n"+
				"<li>Levels>4 seldom necessary, the parameters of the forth level are used\n"+
				"for all higher levels.</li></ul>\n"+
				"Leaves are considered to be one level above the last stem level.<br>\n"+
				"and uses it's down and rotation angles.\n"
		);
		
		dblParam(Params.NAME_SCALE,0.000001,Double.POSITIVE_INFINITY,10.0,
				GroupNode.NAME_SHAPE,"average tree size in meters",
				"<strong>Scale</strong> is the average tree size in meters.<br>\n"+
				"With Scale = 10.0 and ScaleV = 2.0 trees of this species\n"+
				"reach from 8.0 to 12.0 meters.<br>\n"+
				"Note, that the trunk length can be different from the tree size.\n"+
				"(See 0Length and 0LengthV)\n"
		);
		
		dblParam(Params.NAME_SCALE_V,0.0,Double.POSITIVE_INFINITY,0.0,
				GroupNode.NAME_SHAPE,"variation of tree size in meters",
				"<strong>ScaleV</strong> is the variation range of the tree size in meters.<br>\n"+
				"Scale = 10.0, ScaleV = 2.0 means trees of this species\n"+
				"reach from 8.0 to 12.0 meters.\n"+
				"(See Scale)\n"
		);
		
		dblParam (Params.NAME_BASE_SIZE,0.0,1.0,0.25,GroupNode.NAME_SHAPE,"fractional branchless area at tree base",
				"<strong>BaseSize</strong> is the fractional branchless part of the trunk. E.g.\n<ul>"+
				"<li>BaseSize=&nbsp;&nbsp;0</code> means branches begin on the bottom of the tree,</li>\n"+
				"<li>BaseSize=0.5</code> means half of the trunk is branchless,</li>\n"+
				"<li>BaseSize=1.0</code> branches grow out from the peak of the trunk only.</li></ul>\n"
		);
		
		intParam(Params.NAME_0BASE_SPLITS,0,Integer.MAX_VALUE,0,GroupNode.NAME_SHAPE,
				"stem splits at base of trunk",
				"<strong>BaseSplits</strong> are the stem splits at the top of the first trunk segment.<br>\n"+
				"So with BaseSplits=2 you get a trunk splitting into three parts. Other then<br>\n"+
				"with 0SegSplits the clones are evenly distributed over<br>\n"+
				"the 360&deg;. So, if you want to use splitting, you should<br>\n"+
				"use BaseSplits for the first splitting to get a circular<br>\n"+
				"stem distribution (seen from top).<br>\n"
		);
		
		dblParam(Params.NAME_RATIO,0.000001,Double.POSITIVE_INFINITY,0.05,GroupNode.NAME_TRUNK,
				"trunk radius/length ratio",
				"<strong>Ratio</strong> is the radius/length ratio of the trunk.<br>\n"+
				"Ratio=0.05 means the trunk is 1/20 as thick as it is long,<br>\n"+
				"t.e. a 10m long trunk has a base radius of 50cm.<br>\n"+
				"Note, that the real base radius could be greater, when Flare<br>\n"+
				"and/or Lobes are used. (See Flare, Lobes, LobesDepth, RatioPower)\n"
		);
		
		dblParam(Params.NAME_RATIO_POWER,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,1.0,
				GroupNode.NAME_SHAPE,"radius reduction",
				"<strong>RatioPower</strong> is a reduction value for the radius of the\n"+
				"substems.\n<ul>"+
				"<li>RatioPower=1.0  means the radius decreases linearly with\n"+
				"decreasing stem length</li>\n"+
				"<li>RatioPower=2.0  means it decreases with the second power</li>\n"+
				"<li>RatioPower=0.0  means radius is the same as parent radius\n"+
				"(t.e. it doesn't depend of the length)</li>\n"+
				"<li>RatioPower=-1.0 means the shorter the stem the thicker it is\n"+
				"(radius = parent radius * 1 / length)</li></ul>\n"+
				"Note, that the radius of a stem cannot be greater then the parent radius at the stem offset.<br>\n"+
				"So with negative RatioPower you cannot create stems thicker than it's parent.<br>\n"+
				"Instead you can use it to make stems thinner, which are longer than it's parent.<br>\n"+
				"(See Ratio)\n"
		);
		
		dblParam(Params.NAME_FLARE,-1.0,Double.POSITIVE_INFINITY,0.5,
				GroupNode.NAME_TRUNK,"exponential expansion at base of tree",
				"<strong>Flare</strong> makes the trunk base thicker.<ul>\n"+
				"<li>Flare = 0.0 means base radius is used at trunk base</li>\n"+
				"<li>Flare = 1.0 means trunk base is twice as thick as it's base radius\n"+
				"(See Ratio)</li></ul>\n"+
				"Note, that using Lobes make the trunk base thicker too.\n"+
				"(See Lobes, LobeDepth)\n"
		);
		
		intParam(Params.NAME_LOBES,0,Integer.MAX_VALUE,0,GroupNode.NAME_TRUNK,
				"sinusoidal cross-section variation",
				"With <strong>Lobes</strong> you define how much lobes (this are variations in it's<br>\n"+
				"cross-section) the trunk will have. This isn't supported for<br>\n"+
				"cones output, but for mesh only.<br>\n"+
				"(See LobeDepth too)\n"
		);
		
		dblParam(Params.NAME_LOBE_DEPTH,0,Double.POSITIVE_INFINITY,0,
				GroupNode.NAME_TRUNK,"amplitude of cross-section variation",
				"<strong>LobeDepth</strong> defines, how deep the lobes of the trunk will be.<br>\n"+
				"This is the amplitude of the sinusoidal cross-section variations.<br>\n"+
				"(See Lobes)\n"
		);
		
		intParam(Params.NAME_LEAVES,Integer.MIN_VALUE,Integer.MAX_VALUE,0,
				GroupNode.NAME_LEAVES,"number of leaves per stem",
				"<strong>Leaves</strong> gives the maximal number of leaves per stem.<br>\n"+
				"Leaves grow only from stems of the last level. The actual number of leaves on a stem,<br>\n"+
				"depending on the stem offset and length, can be smaller than Leaves.<br>\n"+
				"When Leaves is negative, the leaves grow in a fan at\n"+
				"the end of the stem.\n"
		);
		
		lshParam(Params.NAME_LEAF_SHAPE,"0",GroupNode.NAME_LEAVES,"leaf shape id",
				"<strong>LeafShape</strong> is the shape of the leaf (\"0\" means oval shape).<br>\n"+
				"The length and width of the leaf are given by LeafScale and LeafScaleX.<br>\n"+
				
				"When creating a mesh at the moment you can use the following values:<ul>\n"+
				"<li>\"disc\" - a surface consisting of 6 triangles approximating an oval shape</li>\n"+
				"<li>\"sphere\" - an ikosaeder approximating a shperical shape,<br>\n"+
				"useful for making knots or seeds instead of leaves, or for high quality needles</li>\n"+
				"<li>\"disc1\", \"disc2\", ... - a surface consisting of 1, 2, ... triangles approximating an oval shape<br>\n"+
				"lower values are useful for low quality needles or leaves, to reduce mesh size,<br>\n"+
				"values between 6 and 10 are quite good for big, round leaves.</li>\n"+
				"<li>any other - same like disc</li></ul>\n"+
				
				"When using primitives output, the possible values of LeafShape references<br>\n"+
				"the declarations in arbaro.inc. At the moment there are:<ul>\n"+
				"<li>\"disc\" the standard oval form of a leaf, defined<br>\n"+
				"as a unit circle of radius 0.5m. The real<br>\n"+
				"length and width are given by the LeafScale parameters.</li>\n"+
				"<li>\"sphere\" a spherical form, you can use to<br>\n"+
				"simulate seeds on herbs or knots on branches like in the<br>\n"+
				"desert bush. You can use the sphere shape for needles too,<br>\n"+
				"thus they are visible from all sides</li>\n"+
				"<li>\"palm\" a palm leaf, this are two disc halfs put together<br>\n"+
				"with an angle between them. So they are visible<br>\n"+
				"also from the side and the light effects are<br>\n"+
				"more typically, especialy for fan palms seen from small distances.</li>\n"+
				"<li>any other - add your own leaf shape to the file arbaro.inc</li></ul>\n"
		);
		
		dblParam(Params.NAME_LEAF_SCALE,0.000001,Double.POSITIVE_INFINITY,0.2,
				GroupNode.NAME_LEAVES,"leaf length",
				"<strong>LeafScale</strong> is the length of the leaf in meters.<br>\n"+
				"The unit leaf is scaled in z-direction (y-direction in Povray)\n"+
				"by this factor. (See LeafShape, LeafScaleX)\n"
		);
		
		dblParam(Params.NAME_LEAF_SCALE_X,0.000001,Double.POSITIVE_INFINITY,0.5,GroupNode.NAME_LEAVES,
				"fractional leaf width",
				"<strong>LeafScaleX</strong> is the fractional width of the leaf relativly to it's length. So<ul>\n"+
				"<li>LeafScaleX=0.5 means the leaf is half as wide as long</li>\n"+
				"<li>LeafScaleX=1.0 means the leaf is like a circle</li></ul>\n"+
				"The unit leaf is scaled by LeafScale*LeafScaleX in x- and\n"+
				"y-direction (x- and z-direction in Povray).<br>\n"+
				"So the spherical leaf is transformed to a needle 5cm long and<br>\n"+
				"1mm wide by LeafScale=0.05 and LeafScaleX=0.02.\n"
		);
		
		dblParam(Params.NAME_LEAF_BEND,0,1,0.3,GroupNode.NAME_LEAVES,"leaf orientation toward light",
				"With <strong>LeafBend</strong> you can influence, how much leaves are oriented<br>\n"+
				"outside and upwards.<br>Values near 0.5 are good. For low values the leaves<br>\n"+
				"are oriented to the stem, for high value to the light.<br>\n"+
				"For trees with long leaves like palms you should use lower values.\n"
		);
		
		dblParam(Params.NAME_LEAF_STEM_LENGTH,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,0.5,
				GroupNode.NAME_LEAVES,"fractional leaf stem length",
				"<strong>LeafStemLen</strong is the length of the (virtual) leaf stem.<br>\n"+
				"It's not drawn, so this is the distance between the stem<br>\n"+
				"axis and the leaf. For normal trees with many nearly circular<br>\n"+
				"leaves the default value of 0.5 (meaning the stem has half of the length<br>\n"+
				"of the leaf) is quite good. For other trees like palms with long leaves<br>\n"+
				"or some herbs you need a LeafStemLen near 0. Negative stem length is<br>\n"+
				"allowed for special cases."
		);
		
		intParam (Params.NAME_LEAF_DISTRIB,0,8,4,GroupNode.NAME_LEAVES,"leaf distribution",
				"<strong>LeafDistrib</strong> determines how leaves are distributed over<br>\n"+
				"the branches of the last but one stem level. It takes the same<br>\n"+
				"values like Shape, meaning 3 = even distribution, 0 = most leaves<br>\n"+
				"outside. Default is 4 (some inside, more outside)."
		);
		
		dblParam(Params.NAME_LEAF_QUALITY,0.000001,1.0,1.0,
				GroupNode.NAME_QUALITY,"leaf quality/leaf count reduction",
				"With a <strong>LeafQuality</strong> less then 1.0 you can reduce the number of leaves<br>\n"+
				"to improve rendering speed and memory usage. The leaves are scaled<br>\n"+
				"with the same amount to get the same coverage.<br>\n"+
				"For trees in the background of the scene you will use a reduced<br>\n"+
				"LeafQuality around 0.9. Very small values would cause strange results.<br>\n"+
				"(See LeafScale)" 
		);
		
		dblParam(Params.NAME_SMOOTH,0.0,1.0,0.5,
				GroupNode.NAME_QUALITY,"smooth value for mesh creation",
				"Higher <strong>Smooth</strong> values creates meshes with more vertices and<br>\n"+
				"adds normal vectors to them for some or all branching levels.<br>\n"+
				"Normally you would specify this value on the command line or in<br>\n"+
				"the rendering dialog, but for some species a special default<br>\n"+
				"smooth value could be best. E.g. for shave-grass a low smooth value<br>\n"+
				"is preferable, because this herb has angular stems."
		);
		
		dblParam(Params.NAME_ATTRACTION_UP,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,0.0,
				GroupNode.NAME_SHAPE,"upward/downward growth tendency",
				"<strong>AttractionUp</strong> is the tendency of stems with level>=2 to grow upwards<br>\n"+
				"(downwards for negative values).<br>\n"+
				"A value of 1.0 for a horizontal stem means the last segment should point upwards.<br>\n"+
				"Greater values means earlier reaching of upward direction. Values of 10 and greater<br>\n"+
				"could cause overcorrection resulting in a snaking oscillation.<br>\n"+
				"As an example see the weeping willow, which has a negative AttractionUp value.\n"
		);
		
		dblParam(Params.NAME_PRUNE_RATIO,0.0,1.0,0.0,GroupNode.NAME_PRUNING,
				"fractional effect of pruning",
				"A <strong>PruneRatio</strong> of 1.0 means all branches are inside<br>\n"+
				"the envelope. 0.0 means no pruning.\n"
		);
		
		dblParam(Params.NAME_PRUNE_WIDTH,0.0,1.0,0.5,GroupNode.NAME_PRUNING,"width of envelope peak",
				"<strong>PruneWidth</strong> is the fractional width of the pruning envelope at the<br>\n"+
				"peak. A value of 0.5 means the tree is half as wide as high.<br>\n"+
				"This parameter is used for the shape \"envelope\" too, even if PruneRatio is off.\n"
		);
		
		dblParam(Params.NAME_PRUNE_WIDTH_PEAK,0.0,1.0,0.5,GroupNode.NAME_PRUNING,"position of envelope peak",
				"<strong>PruneWidthPeak</strong> is the fractional height of the envelope peak.<br>\n"+
				"A value of 0.5 means upper part and lower part of the envelope have the same height.<br>\n"+
				"This parameter is used for the shape \"envelope\" too, even if PruneRatio is off.\n"
		);
		
		dblParam(Params.NAME_PRUNE_POWER_LOW,0.0,Double.POSITIVE_INFINITY,0.5,GroupNode.NAME_PRUNING,
				"curvature of envelope",
				"<strong>PrunePowerLow</strong> describes the envelope curve below the peak.<br>\n"+
				"A value of 1 means linear decreasing. Higher values means concave,<br>\n"+
				"lower values convex curve.<br>\n"+
				"This parameter is used for the shape \"envelope\" too, even if PruneRatio is off.\n"
		);
		
		dblParam(Params.NAME_PRUNE_POWER_HIGH,0.0,Double.POSITIVE_INFINITY,0.5,GroupNode.NAME_PRUNING,
				"curvature of envelope",
				"<strong>PrunePowerHigh</strong> describes the envelope curve above the peak.<br>\n"+
				"A value of 1 means linear decreasing. Higher values means concave,<br>\n"+
				"lower values convex curve.<br>\n"+
				"This parameter is used for the shape \"envelope\" too, even if PruneRatio is off.\n"
		);
		
		dblParam(Params.NAME_0SCALE,0.000001,Double.POSITIVE_INFINITY,1.0,
				GroupNode.NAME_TRUNK,"extra trunk scaling",
				"<strong>0Scale</strong> and 0ScaleV makes the trunk thicker.<br>\n"+
				"This parameters exists for the level 0 only. From the Weber/Penn paper it is<br>\n"+
				"not clear, why there are two trunk scaling parameters<br> \n"+
				"0Scale and Ratio. See Ratio, 0ScaleV, Scale, ScaleV.<br>\n"+
				"In this implementation 0Scale does not influence the trunk base radius<br>\n"+
				"but is applied finally to the stem radius formular. Thus the<br>\n"+
				"trunk radius could be influenced independently from the<br>\n"+
				"Ratio/RatioPower parameters and the periodic tapering (0Taper > 2.0)<br>\n"+
				"could be scaled, so that the sections are elongated spheres.\n"
		);
		
		dblParam(Params.NAME_0SCALE_V,0.0,Double.POSITIVE_INFINITY,0.0,GroupNode.NAME_TRUNK,
				"variation for extra trunk scaling",
				"0Scale and <strong>0ScaleV</strong> makes the trunk thicker. This parameters<br>\n"+
				"exists for the level 0 only. From the Weber/Penn paper it is<br>\n"+
				"not clear, why there are two trunk scaling parameters<br>\n"+
				"0Scale and Ratio. See Ratio, 0ScaleV, Scale, ScaleV.<br>\n"+
				"In this implementation 0ScaleV is used to perturb the<br>\n"+
				"mesh of the trunk. But use with care, because the mesh<br>\n"+
				"could got fissures when using too big values.<br>\n"
		);
		
		dbl4Param(Params.NAME_N_LENGTH,0.0000001,Double.POSITIVE_INFINITY,1.0,0.5,0.5,0.5,
				GroupNode.NAME_LENTAPER,"fractional trunk scaling",
				"<strong>0Length</strong> and 0LengthV give the fractional length of the<br>\n"+
				"trunk. So with Scale=10 and 0Length=0.8 the length of the<br>\n"+
				"trunk will be 8m. Dont' confuse the height of the tree with<br>\n"+
				"the length of the trunk here.<br><br>\n"+
				"<strong>nLength</strong> and nLengthV define the fractional length of a stem<br>\n"+
				"relating to the length of theire parent.<br>\n"
		);
		
		dbl4Param(Params.NAME_N_LENGTH_V,0.0,Double.POSITIVE_INFINITY,0.0,0.0,0.0,0.0,
				GroupNode.NAME_LENTAPER,"variation of fractional trunk scaling",
				"<strong>nLengthV</strong> is the variation of the length given by nLength.<br>\n"
		);
		
		dbl4Param(Params.NAME_N_TAPER,0.0,2.99999999,1.0,1.0,1.0,1.0,
				GroupNode.NAME_LENTAPER,"cross-section scaling",
				"<strong>nTaper</strong> is the tapering of the stem along its length.<ul>\n"+
				"<li>0 - non-tapering cylinder</li>\n"+
				"<li>1 - taper to a point (cone)</li>\n"+
				"<li>2 - taper to a spherical end</li>\n"+
				"<li>3 - periodic tapering (concatenated spheres)</li></ul>\n"+
				"You can use fractional values, to get intermediate results.<br>\n"
		);
		
		dbl4Param("nSegSplits",0,Double.POSITIVE_INFINITY,0,0,0,0,
				GroupNode.NAME_SPLITTING,"stem splits per segment",
				"<strong>nSegSplits</strong> determines how much splits per segment occures.<br><br>\n"+
				"Normally you would use a value between 0.0 and 1.0. A value of<br>\n"+
				"0.5 means a split at every second segment. If you use splitting<br>\n"+
				"for the trunk you should use 0BaseSplits for the first split, <br>\n"+
				"otherwise the tree will tend to one side."
		);
		
		dbl4Param("nSplitAngle",0,180,0,0,0,0,GroupNode.NAME_SPLITTING,
				"splitting angle",
				"<strong>nSplitAngle</strong> is the vertical splitting angle. A horizontal diverging<br>\n"+
				"angle will be added too, but this one you cannot influence with parameters.<br>\n"+
				"The declination of the splitting branches won't exceed the splitting angle.<br>\n"
		);
		
		dbl4Param("nSplitAngleV",0,180,0,0,0,0,GroupNode.NAME_SPLITTING,
				"splitting angle variation",
				"<strong>nSplitAngleV</strong> is the variation of the splitting angle. See nSplitAngle.<br>\n"
		);
		
		int4Param("nCurveRes",1,Integer.MAX_VALUE,3,3,1,1,
				GroupNode.NAME_CURVATURE,"curvature resolution",
				"<strong>nCurveRes</strong> determines how many segments the branches consist of.<br><br>\n"+
				"Normally you will use higher values for the first levels, and low<br>\n"+
				"values for the higher levels.<br>\n"
		);
		
		dbl4Param("nCurve",Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,0,0,0,0,
				GroupNode.NAME_CURVATURE,"curving angle",
				"<strong>nCurve</strong> is the angle the branches are declined over theire whole length.<br>\n"+
				"If nCurveBack is used, the curving angle is distributed only over the<br>\n"+
				"first half of the stem.<br>\n"
		);
		
		dbl4Param("nCurveV",-90,Double.POSITIVE_INFINITY,0,0,0,0,
				GroupNode.NAME_CURVATURE,"curving angle variation",
				"<strong>nCurveV</strong> is the variation of the curving angle. See nCurve, nCurveBack.<br>\n"+
				"A negative value means helical curvature<br>\n"
		);
		
		dbl4Param("nCurveBack",Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,0,0,0,0,
				GroupNode.NAME_CURVATURE,"curving angle upper stem half",
				"Using <strong>nCurveBack</strong> you can give the stem an S-like shape.<br>\n"+
				"The first half of the stem the nCurve value is applied.<br>\n"+
				"The second half the nCurveBack value.<br><br>\n"+
				"It's also possible to give both parametera the same sign to<br>\n"+
				"get different curving over the stem length, instead of a S-shape<br>\n"
		);
		
		dbl4Param("nDownAngle",-179.9999999,179.999999,0,30,30,30,
				GroupNode.NAME_BRANCHING,"angle from parent",
				"<strong>nDownAngle</strong> is the angle between a stem and it's parent.<br>\n"
		);
		
		dbl4Param("nDownAngleV",-179.9999999,179.9999999,0,0,0,0,
				GroupNode.NAME_BRANCHING,"down angle variation",
				"<strong>nDownAngleV</strong> is the variation of the downangle. See nDownAngle.<br>\n"+
				"Using a negative value, the nDownAngleV is variated over the<br>\n"+
				"length of the stem, so that the lower branches have a bigger<br>\n"+
				"downangle then the higher branches.<br>\n"
		);
		
		dbl4Param("nRotate",-360,360,0,120,120,120,
				GroupNode.NAME_BRANCHING,"spiraling angle",
				"<strong>nRotate</strong> is the angle, the branches are rotating around the parent<br>\n"+
				"If nRotate is negative the branches are located on alternating<br>\n"+
				"sides of the parent.<br>\n"
		);
		
		dbl4Param("nRotateV",-360,360,0,0,0,0,
				GroupNode.NAME_BRANCHING,"spiraling angle variation",
				"<strong>nRotateV</strong> is the variation of nRotate.<br>\n"
		);
		
		int4Param("nBranches",0,Integer.MAX_VALUE,1,10,5,5,
				GroupNode.NAME_BRANCHING,"number of branches",
				"<strong>nBranches</strong> is the maximal number of branches on a parent stem.<br>\n"+
				"The number of branches are reduced proportional to the<br>\n"+
				"relative length of theire parent.<br>\n"
		);
		
		dbl4Param("nBranchDist",0,1,0,1,1,1,
				GroupNode.NAME_BRANCHING,"branch distribution along the segment",
				"<strong>nBranchDist</strong> is an additional parameter of Arbaro. It influences the<br>\n"+
				"distribution of branches over a segment of the parent stem.<br>\n"+
				"With 1.0 you get evenly distribution of branches like in the<br>\n"+
				"original model. With 0.0 all branches grow from the segments<br>\n"+
				"base like for conifers.<br>\n"
		);
	}

}

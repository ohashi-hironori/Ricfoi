package jp.gr.java_conf.ricfoi.params;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TreeMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import jp.gr.java_conf.ricfoi.math.Random;

import org.xml.sax.InputSource;

public class Params {

	// Tree Shapes 
	public final static int CONICAL             = 0;
	public final static int SPHERICAL           = 1;
	public final static int HEMISPHERICAL       = 2;
	public final static int CYLINDRICAL         = 3;
	public final static int TAPERED_CYLINDRICAL = 4;
	public final static int FLAME               = 5;
	public final static int INVERSE_CONICAL     = 6;
	public final static int TEND_FLAME          = 7;
	public final static int ENVELOPE            = 8;
	
	public static final String NAME_LEVELS = "Levels";
	public static final String NAME_SMOOTH = "Smooth";
	public static final String NAME_RATIO_POWER = "RatioPower";
	public static final String NAME_LEAVES = "Leaves";
	public static final String NAME_LEAF_SHAPE = "LeafShape";
	public static final String NAME_LEAF_SCALE = "LeafScale";
	public static final String NAME_LEAF_SCALE_X = "LeafScaleX";
	public static final String NAME_LEAF_BEND = "LeafBend";
	public static final String NAME_LEAF_DISTRIB = "LeafDistrib";
	public static final String NAME_LEAF_QUALITY = "LeafQuality";
	public static final String NAME_LEAF_STEM_LENGTH = "LeafStemLen";
	public static final String NAME_SHAPE = "Shape";
	public static final String NAME_PRUNE_RATIO = "PruneRatio";
	public static final String NAME_PRUNE_POWER_HIGH = "PrunePowerHigh";
	public static final String NAME_PRUNE_POWER_LOW = "PrunePowerLow";
	public static final String NAME_PRUNE_WIDTH = "PruneWidth";
	public static final String NAME_PRUNE_WIDTH_PEAK = "PruneWidthPeak";
	public static final String NAME_LOBES = "Lobes";
	public static final String NAME_LOBE_DEPTH = "LobeDepth";
	public static final String NAME_ATTRACTION_UP = "AttractionUp";
	public static final String NAME_SPECIES = "Species";
	public static final String NAME_RATIO = "Ratio";
	public static final String NAME_BASE_SIZE = "BaseSize";
	public static final String NAME_SCALE = "Scale";
	public static final String NAME_SCALE_V = "ScaleV";
	public static final String NAME_FLARE = "Flare";
	public static final String NAME_0SCALE = "0Scale";
	public static final String NAME_0SCALE_V = "0ScaleV";
	public static final String NAME_0BASE_SPLITS = "0BaseSplits";
	
	public static final String NAME_N_LENGTH = "nLength";
	public static final String NAME_N_LENGTH_V = "nLengthV";
	public static final String NAME_N_TAPER = "nTaper";

	public static final String DEFAULT_SPECIES = "default";
	
	private boolean _ignoreVParams;
	private int _stopLevel;
	private String _species;
	private double _leafQuality;
	private double _smooth;
	private Hashtable<String, AbstractParam> _paramDB;
	private LevelParams[] _levelParams;
	
	private double _0Scale;		// only 0SCale used
	private double _0ScaleV;	// only 0ScaleV used
	private int _0BaseSplits;
	private double _attractionUp;
	private double _baseSize;
	private double _flare;
	private double _leafBend;
	private int _leafDistrib;
	private double _leafScale;
	private double _leafScaleX;
	private String _leafShape;
	private double _leafStemLen;
	private int _leaves;
	private double _leavesErrorValue;
	private int _levels;
	private double _lobeDepth;
	private int _lobes;
	private double _pruneRatio;
	private double _pruneWidth;
	private double _prunePowerLow;
	private double _prunePowerHigh;
	private double _pruneWidthPeak;
	private Random _random;
	private double _ratio;
	private double _ratioPower;
	private double _scale_tree;
	private int _shape;
	private double _mesh_quality;  // 0..1 - factor for mesh point number 
	private int _smooth_mesh_level; // -1..Levels - add average normals 
	private double _scale;
	private double _scaleV;
	private boolean _preview=false;
	private ChangeEvent _changeEvent = null;
	private EventListenerList _listenerList = new EventListenerList();

	public Params() {
		setIgnoreVParams(false);
		setStopLevel(-1);
		setSpecies(DEFAULT_SPECIES );
		setLeafQuality(1);
		setSmooth(0.5);
		// create paramDB
		_paramDB = new DefaultParams();
		_levelParams = new LevelParams[4];
		for (int l=0; l<4; l++) {
			_levelParams[l] = new LevelParams(l,_paramDB);
		}
	}

	public Params(Params other) {
		// copy values from other
		_ignoreVParams = other.getIgnoreVParams();
		_stopLevel = other.getStopLevel();
		_species = other.getSpecies();
		_smooth = other.getSmooth();
		// create paramDB
		_paramDB = new DefaultParams();
		_levelParams = new LevelParams[4];
		for (int l=0; l<4; l++) {
			_levelParams[l] = new LevelParams(l,_paramDB);
		}
		
		// copy param values
		for (Enumeration<AbstractParam> e = _paramDB.elements(); e.hasMoreElements();) {
			AbstractParam p = ((AbstractParam)e.nextElement());
			try {
				AbstractParam otherParam = other.getParam(p.getName());
				if (! otherParam.isEmpty()) {
					p.setValue(otherParam.getValue());
				} // else use default value
			} catch (ParamException err) {
//				Console.errorOutput("Error copying params: "+err.getMessage());
			}
		}
	}

	public void clearParams() {
		for (Enumeration<AbstractParam> e = _paramDB.elements(); e.hasMoreElements();) {
			((AbstractParam)e.nextElement()).clear();
		}
	}

	public void enableDisable() {
		boolean enable;
		String str;
		
		// ############ general params ##############
		// enable RatioPower/Leaves if Levels>1
		enable = (((IntParam)getParam(NAME_LEVELS)).getIntValue() > 1);
		getParam(NAME_RATIO_POWER).setEnabled(enable);
		getParam(NAME_LEAVES).setEnabled(enable);
		
		// enable leaf params if Leaves != 0
		enable = (((IntParam)getParam(NAME_LEAVES)).getIntValue() != 0 &&
				((IntParam)getParam(NAME_LEVELS)).getIntValue() > 1);
		getParam(NAME_LEAF_SHAPE).setEnabled(enable);
		getParam(NAME_LEAF_SCALE).setEnabled(enable);
		getParam(NAME_LEAF_SCALE_X).setEnabled(enable);
		getParam(NAME_LEAF_BEND).setEnabled(enable);
		getParam(NAME_LEAF_DISTRIB).setEnabled(enable);
		getParam(NAME_LEAF_QUALITY).setEnabled(enable);
		getParam(NAME_LEAF_STEM_LENGTH).setEnabled(enable);
		
		// enable Pruning parameters, if PruneRatio>0 or Shape=envelope
		enable = (((IntParam)getParam(NAME_SHAPE)).getIntValue() == 8 ||
				((FloatParam)getParam(NAME_PRUNE_RATIO)).getDoubleValue()>0);
		getParam(NAME_PRUNE_POWER_HIGH).setEnabled(enable);
		getParam(NAME_PRUNE_POWER_LOW).setEnabled(enable);
		getParam(NAME_PRUNE_WIDTH).setEnabled(enable);
		getParam(NAME_PRUNE_WIDTH_PEAK).setEnabled(enable);
		
		// enable LobeDepth if Lobes>0
		enable = (((IntParam)getParam(NAME_LOBES)).getIntValue() > 0);
		getParam(NAME_LOBE_DEPTH).setEnabled(enable);
		
		// enable AttractionUp if Levels>2
		enable = (((IntParam)getParam(NAME_LEVELS)).getIntValue() > 2);
		getParam(NAME_ATTRACTION_UP).setEnabled(enable);
		
		// ############## disable unused levels ###########
		
		for (int i=0; i<4; i++) {
			
			enable = i<((IntParam)getParam(NAME_LEVELS)).getIntValue();
			
			str = Integer.toString(i);
			
			getParam(str+NAME_N_LENGTH.substring(1)).setEnabled(enable);
			getParam(str+NAME_N_LENGTH_V.substring(1)).setEnabled(enable);
			getParam(str+NAME_N_TAPER.substring(1)).setEnabled(enable);
			
			getParam(str+"Curve").setEnabled(enable);
			getParam(str+"CurveV").setEnabled(enable);
			getParam(str+"CurveRes").setEnabled(enable);
			getParam(str+"CurveBack").setEnabled(enable);
			
			getParam(str+"SegSplits").setEnabled(enable);
			getParam(str+"SplitAngle").setEnabled(enable);
			getParam(str+"SplitAngleV").setEnabled(enable);
			
			getParam(str+"BranchDist").setEnabled(enable);
			getParam(str+"Branches").setEnabled(enable);
			
			// down and rotation angle of last level are
			// used for leaves
			enable = enable || 
				(((IntParam)getParam(NAME_LEAVES)).getIntValue() != 0 &&
					i==((IntParam)getParam(NAME_LEVELS)).getIntValue());
			
			getParam(str+"DownAngle").setEnabled(enable);
			getParam(str+"DownAngleV").setEnabled(enable);
			getParam(str+"Rotate").setEnabled(enable);
			getParam(str+"RotateV").setEnabled(enable);
		}
		
		for (int i=0; i<((IntParam)getParam(NAME_LEVELS)).getIntValue() && i<4; i++) {
			
			str = Integer.toString(i);
			
			// enable nSplitAngle/nSplitAngleV if nSegSplits>0
			enable = (((FloatParam)getParam(str+"SegSplits")).getDoubleValue()>0) ||
				(i==0 && ((IntParam)getParam(NAME_0BASE_SPLITS)).getIntValue()>0);
			getParam(str+"SplitAngle").setEnabled(enable);
			getParam(str+"SplitAngleV").setEnabled(enable);
			
			// enable Curving parameters only when CurveRes>1
			enable = (((IntParam)getParam(str+"CurveRes")).getIntValue()>1);
			getParam(str+"Curve").setEnabled(enable);
			getParam(str+"CurveV").setEnabled(enable);
			getParam(str+"CurveBack").setEnabled(enable);
		}
		
	}

	public void fireStateChanged() {
		Object [] listeners = _listenerList.getListenerList();
		for (int i = listeners.length -2; i>=0; i-=2) {
			if (listeners[i] == ChangeListener.class) {
				if (_changeEvent == null) {
					_changeEvent = new ChangeEvent(this);
				}
				((ChangeListener)listeners[i+1]).stateChanged(_changeEvent);
			}
		}
	}

	private void fromDB() {
		_leafQuality = getDblParam(NAME_LEAF_QUALITY);
		_smooth = getDblParam(NAME_SMOOTH);
		_levels = getIntParam(NAME_LEVELS);
		_ratio = getDblParam(NAME_RATIO);
		_ratioPower = getDblParam(NAME_RATIO_POWER);
		_shape = getIntParam(NAME_SHAPE);
		_baseSize = getDblParam(NAME_BASE_SIZE);
		_flare = getDblParam(NAME_FLARE);
		_lobes = getIntParam(NAME_LOBES);
		_lobeDepth = getDblParam(NAME_LOBE_DEPTH);
		_leaves = getIntParam(NAME_LEAVES);
		_leafShape = getStrParam(NAME_LEAF_SHAPE);
		_leafScale = getDblParam(NAME_LEAF_SCALE);
		_leafScaleX = getDblParam(NAME_LEAF_SCALE_X);
		_leafStemLen = getDblParam(NAME_LEAF_STEM_LENGTH);
		_leafDistrib = getIntParam(NAME_LEAF_DISTRIB);
		_leafBend = getDblParam(NAME_LEAF_BEND);
		_scale = getDblParam(NAME_SCALE);
		_scaleV = getDblParam(NAME_SCALE_V);
		_0Scale = getDblParam(NAME_0SCALE); 
		_0ScaleV = getDblParam(NAME_0SCALE_V);
		_attractionUp = getDblParam(NAME_ATTRACTION_UP);
		_pruneRatio = getDblParam(NAME_PRUNE_RATIO);
		_prunePowerLow = getDblParam(NAME_PRUNE_POWER_LOW);
		_prunePowerHigh = getDblParam(NAME_PRUNE_POWER_HIGH);
		_pruneWidth = getDblParam(NAME_PRUNE_WIDTH);
		_pruneWidthPeak = getDblParam(NAME_PRUNE_WIDTH_PEAK);
		_0BaseSplits = getIntParam(NAME_0BASE_SPLITS);
		_species = getStrParam(NAME_SPECIES);
		
		for (int i=0; i<=Math.min(_levels,3); i++) {
			_levelParams[i].fromDB(i==_levels); // i==Levels => leaf level only
		}
	}

	private int getIntParam(String name) throws ParamException {
		IntParam par = (IntParam)_paramDB.get(name);
		if (par != null) {
			return par.getIntValue();
		} else {
			throw new ParamException("param "+name+" not found!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private double getDblParam(String name) {
		FloatParam par = (FloatParam)_paramDB.get(name);
		if (par != null) {
			return par.getDoubleValue();
		} else {
			throw new ParamException("param "+name+" not found!"); //$NON-NLS-1$ //$NON-NLS-2$
		}   
	}

	private String getStrParam(String name) throws ParamException {
		StringParam par = (StringParam)_paramDB.get(name);
		if (par != null) {
			return par.getValue();
		} else {
			throw new ParamException("param "+name+" not found!"); //$NON-NLS-1$ //$NON-NLS-2$
		}    
	}

	public int get0BaseSplits() {
		return _0BaseSplits;
	}

	public double get0Scale() {
		return _0Scale;
	}

	public double get0ScaleV() {
		return _0ScaleV;
	}

	public double getAttractionUp() {
		return _attractionUp;
	}

	public double getBaseSize() {
		return _baseSize;
	}

	public double getFlare() {
		return _flare;
	}

	/**
	 * @return ignoreVParams
	 */
	public boolean getIgnoreVParams() {
		return _ignoreVParams;
	}

	public double getLeafBend() {
		return _leafBend;
	}

	public int getLeafDistrib() {
		return _leafDistrib;
	}

	public double getLeafQuality() {
		return _leafQuality;
	}

	public double getLeafScale() {
		return _leafScale;
	}

	public double getLeafScaleX() {
		return _leafScaleX;
	}

	public String getLeafShape() {
		return _leafShape;
	}

	public double getLeafStemLength() {
		return _leafStemLen;
	}

	public int getLeaves() {
		return _leaves;
	}

	public double getLeavesErrorValue() {
		return _leavesErrorValue;
	}

	public LevelParams getLevelParams(int stemlevel) {
		return _levelParams[Math.min(stemlevel,3)];
	}

	public int getLevels() {
		return _levels;
	}

	public double getLobeDepth() {
		return _lobeDepth;
	}

	public int getLobes() {
		return _lobes;
	}

	/**
	 * @return mesh_quality
	 */
	public double getMeshQuality() {
		return _mesh_quality;
	}

	public AbstractParam getParam(String param) {
		return (AbstractParam)_paramDB.get(param);
	}

	public TreeMap<Integer, AbstractParam> getParamGroup(int level, String group) {
		TreeMap<Integer, AbstractParam> result = new TreeMap<>();
		for (Enumeration<AbstractParam> e = _paramDB.elements(); e.hasMoreElements();) {
			AbstractParam p = (AbstractParam)e.nextElement();
			if (p.getLevel() == level && p.getGroup().equals(group)) {
				result.put(new Integer(p.getOrder()),p);
			}
		}
		return result;
	}

	/**
	 * @return _prunePowerHigh
	 */
	public double getPrunePowerHigh() {
		return _prunePowerHigh;
	}

	/**
	 * @return _prunePowerLow
	 */
	public double getPrunePowerLow() {
		return _prunePowerLow;
	}

	public double getPruneRatio() {
		return _pruneRatio;
	}

	public double getPruneWidth() {
		return _pruneWidth;
	}

	/**
	 * @return _pruneWidthPeak
	 */
	public double getPruneWidthPeak() {
		return _pruneWidthPeak;
	}

	public Random getRandom() {
		return _random;
	}

	public double getRatio() {
		return _ratio;
	}

	public double getRatioPower() {
		return _ratioPower;
	}

	/**
	 * @return scale
	 */
	public double getScale() {
		return _scale;
	}

	public double getScaleTree() {
		return _scale_tree;
	}

	/**
	 * @return scaleV
	 */
	public double getScaleV() {
		return _scaleV;
	}

	/**
	 * @return shape
	 */
	public int getShape() {
		return _shape;
	}

	public double getShapeRatio(double ratio) {
		return getShapeRatio(ratio,_shape);
	}

	public double getShapeRatio(double ratio, int shape) {
		
		switch (shape) { 
		case CONICAL: return ratio; // FIXME: this would be better: 0.05+0.95*ratio; ?
		case SPHERICAL: return 0.2+0.8*Math.sin(Math.PI*ratio);
		case HEMISPHERICAL: return 0.2+0.8*Math.sin(0.5*Math.PI*ratio);
		case CYLINDRICAL: return 1.0;
		case TAPERED_CYLINDRICAL: return 0.5+0.5*ratio;
		case FLAME: 
			return ratio<=0.7? 
					ratio/0.7 : 
						(1-ratio)/0.3;
		case INVERSE_CONICAL: return 1-0.8*ratio;
		case TEND_FLAME: 
			return ratio<=0.7? 
					0.5+0.5*ratio/0.7 :
						0.5+0.5*(1-ratio)/0.3;
		case ENVELOPE:
			if (ratio<0 || ratio>1) {
				return 0;
			} else if (ratio<(1-_pruneWidthPeak)) {
				return Math.pow(ratio/(1-_pruneWidthPeak),_prunePowerHigh);
			} else {
				return Math.pow((1-ratio)/(1-_pruneWidthPeak),_prunePowerLow);
			}
			// tested in prepare() default: throw new ErrorParam("Shape must be between 0 and 8");
		default:
			break;
		}
		return 0; // shouldn't reach here
	}

	/**
	 * @return smooth
	 */
	public double getSmooth() {
		return _smooth;
	}

	public int getSmoothMeshLevel() {
		return _smooth_mesh_level;
	}

	public String getSpecies() {
		return _species;
	}

	/**
	 * @return _stopLevel
	 */
	public int getStopLevel() {
		return _stopLevel;
	}

	/**
	 * @return ignoreVParams
	 */
	public boolean isIgnoreVParams() {
		return _ignoreVParams;
	}

	public boolean isPreview() {
		return _preview;
	}
	public void prepare(int seed) {
		// read in parameter values from ParamDB
		fromDB();
		
		if (_ignoreVParams) {
			_scaleV=0;
			for (int i=1; i<4; i++) {
				LevelParams lp = _levelParams[i];
				lp.setNCurveV(0);
				lp.setNLengthV(0);
				lp.setNSplitAngleV(0);
				lp.setNRotateV(0);
				if (lp.getNDownAngle()>0) { lp.setNDownAngle(0); }
			}
		}
		
		// additional params checks
		for (int l=0; l < Math.min(_levels,4); l++) {
			LevelParams lp = _levelParams[l];
			if (lp.getNSegSplits()>0 && lp.getNSplitAngle()==0) {
				throw new ParamException("nSplitAngle may not be 0."); //$NON-NLS-1$
			}
		}
		
		// create one random generator for every level
		// so you can develop a tree level by level without
		// influences between the levels
		long l = _levelParams[0].initRandom(seed);
		for (int i=1; i<4; i++) {
			l = _levelParams[i].initRandom(l);
		}
		
		// create a random generator for myself (used in stem_radius)
		_random = new Random(seed);
		
		// mesh settings
		if (_smooth <= 0.2) {
			_smooth_mesh_level = -1;
		} else {
			_smooth_mesh_level = (int)(_levels*_smooth);
		}
		_mesh_quality = _smooth;
		
		// mesh points per cross-section for the levels
		// minima
		_levelParams[0].setMeshPoints(4);
		_levelParams[1].setMeshPoints(3);
		_levelParams[2].setMeshPoints(2);
		_levelParams[3].setMeshPoints(1);
		// set meshpoints with respect to mesh_quality and Lobes
		if (_lobes>0) {
			_levelParams[0].setMeshPoints((int)(_lobes*(Math.pow(2,(int)(1+2.5*_mesh_quality)))));
			_levelParams[0].setMeshPoints(Math.max(_levelParams[0].getMeshPoints(), (int)(4*(1+2*_mesh_quality))));
		}
		for (int i=1; i<4; i++) {
			_levelParams[i].setMeshPoints(Math.max(3,(int)(_levelParams[i].getMeshPoints()*(1+1.5*_mesh_quality))));
		}
		
		// stop generation at some level?
		if (_stopLevel>=0 && _stopLevel<=_levels) {
			_levels = _stopLevel;
			_leaves = 0;
		}
		
		_scale_tree = _scale + _levelParams[0].getRandom().uniform(-_scaleV,_scaleV);
	}

	public void readFromConfig(InputStream is) {
//		ConfigTreeParser parser = new ConfigTreeParser();
		try {
			ConfigTreeParser.parse(is,this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void readFromXML(InputStream is) {
		try {
			XMLTreeParser parser = new XMLTreeParser();
			parser.parse(new InputSource(is),this);
		} catch (Exception e) {
			throw new ParamException(e.getMessage());
		}
	}


	/**
	 * @param 0BaseSplits セットする 0BaseSplits
	 */
	public void set0BaseSplits(int zeroBaseSplits) {
		_0BaseSplits = zeroBaseSplits;
	}

	/**
	 * @param 0Scale セットする 0Scale
	 */
	public void set0Scale(double zeroScale) {
		_0Scale = zeroScale;
	}

	/**
	 * @param 0ScaleV セットする 0ScaleV
	 */
	public void set0ScaleV(double zeroScaleV) {
		_0ScaleV = zeroScaleV;
	}

	/**
	 * @param attractionUp セットする attractionUp
	 */
	public void setAttractionUp(double attractionUp) {
		_attractionUp = attractionUp;
	}

	/**
	 * @param baseSize セットする baseSize
	 */
	public void setBaseSize(double baseSize) {
		_baseSize = baseSize;
	}

	/**
	 * @param flare セットする flare
	 */
	public void setFlare(double flare) {
		_flare = flare;
	}

	/**
	 * @param ignoreVParams セットする ignoreVParams
	 */
	public void setIgnoreVParams(boolean ignoreVParams) {
		_ignoreVParams = ignoreVParams;
	}

	/**
	 * @param leafBend セットする leafBend
	 */
	public void setLeafBend(double leafBend) {
		_leafBend = leafBend;
	}

	/**
	 * @param leafDistrib セットする leafDistrib
	 */
	public void setLeafDistrib(int leafDistrib) {
		_leafDistrib = leafDistrib;
	}

	/**
	 * @param leafQuality セットする leafQuality
	 */
	public void setLeafQuality(double leafQuality) {
		_leafQuality = leafQuality;
	}

	/**
	 * @param leafScale セットする leafScale
	 */
	public void setLeafScale(double leafScale) {
		_leafScale = leafScale;
	}

	/**
	 * @param leafScaleX セットする leafScaleX
	 */
	public void setLeafScaleX(double leafScaleX) {
		_leafScaleX = leafScaleX;
	}

	/**
	 * @param leafShape セットする leafShape
	 */
	public void setLeafShape(String leafShape) {
		_leafShape = leafShape;
	}

	/**
	 * @param leafStemLen セットする leafStemLen
	 */
	public void setLeafStemLength(double leafStemLen) {
		_leafStemLen = leafStemLen;
	}

	/**
	 * @param leaves セットする leaves
	 */
	public void setLeaves(int leaves) {
		_leaves = leaves;
	}

	public void setLeavesErrorValue(double d) {
		_leavesErrorValue -= d;
	}

	/**
	 * @param levels セットする levels
	 */
	public void setLevels(int levels) {
		_levels = levels;
	}

	/**
	 * @param lobeDepth セットする lobeDepth
	 */
	public void setLobeDepth(double lobeDepth) {
		_lobeDepth = lobeDepth;
	}

	/**
	 * @param lobes セットする lobes
	 */
	public void setLobes(int lobes) {
		_lobes = lobes;
	}

	/**
	 * @param mesh_quality セットする mesh_quality
	 */
	public void setMeshQuality(double mesh_quality) {
		_mesh_quality = mesh_quality;
	}

	public void setParam(String name, String value) {
		AbstractParam p = (AbstractParam)_paramDB.get(name);
		if (p!=null) {
			p.setValue(value);
		} else {
			throw new ParamException("Unknown parameter "+name+'!'); //$NON-NLS-1$
		}
	}

	/**
	 * @param preview セットする preview
	 */
	public void setPreview(boolean preview) {
		_preview = preview;
	}

	/**
	 * @param prunePowerHigh セットする prunePowerHigh
	 */
	public void setPrunePowerHigh(double prunePowerHigh) {
		_prunePowerHigh = prunePowerHigh;
	}

	/**
	 * @param prunePowerLow セットする prunePowerLow
	 */
	public void setPrunePowerLow(double prunePowerLow) {
		_prunePowerLow = prunePowerLow;
	}

	/**
	 * @param pruneRatio セットする pruneRatio
	 */
	public void setPruneRatio(double pruneRatio) {
		_pruneRatio = pruneRatio;
	}

	/**
	 * @param pruneWidth セットする pruneWidth
	 */
	public void setPruneWidth(double pruneWidth) {
		_pruneWidth = pruneWidth;
	}

	/**
	 * @param pruneWidthPeak セットする pruneWidthPeak
	 */
	public void setPruneWidthPeak(double pruneWidthPeak) {
		_pruneWidthPeak = pruneWidthPeak;
	}

	/**
	 * @param random セットする random
	 */
	public void setRandom(Random random) {
		_random = random;
	}

	/**
	 * @param ratio セットする ratio
	 */
	public void setRatio(double ratio) {
		_ratio = ratio;
	}

	/**
	 * @param ratioPower セットする ratioPower
	 */
	public void setRatioPower(double ratioPower) {
		_ratioPower = ratioPower;
	}

	/**
	 * @param scale セットする scale
	 */
	public void setScale(double scale) {
		_scale = scale;
	}

	/**
	 * @param scale_tree セットする scale_tree
	 */
	public void setScaleTree(double scale_tree) {
		_scale_tree = scale_tree;
	}

	/**
	 * @param scaleV セットする scaleV
	 */
	public void setScaleV(double scaleV) {
		_scaleV = scaleV;
	}

	/**
	 * @param shape セットする shape
	 */
	public void setShape(int shape) {
		_shape = shape;
	}

	/**
	 * @param smooth セットする smooth
	 */
	public void setSmooth(double smooth) {
		this._smooth = smooth;
	}

	/**
	 * @param smooth_mesh_level セットする smooth_mesh_level
	 */
	public void setSmoothMeshLevel(int smooth_mesh_level) {
		_smooth_mesh_level = smooth_mesh_level;
	}

	/**
	 * @param species セットする species
	 */
	public void setSpecies(String species) {
		_species = species;
		fireStateChanged();
	}

	/**
	 * @param stopLevel セットする stopLevel
	 */
	public void setStopLevel(int stopLevel) {
		_stopLevel = stopLevel;
	}

	@SuppressWarnings("nls")
	public void toXML(PrintWriter w) {
		fromDB();

		w.println("<?xml version='1.0' ?>");
		w.println();
		w.println("<ricfoi>");
		w.println("  <species name='" + _species + "'>");
		w.println("    <!-- general params -->");
		// FIXME: maybe use paramDB to print out params thus no one could be forgotten?
		writeParamXML(w,NAME_SHAPE,_shape);
		writeParamXML(w,NAME_LEVELS,_levels);
		writeParamXML(w,NAME_SCALE,_scale);
		writeParamXML(w,NAME_SCALE_V,_scaleV);
		writeParamXML(w,NAME_BASE_SIZE,_baseSize);
		writeParamXML(w,NAME_RATIO,_ratio);
		writeParamXML(w,NAME_RATIO_POWER,_ratioPower);
		writeParamXML(w,NAME_FLARE,_flare);
		writeParamXML(w,NAME_LOBES,_lobes);
		writeParamXML(w,NAME_LOBE_DEPTH,_lobeDepth);
		writeParamXML(w,NAME_SMOOTH,_smooth);
		writeParamXML(w,NAME_LEAVES,_leaves);
		writeParamXML(w,NAME_LEAF_SHAPE,_leafShape);
		writeParamXML(w,NAME_LEAF_SCALE,_leafScale);
		writeParamXML(w,NAME_LEAF_SCALE_X,_leafScaleX);
		writeParamXML(w,NAME_LEAF_QUALITY,_leafQuality);
		writeParamXML(w,NAME_LEAF_STEM_LENGTH,_leafStemLen);
		writeParamXML(w,NAME_LEAF_DISTRIB,_leafDistrib);
		writeParamXML(w,NAME_LEAF_BEND,_leafBend);
		writeParamXML(w,NAME_ATTRACTION_UP,_attractionUp);
		writeParamXML(w,NAME_PRUNE_RATIO,_pruneRatio);
		writeParamXML(w,NAME_PRUNE_POWER_LOW,_prunePowerLow);
		writeParamXML(w,NAME_PRUNE_POWER_HIGH,_prunePowerHigh);
		writeParamXML(w,NAME_PRUNE_WIDTH,_pruneWidth);
		writeParamXML(w,NAME_PRUNE_WIDTH_PEAK,_pruneWidthPeak);
		writeParamXML(w,NAME_0SCALE,_0Scale); 
		writeParamXML(w,NAME_0SCALE_V,_0ScaleV);
		writeParamXML(w,NAME_0BASE_SPLITS,_0BaseSplits);
		
		for (int i=0; i <= Math.min(_levels,3); i++) {
			_levelParams[i].toXML(w,i==_levels); // i==Levels => leaf level only
		}
		w.println("  </species>");
		w.println("</ricfoi>");
		w.flush();
	}

	private static void writeParamXML(PrintWriter w, String name, double value) {
		writeParamXML(w, name, Double.toString(value));
	}

	private static void writeParamXML(PrintWriter w, String name, int value) {
		writeParamXML(w, name, Integer.toString(value));
	}
	
	@SuppressWarnings("nls")
	private static void writeParamXML(PrintWriter w, String name, String value) {
		w.println("    <param name='" + name + "' value='"+value+"'/>");
	}

	public void addChangeListener(ChangeListener listener) {
		_listenerList.add(ChangeListener.class, listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		_listenerList.remove(ChangeListener.class, listener);
	}

}

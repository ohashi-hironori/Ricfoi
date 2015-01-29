package jp.gr.java_conf.ricfoi.params;

import java.io.PrintWriter;
import java.util.Hashtable;

import jp.gr.java_conf.ricfoi.math.Random;

public class LevelParams {

	private int _level;
	
	// stem length and appearance
	private double _nTaper;
	private int    _nCurveRes;
	private double _nCurve;
	private double _nCurveV;
	private double _nCurveBack;
	private double _nLength;
	private double _nLengthV;
	
	// splitting
	private double _nSegSplits;
	private double _nSplitAngle;
	private double _nSplitAngleV;
	
	// substems
	private int    _nBranches;
	
	/**
	 * <code>nBranchDist</code>
	 * is the substem distance within a segment 
	 * <ul>
	 * <li>0: all substems at segment base</li>
	 * <li>1: distributed over full segment</li>
	 * </ul>
	 * 
	 */
	private double _nBranchDist; 
	
	private double _nDownAngle;
	private double _nDownAngleV;
	private double _nRotate;
	private double _nRotateV;
	
	/**
	 * <code>mesh_points</code> -
	 * how many meshpoints per cross-section
	 */
	private int _mesh_points; 
	
	// Error values for splitting, substem and leaf distribution
	private double _splitErrorValue;
	private double _substemErrorValue;

	private Random _random;
	private long   _randstate;
	private double _spliterrval;

	private Hashtable<String, AbstractParam> _paramDB;

	public LevelParams(int level, Hashtable<String, AbstractParam> paramDB) {
		_level = level;
		_paramDB = paramDB;
		_randstate = Long.MIN_VALUE;
		_spliterrval = Double.NaN;
	}

	public int getLevel() {
		return _level;
	}

	public int getMeshPoints() {
		return _mesh_points;
	}

	public double getNBranchDist() {
		return _nBranchDist;
	}

	public int getNBranches() {
		return _nBranches;
	}

	public double getNCurve() {
		return _nCurve;
	}

	public double getNCurveBack() {
		return _nCurveBack;
	}

	public int getNCurveRes() {
		return _nCurveRes;
	}

	public double getNCurveV() {
		return _nCurveV;
	}

	public double getNDownAngle() {
		return _nDownAngle;
	}

	public double getNDownAngleV() {
		return _nDownAngleV;
	}

	public double getNLength() {
		return _nLength;
	}

	public double getNLengthV() {
		return _nLengthV;
	}

	public double getNRotate() {
		return _nRotate;
	}

	public double getNRotateV() {
		return _nRotateV;
	}

	public double getNSegSplits() {
		return _nSegSplits;
	}

	public double getNSplitAngle() {
		return _nSplitAngle;
	}

	public double getNSplitAngleV() {
		return _nSplitAngleV;
	}

	public double getNTaper() {
		return _nTaper;
	}

	public Hashtable<String, AbstractParam> getParamDB() {
		return _paramDB;
	}

	public Random getRandom() {
		return _random;
	}

	public long getRandstate() {
		return _randstate;
	}

	public double getSplitErrorValue() {
		return _splitErrorValue;
	}

	public double getSpliterrval() {
		return _spliterrval;
	}

	public double getSubstemErrorValue() {
		return _substemErrorValue;
	}

	public long initRandom(long seed) {
		_random = new Random(seed);
		return _random.nextLong();
	}

	public void restoreState() {
		if (Double.isNaN(_spliterrval)) {
			System.exit(1);
		}
		_random.setState(_randstate);
		_splitErrorValue = _spliterrval;
	}

	public void saveState() {
		_randstate = _random.getState();
		_spliterrval= _splitErrorValue;
	}
	
	public void setLevel(int level) {
		_level = level;
	}

	public void setMeshPoints(int mesh_points) {
		_mesh_points = mesh_points;
	}

	public void setNBranchDist(double nBranchDist) {
		_nBranchDist = nBranchDist;
	}

	public void setNBranches(int nBranches) {
		_nBranches = nBranches;
	}

	public void setNCurve(double nCurve) {
		_nCurve = nCurve;
	}

	public void setNCurveBack(double nCurveBack) {
		_nCurveBack = nCurveBack;
	}

	public void setNCurveRes(int nCurveRes) {
		_nCurveRes = nCurveRes;
	}

	public void setNCurveV(double nCurveV) {
		_nCurveV = nCurveV;
	}

	public void setNDownAngle(double nDownAngle) {
		_nDownAngle = nDownAngle;
	}

	public void setNDownAngleV(double nDownAngleV) {
		_nDownAngleV = nDownAngleV;
	}

	public void setNLength(double nLength) {
		_nLength = nLength;
	}

	public void setNLengthV(double nLengthV) {
		_nLengthV = nLengthV;
	}

	public void setNRotate(double nRotate) {
		_nRotate = nRotate;
	}

	public void setNRotateV(double nRotateV) {
		_nRotateV = nRotateV;
	}

	public void setNSegSplits(double nSegSplits) {
		_nSegSplits = nSegSplits;
	}

	public void setNSplitAngle(double nSplitAngle) {
		_nSplitAngle = nSplitAngle;
	}

	public void setNSplitAngleV(double nSplitAngleV) {
		_nSplitAngleV = nSplitAngleV;
	}

	public void setNTaper(double nTaper) {
		_nTaper = nTaper;
	}

	public void setParamDB(Hashtable<String, AbstractParam> paramDB) {
		_paramDB = paramDB;
	}

	public void setRandom(Random random) {
		_random = random;
	}

	public void setRandstate(long randstate) {
		_randstate = randstate;
	}

	public void setSplitErrorValue(double d) {
		_splitErrorValue = d;
	}

	public void setSpliterrval(double spliterrval) {
		_spliterrval = spliterrval;
	}

	public void setSubstemErrorValue(double d) {
		_substemErrorValue = d;
	}

	private int intParam(String name) throws ParamException {
		String fullname = Integer.toString(_level) + name.substring(1);
		IntParam par = (IntParam)_paramDB.get(fullname);
		if (par != null) {
			return par.getIntValue();
		} 
		throw new ParamException("param "+fullname+" not found!"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private double dblParam(String name) throws ParamException {
		String fullname = Integer.toString(_level) + name.substring(1);
		FloatParam par = (FloatParam)_paramDB.get(fullname);
		if (par != null) {
			return par.getDoubleValue();
		} 
		throw new ParamException("param "+fullname+" not found!"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	void fromDB(boolean leafLevelOnly) throws ParamException {
		if (! leafLevelOnly) {
			_nTaper = dblParam(Params.NAME_N_TAPER);
			_nCurveRes = intParam("nCurveRes");
			_nCurve = dblParam("nCurve");
			_nCurveV = dblParam("nCurveV");
			_nCurveBack = dblParam("nCurveBack");
			_nLength = dblParam(Params.NAME_N_LENGTH);
			_nLengthV = dblParam(Params.NAME_N_LENGTH_V);
			_nSegSplits = dblParam("nSegSplits");
			_nSplitAngle = dblParam("nSplitAngle");
			_nSplitAngleV = dblParam("nSplitAngleV");
			_nBranches = intParam("nBranches");
		}
		_nBranchDist = dblParam("nBranchDist");
		_nDownAngle = dblParam("nDownAngle");
		_nDownAngleV = dblParam("nDownAngleV");
		_nRotate = dblParam("nRotate");
		_nRotateV = dblParam("nRotateV");
	}

	@SuppressWarnings("nls")
	void toXML(PrintWriter w, boolean leafLevelOnly) {
		w.println("    <!-- level " + _level  + " -->");
		writeParamXML(w,"nDownAngle",_nDownAngle);
		writeParamXML(w,"nDownAngleV",_nDownAngleV);
		writeParamXML(w,"nRotate",_nRotate);
		writeParamXML(w,"nRotateV",_nRotateV);
		if (! leafLevelOnly) {
			writeParamXml(w,"nBranches",_nBranches);
			writeParamXML(w,"nBranchDist",_nBranchDist);
			writeParamXML(w,Params.NAME_N_LENGTH,_nLength);
			writeParamXML(w,Params.NAME_N_LENGTH_V,_nLengthV);
			writeParamXML(w,Params.NAME_N_TAPER,_nTaper);
			writeParamXML(w,"nSegSplits",_nSegSplits);
			writeParamXML(w,"nSplitAngle",_nSplitAngle);
			writeParamXML(w,"nSplitAngleV",_nSplitAngleV);
			writeParamXml(w,"nCurveRes",_nCurveRes);
			writeParamXML(w,"nCurve",_nCurve);
			writeParamXML(w,"nCurveBack",_nCurveBack);
			writeParamXML(w,"nCurveV",_nCurveV);
		}
	}

	public double var(double variation) {
		return _random.uniform(-variation,variation);
	}

	private void writeParamXml(PrintWriter w, String name, int value) {
		writeParamXml(w, name, Integer.toString(value));
	}

	private void writeParamXml(PrintWriter w, String name, String value) {
		String fullname = Integer.toString(_level) + name.substring(1);
		w.println("    <param name='" + fullname + "' value='"+value+"'/>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void writeParamXML(PrintWriter w, String name, double value) {
		writeParamXml(w, name, Double.toString(value));
	}

}

package jp.gr.java_conf.ricfoi.export;

import jp.gr.java_conf.ricfoi.gui.Config;
import jp.gr.java_conf.ricfoi.gui.Workplace;
import jp.gr.java_conf.ricfoi.mesh.MeshGeneratorFactory;
import jp.gr.java_conf.ricfoi.tree.Tree;

public class ExporterFactory {

	public final static int POV_MESH  = 0;
	public final static int POV_CONES = 1;
	public final static int DXF       = 2;
	public final static int OBJ       = 3;

	private static int _exportFormat = Integer.parseInt(Workplace.getConfig().getProperty(Config.KEY_EXPORT_FORMAT));
	private static String _outputPath = Workplace.getConfig().getProperty(Config.KEY_EXPORT_PATH);
	private static int _renderWidth = Integer.parseInt(Workplace.getConfig().getProperty(Config.KEY_POVRAY_WIDTH));
	private static int _renderHeight = Integer.parseInt(Workplace.getConfig().getProperty(Config.KEY_POVRAY_HEIGHT));
	private static boolean _outputStemUVs = false;
	private static boolean _outputLeafUVs = false;
	
	private final static String[] _formats = {
		"Povray meshes",		//$NON-NLS-1$
		"Povray primitives",	//$NON-NLS-1$
		"AutoCAD DXF",			//$NON-NLS-1$
		"Wavefront OBJ"			//$NON-NLS-1$
	};
	
	private final static String[] _shortformats = {
		"POV_MESH",				//$NON-NLS-1$
		"POV_CONES",			//$NON-NLS-1$
		"DXF",					//$NON-NLS-1$
		"OBJ"					//$NON-NLS-1$
	};

	public static void apply() {
		setExportFormat(Integer.parseInt(Workplace.getConfig().getProperty(Config.KEY_EXPORT_FORMAT)));
		setRenderWidth(Integer.parseInt(Workplace.getConfig().getProperty(Config.KEY_POVRAY_WIDTH)));
		setRenderHeight(Integer.parseInt(Workplace.getConfig().getProperty(Config.KEY_POVRAY_HEIGHT)));
		setOutputPath(Workplace.getConfig().getProperty(Config.KEY_EXPORT_PATH));
	}

	public static Exporter createExporter(Tree tree) {
		
		Exporter exporter = null;
		
		if (_exportFormat == POV_CONES) {
			exporter = new POVConeExporter(tree);
		} else if (_exportFormat == POV_MESH) {
			exporter = new POVMeshExporter(tree, MeshGeneratorFactory.createMeshGenerator(false));
			((POVMeshExporter)exporter).setOutputStemUVs(_outputStemUVs);
			((POVMeshExporter)exporter).setOutputLeafUVs(_outputLeafUVs);
		} else if (_exportFormat == DXF) {
			exporter = new DXFExporter(tree, MeshGeneratorFactory.createMeshGenerator(false));
		} else if (_exportFormat == OBJ) {
			exporter = new OBJExporter(tree, MeshGeneratorFactory.createMeshGenerator(true));
			((OBJExporter)exporter).setOutputStemUVs(_outputStemUVs);
			((OBJExporter)exporter).setOutputLeafUVs(_outputLeafUVs);
		}
		
		return exporter;
	}

	public static Exporter createSceneExporter(Tree tree) { 
		return new POVSceneExporter(tree, _renderWidth, _renderHeight);
	}

	public static Exporter createShieldedExporter(Tree tree)
	{
		return new ShieldedExporter(createExporter(tree));
	}

	public static Exporter createShieldedSceneExporter(Tree tree) {
		return new ShieldedExporter(createSceneExporter(tree));
	}

	/**
	 * @return exportFormat
	 */
	public static int getExportFormat() {
		return _exportFormat;
	}

	/**
	 * @return Formats
	 */
	public static String[] getFormats() {
		return _formats;
	}

	/**
	 * @return _outputPath
	 */
	public static String getOutputPath() {
		return _outputPath;
	}

	/**
	 * @return _renderHeight
	 */
	public static int getRenderHeight() {
		return _renderHeight;
	}

	/**
	 * @return _renderWidth
	 */
	public static int getRenderWidth() {
		return _renderWidth;
	}

	/**
	 * @return Shortformats
	 */
	public static String[] getShortFormats() {
		return _shortformats;
	}

	/**
	 * @return _outputLeafUVs
	 */
	public static boolean isOutputLeafUVs() {
		return _outputLeafUVs;
	}

	/**
	 * @return _outputStemUVs
	 */
	public static boolean isOutputStemUVs() {
		return _outputStemUVs;
	}

	/**
	 * @param exportFormat セットする exportFormat
	 */
	public static void setExportFormat(int exportFormat) {
		_exportFormat = exportFormat;
	}

	/**
	 * @param outputLeafUVs セットする _outputLeafUVs
	 */
	public static void setOutputLeafUVs(boolean outputLeafUVs) {
		_outputLeafUVs = outputLeafUVs;
	}

	/**
	 * @param _outputPath セットする _outputPath
	 */
	public static void setOutputPath(String outputPath) {
		_outputPath = outputPath;
	}

	/**
	 * @param outputStemUVs セットする outputStemUVs
	 */
	public static void setOutputStemUVs(boolean outputStemUVs) {
		_outputStemUVs = outputStemUVs;
	}

	/**
	 * @param height セットする _renderHeight
	 */
	public static void setRenderHeight(int height) {
		_renderHeight = height;
	}

	/**
	 * @param width セットする _renderWidth
	 */
	public static void setRenderWidth(int width) {
		_renderWidth = width;
	}

}

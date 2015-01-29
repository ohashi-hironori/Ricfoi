package jp.gr.java_conf.ricfoi.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import jp.gr.java_conf.ricfoi.export.Exporter;
import jp.gr.java_conf.ricfoi.export.ExporterFactory;
import jp.gr.java_conf.ricfoi.export.ShieldedExporter;
import jp.gr.java_conf.ricfoi.gui.Config;
import jp.gr.java_conf.ricfoi.gui.Workplace;
import jp.gr.java_conf.ricfoi.tree.Tree;
import jp.gr.java_conf.ricfoi.tree.TreeGenerator;

public class TreeCreationTask {

	TreeGenerator treeGenerator;
	PrintWriter writer;
	File scene_file = null;
	PrintWriter scenewriter = null;
	String renderFilename = null;
	boolean isNotActive;
	String povrayexe;
//	Component parent;
	
	final class TreeWorker extends SwingWorker {
		
		@Override
		public Object construct() {
//			return new DoTask(parent);
			return new DoTask();
		}
		
		@Override
		public void finished() {
			// may be this should be done in DoTask instead?
			isNotActive = true;
		}
	};
	
	TreeWorker worker;
	
	public TreeCreationTask() {
//		parent = Workplace.getParentFrame();
		povrayexe = Workplace.getConfig().getProperty(Config.KEY_POVRAY_EXE);
		isNotActive=true;
	};
	
	public void start(TreeGenerator treeFactory, File outFile, File sceneFile, String imgFilename) {
		// create new Tree copying the parameters of tree
		try {
			this.treeGenerator = treeFactory;
			
			writer = new PrintWriter(new FileWriter(outFile)); 
			if (sceneFile != null) {
				scene_file = sceneFile;
				scenewriter = new PrintWriter(new FileWriter(sceneFile)); 
			}
			renderFilename = imgFilename;
			
			isNotActive = false;
			
			worker = new TreeWorker();
			worker.start();
		} catch (Exception e) {
//			Console.printException(e);
		}
	}
	
	public void stop() {
		if (worker != null) {
			worker.interrupt();
			isNotActive = true;
			worker = null;
		}
	}
	
	public boolean notActive() {
		return isNotActive;
	}
	
	class DoTask {
		void render() {
			try {
				String [] povcmd = { povrayexe,
						"+L"+scene_file.getParent(),
						"+w"+ExporterFactory.getRenderWidth(),
						"+h"+ExporterFactory.getRenderHeight(),
						"+o"+renderFilename,
						scene_file.getPath()};
//				System.err.println(povcmd);
//				System.err.println("rendering...");
				Process povProc = Runtime.getRuntime().exec(povcmd);
				BufferedReader pov_in = new BufferedReader(new InputStreamReader(povProc.getErrorStream()));
				
				String str;
				while ((str = pov_in.readLine()) != null) {
					System.err.println(str);
				}
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		
//		DoTask(Component parent) {
		DoTask() {
			try {
				// create the tree
				Tree tree = treeGenerator.makeTree();
				// export the tree
//				Exporter exporter = new ShieldedGUIExporter(parent, ExporterFactory.createExporter(tree));
				Exporter exporter = new ShieldedExporter(ExporterFactory.createExporter(tree));
				exporter.write(writer);
				
				// export Povray scene ?
				if (scenewriter != null) {
//					exporter = new ShieldedGUIExporter(parent, ExporterFactory.createSceneExporter(tree));
					exporter = new ShieldedExporter(ExporterFactory.createSceneExporter(tree));
					exporter.write(scenewriter);
				}
				
				// render scene ?
				if (renderFilename != null && renderFilename.length()>0) {
					render();
				}
				
			} catch (Exception err) {
				err.printStackTrace(System.err);
			}
		}
	}

//	public int getProgress() {
//		// TODO 自動生成されたメソッド・スタブ
//		return 0;
//	}
//
//	public String getProgressMsg() {
//		// TODO 自動生成されたメソッド・スタブ
//		return null;
//	};

}

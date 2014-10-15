/**
 * 
 */
package cn.edu.fudan.se.defect.track.diff;

import java.util.LinkedList;
import java.util.List;

import org.incava.analysis.FileDiffs;
import org.incava.diffj.app.DiffJ;
import org.incava.diffj.app.Options;

/**
 * @author Lotay
 * 
 */
public class DiffJMain {

	public static void main(String[] args) {
		String fileName1 = "C:\\Users\\Lotay\\AppData\\Local\\Temp\\tempFileName8641154538125517678.java"; // "test/test21.java";//
		String fileName2 = "C:\\Users\\Lotay\\AppData\\Local\\Temp\\tempFileName2787777608855202902.java"; // "test/test22.java";//
		new DiffJMain().diffExecute(fileName1, fileName2);
	}

	private FileDiffs fileDiffs = null;
	private int exitCode;

	/**
	 * 
	 */
	public void diffExecute(String comparedFile1, String comparedFile2,
			String... fileName) {
		fileDiffs = null;
		exitCode = -1;
		Options opts = new Options();
		List<String> names = new LinkedList<String>();

		names.add(comparedFile1);
		names.add(comparedFile2);

		if (opts.showVersion()) {
			System.out.println("diffj, version " + Options.VERSION);
			System.out
					.println("Written by Jeff Pace (jpace [at] incava [dot] org)");
			System.out.println("Released under the Lesser GNU Public License");
			System.exit(0);
		}

		DiffJ diffj = new DiffJ(opts.showBriefOutput(),
				opts.showContextOutput(), opts.highlightOutput(),
				opts.recurse(), opts.getFirstFileName(), opts.getFromSource(),
				opts.getSecondFileName(), opts.getToSource());
		diffj.processNames(names);
		fileDiffs = diffj.getFileDiffs();
		exitCode = diffj.getExitValue();
	}

	/**
	 * @return the fileDiffs
	 */
	public FileDiffs getFileDiffs() {
		return fileDiffs;
	}

	/**
	 * @return the exitCode
	 */
	public int getExitCode() {
		return exitCode;
	}
}

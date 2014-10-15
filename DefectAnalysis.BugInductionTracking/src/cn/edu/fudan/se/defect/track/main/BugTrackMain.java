package cn.edu.fudan.se.defect.track.main;

public class BugTrackMain {
	public static void main(String[] args) {
		int bugId = 196249;

		BugFileTracker tracker = new BugFileTracker();
		tracker.track(bugId);

		/*
		 * BugFixFileTracker tracker = new BugFixFileTracker();
		 * Set<GitSourceFile> sourceFiles = tracker.track2SourceFile(bugId);
		 * for(GitSourceFile srcFile:sourceFiles){ System.out.println(srcFile);
		 * }
		 * 
		 * BugzillaBugExtractor bugExtractor = new BugzillaBugExtractor();
		 * BugzillaBug bug = bugExtractor.loadBug(bugId);
		 * System.out.println("bug:"+bug);
		 */
	}

}

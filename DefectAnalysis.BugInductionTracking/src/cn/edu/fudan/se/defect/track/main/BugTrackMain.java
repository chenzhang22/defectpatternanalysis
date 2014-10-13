package cn.edu.fudan.se.defect.track.main;

import java.util.Set;

import cn.edu.fudan.se.defect.track.link.SourceFileTracker;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;

public class BugTrackMain {
	public static void main(String[] args){
		int bugId = 196249;
		SourceFileTracker tracker = new SourceFileTracker();
		Set<GitSourceFile> sourceFiles = tracker.track2SourceFile(bugId);
		for(GitSourceFile srcFile:sourceFiles){
			System.out.println(srcFile);
		}
	}
}

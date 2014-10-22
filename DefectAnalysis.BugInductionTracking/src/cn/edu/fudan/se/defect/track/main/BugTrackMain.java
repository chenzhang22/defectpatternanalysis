package cn.edu.fudan.se.defect.track.main;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.defect.track.execute.BugFileTracker;
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitFiltedLink;
import cn.edu.fudan.se.defectAnalysis.dao.link.LinkDao;

public class BugTrackMain {
	public static void main(String[] args) {
		int start =5000;
		int length = 1000;
		run(start,length);
	}
	
	private static void run(int start,int length){
		List<FixedBugCommitFiltedLink> links = filtedLink();
		List<Integer> bugIds = filtedBugs(links);
		
		BugFileTracker tracker = new BugFileTracker();
		int size = bugIds.size();
		for(int i=start;i<start+length&&i<size;i++){
			int bugId = bugIds.get(i);
			System.out.println("Bug Tracking for bugId:" + bugId+" index:"+i+"/"+size);
			tracker.diffJTrack(bugId);
		}
	}

	private static List<FixedBugCommitFiltedLink> filtedLink(){
		LinkDao linkDao = new LinkDao();
		return linkDao.listLinks();
	}
	
	private static List<Integer> filtedBugs(List<FixedBugCommitFiltedLink> links){
		List<Integer> bugIds = new ArrayList<Integer>();
		for(FixedBugCommitFiltedLink l :links){
			if(!bugIds.contains(l.getBugId())){
				bugIds.add(l.getBugId());
			}
		}
		return bugIds;
	}
}

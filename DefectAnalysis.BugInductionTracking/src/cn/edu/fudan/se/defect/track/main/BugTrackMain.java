package cn.edu.fudan.se.defect.track.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defect.track.execute.BugFileTracker;
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitFiltedLink;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.BugInduceBlameLine;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffEntity;
import cn.edu.fudan.se.defectAnalysis.dao.link.LinkDao;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

public class BugTrackMain {
	public static void main(String[] args) {
		int start = 4780;//4530
		int length = 2;
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
			Collection<BugInduceBlameLine> inducedBlameLines =new ArrayList<BugInduceBlameLine>();

			Set<DiffEntity> diffEntities = tracker.changeDistillerTrack(bugId,inducedBlameLines);
			if(!diffEntities.isEmpty()){
				HibernateUtils.saveAll(diffEntities, BugTrackingConstants.HIBERNATE_CONF_PATH);
			}
			if(!inducedBlameLines.isEmpty()){
				HibernateUtils.saveAll(inducedBlameLines, BugTrackingConstants.HIBERNATE_CONF_PATH);
			}
		}
	}

	private static List<FixedBugCommitFiltedLink> filtedLink(){
		LinkDao linkDao = new LinkDao();
		return linkDao.listLinks(BugTrackingConstants.HIBERNATE_CONF_PATH);
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

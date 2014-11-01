/**
 * 
 */
package cn.edu.fudan.se.defect.track.extract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitFiltedLink;
import cn.edu.fudan.se.defectAnalysis.dao.link.LinkDao;

/**
 * @author Lotay
 *
 */
public class FixBugCommitFiltedLinkManager {
	private static final Map<String,Set<Integer>> bugFixedLinks = new HashMap<String,Set<Integer>>();

	static {
		if(bugFixedLinks.isEmpty()){
			LinkDao linkDao = new LinkDao();
			List<FixedBugCommitFiltedLink> links = linkDao.listLinks(BugTrackingConstants.HIBERNATE_CONF_PATH);
			for(FixedBugCommitFiltedLink l:links){
				String revisionIs  =l.getRevisionId();
				if(revisionIs==null){
					continue;
				}
				Set<Integer> bugIds = bugFixedLinks.get(revisionIs);
				if(bugIds==null){
					bugIds = new HashSet<Integer>();
					bugFixedLinks.put(l.getRevisionId(), bugIds);
				}
				bugIds.add(l.getBugId());
			}
		}
	}
	
	/**
	 * @return the bugfixedlinks
	 */
	public static Map<String,Set<Integer>> getBugfixedlinks() {
		return bugFixedLinks;
	}
}

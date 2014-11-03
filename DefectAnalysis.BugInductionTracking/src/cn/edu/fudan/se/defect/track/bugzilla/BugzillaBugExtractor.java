/**
 * 
 */
package cn.edu.fudan.se.defect.track.bugzilla;

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;
import cn.edu.fudan.se.defectAnalysis.dao.bugzilla.BugzillaBugDao;

/**
 * @author Lotay
 * 
 */
public class BugzillaBugExtractor {
	public BugzillaBug loadBug(int bugId) {
		BugzillaBugDao dao = new BugzillaBugDao();
		BugzillaBug bug = dao.loadBugzillaBugsByBugId(bugId,BugTrackingConstants.HIBERNATE_CONF_PATH);
		if (bug != null && BugzillaBugFilter.filteBug(bug)) {
			return bug;
		}
		return null;
	}
}

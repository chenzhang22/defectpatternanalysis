/**
 * 
 */
package cn.edu.fudan.se.defect.track.bugzilla;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;
import cn.edu.fudan.se.defectAnalysis.dao.bugzilla.BugzillaBugDao;

/**
 * @author Lotay
 * 
 */
public class BugzillaBugExtractor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public BugzillaBug loadBug(int bugId) {
		BugzillaBugDao dao = new BugzillaBugDao();
		return dao.loadBugzillaBugsByBugId(bugId);
	}
}

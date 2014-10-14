/**
 * 
 */
package cn.edu.fudan.se.defect.track.bugzilla;

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;

/**
 * @author Lotay
 * 
 */
public class BugzillaBugFilter {
	public static boolean filteBug(BugzillaBug bug) {
		if (bug == null) {
			return false;
		}

		String status = bug.getStatus();
		for (String st : BugTrackingConstants.BUG_FIXED_STATUS) {
			if (st.equals(status)) {
				return true;
			}
		}
		return false;
	}
}

/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.bugzilla;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaHistory;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 * 
 */
public class BugzillaHistoryDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int bugId = 10;
		System.out.println(new BugzillaHistoryDao().loadBugzillaHistoryByBugId(
				bugId, DaoConstants.TOMCAT_HIBERNATE_LOCATION_PATH));
	}

	@SuppressWarnings("unchecked")
	public List<BugzillaHistory> loadBugzillaHistoryByBugId(int bugId,
			final String hbmConf) {
		if (hbmConf == null) {
			return null;
		}
		String hql = "from BugzillaHistory where bug_id = " + bugId;
		return HibernateUtils.retrieveObjects(hql, hbmConf);
	}
}

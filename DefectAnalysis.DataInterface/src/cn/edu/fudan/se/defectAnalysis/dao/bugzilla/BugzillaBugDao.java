/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.bugzilla;

import java.util.Collection;
import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 * 
 */
public class BugzillaBugDao {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		BugzillaBugDao bugDao = new BugzillaBugDao();
		String product = "Platform";
		Collection<BugzillaBug> bugs = bugDao.loadBugZillaBugs(DaoConstants.HIBERNATE_LOCATION_PATH);
		System.out.println(bugs.size());
	}

	@SuppressWarnings("unchecked")
	public List<BugzillaBug> loadBugzillaBugsByProduct(String product) {
		if (product == null) {
			return null;
		}
		String hql = "from BugzillaBug where product = '" + product + "'";
		return HibernateUtils.retrieveObjects(hql,
				DaoConstants.HIBERNATE_LOCATION_PATH);
	}

	@SuppressWarnings("unchecked")
	public BugzillaBug loadBugzillaBugsByBugId(String product, int bugId) {
		if (product == null) {
			return null;
		}
		String hql = "from BugzillaBug where product = '" + product
				+ "' and id = " + bugId;
		List<BugzillaBug> bugzillaBugs = HibernateUtils.retrieveObjects(hql,
				DaoConstants.HIBERNATE_LOCATION_PATH);
		if (bugzillaBugs == null || bugzillaBugs.size() != 1) {
			return null;
		}
		return bugzillaBugs.get(0);
	}

	@SuppressWarnings("unchecked")
	public BugzillaBug loadBugzillaBugsByBugId(int bugId) {
		String hql = "from BugzillaBug where id = " + bugId;
		List<BugzillaBug> bugzillaBugs = HibernateUtils.retrieveObjects(hql,
				DaoConstants.HIBERNATE_LOCATION_PATH);
		if (bugzillaBugs == null || bugzillaBugs.size() != 1) {
			return null;
		}
		return bugzillaBugs.get(0);
	}

	@SuppressWarnings("unchecked")
	public Collection<BugzillaBug> loadBugZillaBugs(String conFile) {
		return HibernateUtils.retrieveAll(BugzillaBug.class,conFile);
	}
}

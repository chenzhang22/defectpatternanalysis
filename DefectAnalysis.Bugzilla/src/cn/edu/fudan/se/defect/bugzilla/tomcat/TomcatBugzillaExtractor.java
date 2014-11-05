/**
 * 
 */
package cn.edu.fudan.se.defect.bugzilla.tomcat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.fudan.se.defect.bugzilla.main.BugzillaMain;
import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitFiltedLink;
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitLink;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.defectAnalysis.dao.bugzilla.BugzillaBugDao;
import cn.edu.fudan.se.defectAnalysis.dao.link.LinkDao;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class TomcatBugzillaExtractor {

	private static final String TOMCAT_HIBERNATE_CONF_PATH = DaoConstants.TOMCAT_HIBERNATE_LOCATION_PATH;
	private static final String TOMCAT_BUGZILLA_PATH = "https://issues.apache.org/bugzilla";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		Set<Integer> inBugs = loadDataBaseBugIDs();
		BugzillaMain bugzillaMain = new BugzillaMain();

		LinkDao dao = new LinkDao();
		Collection<FixedBugCommitLink> bugs = dao
				.listUnFiltedLinks(TOMCAT_HIBERNATE_CONF_PATH);
		int index = 0;
		for (FixedBugCommitLink bug : bugs) {
			int bugId = bug.getBugId();
			if (inBugs.contains(bugId)) {
				continue;
			}
			inBugs.add(bugId);

			System.out.println("index:" + (++index) + "/" + bugs.size() + "-->"
					+ bugId);
			List<Object> bugzillaObjs;
			try {
				bugzillaObjs = bugzillaMain.extractBugzilla(
						TOMCAT_BUGZILLA_PATH, bug.getBugId());
				FixedBugCommitFiltedLink link = new FixedBugCommitFiltedLink();
				link.setBugId(bugId);
				link.setRevisionId(bug.getRevisionId());
				bugzillaObjs.add(link);
				HibernateUtils
						.saveAll(bugzillaObjs, TOMCAT_HIBERNATE_CONF_PATH);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static Set<Integer> loadDataBaseBugIDs() {
		BugzillaBugDao dao = new BugzillaBugDao();
		Collection<BugzillaBug> bugs = dao
				.loadBugZillaBugs(TOMCAT_HIBERNATE_CONF_PATH);
		Set<Integer> bugIds = new HashSet<Integer>();
		for (BugzillaBug bug : bugs) {
			bugIds.add(bug.getId());
		}
		return bugIds;
	}
}

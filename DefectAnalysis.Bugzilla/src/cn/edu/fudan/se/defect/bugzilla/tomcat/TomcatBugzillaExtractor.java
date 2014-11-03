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
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitFiltedLink;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.defectAnalysis.dao.link.LinkDao;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class TomcatBugzillaExtractor {

	private static final String TOMCAT_HIBERNATE_CONF_PATH =DaoConstants.TOMCAT_HIBERNATE_LOCATION_PATH;
	private static final String TOMCAT_BUGZILLA_PATH = "https://issues.apache.org/bugzilla";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		allBugIds = loadBugIDs();
		List<Integer> bugIds = new ArrayList<Integer>(allBugIds);
		Collections.sort(bugIds);
		BugzillaMain bugzillaMain = new BugzillaMain();
		int startIndex = 1200;
		int end = 1203;
		int index = startIndex;
		for (; index < bugIds.size() && index < end; index++) {
			System.out.println("index:" + index + "/" + bugIds.size() + "-->"
					+ bugIds.get(index));
			List<Object> bugzillaObjs;
			try {
				bugzillaObjs = bugzillaMain.extractBugzilla(
						TOMCAT_BUGZILLA_PATH, bugIds.get(index));
				HibernateUtils
						.saveAll(bugzillaObjs, TOMCAT_HIBERNATE_CONF_PATH);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static Set<Integer> allBugIds = null;

	static Set<Integer> loadBugIDs() {
		LinkDao dao = new LinkDao();
		Collection<FixedBugCommitFiltedLink> bugs = dao
				.listLinks(TOMCAT_HIBERNATE_CONF_PATH);
		Set<Integer> bugIds = new HashSet<Integer>();
		for (FixedBugCommitFiltedLink bug : bugs) {
			bugIds.add(bug.getBugId());
		}
		return bugIds;
	}
}

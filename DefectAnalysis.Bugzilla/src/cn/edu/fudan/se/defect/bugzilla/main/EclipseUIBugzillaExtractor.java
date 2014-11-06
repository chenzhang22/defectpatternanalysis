/**
 * 
 */
package cn.edu.fudan.se.defect.bugzilla.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.fudan.se.defect.bugzilla.extract.BugzillaMain;
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
public class EclipseUIBugzillaExtractor implements Runnable {

	private static final String ECLIPSE_UI_HIBERNATE_CONF_PATH = DaoConstants.ECLIPSE_UI_HIBERNATE_LOCATION_PATH;
	private static final String ECLIPSE_UI_BUGZILLA_PATH = "https://bugs.eclipse.org/bugs/";

	static void init() {

		LinkDao dao = new LinkDao();
		bugs = dao.listUnFiltedLinks(ECLIPSE_UI_HIBERNATE_CONF_PATH);
		inBugs = loadDataBaseBugIDs();
		visitedBugs = new HashSet<>();
		visitedBugs.addAll(inBugs);
	}

	static Collection<FixedBugCommitLink> bugs = null;
	static Set<Integer> inBugs = null;
	static Set<Integer> visitedBugs = null;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		init();
		int totalNum = bugs.size();
		int start = 3857+2600+2600+2600;
		int num = 2600;

		new EclipseUIBugzillaExtractor(start, num).run();
//		for (start = 0; start < totalNum; start += num) {
//			Thread th = new Thread(new EclipseUIBugzillaExtractor(start, num));
//			th.start();
//		}
	}

	private int index = 0;
	private int num = 0;

	/**
	 * @param index
	 */
	public EclipseUIBugzillaExtractor(int index, int num) {
		super();
		this.index = index;
		this.num = num;
	}

	static Set<Integer> loadDataBaseBugIDs() {
		BugzillaBugDao dao = new BugzillaBugDao();
		Collection<BugzillaBug> bugs = dao
				.loadBugZillaBugs(ECLIPSE_UI_HIBERNATE_CONF_PATH);
		Set<Integer> bugIds = new HashSet<Integer>();
		for (BugzillaBug bug : bugs) {
			bugIds.add(bug.getId());
		}
		return bugIds;
	}

	@Override
	public void run() {
		BugzillaMain bugzillaMain = new BugzillaMain();

		List<FixedBugCommitLink> links = new ArrayList<FixedBugCommitLink>(bugs);
		for (int i = index; i < index + num && i < links.size(); i++) {
			FixedBugCommitLink bug = links.get(i);
			int bugId = bug.getBugId();
			if (visitedBugs.contains(bugId)) {
				if (inBugs.contains(bugId)) {
					FixedBugCommitFiltedLink link = new FixedBugCommitFiltedLink();
					link.setBugId(bugId);
					link.setRevisionId(bug.getRevisionId());
					HibernateUtils.save(link, ECLIPSE_UI_HIBERNATE_CONF_PATH);
				}
				continue;

			}
			visitedBugs.add(bugId);
			System.out.println("index:" + (i) + "/" + bugs.size() + "-->"
					+ bugId);
			List<Object> bugzillaObjs;
			try {
				bugzillaObjs = bugzillaMain.extractBugzilla(
						ECLIPSE_UI_BUGZILLA_PATH, bug.getBugId());
				if (!bugzillaObjs.isEmpty()) {
					inBugs.add(bugId);
					FixedBugCommitFiltedLink link = new FixedBugCommitFiltedLink();
					link.setBugId(bugId);
					link.setRevisionId(bug.getRevisionId());
					bugzillaObjs.add(link);
					HibernateUtils.saveAll(bugzillaObjs,
							ECLIPSE_UI_HIBERNATE_CONF_PATH);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}

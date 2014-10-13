/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.bugzilla;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaComment;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 * 
 */
public class BugzillaCommentDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(new BugzillaCommentDao().loadBugzillaCommentByBugId(10));
	}

	@SuppressWarnings("unchecked")
	public List<BugzillaComment> loadBugzillaCommentByBugId(int bugId) {
		String hql = "from BugzillaComment where bugId =" + bugId;
		return HibernateUtils.retrieveObjects(hql,
				DaoConstants.HIBERNATE_LOCATION_PATH);
	}

	@SuppressWarnings("unchecked")
	public BugzillaComment loadBugzillaCommentByCommentId(int commentId) {
		String hql = "from BugzillaComment where commentId =" + commentId;
		List<BugzillaComment> comments = HibernateUtils.retrieveObjects(hql,
				DaoConstants.HIBERNATE_LOCATION_PATH);
		return comments == null || comments.size() != 1 ? null : comments
				.get(0);
	}
}

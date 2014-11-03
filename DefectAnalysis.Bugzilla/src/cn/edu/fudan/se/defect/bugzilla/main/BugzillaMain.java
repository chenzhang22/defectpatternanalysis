/**
 * 
 */
package cn.edu.fudan.se.defect.bugzilla.main;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.defect.bugzilla.constants.BugzillaConstants;
import cn.edu.fudan.se.defect.bugzilla.extract.BugzillaExtractor;
import cn.edu.fudan.se.defect.bugzilla.factory.BugzillaAttachmentFactory;
import cn.edu.fudan.se.defect.bugzilla.factory.BugzillaBugFactory;
import cn.edu.fudan.se.defect.bugzilla.factory.BugzillaCommentFactory;
import cn.edu.fudan.se.defect.bugzilla.factory.BugzillaHistoryFactory;
import cn.edu.fudan.se.defect.bugzilla.get.GetBugzilla;

import com.j2bugzilla.base.BugzillaMethod;

/**
 * @author Lotay
 *
 */
public class BugzillaMain {
	private static BugzillaBugFactory bugFactory = new BugzillaBugFactory();
	private static BugzillaAttachmentFactory attachmentFactory = new BugzillaAttachmentFactory();
	private static BugzillaCommentFactory commentFactory = new BugzillaCommentFactory();
	private static BugzillaHistoryFactory historyFactory = new BugzillaHistoryFactory();

	public List<Object> extractBugzilla(String url, int bugId) throws Exception {
		List<Object> bugzillaObjs = new ArrayList<Object>();
		BugzillaExtractor bugzillaExtractor = new BugzillaExtractor(url);
		
		BugzillaMethod getBug = new GetBugzilla(bugId,
				BugzillaConstants.BUGZILLA_METHOD_GETBUG);
		getBug = bugzillaExtractor.executMethod(getBug);
		bugzillaObjs.add(bugFactory.buildBugzillaBean(((GetBugzilla) getBug)
				.getResultMap()));

		GetBugzilla getBugzilla = new GetBugzilla(bugId,
				BugzillaConstants.BUGZILLA_METHOD_GETATTACHMENT);
		getBugzilla = (GetBugzilla) bugzillaExtractor.executMethod(getBugzilla);
		bugzillaObjs.addAll(attachmentFactory.buildBugzillaBean(bugId,
				getBugzilla.getResultMap()));

		GetBugzilla getComment = new GetBugzilla(bugId,
				BugzillaConstants.BUGZILLA_METHOD_GETCOMMENT);
		getComment = (GetBugzilla) bugzillaExtractor.executMethod(getComment);
		bugzillaObjs.addAll(commentFactory.buildBugzillaBean(bugId,
				getComment.getResultMap()));

		GetBugzilla getHistory = new GetBugzilla(bugId,
				BugzillaConstants.BUGZILLA_METHOD_GETHISTORY);
		getHistory = (GetBugzilla) bugzillaExtractor.executMethod(getHistory);
		bugzillaObjs.addAll(historyFactory.buildBugzillaBean(bugId,
				getHistory.getResultMap()));
		
		return bugzillaObjs;
	}
}

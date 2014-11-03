/**
 * 
 */
package cn.edu.fudan.se.defect.bugzilla.factory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaHistory;

/**
 * @author Lotay
 *
 */
public class BugzillaHistoryFactory {
	@SuppressWarnings("unchecked")
	public List<BugzillaHistory> buildBugzillaBean(int bugId,
			Map<Object, Object> bugzillaData) {
		List<BugzillaHistory> bugzillaAttachments = new ArrayList<BugzillaHistory>();
		if (bugzillaData == null || bugzillaData.isEmpty()) {
			return bugzillaAttachments;
		}

		int historyCount = 0;

		if (bugzillaData.containsKey("bugs")) {
			Object bugs = bugzillaData.get("bugs");
			if (bugs == null) {
				return bugzillaAttachments;
			}

			Object[] bugMap = (Object[]) bugs;
			if (bugMap == null || bugMap.length != 1) {
				return bugzillaAttachments;
			}

			Map<Object, Object> historyMap = (Map<Object, Object>) bugMap[0];
			if (historyMap.get("id").equals(new Integer(bugId))) {
				Object historys = historyMap.get("history");
				if (historys == null) {
					return bugzillaAttachments;
				}

				Object[] historyArray = (Object[]) historys;
				for (Object history : historyArray) {
					if (history == null) {
						continue;
					}
					Map<Object, Object> historyData = (Map<Object, Object>) history;

					Timestamp when = null;
					String who = null;
					if (historyData.containsKey("when")
							&& historyData.get("when") != null) {
						when = new Timestamp(
								((Date) historyData.get("when")).getTime());
					}
					if (historyData.containsKey("who")
							&& historyData.get("who") != null) {
						who = historyData.get("who").toString();
					}
					if (historyData.containsKey("changes")
							&& historyData.get("changes") != null) {
						Object[] changes = (Object[]) historyData
								.get("changes");
						for (Object changeObj : changes) {
							Map<Object, Object> changeMap = (Map<Object, Object>) changeObj;
							BugzillaHistory bugzillaHistory = new BugzillaHistory();
							bugzillaAttachments.add(bugzillaHistory);

							bugzillaHistory.setBug_id(bugId);
							bugzillaHistory.setHistory_count(historyCount++);
							bugzillaHistory.setWho(who);
							bugzillaHistory.setTime(when);
							bugzillaHistory.setAttachment_id(-1);
							if (changeMap.containsKey("field_name")
									&& changeMap.get("field_name") != null) {
								bugzillaHistory.setField_name(changeMap.get(
										"field_name").toString());
							}
							if (changeMap.containsKey("removed")
									&& changeMap.get("removed") != null) {
								bugzillaHistory.setRemoved(changeMap.get(
										"removed").toString());
							}
							if (changeMap.containsKey("added")
									&& changeMap.get("added") != null) {
								bugzillaHistory.setAdded(changeMap.get("added")
										.toString());
							}
							if (changeMap.containsKey("attachment_id")
									&& changeMap.get("attachment_id") != null) {
								try {
									bugzillaHistory
											.setAttachment_id(Integer
													.parseInt(changeMap.get(
															"attachment_id")
															.toString()));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		return bugzillaAttachments;
	}
}

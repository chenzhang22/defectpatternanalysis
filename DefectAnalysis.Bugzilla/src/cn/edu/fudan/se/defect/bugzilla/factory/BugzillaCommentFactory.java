/**
 * 
 */
package cn.edu.fudan.se.defect.bugzilla.factory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaComment;

/**
 * @author Lotay
 * 
 */
public class BugzillaCommentFactory {
	@SuppressWarnings("unchecked")
	public List<BugzillaComment> buildBugzillaBean(int bugId,
			Map<Object, Object> bugzillaData) {
		List<BugzillaComment> bugzillaAttachments = new ArrayList<BugzillaComment>();
		if (bugzillaData == null || bugzillaData.isEmpty()) {
			return bugzillaAttachments;
		}

		if (bugzillaData.containsKey("bugs")) {
			Object bugs = bugzillaData.get("bugs");
			if (bugs == null) {
				return bugzillaAttachments;
			}

			Map<Object, Object> bugMap = (Map<Object, Object>) bugs;
			if (bugMap.size() != 1 && !bugMap.containsKey(new Integer(bugId))) {
				return bugzillaAttachments;
			}
			Object commentsArray[] = bugMap.values().toArray();
			if (commentsArray.length != 1) {
				return bugzillaAttachments;
			}

			Map<Object, Object> commentsMap = (Map<Object, Object>) commentsArray[0];
			if (!commentsMap.containsKey("comments")) {
				return bugzillaAttachments;
			}
			Object[] comments = (Object[]) commentsMap.values().toArray()[0];
			for (Object comment : comments) {
				Map<Object, Object> commentData = (Map<Object, Object>) comment;
				if (commentData == null) {
					continue;
				}
				int mustField = 0;
				BugzillaComment bugzillaComment = new BugzillaComment();
				for (Object key : commentData.keySet()) {
					if (key == null) {
						continue;
					}
					Object valueObj = commentData.get(key);
					String keyName = key.toString();
					if (valueObj == null || keyName == null) {
						continue;
					}
					switch (keyName) {
					case "id":
						try {
							bugzillaComment.setCommentId(Integer
									.parseInt(valueObj.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						mustField++;
						break;
					case "bug_id":
						try {
							bugzillaComment.setBugId(Integer.parseInt(valueObj
									.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						mustField++;
						break;
					case "attachment_id":
						try {
							bugzillaComment.setAttachmentId(Integer
									.parseInt(valueObj.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case "count":
						try {
							bugzillaComment.setCount(Integer.parseInt(valueObj
									.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case "text":
						bugzillaComment.setCommentText(valueObj.toString());
						break;
					case "creator":
						bugzillaComment.setCreator(valueObj.toString());
						break;
					case "time":
						bugzillaComment.setTime(new Timestamp(((Date) valueObj)
								.getTime()));
						break;
					case "creation_time":
						bugzillaComment.setCreation_time(new Timestamp(((Date) valueObj)
								.getTime()));
						break;
					case "is_private":
						try {
							bugzillaComment.setIs_private(Boolean.parseBoolean(valueObj
									.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case "is_markdown":
						break;
					default:
						break;
					}
				}
				if(mustField ==2){
					bugzillaAttachments.add(bugzillaComment);
				}
			}
		}
		return bugzillaAttachments;
	}
}

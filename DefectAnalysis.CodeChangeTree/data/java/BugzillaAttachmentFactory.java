package cn.edu.fudan.se.defect.bugzilla.factory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaAttachment;

/**
 * @author Lotay
 *
 */
public class BugzillaAttachmentFactory {
	@SuppressWarnings("unchecked")
	public List<BugzillaAttachment> buildBugzillaBean(int bugId,
			Map<Object, Object> bugzillaData) {
		List<BugzillaAttachment> bugzillaAttachments = new ArrayList<BugzillaAttachment>();
		if (bugzillaData == null || bugzillaData.isEmpty()) {
			return bugzillaAttachments;
		}
		if (bugzillaData.containsKey("bugs")) {
			Object bugs = bugzillaData.get("bugs");
			if (bugs == null) {
				return bugzillaAttachments;
			}
			Map<Object, Object> bugAttachments = (Map<Object, Object>) bugs;
			if (bugAttachments.size() != 1&&!bugAttachments.containsKey(new Integer(bugId))) {
				return bugzillaAttachments;
			}
			Object attachmentArray[] = bugAttachments.values().toArray();
			if (attachmentArray.length != 1) {
				return bugzillaAttachments;
			}

			Object attachments[] = (Object[]) attachmentArray[0];
			for (Object attachment : attachments) {
				Map<Object, Object> attachmentData = (Map<Object, Object>) attachment;
				if (attachmentData == null) {
					continue;
				}
				BugzillaAttachment bugzillaAttachment = new BugzillaAttachment();
				int mustField = 0;
				for (Object attKey : attachmentData.keySet()) {
					if (attKey == null) {
						continue;
					}
					String attKeyName = attKey.toString();
					Object attValue = attachmentData.get(attKeyName);
					if (attValue == null) {
						continue;
					}
					switch (attKeyName) {
					case "size":
						try {
							bugzillaAttachment.setSize(Integer.parseInt(attValue.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case "creation_time":
						bugzillaAttachment.setCreation_time(new Timestamp(((Date) attValue)
								.getTime()));
						break;
					case "last_change_time":
						bugzillaAttachment.setLast_change_time(new Timestamp(((Date) attValue)
								.getTime()));
						break;
					case "id":
						try {
							bugzillaAttachment.setId(Integer.parseInt(attValue.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						mustField++;
						break;
					case "bug_id":
						try {
							bugzillaAttachment.setBug_id(Integer.parseInt(attValue.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						mustField++;
						break;
					case "file_name":
						bugzillaAttachment.setFile_name(attValue.toString());
						break;
					case "summary":
						bugzillaAttachment.setSummary(attValue.toString());
						break;
					case "content_type":
						bugzillaAttachment.setContent_type(attValue.toString());
						break;
					case "is_private":
						try {
							bugzillaAttachment.setIs_private(Boolean.parseBoolean(attValue.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case "is_obsolete":
						try {
							bugzillaAttachment.setIs_obsolete(Boolean.parseBoolean(attValue.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case "is_patch":
						try {
							bugzillaAttachment.setIs_patch(Boolean.parseBoolean(attValue.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case "creator":
						bugzillaAttachment.setCreator(attValue.toString());
						break;
					case "attacher":
						bugzillaAttachment.setAttacher(attValue.toString());
						break;
					case "description":
						bugzillaAttachment.setDescription(attValue.toString());
						break;
					case "flags":
						@SuppressWarnings("unused")
						Object[] flags = (Object[]) attValue;
						//Un finished...
						break;
					case "data":
						break;
					default:
						break;
					}
				}
				if(mustField==2){
					bugzillaAttachments.add(bugzillaAttachment);
				}
			}
		}

		return bugzillaAttachments;
	}
}

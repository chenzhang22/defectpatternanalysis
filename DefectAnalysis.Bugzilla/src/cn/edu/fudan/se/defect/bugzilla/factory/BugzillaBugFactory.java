/**
 * 
 */
package cn.edu.fudan.se.defect.bugzilla.factory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;

/**
 * @author Lotay
 * 
 */
public class BugzillaBugFactory {
	@SuppressWarnings("unchecked")
	public BugzillaBug buildBugzillaBean(Map<Object, Object> bugzillaData) {
		if (bugzillaData == null || bugzillaData.isEmpty()) {
			return null;
		}

		if (bugzillaData.containsKey("bugs")) {
			Object bugs = bugzillaData.get("bugs");
			if (bugs == null) {
				return null;
			}
			Object[] bugsArray = (Object[]) bugs;
			if (bugsArray.length != 1) {
				return null;
			}
			Object bug = bugsArray[0];
			Map<Object, Object> bugDatas = (Map<Object, Object>) bug;
			BugzillaBug bugzillaBug = new BugzillaBug();
			Object data = null;
			for (Object name : bugDatas.keySet()) {
				if (name == null) {
					continue;
				}
				data = null;
				switch (name.toString()) {
				case "alias":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					String aliasStr = null;

					if(data instanceof String){
						aliasStr = data.toString();
					}else{
					Object[] alias = (Object[]) data;
					for (Object obj : alias) {
						if (aliasStr == null) {
							aliasStr = obj.toString();
						} else {
							aliasStr += ";" + obj.toString();
						}
					}
					}
					bugzillaBug.setAlias(aliasStr);
					break;
				case "assigned_to":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setAssigned_to(data.toString());
					break;
				case "component":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setComponent(data.toString());
					break;
				case "creation_time":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setCreation_time(new Timestamp(((Date) data)
							.getTime()));
					break;
				case "dupe_of":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					try {
						bugzillaBug
								.setDupe_of(Integer.parseInt(data.toString()));
					} catch (Exception e) {
						System.err.println("dupe_of excpetion at bug factory.");
					}
					break;
				case "id":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					try {
						bugzillaBug.setId(Integer.parseInt(data.toString()));
					} catch (Exception e) {
						System.err.println("id excpetion at bug factory.");
						return null;
					}
					break;
				case "internals":
					break;
				case "is_open":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					try {
						bugzillaBug.setIs_open(Boolean.parseBoolean(data
								.toString()));
					} catch (Exception e) {
						System.err.println("id excpetion at bug factory.");
						return null;
					}
					break;
				case "last_change_time":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setLast_change_time(new Timestamp(((Date) data)
							.getTime()));
					break;
				case "priority":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setPriority(data.toString());
					break;
				case "product":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setProduct(data.toString());
					break;
				case "resolution":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setResolution(data.toString());
					break;
				case "severity":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setSeverity(data.toString());
					break;
				case "status":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setStatus(data.toString());
					break;
				case "summary":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setSummary(data.toString());
					break;

				case "target_milestone":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setTarget_milestone(data.toString());
					break;
				case "see_also":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					Object[] seeAlso = (Object[]) data;
					String seeAlsoStr = null;
					for (Object obj : seeAlso) {
						if (seeAlsoStr == null) {
							seeAlsoStr = obj.toString();
						} else {
							seeAlsoStr += ";" + obj.toString();
						}
					}
					bugzillaBug.setSee_also(seeAlsoStr);
					break;
				case "depends_on":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					Object[] dependOn = (Object[]) data;
					String dependOnStr = null;
					for (Object obj : dependOn) {
						if (dependOnStr == null) {
							dependOnStr = obj.toString();
						} else {
							dependOnStr += ";" + obj.toString();
						}
					}
					bugzillaBug.setDepends_on(dependOnStr);
					break;
				case "groups":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					Object[] groups = (Object[]) data;
					String groupsStr = null;
					for (Object obj : groups) {
						if (groupsStr == null) {
							groupsStr = obj.toString();
						} else {
							groupsStr += ";" + obj.toString();
						}
					}
					bugzillaBug.setGroups(groupsStr);
					break;
				case "platform":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setPlatform(data.toString());
					break;
				case "keywords":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					Object[] ketwords = (Object[]) data;
					String keywordsStr = null;
					for (Object obj : ketwords) {
						if (keywordsStr == null) {
							keywordsStr = obj.toString();
						} else {
							keywordsStr += ";" + obj.toString();
						}
					}
					bugzillaBug.setKeywords(keywordsStr);
					break;
				case "url":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setUrl(data.toString());
					break;
				case "whiteboard":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setWhiteboard(data.toString());
					break;
				case "cc":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					Object[] cc = (Object[]) data;
					String ccStr = null;
					for (Object obj : cc) {
						if (ccStr == null) {
							ccStr = obj.toString();
						} else {
							ccStr += ";" + obj.toString();
						}
					}
					bugzillaBug.setKeywords(ccStr);
					break;
				case "is_confirmed":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					try {
						bugzillaBug.setIs_confirmed(Boolean.parseBoolean(data
								.toString()));
					} catch (Exception e) {
						System.err.println("id excpetion at bug factory.");
						return null;
					}
					break;
				case "version":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setVersion(data.toString());
					break;
				case "creator":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setCreator(data.toString());
					break;
				case "op_sys":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setOp_sys(data.toString());
					break;
				case "creator_detail":

					break;
				case "qa_contact":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setQa_contact(data.toString());
					break;
				case "flags":

					break;
				case "is_creator_accessible":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					try {
						bugzillaBug.setIs_creator_accessible(Boolean
								.parseBoolean(data.toString()));
					} catch (Exception e) {
						System.err.println("id excpetion at bug factory.");
						return null;
					}
					break;
				case "is_cc_accessible":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					try {
						bugzillaBug.setIs_cc_accessible(Boolean
								.parseBoolean(data.toString()));
					} catch (Exception e) {
						System.err.println("id excpetion at bug factory.");
						return null;
					}
					break;
				case "blocks":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					Object[] blocks = (Object[]) data;
					String blocksStr = null;
					for (Object obj : blocks) {
						if (blocksStr == null) {
							blocksStr = obj.toString();
						} else {
							blocksStr += ";" + obj.toString();
						}
					}
					bugzillaBug.setKeywords(blocksStr);
					break;
				case "classification":
					data = bugDatas.get(name.toString());
					if (data == null) {
						break;
					}
					bugzillaBug.setClassification(data.toString());
					break;
				default:

					break;
				}
			}
			return bugzillaBug;
		}
		return null;
	}
}

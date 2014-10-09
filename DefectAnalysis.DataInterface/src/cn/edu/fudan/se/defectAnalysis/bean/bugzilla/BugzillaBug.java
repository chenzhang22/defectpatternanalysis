/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.bugzilla;

import java.sql.Timestamp;

/**
 * @author Lotay
 * 
 */
public class BugzillaBug {
	private int id;
	private String summary;
	private String target_milestone;
	private String see_also;
	private Timestamp last_change_time;
	private boolean is_confirmed;
	private boolean is_open;
	private String resolution;
	private String depends_on;
	private String version;
	private String creator;
	private String op_sys;
	private String component;
	private String priority;
	private Timestamp creation_time;
	private String qa_contact;
	private String groups;
	private String platform;
	private String flags;
	private String keywords;
	private boolean is_creator_accessible;
	private String status;
	private boolean is_cc_accessible;
	private String severity;
	private String blocks;
	private String url;
	private String product;
	private String assigned_to;
	private String whiteboard;
	private String classification;
	private String cc;
	private int dupe_of;
	private String alias;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary
	 *            the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the target_milestone
	 */
	public String getTarget_milestone() {
		return target_milestone;
	}

	/**
	 * @param target_milestone
	 *            the target_milestone to set
	 */
	public void setTarget_milestone(String target_milestone) {
		this.target_milestone = target_milestone;
	}

	/**
	 * @return the see_also
	 */
	public String getSee_also() {
		return see_also;
	}

	/**
	 * @param see_also
	 *            the see_also to set
	 */
	public void setSee_also(String see_also) {
		this.see_also = see_also;
	}

	/**
	 * @return the last_change_time
	 */
	public Timestamp getLast_change_time() {
		return last_change_time;
	}

	/**
	 * @param last_change_time
	 *            the last_change_time to set
	 */
	public void setLast_change_time(Timestamp last_change_time) {
		this.last_change_time = last_change_time;
	}

	/**
	 * @return the is_confirmed
	 */
	public boolean isIs_confirmed() {
		return is_confirmed;
	}

	/**
	 * @param is_confirmed
	 *            the is_confirmed to set
	 */
	public void setIs_confirmed(boolean is_confirmed) {
		this.is_confirmed = is_confirmed;
	}

	/**
	 * @return the is_open
	 */
	public boolean isIs_open() {
		return is_open;
	}

	/**
	 * @param is_open
	 *            the is_open to set
	 */
	public void setIs_open(boolean is_open) {
		this.is_open = is_open;
	}

	/**
	 * @return the resolution
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * @param resolution
	 *            the resolution to set
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	/**
	 * @return the depends_on
	 */
	public String getDepends_on() {
		return depends_on;
	}

	/**
	 * @param depends_on
	 *            the depends_on to set
	 */
	public void setDepends_on(String depends_on) {
		this.depends_on = depends_on;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the op_sys
	 */
	public String getOp_sys() {
		return op_sys;
	}

	/**
	 * @param op_sys
	 *            the op_sys to set
	 */
	public void setOp_sys(String op_sys) {
		this.op_sys = op_sys;
	}

	/**
	 * @return the component
	 */
	public String getComponent() {
		return component;
	}

	/**
	 * @param component
	 *            the component to set
	 */
	public void setComponent(String component) {
		this.component = component;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * @return the creation_time
	 */
	public Timestamp getCreation_time() {
		return creation_time;
	}

	/**
	 * @param creation_time
	 *            the creation_time to set
	 */
	public void setCreation_time(Timestamp creation_time) {
		this.creation_time = creation_time;
	}

	/**
	 * @return the qa_contact
	 */
	public String getQa_contact() {
		return qa_contact;
	}

	/**
	 * @param qa_contact
	 *            the qa_contact to set
	 */
	public void setQa_contact(String qa_contact) {
		this.qa_contact = qa_contact;
	}

	/**
	 * @return the groups
	 */
	public String getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(String groups) {
		this.groups = groups;
	}

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform
	 *            the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * @return the flags
	 */
	public String getFlags() {
		return flags;
	}

	/**
	 * @param flags
	 *            the flags to set
	 */
	public void setFlags(String flags) {
		this.flags = flags;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the is_creator_accessible
	 */
	public boolean isIs_creator_accessible() {
		return is_creator_accessible;
	}

	/**
	 * @param is_creator_accessible
	 *            the is_creator_accessible to set
	 */
	public void setIs_creator_accessible(boolean is_creator_accessible) {
		this.is_creator_accessible = is_creator_accessible;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the is_cc_accessible
	 */
	public boolean isIs_cc_accessible() {
		return is_cc_accessible;
	}

	/**
	 * @param is_cc_accessible
	 *            the is_cc_accessible to set
	 */
	public void setIs_cc_accessible(boolean is_cc_accessible) {
		this.is_cc_accessible = is_cc_accessible;
	}

	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * @param severity
	 *            the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * @return the blocks
	 */
	public String getBlocks() {
		return blocks;
	}

	/**
	 * @param blocks
	 *            the blocks to set
	 */
	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the assigned_to
	 */
	public String getAssigned_to() {
		return assigned_to;
	}

	/**
	 * @param assigned_to
	 *            the assigned_to to set
	 */
	public void setAssigned_to(String assigned_to) {
		this.assigned_to = assigned_to;
	}

	/**
	 * @return the whiteboard
	 */
	public String getWhiteboard() {
		return whiteboard;
	}

	/**
	 * @param whiteboard
	 *            the whiteboard to set
	 */
	public void setWhiteboard(String whiteboard) {
		this.whiteboard = whiteboard;
	}

	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * @param classification
	 *            the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * @param cc
	 *            the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * @return the dupe_of
	 */
	public int getDupe_of() {
		return dupe_of;
	}

	/**
	 * @param dupe_of the dupe_of to set
	 */
	public void setDupe_of(int dupe_of) {
		this.dupe_of = dupe_of;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BugzillaBug [id=" + id + ", summary=" + summary
				+ ", target_milestone=" + target_milestone + ", see_also="
				+ see_also + ", last_change_time=" + last_change_time
				+ ", is_confirmed=" + is_confirmed + ", is_open=" + is_open
				+ ", resolution=" + resolution + ", depends_on=" + depends_on
				+ ", version=" + version + ", creator=" + creator + ", op_sys="
				+ op_sys + ", component=" + component + ", priority="
				+ priority + ", creation_time=" + creation_time
				+ ", qa_contact=" + qa_contact + ", groups=" + groups
				+ ", platform=" + platform + ", flags=" + flags + ", keywords="
				+ keywords + ", is_creator_accessible=" + is_creator_accessible
				+ ", status=" + status + ", is_cc_accessible="
				+ is_cc_accessible + ", severity=" + severity + ", blocks="
				+ blocks + ", url=" + url + ", product=" + product
				+ ", assigned_to=" + assigned_to + ", whiteboard=" + whiteboard
				+ ", classification=" + classification + ", cc=" + cc
				+ ", dupe_of=" + dupe_of + ", alias=" + alias + "]";
	}
}

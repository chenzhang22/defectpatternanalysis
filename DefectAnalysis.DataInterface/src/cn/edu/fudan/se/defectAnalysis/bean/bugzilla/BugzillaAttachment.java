/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.bugzilla;

import java.sql.Timestamp;

/**
 * @author Lotay
 * 
 */
public class BugzillaAttachment {
	private int id;
	private Timestamp last_change_time;
	private String summary;
	private String flags;
	private boolean is_obsolete;
	private String data;
	private boolean is_patch;
	private int bug_id;
	private String file_name;
	private String attacher;
	private String creator;
	private int size;
	private String description;
	private boolean is_private;
	private Timestamp creation_time;
	private String content_type;

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
	 * @return the is_obsolete
	 */
	public boolean isIs_obsolete() {
		return is_obsolete;
	}

	/**
	 * @param is_obsolete
	 *            the is_obsolete to set
	 */
	public void setIs_obsolete(boolean is_obsolete) {
		this.is_obsolete = is_obsolete;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the is_patch
	 */
	public boolean isIs_patch() {
		return is_patch;
	}

	/**
	 * @param is_patch
	 *            the is_patch to set
	 */
	public void setIs_patch(boolean is_patch) {
		this.is_patch = is_patch;
	}

	/**
	 * @return the bug_id
	 */
	public int getBug_id() {
		return bug_id;
	}

	/**
	 * @param bug_id
	 *            the bug_id to set
	 */
	public void setBug_id(int bug_id) {
		this.bug_id = bug_id;
	}

	/**
	 * @return the file_name
	 */
	public String getFile_name() {
		return file_name;
	}

	/**
	 * @param file_name
	 *            the file_name to set
	 */
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	/**
	 * @return the attacher
	 */
	public String getAttacher() {
		return attacher;
	}

	/**
	 * @param attacher
	 *            the attacher to set
	 */
	public void setAttacher(String attacher) {
		this.attacher = attacher;
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
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the is_private
	 */
	public boolean isIs_private() {
		return is_private;
	}

	/**
	 * @param is_private
	 *            the is_private to set
	 */
	public void setIs_private(boolean is_private) {
		this.is_private = is_private;
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
	 * @return the content_type
	 */
	public String getContent_type() {
		return content_type;
	}

	/**
	 * @param content_type
	 *            the content_type to set
	 */
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BugzillaAttachment [id=" + id + ", last_change_time="
				+ last_change_time + ", summary=" + summary + ", flags="
				+ flags + ", is_obsolete=" + is_obsolete + ", data=" + data
				+ ", is_patch=" + is_patch + ", bug_id=" + bug_id
				+ ", file_name=" + file_name + ", attacher=" + attacher
				+ ", creator=" + creator + ", size=" + size + ", description="
				+ description + ", is_private=" + is_private
				+ ", creation_time=" + creation_time + ", content_type="
				+ content_type + "]";
	}
}

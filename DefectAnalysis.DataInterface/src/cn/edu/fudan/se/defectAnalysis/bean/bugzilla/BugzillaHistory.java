/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.bugzilla;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Lotay
 *
 */
public class BugzillaHistory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3053439837507337307L;
	private int history_count;
	private int bug_id;
	private Timestamp time;
	private String who;
	private String field_name;
	private String added;
	private String removed;
	private int attachment_id;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bug_id;
		result = prime * result + history_count;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BugzillaHistory)) {
			return false;
		}
		BugzillaHistory other = (BugzillaHistory) obj;
		if (bug_id != other.bug_id) {
			return false;
		}
		if (history_count != other.history_count) {
			return false;
		}
		return true;
	}

	/**
	 * @return the history_count
	 */
	public int getHistory_count() {
		return history_count;
	}

	/**
	 * @param history_count
	 *            the history_count to set
	 */
	public void setHistory_count(int history_count) {
		this.history_count = history_count;
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
	 * @return the time
	 */
	public Timestamp getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Timestamp time) {
		this.time = time;
	}

	/**
	 * @return the who
	 */
	public String getWho() {
		return who;
	}

	/**
	 * @param who
	 *            the who to set
	 */
	public void setWho(String who) {
		this.who = who;
	}

	/**
	 * @return the field_name
	 */
	public String getField_name() {
		return field_name;
	}

	/**
	 * @param field_name
	 *            the field_name to set
	 */
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	/**
	 * @return the added
	 */
	public String getAdded() {
		return added;
	}

	/**
	 * @param added
	 *            the added to set
	 */
	public void setAdded(String added) {
		this.added = added;
	}

	/**
	 * @return the removed
	 */
	public String getRemoved() {
		return removed;
	}

	/**
	 * @param removed
	 *            the removed to set
	 */
	public void setRemoved(String removed) {
		this.removed = removed;
	}

	/**
	 * @return the attachment_id
	 */
	public int getAttachment_id() {
		return attachment_id;
	}

	/**
	 * @param attachment_id
	 *            the attachment_id to set
	 */
	public void setAttachment_id(int attachment_id) {
		this.attachment_id = attachment_id;
	}
}

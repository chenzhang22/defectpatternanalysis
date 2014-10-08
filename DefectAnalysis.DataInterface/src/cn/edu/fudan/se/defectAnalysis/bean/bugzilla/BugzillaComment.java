/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.bugzilla;

import java.sql.Timestamp;

/**
 * @author Lotay
 * 
 */
public class BugzillaComment {
	private int commentiId;
	private String commentText;
	private int bugId;
	private String author;
	private Timestamp time;
	private int count;
	private Timestamp creation_time;
	private boolean is_private;
	private String creator;

	/**
	 * @return the commentiId
	 */
	public int getCommentiId() {
		return commentiId;
	}

	/**
	 * @param commentiId
	 *            the commentiId to set
	 */
	public void setCommentiId(int commentiId) {
		this.commentiId = commentiId;
	}

	/**
	 * @return the commentText
	 */
	public String getCommentText() {
		return commentText;
	}

	/**
	 * @param commentText
	 *            the commentText to set
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/**
	 * @return the bugId
	 */
	public int getBugId() {
		return bugId;
	}

	/**
	 * @param bugId
	 *            the bugId to set
	 */
	public void setBugId(int bugId) {
		this.bugId = bugId;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
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
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
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
}

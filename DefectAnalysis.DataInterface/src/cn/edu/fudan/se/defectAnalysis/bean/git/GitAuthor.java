/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.git;

import java.sql.Timestamp;

/**
 * @author Lotay
 *
 */
public class GitAuthor {
	private String authorID;
	private String authorName;
	private Timestamp when;
	/**
	 * @return the authorID
	 */
	public String getAuthorID() {
		return authorID;
	}
	/**
	 * @param authorID the authorID to set
	 */
	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}
	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}
	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	/**
	 * @return the when
	 */
	public Timestamp getWhen() {
		return when;
	}
	/**
	 * @param when the when to set
	 */
	public void setWhen(Timestamp when) {
		this.when = when;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GitAuthor [authorID=" + authorID + ", authorName=" + authorName
				+ "]";
	}
}

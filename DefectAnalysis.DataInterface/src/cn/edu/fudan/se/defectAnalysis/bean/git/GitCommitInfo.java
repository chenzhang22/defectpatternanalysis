/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.git;

import java.sql.Timestamp;

/**
 * @author Lotay
 *
 */
public class GitCommitInfo {
	private String revisionID;
	private String previousRID;
	private String postRID;
	private String authorID;
	private String log;
	private Timestamp time;
	private String component;
	/**
	 * @return the revisionID
	 */
	public String getRevisionID() {
		return revisionID;
	}
	/**
	 * @param revisionID the revisionID to set
	 */
	public void setRevisionID(String revisionID) {
		this.revisionID = revisionID;
	}
	/**
	 * @return the previousRID
	 */
	public String getPreviousRID() {
		return previousRID;
	}
	/**
	 * @param previousRID the previousRID to set
	 */
	public void setPreviousRID(String previousRID) {
		this.previousRID = previousRID;
	}
	/**
	 * @return the postRID
	 */
	public String getPostRID() {
		return postRID;
	}
	/**
	 * @param postRID the postRID to set
	 */
	public void setPostRID(String postRID) {
		this.postRID = postRID;
	}
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
	 * @return the log
	 */
	public String getLog() {
		return log;
	}
	/**
	 * @param log the log to set
	 */
	public void setLog(String log) {
		this.log = log;
	}
	/**
	 * @return the time
	 */
	public Timestamp getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Timestamp time) {
		this.time = time;
	}
	/**
	 * @return the component
	 */
	public String getComponent() {
		return component;
	}
	/**
	 * @param component the component to set
	 */
	public void setComponent(String component) {
		this.component = component;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GitCommitInfo [revisionID=" + revisionID + ", previousRID="
				+ previousRID + ", postRID=" + postRID + ", authorID="
				+ authorID + ", log=" + log + ", time=" + time + ", component="
				+ component + "]";
	}
	
}

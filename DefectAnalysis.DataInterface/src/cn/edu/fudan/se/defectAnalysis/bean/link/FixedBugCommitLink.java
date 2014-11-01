/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.link;

import java.io.Serializable;

/**
 * @author Lotay
 * 
 */
@SuppressWarnings("serial")
public class FixedBugCommitLink implements Serializable{
	private String revisionId;
	private int bugId;

	/**
	 * @return the revisionId
	 */
	public String getRevisionId() {
		return revisionId;
	}

	/**
	 * @param revisionId
	 *            the revisionId to set
	 */
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bugId;
		result = prime * result
				+ ((revisionId == null) ? 0 : revisionId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		if (!(obj instanceof FixedBugCommitLink)) {
			return false;
		}
		FixedBugCommitLink other = (FixedBugCommitLink) obj;
		if (bugId != other.bugId) {
			return false;
		}
		if (revisionId == null) {
			if (other.revisionId != null) {
				return false;
			}
		} else if (!revisionId.equals(other.revisionId)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FixedBugCommitLink [revisionId=" + revisionId
				+ ", bugId=" + bugId + "]";
	}
}

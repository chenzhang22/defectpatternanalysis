/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.git;

import java.io.Serializable;

/**
 * @author Lotay
 * 
 */
@SuppressWarnings("serial")
public class GitChange implements Serializable {
	private String revisionId;
	private String fileName;
	private int changeType;

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
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the changeType
	 */
	public int getChangeType() {
		return changeType;
	}

	/**
	 * @param changeType
	 *            the changeType to set
	 */
	public void setChangeType(int changeType) {
		this.changeType = changeType;
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
		result = prime * result + changeType;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
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
		if (!(obj instanceof GitChange)) {
			return false;
		}
		GitChange other = (GitChange) obj;
		if (changeType != other.changeType) {
			return false;
		}
		if (fileName == null) {
			if (other.fileName != null) {
				return false;
			}
		} else if (!fileName.equals(other.fileName)) {
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
		return "GitChange [revisionId=" + revisionId + ", fileName=" + fileName
				+ ", changeType=" + changeType + "]";
	}
}

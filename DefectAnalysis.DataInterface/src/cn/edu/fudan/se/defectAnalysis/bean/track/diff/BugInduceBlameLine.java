/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.track.diff;

import java.sql.Timestamp;

/**
 * @author Lotay
 * 
 */
@SuppressWarnings("serial")
public class BugInduceBlameLine implements java.io.Serializable{
	private int bugId;
	private String inducedRevisionId;
	private String fixedRevisionId;
	private String fileName;
	private int inducedlineNumber;
	private Timestamp inducedTime;
	private int fixedLineStart;
	private int fixedLineEnd;
	private boolean shouldCurCare;
	private boolean shouldPreCare;
	private Timestamp fixedTime;
	private String changeType;
	
	public int getBugId() {
		return bugId;
	}

	public void setBugId(int bugId) {
		this.bugId = bugId;
	}

	public String getInducedRevisionId() {
		return inducedRevisionId;
	}

	public void setInducedRevisionId(String inducedRevisionId) {
		this.inducedRevisionId = inducedRevisionId;
	}

	public String getFixedRevisionId() {
		return fixedRevisionId;
	}

	public void setFixedRevisionId(String fixedRevisionId) {
		this.fixedRevisionId = fixedRevisionId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getInducedlineNumber() {
		return inducedlineNumber;
	}

	public void setInducedlineNumber(int inducedlineNumber) {
		this.inducedlineNumber = inducedlineNumber;
	}

	/**
	 * @return the inducedTime
	 */
	public Timestamp getInducedTime() {
		return inducedTime;
	}

	/**
	 * @param inducedTime the inducedTime to set
	 */
	public void setInducedTime(Timestamp inducedTime) {
		this.inducedTime = inducedTime;
	}


	public int getFixedLineStart() {
		return fixedLineStart;
	}

	public void setFixedLineStart(int fixedLineStart) {
		this.fixedLineStart = fixedLineStart;
	}

	public int getFixedLineEnd() {
		return fixedLineEnd;
	}

	public void setFixedLineEnd(int fixedLineEnd) {
		this.fixedLineEnd = fixedLineEnd;
	}


	public boolean isShouldCurCare() {
		return shouldCurCare;
	}

	public void setShouldCurCare(boolean shouldCurCare) {
		this.shouldCurCare = shouldCurCare;
	}

	public boolean isShouldPreCare() {
		return shouldPreCare;
	}

	public void setShouldPreCare(boolean shouldPreCare) {
		this.shouldPreCare = shouldPreCare;
	}

	/**
	 * @return the fixedTime
	 */
	public Timestamp getFixedTime() {
		return fixedTime;
	}

	/**
	 * @param fixedTime the fixedTime to set
	 */
	public void setFixedTime(Timestamp fixedTime) {
		this.fixedTime = fixedTime;
	}

	/**
	 * @return the changeType
	 */
	public String getChangeType() {
		return changeType;
	}

	/**
	 * @param changeType the changeType to set
	 */
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bugId;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + inducedlineNumber;
		result = prime * result
				+ ((fixedRevisionId == null) ? 0 : fixedRevisionId.hashCode());
		result = prime
				* result
				+ ((inducedRevisionId == null) ? 0 : inducedRevisionId
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BugInduceBlameLine)) {
			return false;
		}
		BugInduceBlameLine other = (BugInduceBlameLine) obj;
		if (bugId != other.bugId) {
			return false;
		}
		if (fileName == null) {
			if (other.fileName != null) {
				return false;
			}
		} else if (!fileName.equals(other.fileName)) {
			return false;
		}
		if (inducedlineNumber != other.inducedlineNumber) {
			return false;
		}
		if (fixedRevisionId == null) {
			if (other.fixedRevisionId != null) {
				return false;
			}
		} else if (!fixedRevisionId.equals(other.fixedRevisionId)) {
			return false;
		}
		if (inducedRevisionId == null) {
			if (other.inducedRevisionId != null) {
				return false;
			}
		} else if (!inducedRevisionId.equals(other.inducedRevisionId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BugInduceBlameLine [bugId=" + bugId + ", inducedRevisionId="
				+ inducedRevisionId + ", fixedRevisionId=" + fixedRevisionId
				+ ", fileName=" + fileName + ", inducedlineNumber="
				+ inducedlineNumber + ", inducedTime=" + inducedTime
				+ ", fixedLineStart=" + fixedLineStart + ", fixedLineEnd="
				+ fixedLineEnd + ", shouldCurCare=" + shouldCurCare
				+ ", shouldPreCare=" + shouldPreCare + ", fixedTime="
				+ fixedTime + "]";
	}
}
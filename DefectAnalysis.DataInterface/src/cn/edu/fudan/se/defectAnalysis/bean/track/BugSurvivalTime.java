/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.track;

import java.sql.Timestamp;

/**
 * @author Lotay
 *
 */
@SuppressWarnings("serial")
public class BugSurvivalTime implements java.io.Serializable{
	private String fileName;
	private int bugId;
	private Timestamp minInducedTime;
	private Timestamp maxInducedTime;
	private Timestamp minFixedTime;
	private Timestamp maxFixedTime;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getBugId() {
		return bugId;
	}

	public void setBugId(int bugId) {
		this.bugId = bugId;
	}

	public Timestamp getMinInducedTime() {
		return minInducedTime;
	}

	public void setMinInducedTime(Timestamp minInducedTime) {
		this.minInducedTime = minInducedTime;
	}

	public Timestamp getMaxInducedTime() {
		return maxInducedTime;
	}

	public void setMaxInducedTime(Timestamp maxInducedTime) {
		this.maxInducedTime = maxInducedTime;
	}

	public Timestamp getMinFixedTime() {
		return minFixedTime;
	}

	public void setMinFixedTime(Timestamp minFixedTime) {
		this.minFixedTime = minFixedTime;
	}

	public Timestamp getMaxFixedTime() {
		return maxFixedTime;
	}

	public void setMaxFixedTime(Timestamp maxFixedTime) {
		this.maxFixedTime = maxFixedTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bugId;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
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
		if (!(obj instanceof BugSurvivalTime)) {
			return false;
		}
		BugSurvivalTime other = (BugSurvivalTime) obj;
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
		return true;
	}

	@Override
	public String toString() {
		return "BugSurvivalTime [fileName=" + fileName + ", budId=" + bugId
				+ ", minInducedTime=" + minInducedTime + ", maxInducedTime="
				+ maxInducedTime + ", minFixedTime=" + minFixedTime
				+ ", maxFixedTime=" + maxFixedTime + "]";
	}
}

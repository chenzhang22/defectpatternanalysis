/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.track.diff;

/**
 * @author Lotay
 *
 */
@SuppressWarnings("serial")
public class CodeLineChange implements java.io.Serializable{
	private String revisionId;
	private String fileName;
	private int lineNumber;
	private boolean shouldCare;
	public String getRevisionId() {
		return revisionId;
	}
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public boolean isShouldCare() {
		return shouldCare;
	}
	public void setShouldCare(boolean shouldCare) {
		this.shouldCare = shouldCare;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + lineNumber;
		result = prime * result
				+ ((revisionId == null) ? 0 : revisionId.hashCode());
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
		if (!(obj instanceof CodeLineChange)) {
			return false;
		}
		CodeLineChange other = (CodeLineChange) obj;
		if (fileName == null) {
			if (other.fileName != null) {
				return false;
			}
		} else if (!fileName.equals(other.fileName)) {
			return false;
		}
		if (lineNumber != other.lineNumber) {
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
	@Override
	public String toString() {
		return "CodeLineChange [revisionId=" + revisionId + ", fileName="
				+ fileName + ", lineNumber=" + lineNumber + ", shouldCare="
				+ shouldCare + "]";
	}
}

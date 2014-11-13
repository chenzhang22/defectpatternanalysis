/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.track;

/**
 * @author Lotay
 *
 */
@SuppressWarnings("serial")
public class CodeLineChangeBlock implements java.io.Serializable{
	private String revisionId;
	private String preRevisionId;
	private String fileName;
	private int curStartLine;
	private int curEndLine;
	private int preStartLine;
	private int preEndLine;
	public String getRevisionId() {
		return revisionId;
	}
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}
	public String getPreRevisionId() {
		return preRevisionId;
	}
	public void setPreRevisionId(String preRevisionId) {
		this.preRevisionId = preRevisionId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getCurStartLine() {
		return curStartLine;
	}
	public void setCurStartLine(int curStartLine) {
		this.curStartLine = curStartLine;
	}
	public int getCurEndLine() {
		return curEndLine;
	}
	public void setCurEndLine(int curEndLine) {
		this.curEndLine = curEndLine;
	}
	public int getPreStartLine() {
		return preStartLine;
	}
	public void setPreStartLine(int preStartLine) {
		this.preStartLine = preStartLine;
	}
	public int getPreEndLine() {
		return preEndLine;
	}
	public void setPreEndLine(int preEndLine) {
		this.preEndLine = preEndLine;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + curEndLine;
		result = prime * result + curStartLine;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
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
		if (!(obj instanceof CodeLineChangeBlock)) {
			return false;
		}
		CodeLineChangeBlock other = (CodeLineChangeBlock) obj;
		if (curEndLine != other.curEndLine) {
			return false;
		}
		if (curStartLine != other.curStartLine) {
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
	@Override
	public String toString() {
		return "CodeLineChange [revisionId=" + revisionId + ", preRevisionId="
				+ preRevisionId + ", fileName=" + fileName + ", curStartLine="
				+ curStartLine + ", curEndLine=" + curEndLine
				+ ", preStartLine=" + preStartLine + ", preEndLine="
				+ preEndLine + "]";
	}
}

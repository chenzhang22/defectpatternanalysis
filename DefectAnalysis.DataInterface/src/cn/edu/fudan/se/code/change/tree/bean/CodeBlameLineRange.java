/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

/**
 * @author Lotay
 *
 */
public class CodeBlameLineRange {
	private String changeType;
	private int bugId;

	private int inducedStartLine;
	private int inducedEndLine;

	private int fixedStartLine;
	private int fixedEndLine;

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public int getBugId() {
		return bugId;
	}

	public void setBugId(int bugId) {
		this.bugId = bugId;
	}

	public int getInducedStartLine() {
		return inducedStartLine;
	}

	public void setInducedStartLine(int inducedStartLine) {
		this.inducedStartLine = inducedStartLine;
	}

	public int getInducedEndLine() {
		return inducedEndLine;
	}

	public void setInducedEndLine(int inducedEndLine) {
		this.inducedEndLine = inducedEndLine;
	}

	public int getFixedStartLine() {
		return fixedStartLine;
	}

	public void setFixedStartLine(int fixedStartLine) {
		this.fixedStartLine = fixedStartLine;
	}

	public int getFixedEndLine() {
		return fixedEndLine;
	}

	public void setFixedEndLine(int fixedEndLine) {
		this.fixedEndLine = fixedEndLine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + inducedEndLine;
		result = prime * result + inducedStartLine;
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
		if (!(obj instanceof CodeBlameLineRange)) {
			return false;
		}
		CodeBlameLineRange other = (CodeBlameLineRange) obj;
		if (inducedEndLine != other.inducedEndLine) {
			return false;
		}
		if (inducedStartLine != other.inducedStartLine) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChangeLineRange [changeType=" + changeType + ", bugId=" + bugId
				+ ", inducedStartLine=" + inducedStartLine
				+ ", inducedEndLine=" + inducedEndLine + ", fixedStartLine="
				+ fixedStartLine + ", fixedEndLine=" + fixedEndLine + "]";
	}
}

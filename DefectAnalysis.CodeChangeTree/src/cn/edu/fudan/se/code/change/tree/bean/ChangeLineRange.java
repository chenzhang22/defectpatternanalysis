/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

/**
 * @author Lotay
 *
 */
public class ChangeLineRange {
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
	public String toString() {
		return "ChangeLineRange [changeType=" + changeType + ", bugId=" + bugId
				+ ", inducedStartLine=" + inducedStartLine
				+ ", inducedEndLine=" + inducedEndLine + ", fixedStartLine="
				+ fixedStartLine + ", fixedEndLine=" + fixedEndLine + "]";
	}
}

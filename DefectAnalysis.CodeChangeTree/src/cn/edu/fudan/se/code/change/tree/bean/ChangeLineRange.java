/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

/**
 * @author Lotay
 *
 */
public class ChangeLineRange {
	private int inducedStartLine;
	private int inducedEndLine;
	
	private int fixedStartLine;
	private int fixedEndLine;
	
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
}

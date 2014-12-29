/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

/**
 * @author Lotay
 *
 */
public class CodeRange {
	private int startLine;
	private int endLine;
	public int getStartLine() {
		return startLine;
	}
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	public int getEndLine() {
		return endLine;
	}
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endLine;
		result = prime * result + startLine;
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
		if (!(obj instanceof CodeRange)) {
			return false;
		}
		CodeRange other = (CodeRange) obj;
		if (endLine != other.endLine) {
			return false;
		}
		if (startLine != other.startLine) {
			return false;
		}
		return true;
	}
}

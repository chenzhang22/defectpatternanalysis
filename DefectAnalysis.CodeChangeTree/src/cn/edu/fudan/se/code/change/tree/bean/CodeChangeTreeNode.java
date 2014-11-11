/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * @author Lotay
 *
 */
public class CodeChangeTreeNode extends CodeTreeNode {
	private String preRevisionId;
	private String preType;
	private ASTNode preNode;
	private int preStartIndex;
	private int preEendIndex;
	private int preStartLine;
	private int preStartColumn;
	private int preEndLine;
	private int preEndColumn;

	public String getPreRevisionId() {
		return preRevisionId;
	}

	public void setPreRevisionId(String preRevisionId) {
		this.preRevisionId = preRevisionId;
	}

	public String getPreType() {
		return preType;
	}

	public void setPreType(String preType) {
		this.preType = preType;
	}

	public ASTNode getPreNode() {
		return preNode;
	}

	public void setPreNode(ASTNode node) {
		this.preNode = node;
	}

	public int getPreStartIndex() {
		return preStartIndex;
	}

	public void setPreStartIndex(int preStartIndex) {
		this.preStartIndex = preStartIndex;
	}

	public int getPreEendIndex() {
		return preEendIndex;
	}

	public void setPreEendIndex(int preEendIndex) {
		this.preEendIndex = preEendIndex;
	}

	public int getPreStartLine() {
		return preStartLine;
	}

	public void setPreStartLine(int preStartLine) {
		this.preStartLine = preStartLine;
	}

	public int getPreStartColumn() {
		return preStartColumn;
	}

	public void setPreStartColumn(int preStartColumn) {
		this.preStartColumn = preStartColumn;
	}

	public int getPreEndLine() {
		return preEndLine;
	}

	public void setPreEndLine(int preEndLine) {
		this.preEndLine = preEndLine;
	}

	public int getPreEndColumn() {
		return preEndColumn;
	}

	public void setPreEndColumn(int preEndColumn) {
		this.preEndColumn = preEndColumn;
	}

	@Override
	public String toString() {
		String toStr = "CodeChangeTreeNode [preType=" + preType + ", preNode="
				+ preNode + "]";

		return toStr;
	}

	@Override
	public String toNormalString() {
		return super.toNormalString() + "+/-";
	}

	@Override
	public String toTypeString() {
		return super.toTypeString() + "+/-";
	}

	@Override
	public String toSimpleTypeString() {
		return super.toSimpleTypeString() + "+/-";
	}

}

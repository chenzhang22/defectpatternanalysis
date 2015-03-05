/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * @author Lotay
 *
 */
public class CodeChangeTreeNode extends CodeTreeNode {
	private String preRevisionId;
	private String preType;
	private String preSimpleType;
	private String preSimpleNameType;
	private ASTNode preNode;
	private int preStartIndex;
	private int preEndIndex;
	private int preStartLine;
	private int preStartColumn;
	private int preEndLine;
	private int preEndColumn;
	private String preContent;
	private SourceCodeChange sourceCodeChange;

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

	public String getPreSimpleType() {
		return preSimpleType;
	}

	public void setPreSimpleType(String preSimpleType) {
		this.preSimpleType = preSimpleType;
	}

	public ASTNode getPreNode() {
		return preNode;
	}

	public String getPreSimpleNameType() {
		return preSimpleNameType;
	}

	public void setPreSimpleNameType(String preSimpleNameType) {
		this.preSimpleNameType = preSimpleNameType;
	}

	public void setPreNode(ASTNode node) {
		this.preNode = node;
	}

	public int getPreNodeType() {
		if (this.preNode != null)
			return this.preNode.getNodeType();
		return -1;
	}

	public int getPreStartIndex() {
		return preStartIndex;
	}

	public void setPreStartIndex(int preStartIndex) {
		this.preStartIndex = preStartIndex;
	}

	public int getPreEndIndex() {
		return preEndIndex;
	}

	public void setPreEndIndex(int preEndIndex) {
		this.preEndIndex = preEndIndex;
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

	/**
	 * @return the sourceCodeChange
	 */
	public SourceCodeChange getSourceCodeChange() {
		return sourceCodeChange;
	}

	/**
	 * @param sourceCodeChange
	 *            the sourceCodeChange to set
	 */
	public void setSourceCodeChange(SourceCodeChange sourceCodeChange) {
		this.sourceCodeChange = sourceCodeChange;
	}

	public String getPreContent() {
		return preContent;
	}

	public void setPreContent(String preContent) {
		this.preContent = preContent;
	}

	@Override
	public String toString() {
		return "CodeChangeTreeNode [preRevisionId=" + preRevisionId
				+ ", preType=" + preType + ", preSimpleType=" + preSimpleType
				+ ", preNode=" + preNode + ", preStartIndex=" + preStartIndex
				+ ", preEndIndex=" + preEndIndex + ", preStartLine="
				+ preStartLine + ", preStartColumn=" + preStartColumn
				+ ", preEndLine=" + preEndLine + ", preEndColumn="
				+ preEndColumn + ", preContent=" + preContent
				+ ", sourceCodeChange=" + sourceCodeChange + "]";
	}

	@Override
	public String toNormalString() {
		return toString() + super.toNormalString() + "+/-";
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

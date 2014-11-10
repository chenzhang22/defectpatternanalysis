/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * @author Lotay
 *
 */
public class CodeTreeNode {
	private String repoName;
	private String revisionId;
	private String fileName;
	private String type;
	private ArrayList<CodeTreeNode> children = new ArrayList<CodeTreeNode>();
	private ASTNode node;

	private int startIndex;
	private int endIndex;
	private int startLine;
	private int startColumn;
	private int endLine;
	private int endColumn;

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<CodeTreeNode> getChildren() {
		return children;
	}

	public ASTNode getNode() {
		return node;
	}

	public void setNode(ASTNode node) {
		this.node = node;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEnColumn(int enColumn) {
		this.endColumn = enColumn;
	}

	@Override
	public String toString() {
		String toStr = "CodeTreeNode [type=" + type + ", node=" + node
				+ ", startLine=" + startLine + ", startColumn=" + startColumn
				+ ", endLine=" + endLine + ", endColumn=" + endColumn + "]";
		for (CodeTreeNode node : this.children) {
			toStr += "\n" + node.toString();
		}
		return toStr;
	}
}

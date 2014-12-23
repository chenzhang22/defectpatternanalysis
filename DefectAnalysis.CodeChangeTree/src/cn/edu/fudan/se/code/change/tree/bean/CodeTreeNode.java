/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * @author Lotay
 *
 */
public class CodeTreeNode {
	private String repoName;
	private String revisionId;
	private String fileName;
	private String content;
	private String name;
	private String type;
	private String simpleType;
	private CodeTreeNode parentTreeNode = null;
	private ArrayList<CodeTreeNode> children = new ArrayList<CodeTreeNode>();
	private ASTNode node;

	private HashSet<Integer> bugIds = new HashSet<Integer>();

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

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the simpleType
	 */
	public String getSimpleType() {
		return simpleType;
	}

	/**
	 * @param simpleType
	 *            the simpleType to set
	 */
	public void setSimpleType(String simpleType) {
		this.simpleType = simpleType;
	}

	/**
	 * @return the bugId
	 */
	public HashSet<Integer> getBugIds() {
		return bugIds;
	}

	/**
	 * @param bugId
	 *            the bugId to set
	 */
	public void addBugId(int bugId) {
		this.bugIds.add(bugId);
	}

	public ArrayList<CodeTreeNode> getChildren() {
		return children;
	}

	public void addChild(CodeTreeNode treeNode) {
		this.children.add(treeNode);
		treeNode.setParentTreeNode(this);
	}

	public CodeTreeNode getParentTreeNode() {
		return parentTreeNode;
	}

	public void setParentTreeNode(CodeTreeNode parentTreeNode) {
		this.parentTreeNode = parentTreeNode;
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

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	public String toNormalString() {
		return "CodeTreeNode [type=" + type + ",startIndex=" + startIndex
				+ ",endIndex=" + endIndex + ", startLine=" + startLine
				+ ", startColumn=" + startColumn + ", endLine=" + endLine
				+ ", endColumn=" + endColumn + "]";
	}

	@Override
	public String toString() {
		String toStr = "CodeTreeNode [repoName=" + repoName + ", revisionId="
				+ revisionId + ", fileName=" + fileName + ", name=" + name
				+ ", type=" + type + ", startIndex=" + startIndex
				+ ", endIndex=" + endIndex + ", startLine=" + startLine
				+ ", startColumn=" + startColumn + ", endLine=" + endLine
				+ ", endColumn=" + endColumn + "]";
		return toStr;
	}

	public String toTypeString() {
		return type;
	}

	public String toSimpleTypeString() {
		return simpleType;
	}
}

/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import java.util.ArrayList;
import java.util.HashMap;
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
	private String simpleNameType;
	private String type;
	private String simpleType;
	private CodeTreeNode parentTreeNode = null;
	private ArrayList<CodeTreeNode> children = new ArrayList<CodeTreeNode>();
	private ASTNode node;

	private HashMap<String, String> nameTypes = new HashMap<String, String>();

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSimpleNameType() {
		return simpleNameType;
	}

	public void setSimpleNameType(String name) {
		this.simpleNameType = name;
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
	public void addBugIds(HashSet<Integer> bugIds) {
		this.bugIds.addAll(bugIds);
	}
	public ArrayList<CodeTreeNode> getChildren() {
		return children;
	}

	public void addChild(CodeTreeNode treeNode) {
		this.children.add(treeNode);
		treeNode.setParentTreeNode(this);
	}
	
	public void removeAllChildren() {
		this.children.clear();
	}

	public CodeTreeNode getParentTreeNode() {
		return parentTreeNode;
	}

	public void setParentTreeNode(CodeTreeNode parentTreeNode) {
		this.parentTreeNode = parentTreeNode;
	}

	public String getNameType(String name) {
		return this.nameTypes.get(name);
	}

	public boolean hasTypeName(String name) {
		return this.nameTypes.containsKey(name);
	}
	
	public HashMap<String, String> getNameTypes() {
		return this.nameTypes;
	}

	public void addNameType(String name, String type) {
		this.nameTypes.put(name, type);
	}

	public void addNameTypes(HashMap<String, String> nameType) {
		this.nameTypes.putAll(nameType);
	}

	public ASTNode getNode() {
		return node;
	}

	public int getNodeType() {
		if(this.node!=null)
			return this.node.getNodeType();
		return -1;
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
				+ revisionId + ", fileName=" + fileName + ", name=" + simpleNameType
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

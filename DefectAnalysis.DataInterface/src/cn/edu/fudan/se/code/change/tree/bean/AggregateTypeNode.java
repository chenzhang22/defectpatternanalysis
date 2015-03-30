/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import java.util.ArrayList;

/**
 * @author Lotay
 *
 */
public class AggregateTypeNode implements TreeNode{
	private String changeType = "NORMAL";
	private String postNodeValue = null;
	private String preNodeValue = null;
	private AggregateTypeNode parentTreeNode = null; 
	private ArrayList<AggregateTypeNode> children = new ArrayList<AggregateTypeNode>();
	private CodeTreeNode codeTreeNode;

	public AggregateTypeNode(CodeTreeNode codeTreeNode) {
		super();
		this.codeTreeNode = codeTreeNode;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getPostNodeValue() {
		return postNodeValue;
	}

	public void setPostNodeValue(String postNodeValue) {
		this.postNodeValue = postNodeValue;
	}

	public String getPreNodeValue() {
		return preNodeValue;
	}

	public void setPreNodeValue(String preNodeValue) {
		this.preNodeValue = preNodeValue;
	}

	public AggregateTypeNode getParentTreeNode() {
		return parentTreeNode;
	}

	public void setParentTreeNode(AggregateTypeNode parentNode) {
		this.parentTreeNode = parentNode;
	}

	public ArrayList<AggregateTypeNode> getChildren() {
		return children;
	}

	public void addChildNode(AggregateTypeNode child) {
		if (child != null) {
			this.children.add(child);
			child.setParentTreeNode(this);
		}
	}

	public CodeTreeNode getCodeTreeNode() {
		return codeTreeNode;
	}

	public void setCodeTreeNode(CodeTreeNode codeTreeNode) {
		this.codeTreeNode = codeTreeNode;
	}

	@Override
	public String toString() {
		return "AggregateTypeNode [changeType=" + changeType
				+ ", postNodeValue=" + postNodeValue + ", preNodeValue="
				+ preNodeValue + "]";
	}

	@Override
	public void addChild(TreeNode child) {
		if (child instanceof AggregateTypeNode) {
			this.children.add((AggregateTypeNode) child);
			((AggregateTypeNode) child).setParentTreeNode(this);
		}
	}

	@Override
	public void addChild(int index, TreeNode child) {
		if (child instanceof AggregateTypeNode) {
			this.children.add(index,(AggregateTypeNode) child);
			((AggregateTypeNode) child).setParentTreeNode(this);
		}
	}
}

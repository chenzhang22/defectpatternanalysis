package cn.edu.fudan.se.tree.pattern.test;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

public class TreeNodeTest implements TreeNode {
	private String nodeId = null;
	private ArrayList<TreeNodeTest> children = new ArrayList<TreeNodeTest>();
	private TreeNodeTest parent;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public List<TreeNodeTest> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<TreeNodeTest> childrenNodes) {
		this.children = childrenNodes;
	}

	public void addChild(TreeNodeTest childNode) {
		this.children.add(childNode);
		childNode.setParentTreeNode(this);
	}

	public void setParentTreeNode(TreeNodeTest parent) {
		this.parent = parent;
	}

	@Override
	public TreeNode getParentTreeNode() {
		return this.parent;
	}

	@Override
	public void addChild(TreeNode child) {
		if (child instanceof TreeNodeTest) {
			this.children.add((TreeNodeTest) child);
			((TreeNodeTest) child).setParentTreeNode(this);
		}
	}

	@Override
	public void addChild(int index, TreeNode child) {
		if (child instanceof TreeNodeTest) {
			this.children.add(index, (TreeNodeTest) child);
			((TreeNodeTest) child).setParentTreeNode(this);
		}
	}
}

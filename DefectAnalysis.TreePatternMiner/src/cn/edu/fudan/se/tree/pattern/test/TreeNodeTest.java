package cn.edu.fudan.se.tree.pattern.test;

import java.util.ArrayList;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

public class TreeNodeTest implements TreeNode {
	private String label = null;
	private String nodeId = null;
	private ArrayList<TreeNodeTest> childrenNodes = new ArrayList<TreeNodeTest>();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public ArrayList<TreeNodeTest> getChildrenNodes() {
		return childrenNodes;
	}

	public void setChildrenNodes(ArrayList<TreeNodeTest> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}

	public void addChild(TreeNodeTest childNode) {
		this.childrenNodes.add(childNode);
	}
}

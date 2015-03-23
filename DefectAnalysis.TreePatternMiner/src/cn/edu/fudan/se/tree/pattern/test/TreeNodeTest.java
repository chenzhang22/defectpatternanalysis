package cn.edu.fudan.se.tree.pattern.test;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

public class TreeNodeTest implements TreeNode {
	private String label = null;
	private String nodeId = null;
	private ArrayList<TreeNodeTest> children = new ArrayList<TreeNodeTest>();

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

	public List<TreeNodeTest> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<TreeNodeTest> childrenNodes) {
		this.children = childrenNodes;
	}

	public void addChild(TreeNodeTest childNode) {
		this.children.add(childNode);
	}

	@Override
	public String toString() {
		return "TreeNodeTest [label=" + label + "]";
	}
}

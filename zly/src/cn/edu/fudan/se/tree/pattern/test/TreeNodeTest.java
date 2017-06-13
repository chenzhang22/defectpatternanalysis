package cn.edu.fudan.se.tree.pattern.test;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

public class TreeNodeTest implements TreeNode {
	private String nodeId = null;
	private ArrayList<TreeNodeTest> children = new ArrayList<TreeNodeTest>();
	private TreeNodeTest parent;
	private String label = null;

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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		if (label==null) {
			return this.nodeId;
		}
		return this.nodeId+":"+label;
	}
	
	public String toWholeString() {
		return toWholeString(this, 0);
	}

	public String toWholeString(TreeNodeTest node,int depth ) {
		String toString ="";
		for (int i = 0; i < depth; i++) {
			toString +=" ";
		}
		toString += node.toString()+"\n";
		for (TreeNodeTest treeNodeTest : node.children) {
			toString+=this.toWholeString(treeNodeTest, depth+1);
		}
		return toString;
	}
}

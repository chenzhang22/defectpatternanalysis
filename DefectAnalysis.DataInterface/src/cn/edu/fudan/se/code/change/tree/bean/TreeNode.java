package cn.edu.fudan.se.code.change.tree.bean;

import java.util.List;

public interface TreeNode {
	public List<? extends TreeNode> getChildren();

	public TreeNode getParentTreeNode();

	public void addChild(TreeNode child);

	public void addChild(int index, TreeNode child);
}

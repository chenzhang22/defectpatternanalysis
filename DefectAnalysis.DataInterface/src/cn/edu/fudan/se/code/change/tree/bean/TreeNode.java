package cn.edu.fudan.se.code.change.tree.bean;

import java.util.List;

	@SuppressWarnings("rawtypes")
	public interface TreeNode {
	public List getChildren();
	public TreeNode getParentTreeNode();
	public void addChild(TreeNode child);
	public void addChild(int index, TreeNode child);
}

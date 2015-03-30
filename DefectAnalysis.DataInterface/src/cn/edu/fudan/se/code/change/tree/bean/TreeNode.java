package cn.edu.fudan.se.code.change.tree.bean;

import java.util.List;

public interface TreeNode {
	@SuppressWarnings("rawtypes")
	public List getChildren();
	public TreeNode getParentTreeNode();
	public void addChild(TreeNode child);
	public void addChild(int index, TreeNode child);
}

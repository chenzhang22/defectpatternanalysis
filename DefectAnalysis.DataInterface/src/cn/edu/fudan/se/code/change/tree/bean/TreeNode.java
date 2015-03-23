package cn.edu.fudan.se.code.change.tree.bean;

import java.util.List;

public interface TreeNode {
	@SuppressWarnings("rawtypes")
	public List getChildren();
	public TreeNode getParentTreeNode();
}

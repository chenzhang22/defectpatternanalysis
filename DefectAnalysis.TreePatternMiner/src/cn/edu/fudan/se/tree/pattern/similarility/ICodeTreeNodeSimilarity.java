package cn.edu.fudan.se.tree.pattern.similarility;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

public interface ICodeTreeNodeSimilarity extends ITreeNodeSimilarity{
	public double similarity(TreeNode node1, TreeNode node2);

	public double treeNodeSimilarity(TreeNode node1, TreeNode node2);
}

package cn.edu.fudan.se.tree.pattern.similarility;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

public interface ICodeTreeNodeSimilarity {
	public double similarity(CodeTreeNode node1, CodeTreeNode node2);

	public double similarity(CodeChangeTreeNode node1, CodeChangeTreeNode node2);

	public double treeNodeSimilarity(CodeTreeNode node1, CodeTreeNode node2);
}

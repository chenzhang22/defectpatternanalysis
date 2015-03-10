package cn.edu.fudan.se.tree.pattern.similarility;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

public interface ICodeTreeNodeSimilarity {
	public double similarity(CodeTreeNode aggreTreeNode1,
			CodeTreeNode aggreTreeNode2);
	public double similarity(CodeChangeTreeNode aggreTreeNode1,
			CodeChangeTreeNode aggreTreeNode2);
}

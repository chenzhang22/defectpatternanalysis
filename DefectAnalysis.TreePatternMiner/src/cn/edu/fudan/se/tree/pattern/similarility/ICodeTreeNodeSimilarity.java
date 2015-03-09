package cn.edu.fudan.se.tree.pattern.similarility;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

public interface ICodeTreeNodeSimilarity {
	public double similarity(CodeTreeNode aggreTreeNode1,
			CodeTreeNode aggreTreeNode2);
}

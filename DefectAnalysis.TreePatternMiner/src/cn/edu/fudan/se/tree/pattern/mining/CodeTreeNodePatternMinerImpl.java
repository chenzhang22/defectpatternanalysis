/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.mining;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.ICodeTreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public class CodeTreeNodePatternMinerImpl extends AbsCodeTreeNodePatternMiner {

	public CodeTreeNodePatternMinerImpl(ICodeTreeNodeSimilarity nodeSimilary) {
		super(nodeSimilary);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.fudan.se.tree.pattern.mining.AbsAggregatedTreePatternMiner#mine
	 * (java.util.List)
	 */
	@Override
	public CodeTreeNode mine(List<CodeTreeNode> aggregatedTreeNode) {
		// TODO Auto-generated method stub
		return null;
	}

}

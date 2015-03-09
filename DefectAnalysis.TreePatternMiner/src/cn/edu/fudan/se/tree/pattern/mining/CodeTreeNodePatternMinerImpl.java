/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.mining;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.AggregateTypeNode;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public class CodeTreeNodePatternMinerImpl extends AbsAggregatedTreePatternMiner {

	public CodeTreeNodePatternMinerImpl(ITreeNodeSimilarity nodeSimilary) {
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
	public AggregateTypeNode mine(List<AggregateTypeNode> aggregatedTreeNode) {
		// TODO Auto-generated method stub
		return null;
	}

}

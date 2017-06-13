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
public abstract class AbsAggregatedTreePatternMiner {
	protected ITreeNodeSimilarity nodeSimilary = null;

	public AbsAggregatedTreePatternMiner(ITreeNodeSimilarity nodeSimilary) {
		super();
		this.nodeSimilary = nodeSimilary;
	}

	public abstract AggregateTypeNode mine(
			List<AggregateTypeNode> aggregatedTreeNode);
}

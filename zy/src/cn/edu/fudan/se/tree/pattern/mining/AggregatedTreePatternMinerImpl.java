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
public class AggregatedTreePatternMinerImpl extends
		AbsAggregatedTreePatternMiner {

	public AggregatedTreePatternMinerImpl(ITreeNodeSimilarity nodeSimilary) {
		super(nodeSimilary);
	}

	@Override
	public AggregateTypeNode mine(List<AggregateTypeNode> aggregatedTreeNode) {
		// TODO Auto-generated method stub
		return null;
	}
}

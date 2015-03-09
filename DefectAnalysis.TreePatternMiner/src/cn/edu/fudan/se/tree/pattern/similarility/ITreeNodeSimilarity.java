/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.similarility;

import cn.edu.fudan.se.code.change.tree.bean.AggregateTypeNode;

/**
 * @author Lotay
 *
 */
public interface ITreeNodeSimilarity {
	public double similarity(AggregateTypeNode aggreTreeNode1,
			AggregateTypeNode aggreTreeNode2);
}

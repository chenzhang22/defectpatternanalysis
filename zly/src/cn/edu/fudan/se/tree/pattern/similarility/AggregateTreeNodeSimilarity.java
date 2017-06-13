/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.similarility;

import cn.edu.fudan.se.code.change.tree.bean.AggregateTypeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

/**
 * @author Lotay
 *
 */
public class AggregateTreeNodeSimilarity implements ITreeNodeSimilarity {

	@Override
	public double similarity(TreeNode treeNode1,
			TreeNode treeNode2) {
		if (!(treeNode1 instanceof AggregateTypeNode)|| !(treeNode2 instanceof AggregateTypeNode)) {
			return 0;
		}
		AggregateTypeNode aggreTreeNode1 = (AggregateTypeNode) treeNode1;
		AggregateTypeNode aggreTreeNode2 = (AggregateTypeNode) treeNode2;
		double similarity = 0;
		if (aggreTreeNode1 != null && aggreTreeNode2 != null) {
			String changeType1 = aggreTreeNode1.getChangeType();
			String changeType2 = aggreTreeNode2.getChangeType();
			String preNodeValue1 = aggreTreeNode1.getPreNodeValue();
			String preNodeValue2 = aggreTreeNode2.getPreNodeValue();
			String postNodeValue1 = aggreTreeNode1.getPostNodeValue();
			String postNodeValue2 = aggreTreeNode2.getPostNodeValue();
			if (changeType1 == null || changeType2 == null) {
				return similarity;
			}
			if (changeType1.equals(changeType2)) {
				if (preNodeValue1 != null && postNodeValue1 != null) {
					if (preNodeValue1.equals(preNodeValue2)) {
						similarity += 0.5;
					}
					if (postNodeValue1.equals(postNodeValue2)) {
						similarity += 0.5;
					}
				} else if (preNodeValue1 != null) {
					if (preNodeValue1.equals(preNodeValue2)) {
						similarity += 1;
					}
				} else if (postNodeValue1 != null) {
					if (postNodeValue1.equals(postNodeValue2)) {
						similarity += 1;
					}
				} else {
					similarity = 0;
				}
			}
		}
		return similarity;
	}

	@Override
	public double treeNodeSimilarity(TreeNode node1, TreeNode node2) {
		// TODO Auto-generated method stub
		return 0;
	}
}

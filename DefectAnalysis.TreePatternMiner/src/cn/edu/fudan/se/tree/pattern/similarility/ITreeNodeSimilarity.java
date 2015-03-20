/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.similarility;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

/**
 * @author Lotay
 *
 */
public interface ITreeNodeSimilarity {
	public double similarity(TreeNode aggreTreeNode1,
			TreeNode aggreTreeNode2);
	public double treeNodeSimilarity(TreeNode node1, TreeNode node2);
}

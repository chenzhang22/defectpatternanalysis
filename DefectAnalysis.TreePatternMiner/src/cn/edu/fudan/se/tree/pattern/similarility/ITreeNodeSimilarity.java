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
	public double similarity(TreeNode treeNode1,
			TreeNode treeNode2);
	public double treeNodeSimilarity(TreeNode node1, TreeNode node2);
}

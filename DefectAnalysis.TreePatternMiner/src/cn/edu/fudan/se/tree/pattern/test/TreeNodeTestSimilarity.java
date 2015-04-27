/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.test;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public class TreeNodeTestSimilarity implements ITreeNodeSimilarity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity#similarity
	 * (cn.edu.fudan.se.code.change.tree.bean.TreeNode,
	 * cn.edu.fudan.se.code.change.tree.bean.TreeNode)
	 */
	@Override
	public double similarity(TreeNode treeNode1, TreeNode treeNode2) {
		if (treeNode1 instanceof TreeNodeTest
				&& treeNode2 instanceof TreeNodeTest) {
			TreeNodeTest treeNodeTest1 = (TreeNodeTest) treeNode1;
			TreeNodeTest treeNodeTest2 = (TreeNodeTest) treeNode2;
			if (treeNodeTest1.getNodeId().equals(treeNodeTest2.getNodeId())) {
				return 1;
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity#
	 * treeNodeSimilarity(cn.edu.fudan.se.code.change.tree.bean.TreeNode,
	 * cn.edu.fudan.se.code.change.tree.bean.TreeNode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public double treeNodeSimilarity(TreeNode node1, TreeNode node2) {
		double currentSimilarity = this.similarity(node1, node2);
		if (node1.getChildren().size() != node2.getChildren().size()) {
			return 0;
		}
		for (int i = 0; i < node1.getChildren().size(); i++) {
			currentSimilarity *= this.treeNodeSimilarity(
					((List<TreeNode>) node1.getChildren()).get(i),
					((List<TreeNode>) node2.getChildren()).get(i));
		}
		return currentSimilarity;
	}

}

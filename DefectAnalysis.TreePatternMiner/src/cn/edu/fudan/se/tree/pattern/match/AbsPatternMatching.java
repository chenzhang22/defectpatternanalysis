package cn.edu.fudan.se.tree.pattern.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.CodeTreeNodeSimilarityImpl;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

public abstract class AbsPatternMatching {
	protected CodeTreeNode pattern = null;
	protected double similarityThredhold = 1.0;
	protected ITreeNodeSimilarity similarityFunction = new CodeTreeNodeSimilarityImpl();

	public abstract boolean patternMatchAll(CodeTreeNode instanceCodeTreeNode);

	/**
	 * @param patternNode
	 *            : The pattern for mining,
	 * @param instanceCodeTreeNode
	 *            : The candidate instance to match the pattern.
	 * @return
	 */
	private boolean patternMatch(TreeNode patternNode,
			List<TreeNode> candidateNodeList,
			Map<TreeNode, TreeNode> patternMatchedNodes) {
		if (patternNode == null) {
			return true;
		}
		if (candidateNodeList == null || candidateNodeList.isEmpty()) {
			return false;
		}

		while (!candidateNodeList.isEmpty()) {
			TreeNode candidateNode = candidateNodeList.remove(0);
			if (this.similarityFunction.similarity(patternNode, candidateNode) >= this.similarityThredhold) {
				List<TreeNode> childCandidateNodeList = new ArrayList<TreeNode>(
						candidateNode.getChildren());
				for (Object codeTreeNode : patternNode.getChildren()) {
					if (codeTreeNode instanceof TreeNode&&childCandidateNodeList.isEmpty()
							|| !this.patternMatch((TreeNode) codeTreeNode,
									childCandidateNodeList, patternMatchedNodes)) {
						return false;
					}
				}

				patternMatchedNodes.put(patternNode, candidateNode);
				return true;
			} else {
				candidateNodeList.addAll(0, candidateNode.getChildren());
				return this.patternMatch(patternNode, candidateNodeList,
						patternMatchedNodes);
			}
		}
		return false;
	}

	public Map<TreeNode, TreeNode> patternMatch(
			TreeNode patternNodeTree, TreeNode instanceNodeTree) {
		Map<TreeNode, TreeNode> patternMatchedNodes = new HashMap<TreeNode, TreeNode>();
		List<TreeNode> candidateNodeList = new ArrayList<TreeNode>();
		candidateNodeList.add(instanceNodeTree);
		if (patternMatch(patternNodeTree, candidateNodeList,
				patternMatchedNodes)) {
			return patternMatchedNodes;
		}
		return null;
	}
}

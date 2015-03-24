package cn.edu.fudan.se.tree.pattern.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreeNodeClone;
import cn.edu.fudan.se.code.change.tree.utils.ITreeNodeClone;
import cn.edu.fudan.se.tree.pattern.similarility.CodeTreeNodeSimilarityImpl;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

public abstract class AbsPatternMatching {
	protected TreeNode pattern = null;
	protected double similarityThredhold = 1.0;
	protected ITreeNodeSimilarity similarityFunction = new CodeTreeNodeSimilarityImpl();
	protected ITreeNodeClone treeNodeClone = new CodeTreeNodeClone();

	public ITreeNodeClone getTreeNodeClone() {
		return treeNodeClone;
	}

	public void setTreeNodeClone(ITreeNodeClone treeNodeClone) {
		this.treeNodeClone = treeNodeClone;
	}

	public AbsPatternMatching() {
		super();
	}

	public AbsPatternMatching(TreeNode pattern) {
		super();
		this.pattern = pattern;
	}

	public AbsPatternMatching(TreeNode pattern, double similarityThredhold) {
		this(pattern);
		this.similarityThredhold = similarityThredhold;
	}

	public AbsPatternMatching(TreeNode pattern,
			ITreeNodeSimilarity similarityFunction) {
		this(pattern);
		this.similarityFunction = similarityFunction;
	}

	public AbsPatternMatching(TreeNode pattern, double similarityThredhold,
			ITreeNodeSimilarity similarityFunction) {
		this(pattern, similarityThredhold);
		this.similarityFunction = similarityFunction;
	}

	/**
	 * @param instanceCodeTreeNode
	 *            , this method used to match one of the @groupPattern for the @param
	 *            instanceCodeTreeNode, and return the matched pattern.
	 * @return
	 */
	public abstract List<TreeNode> patternMatch(TreeNode instanceCodeTreeNode);
	/**
	 * @param patternNode
	 *            : The pattern for mining,
	 * @param instanceCodeTreeNode
	 *            : The candidate instance to match the pattern.
	 * @return
	 */
	@SuppressWarnings("unchecked")
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
				List<TreeNode> patternChildren = patternNode.getChildren();
				if (patternChildren.isEmpty()) {
					patternMatchedNodes.put(patternNode, candidateNode);
					return true;
				}
				List<TreeNode> childCandidateNodeList = new ArrayList<TreeNode>(
						candidateNode.getChildren());
				if (childCandidateNodeList.isEmpty()) {
					// return this.patternMatch(patternNode, candidateNodeList,
					// patternMatchedNodes);
					continue;
				}
				boolean isChildrenMatched = true;
				for (TreeNode codeTreeNode : patternChildren) {
					if ( childCandidateNodeList.isEmpty()
							|| !this.patternMatch((TreeNode) codeTreeNode,
									childCandidateNodeList, patternMatchedNodes)) {
						isChildrenMatched = false;
						break;
					}
				}
				if (isChildrenMatched) {
					patternMatchedNodes.put(patternNode, candidateNode);
					return true;
				} else {
					// return this.patternMatch(patternNode, candidateNodeList,
					// patternMatchedNodes);
					continue;
				}
			} else {
				candidateNodeList.addAll(0, candidateNode.getChildren());
				// return this.patternMatch(patternNode, candidateNodeList,
				// patternMatchedNodes);
				continue;
			}
		}
		return false;
	}

	public Map<TreeNode, TreeNode> patternMatch(TreeNode patternNodeTree,
			TreeNode instanceNodeTree) {
		Map<TreeNode, TreeNode> patternMatchedNodes = new HashMap<TreeNode, TreeNode>();
		List<TreeNode> candidateNodeList = new ArrayList<TreeNode>();
		candidateNodeList.add(instanceNodeTree);
		if (patternMatch(patternNodeTree, candidateNodeList,
				patternMatchedNodes)) {
			return patternMatchedNodes;
		}
		patternMatchedNodes.clear();
		return null;
	}
}

/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreeNodeClone;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public class ImplGroupPatternInstanceMatching extends AbsGroupPatternMatching {

	public ImplGroupPatternInstanceMatching(List<TreeNode> groupPattern) {
		super(groupPattern);
	}

	public ImplGroupPatternInstanceMatching(List<TreeNode> groupPattern,
			ITreeNodeSimilarity similarityFunction) {
		super(groupPattern, similarityFunction);
	}

	/**
	 * @param groupPatterns
	 * @param similarityFunction
	 * @param similarityThredhold
	 */
	public ImplGroupPatternInstanceMatching(List<TreeNode> groupPatterns,
			ITreeNodeSimilarity similarityFunction, double similarityThredhold) {
		super(groupPatterns, similarityFunction, similarityThredhold);
	}

	/**
	 * This is the matching method for the @instanceCodeTreeNode should match
	 * all the groupPattern partially, used for mining the tree pattern.
	 */

	@Override
	public List<TreeNode> patternMatch(TreeNode nodeTreeInstances) {
		for (TreeNode patternNodeTree : groupPatterns) {
			if (this.patternMatch(patternNodeTree, nodeTreeInstances) != null) {

			}
		}
		return null;
	}

	@Override
	public Map<TreeNode, Map<TreeNode, TreeNode>> patternMatchAll(
			TreeNode instanceCodeTreeNode) {
		// TODO: clone the whole tree.
		TreeNode clonedTreeNode = null;
		if (instanceCodeTreeNode instanceof CodeTreeNode) {
			clonedTreeNode = CodeTreeNodeClone
					.cloneWholeTree((CodeTreeNode) instanceCodeTreeNode);
		} else {
			clonedTreeNode = instanceCodeTreeNode;
		}
		List<TreeNode> candidateInstances = new ArrayList<TreeNode>();
		candidateInstances.add(clonedTreeNode);

		/**
		 * The first key is the pattern, the Map is the corresponding matched
		 * nodes.
		 */
		Map<TreeNode, Map<TreeNode, TreeNode>> matchedPatternNodes = new HashMap<TreeNode, Map<TreeNode, TreeNode>>();

		for (TreeNode pattern : groupPatterns) {
			if (candidateInstances.isEmpty()) {
				break;
			}
			boolean isMatched = false;
			for (TreeNode candidateInstance : candidateInstances) {

				/**
				 * @matchedNodes the key is the pattern node, and the value is
				 *               the corresponding matched nodes.
				 * */
				Map<TreeNode, TreeNode> matchedNodes = super.patternMatch(
						pattern, candidateInstance);
				if (matchedNodes != null && !matchedNodes.isEmpty()) {
					matchedPatternNodes.put(pattern, matchedNodes);
					List<TreeNode> splitedTreesList = super.splitTree(
							candidateInstance, matchedNodes.values());
					candidateInstances.addAll(splitedTreesList);
					isMatched = true;
					break;
				}
			}
			if (!isMatched) {
				break;
			}
		}

		return matchedPatternNodes;
	}
}

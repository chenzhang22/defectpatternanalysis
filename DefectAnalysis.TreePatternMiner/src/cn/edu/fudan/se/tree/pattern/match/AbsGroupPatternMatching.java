/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.match;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.utils.MapDecreasingComparator;
import cn.edu.fudan.se.code.change.tree.utils.TreeNodeUtils;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public abstract class AbsGroupPatternMatching extends AbsPatternMatching {
	protected List<TreeNode> groupPatterns = null;

	public AbsGroupPatternMatching(List<TreeNode> groupPatterns) {
		super();
		if (groupPatterns == null) {
			System.err.println("The Group Pattern is null.");
			return;
		}
		sortGroupPatternByNodeNumber(groupPatterns);
		this.groupPatterns = groupPatterns;
	}

	public AbsGroupPatternMatching(List<TreeNode> groupPatterns,
			ITreeNodeSimilarity similarityFunction) {
		this(groupPatterns);
		this.similarityFunction = similarityFunction;
	}

	public AbsGroupPatternMatching(List<TreeNode> groupPatterns,
			ITreeNodeSimilarity similarityFunction, double similarityThredhold) {
		this(groupPatterns, similarityFunction);
		this.similarityThredhold = similarityThredhold;
	}

	public abstract Map<TreeNode, Map<TreeNode, TreeNode>> patternMatchAll(
			TreeNode instanceCodeTreeNode);

	public double getSimilarityThredhold() {
		return similarityThredhold;
	}

	public void setSimilarityThredhold(double similarityThredhold) {
		this.similarityThredhold = similarityThredhold;
	}

	/**
	 * @param groupPatterns
	 */
	private void sortGroupPatternByNodeNumber(List<TreeNode> groupPatterns) {
		this.groupPatterns = groupPatterns;
		HashMap<TreeNode, Integer> treeNodeNumbers = new HashMap<TreeNode, Integer>();
		for (TreeNode treeNode : groupPatterns) {
			treeNodeNumbers.put(treeNode,
					TreeNodeUtils.countNumberOfNode(treeNode));
		}
		List<Map.Entry<TreeNode, Integer>> treeNodeNumberList = new ArrayList<Map.Entry<TreeNode, Integer>>(
				treeNodeNumbers.entrySet());
		Collections.sort(treeNodeNumberList,
				new MapDecreasingComparator<TreeNode, Integer>());
		groupPatterns.clear();
		for (Map.Entry<TreeNode, Integer> entry : treeNodeNumberList) {
			groupPatterns.add(entry.getKey());
		}
	}

	@SuppressWarnings("unchecked")
	protected List<TreeNode> splitTree(TreeNode candidateInstance,
			Collection<TreeNode> collection) {
		List<TreeNode> splitedTrees = new ArrayList<TreeNode>();
		splitedTrees.add(candidateInstance);

		for (TreeNode matchedNode : collection) {
			TreeNode parentNode = matchedNode.getParentTreeNode();
			if (parentNode != null) {
				parentNode.getChildren().remove(matchedNode);
			} else {
				splitedTrees.remove(matchedNode);
			}
			List<TreeNode> children = matchedNode.getChildren();
			if (children != null && !children.isEmpty()) {
				splitedTrees.addAll(children);
			}
		}
		return splitedTrees;
	}
}

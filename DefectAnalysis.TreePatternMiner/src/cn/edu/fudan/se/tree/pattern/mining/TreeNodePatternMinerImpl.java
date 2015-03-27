/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.mining;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.utils.ITreeNodeClone;
import cn.edu.fudan.se.tree.pattern.bean.TreePattern;
import cn.edu.fudan.se.tree.pattern.bean.TreePatternInstance;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;
import cn.edu.fudan.se.tree.pattern.similarility.ITreePatternSimilarity;
import cn.edu.fudan.se.tree.pattern.utils.ListPermutation;
import cn.edu.fudan.se.tree.pattern.utils.TreePatternClone;

/**
 * @author Lotay
 *
 */
public class TreeNodePatternMinerImpl {
	private static final int MIN_SAME_EXTENSION_SIZE = 1;
	private ITreePatternSimilarity treePatternSimilarity = null;
	private ITreeNodeSimilarity nodeSimilarity = null;
	private int frequencyThreshold = 2;
	private double similarityThreshold = 1.0;
	private TreeNodeGrouping groupingStrategy = null;
	private ITreeNodeClone treeNodeClone = null;
	private TreePatternClone treePatternClone = null;

	public TreeNodePatternMinerImpl(
			ITreePatternSimilarity treePatternSimilarity,
			ITreeNodeClone treeNodeClone) {
		this.treePatternSimilarity = treePatternSimilarity;
		this.nodeSimilarity = this.treePatternSimilarity
				.getNodeSimilarityCalculator();
		this.treeNodeClone = treeNodeClone;
		this.groupingStrategy = new TreeNodeGrouping(this.nodeSimilarity,
				this.similarityThreshold, this.treeNodeClone);
		this.treePatternClone = new TreePatternClone(this.treeNodeClone);
	}

	public TreeNodePatternMinerImpl(
			ITreePatternSimilarity treePatternSimilarity,
			ITreeNodeClone treeNodeClone, int frequencyThreshold) {
		this(treePatternSimilarity, treeNodeClone);
		this.frequencyThreshold = frequencyThreshold;
	}

	public TreeNodePatternMinerImpl(
			ITreePatternSimilarity treePatternSimilarity,
			ITreeNodeClone treeNodeClone, double similarityThreshold) {
		this(treePatternSimilarity, treeNodeClone);
		this.similarityThreshold = similarityThreshold;
	}

	public TreeNodePatternMinerImpl(
			ITreePatternSimilarity treePatternSimilarity,
			ITreeNodeClone treeNodeClone, int frequencyThreshold,
			double similarityThreshold) {
		this(treePatternSimilarity, treeNodeClone, similarityThreshold);
		this.frequencyThreshold = frequencyThreshold;
	}

	public List<TreePattern> mineTreePattern(List<TreeNode> treeInstances) {
		List<TreePattern> frequentPatterns = new ArrayList<TreePattern>();

		Map<TreeNode, Map<TreeNode, List<TreeNode>>> treeNodeGroups = this.groupingStrategy
				.group(treeInstances);
		List<TreePattern> oneTreePatterns = this
				.mineOneSamePattern(treeNodeGroups);
		List<TreePattern> cloneTreePatterns = new ArrayList<TreePattern>();
		for (TreePattern treePattern : oneTreePatterns) {
			cloneTreePatterns.add(this.treePatternClone.clone(treePattern));
		}
		return frequentPatterns;
	}

	private List<TreePattern> mineOneSamePattern(
			Map<TreeNode, Map<TreeNode, List<TreeNode>>> treeNodeGroups) {
		List<TreePattern> oneTreePatterns = new ArrayList<TreePattern>();

		for (TreeNode candidateOneNode : treeNodeGroups.keySet()) {
			Map<TreeNode, List<TreeNode>> instanceMatchedNodes = treeNodeGroups
					.get(candidateOneNode);
			if (instanceMatchedNodes.size() >= this.frequencyThreshold) {
				List<TreeNode> oneNodePattern = new ArrayList<TreeNode>();
				oneNodePattern.add(candidateOneNode);

				TreePattern treePattern = new TreePattern();
				treePattern.addTreePatterns(oneNodePattern);

				for (TreeNode treeInstance : instanceMatchedNodes.keySet()) {
					for (TreeNode matchedNodesMap : instanceMatchedNodes
							.get(treeInstance)) {
						TreePatternInstance patternInstance = new TreePatternInstance();
						Map<TreeNode, TreeNode> matchedInstanceNodesMap = new HashMap<TreeNode, TreeNode>();
						matchedInstanceNodesMap.put(candidateOneNode,
								matchedNodesMap);
						patternInstance.addMatchedNode(candidateOneNode,
								matchedInstanceNodesMap);
						treePattern.addPatternInstance(treeInstance,
								patternInstance);
					}
				}
				oneTreePatterns.add(treePattern);
			}
		}

		Set<TreePattern> builtPatterns = new HashSet<TreePattern>();
		ListPermutation<Map<TreeNode, TreeNode>> treeNodePermutation = new ListPermutation<Map<TreeNode, TreeNode>>();

		/**
		 * For each treePattern in oneTreePatterns;
		 * */
		// for (TreePattern treePattern : oneTreePatterns) {
		// TreeNode patternRootNode = treePattern.getTreePatterns().get(0);
		// Map<TreeNode, List<PatternTreeInstance>> patternInstances =
		// treePattern
		// .getPatternInstances();
		// for (TreeNode matchedInstance : patternInstances.keySet()) {
		// List<PatternTreeInstance> patternTreeInstances = patternInstances
		// .get(matchedInstance);
		// for (PatternTreeInstance patternTreeInstance : patternTreeInstances)
		// {
		//
		// /**
		// * Get every one pattern-matched node for each instance,
		// * */
		// Map<TreeNode, List<Map<TreeNode, TreeNode>>> patternMatchedNode =
		// patternTreeInstance
		// .getMatchedNodes();
		// List<Map<TreeNode, TreeNode>> matchedNodes = patternMatchedNode
		// .get(patternRootNode);
		//
		// if (matchedNodes.size() > MIN_SAME_EXTENSION_SIZE) {
		// List<List<Map<TreeNode, TreeNode>>> permutedTreeNodeList =
		// treeNodePermutation
		// .permuteSorted(matchedNodes,
		// MIN_SAME_EXTENSION_SIZE + 1,
		// matchedNodes.size());
		//
		// // check relation among the tree node list.
		//
		// for (List<Map<TreeNode, TreeNode>> permutedTreeNodes :
		// permutedTreeNodeList) {
		// List<TreeNode> extendedPatternsList = new ArrayList<TreeNode>();
		// int permutedTreeNodesSize = permutedTreeNodes
		// .size();
		//
		// for (int i = 0; i < permutedTreeNodesSize; i++) {
		// extendedPatternsList.add(this.treeNodeClone
		// .cloneNoChildren(patternRootNode));
		// }
		// List<TreeNode> shouldRemoveNodes = new ArrayList<TreeNode>();
		// for (int i = 0; i < permutedTreeNodesSize - 1; i++) {
		// Map<TreeNode, TreeNode> permutedMatchedNodeMap = permutedTreeNodes
		// .get(i);
		// TreeNode nodei = permutedMatchedNodeMap
		// .get(patternRootNode);
		// for (int j = i + 1; j < permutedTreeNodesSize; j++) {
		// TreeNode nodej = permutedTreeNodes.get(j)
		// .get(patternRootNode);
		// if (nodej.getParentTreeNode() == nodei) {
		// shouldRemoveNodes
		// .add(extendedPatternsList
		// .get(j));
		// extendedPatternsList.get(i).addChild(
		// extendedPatternsList.get(j));
		// }
		// }
		// }
		//
		// for (TreeNode removedNode : shouldRemoveNodes) {
		// extendedPatternsList.remove(removedNode);
		// }
		//
		// // check the new pattern whether in the
		// // builtPatterns
		// boolean hasBuilt = this.checkPatternInCollection(builtPatterns,
		// extendedPatternsList) == null;
		// if (hasBuilt) {
		// TreePattern newPattern = new TreePattern();
		//
		// // TODO: count the the frequency and the
		// // corresponding matched instance node....
		//
		// newPattern
		// .addTreePatterns(extendedPatternsList);
		// builtPatterns.add(newPattern);
		//
		//
		//
		//
		// System.out.println();
		// }
		// }
		// }
		// }
		// }
		// }

		return oneTreePatterns;
	}

	private TreePattern checkPatternInCollection(
			Collection<TreePattern> treePatterns, List<TreeNode> checkedPattern) {
		if (checkedPattern != null && treePatterns != null
				&& !treePatterns.isEmpty()) {
			for (TreePattern treePattern : treePatterns) {
				if (this.treePatternSimilarity.similar(
						treePattern.getTreePatterns(), checkedPattern) >= this.similarityThreshold) {
					return treePattern;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private TreePattern checkPatternInCollection(
			Collection<TreePattern> treePatterns, TreePattern checkedPattern) {
		return this.checkPatternInCollection(treePatterns,
				checkedPattern.getTreePatterns());
	}

	public ITreePatternSimilarity getTreePatternSimilarity() {
		return treePatternSimilarity;
	}

	public void setTreePatternSimilarity(
			ITreePatternSimilarity treePatternSimilarity) {
		this.treePatternSimilarity = treePatternSimilarity;
	}

	public ITreeNodeSimilarity getNodeSimilarity() {
		return nodeSimilarity;
	}

	public void setNodeSimilarity(ITreeNodeSimilarity nodeSimilarity) {
		this.nodeSimilarity = nodeSimilarity;
	}

	public double getFrequencyThreshold() {
		return frequencyThreshold;
	}

	public void setFrequencyThreshold(int frequencyThreshold) {
		this.frequencyThreshold = frequencyThreshold;
	}

	public double getSimilarityThreshold() {
		return similarityThreshold;
	}

	public void setSimilarityThreshold(double similarityThreshold) {
		this.similarityThreshold = similarityThreshold;
	}
}

package cn.edu.fudan.se.tree.pattern.mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.utils.ITreeNodeClone;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

public class TreeNodeGrouping {
	private ITreeNodeSimilarity nodeSimilarity = null;
	private double similarityThreshold = 1.0;
	private ITreeNodeClone treeNodeClone = null;

	public TreeNodeGrouping(ITreeNodeSimilarity nodeSimilarity,
			ITreeNodeClone treeNodeClone) {
		super();
		this.nodeSimilarity = nodeSimilarity;
		this.treeNodeClone = treeNodeClone;
	}

	public TreeNodeGrouping(ITreeNodeSimilarity nodeSimilarity,
			double similarityThreshold, ITreeNodeClone treeNodeClone) {
		this(nodeSimilarity, treeNodeClone);
		this.similarityThreshold = similarityThreshold;
	}

	public Map<TreeNode, Map<TreeNode, List<TreeNode>>> group(
			List<TreeNode> treeInstances) {
		Map<TreeNode, Map<TreeNode, List<TreeNode>>> treeNodeGroups = new HashMap<TreeNode, Map<TreeNode, List<TreeNode>>>();

		for (TreeNode treeInstance : treeInstances) {
			this.groupCodeTreeNode(treeInstance, treeInstance, treeNodeGroups);
		}
		return treeNodeGroups;
	}

	@SuppressWarnings("unchecked")
	private void groupCodeTreeNode(TreeNode rootNode, TreeNode treeNode,
			Map<TreeNode, Map<TreeNode, List<TreeNode>>> treeNodeGroup) {
		Set<TreeNode> treeNodes = treeNodeGroup.keySet();
		TreeNode groupHeadNode = null;
		// search whether the codeTreeNode has the same node in the group.
		for (TreeNode groupTreeNode : treeNodes) {
			if (this.nodeSimilarity.similarity(treeNode, groupTreeNode) >= similarityThreshold) {
				groupHeadNode = groupTreeNode;
				break;
			}
		}
		if (groupHeadNode == null) {
			groupHeadNode = this.treeNodeClone.cloneNoChildren(treeNode);
		}
		Map<TreeNode, List<TreeNode>> groupNodesMap = treeNodeGroup
				.get(groupHeadNode);
		if (groupNodesMap == null) {
			groupNodesMap = new HashMap<TreeNode, List<TreeNode>>();
			treeNodeGroup.put(groupHeadNode, groupNodesMap);
		}

		List<TreeNode> groupNodeTreeInstanceList = groupNodesMap.get(rootNode);
		if (groupNodeTreeInstanceList == null) {
			groupNodeTreeInstanceList = new ArrayList<TreeNode>();
			groupNodesMap.put(rootNode, groupNodeTreeInstanceList);
		}
		groupNodeTreeInstanceList.add(treeNode);

		List<TreeNode> children = treeNode.getChildren();
		for (TreeNode childTreeNode : children) {
			this.groupCodeTreeNode(rootNode, childTreeNode, treeNodeGroup);
		}
	}

	public ITreeNodeSimilarity getNodeSimilarity() {
		return nodeSimilarity;
	}

	public void setNodeSimilarity(ITreeNodeSimilarity nodeSimilarity) {
		this.nodeSimilarity = nodeSimilarity;
	}

	public double getSimilarityThreshold() {
		return similarityThreshold;
	}

	public void setSimilarityThreshold(double similarityThreshold) {
		this.similarityThreshold = similarityThreshold;
	}
}

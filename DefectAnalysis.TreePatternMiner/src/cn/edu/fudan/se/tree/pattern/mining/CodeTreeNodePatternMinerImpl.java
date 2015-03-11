/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.ICodeTreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public class CodeTreeNodePatternMinerImpl extends AbsCodeTreeNodePatternMiner {
	// The first level map is used for the CodeChangeTreeNode, and the second
	// Map is a change/unchange codeTreeNode identifier/(the root), the last
	// list store the same
	// change/unchange node list
	// occur in current codeTreeNode
	private Map<CodeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> codeCodeTreeNodeGroup = new HashMap<CodeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>>();
	private Map<CodeTreeNode, List<CodeChangeTreeNode>> treeCodeChangeNodesMap = new HashMap<CodeTreeNode, List<CodeChangeTreeNode>>();

	public CodeTreeNodePatternMinerImpl(ICodeTreeNodeSimilarity nodeSimilary) {
		super(nodeSimilary);
		codeCodeTreeNodeGroup.clear();
		treeCodeChangeNodesMap.clear();
	}

	private Map<CodeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> oneCodeChangeElementMine() {
		Map<CodeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> freECdeCodeTreeNodeGroup = new HashMap<CodeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>>();
		for (CodeTreeNode codeTreeNode : codeCodeTreeNodeGroup.keySet()) {
			if (codeTreeNode instanceof CodeChangeTreeNode) {
				Map<CodeTreeNode, List<CodeTreeNode>> codeTrees = codeCodeTreeNodeGroup
						.get(codeTreeNode);
				if (codeTrees.size() >= minFrequency) {
					freECdeCodeTreeNodeGroup.put(codeTreeNode, codeTrees);
				}
			}
		}
		return freECdeCodeTreeNodeGroup;
	}

	/*
	 * mining the code tree node
	 */
	@Override
	public List<CodeTreeNode> mine(List<CodeTreeNode> codeTreeNodeList) {
		// group the code tree node into a map with same change node.
		codeCodeTreeNodeGroup.clear();
		treeCodeChangeNodesMap.clear();
		for (CodeTreeNode codeTreeNodeInstance : codeTreeNodeList) {
			treeCodeChangeNodesMap.put(codeTreeNodeInstance,
					new ArrayList<CodeChangeTreeNode>());
			groupCodeTreeNode(codeTreeNodeInstance, codeTreeNodeInstance);
		}

		Map<CodeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> freECdeCodeTreeNodeGroup = oneCodeChangeElementMine();

		printNodeChangeInfo(freECdeCodeTreeNodeGroup);
		return null;
	}

	/**
	 * @param freECdeCodeTreeNodeGroup
	 */
	private void printNodeChangeInfo(
			Map<CodeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> freECdeCodeTreeNodeGroup) {
		// print the b
		for (Map<CodeTreeNode, List<CodeTreeNode>> codeTreeNode : freECdeCodeTreeNodeGroup
				.values()) {
			if (codeTreeNode.size() > 1) {
				for (List<CodeTreeNode> codeTreeNode1 : codeTreeNode.values()) {
					for (int i = 0; i < codeTreeNode1.size(); i++) {
						System.out.print(codeTreeNode1.get(i).getType() + "\t");
					}
					System.out.println();
				}
			}
		}
	}

	/*
	 * */
	/**
	 * @param rootNode
	 *            ： the root of the current node..
	 * @param codeTreeNode
	 *            : the current visited node.
	 */
	private void groupCodeTreeNode(CodeTreeNode rootNode,
			CodeTreeNode codeTreeNode) {
		// store the change treenode of each tree...
		if (codeTreeNode instanceof CodeChangeTreeNode) {
			treeCodeChangeNodesMap.get(rootNode).add(
					(CodeChangeTreeNode) codeTreeNode);
		}
		
		Set<CodeTreeNode> codeTreeNodes = codeCodeTreeNodeGroup.keySet();
		CodeTreeNode groupHeadNode = codeTreeNode;
		// search whether the codeTreeNode has the same node in the group.
		for (CodeTreeNode groupTreeNode : codeTreeNodes) {
			if (this.nodeSimilarity.similarity(codeTreeNode, groupTreeNode) == 1) {
				groupHeadNode = groupTreeNode;
				break;
			}
		}
		Map<CodeTreeNode, List<CodeTreeNode>> groupNodesMap = this.codeCodeTreeNodeGroup
				.get(groupHeadNode);
		if (groupNodesMap == null) {
			groupNodesMap = new HashMap<CodeTreeNode, List<CodeTreeNode>>();
			this.codeCodeTreeNodeGroup.put(groupHeadNode, groupNodesMap);
		}

		List<CodeTreeNode> groupNodeTreeNodeList = groupNodesMap.get(rootNode);
		if (groupNodeTreeNodeList == null) {
			groupNodeTreeNodeList = new ArrayList<CodeTreeNode>();
			groupNodesMap.put(rootNode, groupNodeTreeNodeList);
		}
		groupNodeTreeNodeList.add(codeTreeNode);

		for (CodeTreeNode childTreeNode : codeTreeNode.getChildren()) {
			this.groupCodeTreeNode(rootNode, childTreeNode);
		}
	}
}

/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.utils.ITreeNodeClone;
import cn.edu.fudan.se.tree.pattern.bean.TreePattern;
import cn.edu.fudan.se.tree.pattern.bean.TreePatternInstance;

/**
 * @author Lotay
 *
 */
public class TreePatternClone {
	private ITreeNodeClone treeNodeClone = null;

	public TreePatternClone(ITreeNodeClone treeNodeClone) {
		if (treeNodeClone == null) {
			System.err.println("The TreeNodeClone is null.");
		}
		this.treeNodeClone = treeNodeClone;
	}

	public TreePattern clone(TreePattern treePattern) {
		return this.clone(treePattern, new HashMap<TreeNode, TreeNode>());
	};

	/**
	 * @param treePattern
	 * @param matchedPatternTree
	 * @return
	 * 
	 * The treePattern is the tree pattern to be cloned.
	 * And the matchedPatternTree will record original tree node(key) and the cloned pattern node(value). 
	 */
	public TreePattern clone(TreePattern treePattern,
			Map<TreeNode, TreeNode> matchedPatternTree) {
		if (treePattern == null) {
			return null;
		}
		TreePattern clonePattern = new TreePattern();

		Map<TreeNode, List<TreePatternInstance>> patternInstances = treePattern
				.getPatternInstances();
		
		//Clone patterns
		for (TreeNode patternTree : treePattern.getTreePatterns()) {
			Map<TreeNode, TreeNode> clonedNodesMap = new HashMap<TreeNode, TreeNode>();
			TreeNode clonedPatternNode = treeNodeClone.cloneWholeTree(
					patternTree, clonedNodesMap);
			if (clonedPatternNode != null) {
				clonePattern.addTreePattern(clonedPatternNode);
				// store the clone pattern and original tree pattern node
				for (TreeNode clonedPatNode : clonedNodesMap.keySet()) {
					matchedPatternTree.put(clonedNodesMap.get(clonedPatNode), clonedPatNode);
				}
			} else {
				System.err.println("TreePatternClone: Clone Error 1.");
			}
		}
		
		//clone instances
		for (TreeNode treeInstance : patternInstances.keySet()) {
			List<TreePatternInstance> treePatternInstances = patternInstances
					.get(treeInstance);
			
			for (TreePatternInstance treePatternInstance : treePatternInstances) {
				TreePatternInstance clonedTreePatternInstance = new TreePatternInstance();
				for (TreeNode patternTree : treePatternInstance.getMatchedNodes().keySet()) {
					Map<TreeNode,TreeNode> cloneMatchedNodes = new HashMap<TreeNode, TreeNode>();
					
					for (Map.Entry<TreeNode, TreeNode> patternNode : treePatternInstance.getMatchedNodes().get(patternTree).entrySet()) {
						if (matchedPatternTree.containsKey(patternNode.getKey())) {
							TreeNode clonePatternNode = matchedPatternTree.get(patternNode.getKey());
							TreeNode orginalInstanceNode = patternNode.getValue();
							cloneMatchedNodes.put(clonePatternNode, orginalInstanceNode);
						}else {
							System.err.println("TreePatternClone: Clone Error 2.");
						}
					}
					TreeNode clonePatternNode = matchedPatternTree.get(patternTree);
					if (clonePatternNode!=null) {
						clonedTreePatternInstance.addMatchedNode(clonePatternNode, cloneMatchedNodes);
					}else {
						System.err.println("TreePatternClone: Clone Error 3.");
					}
				}
				clonePattern.addPatternInstance(treeInstance, clonedTreePatternInstance);
			}			
		}
		
		
/*			for (TreeNode treeInstance : patternInstances.keySet()) {
				List<TreePatternInstance> treePatternInstances = patternInstances
						.get(treeInstance);
				List<TreePatternInstance> cloneTreePatternInstances = new ArrayList<TreePatternInstance>();
				for (TreePatternInstance treePatternInstance : treePatternInstances) {
					Map<TreeNode, Map<TreeNode, TreeNode>> matchedInstanceNodes = treePatternInstance
							.getMatchedNodes();
					TreePatternInstance cloneTreePatternInstance = new TreePatternInstance();
					Map<TreeNode, TreeNode> orginalNodeMap = matchedInstanceNodes
							.get(patternTree);
					Map<TreeNode, TreeNode> cloneMatchedNodes = new HashMap<TreeNode, TreeNode>();
					for (TreeNode clonePatternNode : clonedNodesMap.keySet()) {
						TreeNode clonedTreeNode = clonedNodesMap
								.get(clonePatternNode);
//						try {
							if (clonedTreeNode != null) {
								cloneMatchedNodes.put(clonePatternNode,
										orginalNodeMap.get(clonedTreeNode));
							} else {
								System.err
										.println("TreePatternClone: Clone Error 2.");
							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
					}
					cloneTreePatternInstance.addMatchedNode(clonedPatternNode,
							cloneMatchedNodes);
					cloneTreePatternInstances.add(cloneTreePatternInstance);
				}
				clonePattern.addPatternInstances(treeInstance,
						cloneTreePatternInstances);
			}
		*/

		return clonePattern;
	}

}

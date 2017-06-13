/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.bean;

import java.util.HashMap;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

/**
 * @author Lotay
 *
 */
public class TreePatternInstance {
	/**
	 * This is a instance of nodes in the matched tree can matched to the
	 * pattern.
	 * 
	 * The key is a parent node of the pattern, the value is the list of
	 * instance nodes match the each corresponding pattern nodes in the pattern.
	 * 
	 * A pattern node can match node several instance nodes.
	 * 
	 * */
	private Map<TreeNode, Map<TreeNode, TreeNode>> matchedNodes = new HashMap<TreeNode, Map<TreeNode, TreeNode>>();

	public TreePatternInstance() {
	}

	public Map<TreeNode, Map<TreeNode, TreeNode>> getMatchedNodes() {
		return matchedNodes;
	}

	public void setMatchedNodes(
			Map<TreeNode, Map<TreeNode, TreeNode>> matchedNodes) {
		this.matchedNodes = matchedNodes;
	}

	public void addMatchedNode(TreeNode patternNode,
			Map<TreeNode, TreeNode> matchedNodes) {
		this.matchedNodes.put(patternNode, matchedNodes);
	}

	@Override
	public String toString() {
		return "PatternInstanceNode [matchedNodes=" + matchedNodes + "]";
	}
}

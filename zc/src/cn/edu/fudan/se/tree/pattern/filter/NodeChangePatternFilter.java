/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.filter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.bean.TreePattern;

/**
 * @author Lotay
 *
 */
public class NodeChangePatternFilter implements IFrequentPatternFilter {

	@Override
	public boolean filter(TreePattern treePattern) {
		List<TreeNode> patterns = treePattern.getTreePatterns();
		for (TreeNode treeNode : patterns) {
			if (filterNodeChangePatternTree(treeNode)) {
				return true;
			}
		}
		return false;
	}

	public void filter(List<TreePattern> frequentPatterns) {
		List<TreePattern> filteredPatterns = new ArrayList<TreePattern>();
		for (TreePattern treePattern : frequentPatterns) {
			if (!filter(treePattern)) {
				filteredPatterns.add(treePattern);
			}
		}
		for (TreePattern treePattern : filteredPatterns) {
			frequentPatterns.remove(treePattern);
		}
	}
	
	private boolean filterNodeChangePatternTree(TreeNode patternNodeTree) {
		if (patternNodeTree==null) {
			return false;
		}
		if (patternNodeTree instanceof CodeChangeTreeNode) {
			return true;
		}
		for (TreeNode childNode : patternNodeTree.getChildren()) {
			if (filterNodeChangePatternTree(childNode)) {
				return true;
			}
		}
		return false;
	}
}

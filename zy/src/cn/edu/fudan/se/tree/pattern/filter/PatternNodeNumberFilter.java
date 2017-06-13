/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.filter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.bean.TreePattern;

/**
 * @author Lotay
 *
 */
public class PatternNodeNumberFilter implements IFrequentPatternFilter {
	private int minNodeNumber = 5;
	
	/**
	 * 
	 */
	public PatternNodeNumberFilter() {
		super();
	}

	public PatternNodeNumberFilter(int minNodeNumber) {
		super();
		this.minNodeNumber = minNodeNumber;
	}

	@Override
	public boolean filter(TreePattern treePattern) {
		int numOfNode = 0;
		for (TreeNode patternNode : treePattern.getTreePatterns()) {
			numOfNode += countNumOfNode(patternNode);
			if (numOfNode>=this.minNodeNumber) {
				return true;
			}
		}
		return false;
	}
	
	private int countNumOfNode(TreeNode patternNode) {
		if (patternNode==null) {
			return 0;
		}
		int cnt = 1;
		for (TreeNode childNode : patternNode.getChildren()) {
			cnt += countNumOfNode(childNode);
		}
		return cnt;
	}

	@Override
	public void filter(List<TreePattern> treePatterns) {
		List<TreePattern> filteredPatterns = new ArrayList<TreePattern>();
		for (TreePattern treePattern : filteredPatterns) {
			if (!this.filter(treePattern)) {
				filteredPatterns.add(treePattern);
			}
		}
		for (TreePattern treePattern : filteredPatterns) {
			treePatterns.remove(treePattern);
		}
	}

}

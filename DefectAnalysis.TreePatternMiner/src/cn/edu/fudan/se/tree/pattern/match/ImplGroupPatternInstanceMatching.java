/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.match;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
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
		super(groupPattern,similarityFunction);
	}

	/**
	 * This is the matching method for the @instanceCodeTreeNode should match
	 * all the groupPattern partially, used for mining the tree pattern.
	 */

	@Override
	public TreeNode patternMatchOne(TreeNode nodeTreeInstances) {
		for (TreeNode patternNodeTree : groupPatterns) {
			if (this.patternMatch(patternNodeTree, nodeTreeInstances)!=null) {
				
			}
		}
		return null;
	}

	@Override
	public boolean patternMatchAll(TreeNode instanceCodeTreeNode) {
		// TODO Auto-generated method stub
		return false;
	}
}

/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.match;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public class ImplSinglePatternInstanceMatching extends AbsPatternMatching {
	public ImplSinglePatternInstanceMatching(TreeNode pattern) {
		super(pattern);
	}

	public ImplSinglePatternInstanceMatching(TreeNode pattern,
			ITreeNodeSimilarity similarityFunction) {
		super(pattern, similarityFunction);
	}

	public ImplSinglePatternInstanceMatching(TreeNode pattern,
			double similarityThredhold) {
		super(pattern, similarityThredhold);
	}

	public ImplSinglePatternInstanceMatching(TreeNode pattern,
			double similarityThredhold, ITreeNodeSimilarity similarityFunction) {
		super(pattern, similarityThredhold, similarityFunction);
	}

	public boolean patternMatch(TreeNode instanceCodeTreeNode) {
		// TODO Auto-generated method stub
		return false;
	}
}

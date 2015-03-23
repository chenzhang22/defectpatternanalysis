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
public class ImplSinglePatternInstanceMatching extends AbsSinglePatternMatching {
	public ImplSinglePatternInstanceMatching(TreeNode pattern) {
		super(pattern);
	}

	public ImplSinglePatternInstanceMatching(TreeNode pattern,
			ITreeNodeSimilarity similarityFunction) {
		super(pattern,similarityFunction);
	}

	@Override
	public boolean patternMatchAll(TreeNode instanceCodeTreeNode) {
		// TODO Auto-generated method stub
		return false;
	}
}

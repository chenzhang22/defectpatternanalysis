/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.match;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.CodeTreeNodeSimilarityImpl;
import cn.edu.fudan.se.tree.pattern.similarility.ICodeTreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public class ImplSinglePatternInstanceMatching extends AbsSinglePatternMatching {
	public ImplSinglePatternInstanceMatching(CodeTreeNode pattern) {
		super(pattern);
	}

	public ImplSinglePatternInstanceMatching(CodeTreeNode pattern,
			ICodeTreeNodeSimilarity similarityFunction) {
		super(pattern);
	}

	@Override
	public boolean match(CodeTreeNode instanceCodeTreeNode) {
		// TODO Auto-generated method stub
		return false;
	}
}

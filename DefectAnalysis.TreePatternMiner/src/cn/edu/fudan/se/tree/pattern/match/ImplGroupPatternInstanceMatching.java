/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.match;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.CodeTreeNodeSimilarityImpl;
import cn.edu.fudan.se.tree.pattern.similarility.ICodeTreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public class ImplGroupPatternInstanceMatching extends AbsGroupPatternMatching {

	public ImplGroupPatternInstanceMatching(List<CodeTreeNode> groupPattern) {
		super(groupPattern);
	}

	public ImplGroupPatternInstanceMatching(List<CodeTreeNode> groupPattern,
			ICodeTreeNodeSimilarity similarityFunction) {
		super(groupPattern,similarityFunction);
	}

	/**
	 * This is the matching method for the @instanceCodeTreeNode should match
	 * all the groupPattern partially, used for mining the tree pattern.
	 */
	@Override
	public boolean patternMatchAll(CodeTreeNode nodeTreeInstances) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public CodeTreeNode patternMatchOne(CodeTreeNode nodeTreeInstances) {
		for (CodeTreeNode patternNodeTree : groupPatterns) {
			if (this.patternMatch(patternNodeTree, nodeTreeInstances)!=null) {
				
			}
		}
		return null;
	}
}

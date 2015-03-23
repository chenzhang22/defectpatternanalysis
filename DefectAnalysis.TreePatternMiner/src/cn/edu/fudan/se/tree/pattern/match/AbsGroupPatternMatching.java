/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.match;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public abstract class AbsGroupPatternMatching extends AbsPatternMatching {
	protected List<TreeNode> groupPatterns = null;
	public AbsGroupPatternMatching(List<TreeNode> groupPatterns) {
		super();
		this.groupPatterns = groupPatterns;
	}

	public AbsGroupPatternMatching(List<TreeNode> groupPatterns,
			ITreeNodeSimilarity similarityFunction) {
		this(groupPatterns);
		this.similarityFunction = similarityFunction;
	}

	public AbsGroupPatternMatching(List<TreeNode> groupPatterns,
			ITreeNodeSimilarity similarityFunction,
			double similarityThredhold) {
		this(groupPatterns, similarityFunction);
		this.similarityThredhold = similarityThredhold;
	}

	/**
	 * @param instanceCodeTreeNode
	 *            , this method used to match one of the @groupPattern for the @param
	 *            instanceCodeTreeNode, and return the matched pattern.
	 * @return
	 */
	public abstract TreeNode patternMatchOne(TreeNode instanceCodeTreeNode);


	public double getSimilarityThredhold() {
		return similarityThredhold;
	}

	public void setSimilarityThredhold(double similarityThredhold) {
		this.similarityThredhold = similarityThredhold;
	}
}

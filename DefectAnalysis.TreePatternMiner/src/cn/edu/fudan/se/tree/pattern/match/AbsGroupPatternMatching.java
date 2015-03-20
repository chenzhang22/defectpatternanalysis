/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.CodeTreeNodeSimilarityImpl;
import cn.edu.fudan.se.tree.pattern.similarility.ICodeTreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public abstract class AbsGroupPatternMatching extends AbsPatternMatching {
	protected List<CodeTreeNode> groupPatterns = null;
	public AbsGroupPatternMatching(List<CodeTreeNode> groupPatterns) {
		super();
		this.groupPatterns = groupPatterns;
	}

	public AbsGroupPatternMatching(List<CodeTreeNode> groupPatterns,
			ICodeTreeNodeSimilarity similarityFunction) {
		this(groupPatterns);
		this.similarityFunction = similarityFunction;
	}

	public AbsGroupPatternMatching(List<CodeTreeNode> groupPatterns,
			ICodeTreeNodeSimilarity similarityFunction,
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
	public abstract CodeTreeNode patternMatchOne(CodeTreeNode instanceCodeTreeNode);


	public double getSimilarityThredhold() {
		return similarityThredhold;
	}

	public void setSimilarityThredhold(double similarityThredhold) {
		this.similarityThredhold = similarityThredhold;
	}
}

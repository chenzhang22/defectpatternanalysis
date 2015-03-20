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
public abstract class AbsGroupPatternMatching implements IPatternMatching {
	protected List<CodeTreeNode> groupPatterns = null;
	private double similarityThredhold = 1.0;
	private ICodeTreeNodeSimilarity similarityFunction = new CodeTreeNodeSimilarityImpl();

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
	public abstract CodeTreeNode matchOne(CodeTreeNode instanceCodeTreeNode);

	/**
	 * @param patternNode
	 *            : The pattern for mining,
	 * @param instanceCodeTreeNode
	 *            : The candidate instance to match the pattern.
	 * @return
	 */
	protected boolean patternMatch(CodeTreeNode patternNode,
			List<CodeTreeNode> candidateNodeList,
			Map<CodeTreeNode, CodeTreeNode> patternMatchedNodes) {
		if (patternNode == null) {
			return true;
		}
		if (candidateNodeList == null || candidateNodeList.isEmpty()) {
			return false;
		}

		while (!candidateNodeList.isEmpty()) {
			CodeTreeNode candidateNode = candidateNodeList.remove(0);
			if (this.similarityFunction.similarity(patternNode, candidateNode) >= this.similarityThredhold) {
				List<CodeTreeNode> childCandidateNodeList = new ArrayList<CodeTreeNode>(
						candidateNode.getChildren());
				for (CodeTreeNode codeTreeNode : patternNode.getChildren()) {
					if (childCandidateNodeList.isEmpty()
							|| !this.patternMatch(codeTreeNode,
									childCandidateNodeList, patternMatchedNodes)) {
						return false;
					}
				}

				patternMatchedNodes.put(patternNode, candidateNode);
				return true;
			} else {
				candidateNodeList.addAll(0, candidateNode.getChildren());
				return this.patternMatch(patternNode, candidateNodeList,
						patternMatchedNodes);
			}
		}
		return false;
	}

	public Map<CodeTreeNode, CodeTreeNode> patternMatch(
			CodeTreeNode patternNodeTree, CodeTreeNode instanceNodeTree) {
		Map<CodeTreeNode, CodeTreeNode> patternMatchedNodes = new HashMap<CodeTreeNode, CodeTreeNode>();
		List<CodeTreeNode> candidateNodeList = new ArrayList<CodeTreeNode>();
		candidateNodeList.add(instanceNodeTree);
		if (this.patternMatch(patternNodeTree, candidateNodeList,
				patternMatchedNodes)) {
			return patternMatchedNodes;
		}
		return null;
	}

	public ICodeTreeNodeSimilarity getSimilarityFunction() {
		return similarityFunction;
	}

	public void setSimilarityFunction(ICodeTreeNodeSimilarity similarityFunction) {
		this.similarityFunction = similarityFunction;
	}

	public double getSimilarityThredhold() {
		return similarityThredhold;
	}

	public void setSimilarityThredhold(double similarityThredhold) {
		this.similarityThredhold = similarityThredhold;
	}
}

/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.mining;

import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public abstract class AbsTreeNodePatternMiner {
	protected ITreeNodeSimilarity nodeSimilarity = null;
	protected double minFrequencyThredhold = 2;
	protected double minSimilarityThrehold = 1.0;

	/**
	 * @param similarity
	 */
	public AbsTreeNodePatternMiner(ITreeNodeSimilarity similarity) {
		super();
		this.nodeSimilarity = similarity;
	}

	public abstract Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> mine(List<CodeTreeNode> codeNodeList);

	public double getMinFrequency() {
		return minFrequencyThredhold;
	}

	public void setMinFrequency(double minFrequency) {
		this.minFrequencyThredhold = minFrequency;
	}
}

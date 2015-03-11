/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.mining;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.ICodeTreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public abstract class AbsCodeTreeNodePatternMiner {
	protected ICodeTreeNodeSimilarity nodeSimilarity = null;
	protected double minFrequency = 1;

	/**
	 * @param similarity
	 */
	public AbsCodeTreeNodePatternMiner(ICodeTreeNodeSimilarity similarity) {
		super();
		this.nodeSimilarity = similarity;
	}

	public abstract List<CodeTreeNode> mine(List<CodeTreeNode> codeNodeList);

	public double getMinFrequency() {
		return minFrequency;
	}

	public void setMinFrequency(double minFrequency) {
		this.minFrequency = minFrequency;
	}
}

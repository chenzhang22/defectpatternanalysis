/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.similarility;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

/**
 * @author Lotay
 *
 */
public interface ITreePatternSimilarity {
	public double similar(List<TreeNode> pattern1,List<TreeNode> pattern2);
	public ITreeNodeSimilarity getNodeSimilarityCalculator();
}

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
public class TreePatternSimilarityImpl implements ITreePatternSimilarity {
	private ITreeNodeSimilarity nodeSimilarity = null;

	public TreePatternSimilarityImpl(
			ITreeNodeSimilarity nodeSimilarityCalculator) throws Exception {
		super();
		if (nodeSimilarityCalculator == null) {
			throw new Exception("The nodeSimilarityCalculator is null.");
		}
		this.nodeSimilarity = nodeSimilarityCalculator;
	}

	@Override
	public double similar(List<TreeNode> pattern1, List<TreeNode> pattern2) {
		if (pattern1 == null || pattern2 == null) {
			System.err.println("The pattern is null.");
			return 0;
		}
		int size = pattern1.size();
		if (size != pattern2.size()) {
			return 0;
		}
		double similarity = 1;
		for (int i = 0; i < size; i++) {
			TreeNode pNode1 = pattern1.get(i);
			TreeNode pNode2 = pattern2.get(i);
			similarity *= nodeSimilarity.treeNodeSimilarity(pNode1, pNode2);
			if(similarity<=0){
				break;
			}
		}
		return similarity;
	}

	public ITreeNodeSimilarity getNodeSimilarityCalculator() {
		return nodeSimilarity;
	}

	public void setNodeSimilarityCalculator(
			ITreeNodeSimilarity nodeSimilarityCalculator) {
		this.nodeSimilarity = nodeSimilarityCalculator;
	}
}

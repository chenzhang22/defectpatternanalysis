/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.similarility;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class CodeChangeTreeNodeSimilarityImpl extends
		CodeTreeNodeSimilarityImpl {

	@Override
	public double similarity(CodeTreeNode aggreTreeNode1,
			CodeTreeNode aggreTreeNode2) {
		// both node should be CodeChangeTreeNode
		if (!(aggreTreeNode1 instanceof CodeChangeTreeNode && aggreTreeNode2 instanceof CodeChangeTreeNode)) {
			return 0;
		}
		CodeChangeTreeNode changeTreeNode1 = (CodeChangeTreeNode) aggreTreeNode1;
		CodeChangeTreeNode changeTreeNode2 = (CodeChangeTreeNode) aggreTreeNode2;

		return 0;
	}

}

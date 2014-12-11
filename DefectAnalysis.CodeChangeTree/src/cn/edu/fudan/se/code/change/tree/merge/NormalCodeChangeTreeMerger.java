/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.merge;

import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class NormalCodeChangeTreeMerger extends
		IgnoreDeleteCodeChangeTreeMerger {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.fudan.se.code.change.tree.merge.ICodeChangeTreeMerger#merge(cn
	 * .edu.fudan.se.code.change.tree.bean.CodeTreeNode,
	 * cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode, java.util.List)
	 */
	@Override
	public CodeTreeNode merge(CodeTreeNode beforeCodeTree,
			CodeTreeNode afterCodeTree, List<SourceCodeChange> changes) {
		super.merge(beforeCodeTree, afterCodeTree, changes);
		System.out.println("deleteListOrder:" + deleteListOrder);
		if ((!deleteListOrder.isEmpty())
				&& (!super.deleteCodeChangeTreeLocations.isEmpty())
				&& (!super.deleteCodeChangeTreeNodeMap.isEmpty())) {
			merge(afterCodeTree);
		}
		return afterCodeTree;
	}

	private void merge(CodeTreeNode afterCodeTreeNode) {

	}
}

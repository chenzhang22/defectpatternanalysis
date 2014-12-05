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
public class NormalCodeChangeTreeMerger extends ICodeChangeTreeMerger {

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

		return null;
	}
}

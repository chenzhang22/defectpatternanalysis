/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.merge;

import java.util.HashMap;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public abstract class ICodeChangeTreeMerger {
	public abstract CodeTreeNode merge(CodeTreeNode beforeCodeTree,
			CodeTreeNode afterCodeTree, List<SourceCodeChange> changes);

	/**
	 * @param codeTreeNode
	 * @param changeTreeNodeMaps
	 * @param includeDeleteChangeType
	 *            -- false: ignore delete, true: does not ignore
	 *            delete(included).
	 */
	protected void buildChangeTreeNodeMap(CodeTreeNode codeTreeNode,
			HashMap<SourceCodeChange, CodeTreeNode> changeTreeNodeMaps,
			boolean includeDeleteChangeType) {
		if (codeTreeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode codeChangeTreeNode = (CodeChangeTreeNode) codeTreeNode;
			SourceCodeChange change = codeChangeTreeNode.getSourceCodeChange();
			if (includeDeleteChangeType || (!(change instanceof Delete))) {
				changeTreeNodeMaps.put(change, codeChangeTreeNode);
			}
		}
		List<CodeTreeNode> childrenNodes = codeTreeNode.getChildren();
		for (CodeTreeNode childNode : childrenNodes) {
			this.buildChangeTreeNodeMap(childNode, changeTreeNodeMaps,
					includeDeleteChangeType);
		}
	}
}

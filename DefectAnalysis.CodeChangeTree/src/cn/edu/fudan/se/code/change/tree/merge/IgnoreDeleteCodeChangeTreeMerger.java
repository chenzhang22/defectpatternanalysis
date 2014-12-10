/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.merge;

import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class IgnoreDeleteCodeChangeTreeMerger extends ICodeChangeTreeMerger {

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
		buildChangeTreeNodeMap(beforeCodeTree);
		merge(afterCodeTree);
		return afterCodeTree;
	}

	private void merge(CodeTreeNode afterCodeTreeNode) {
		if (afterCodeTreeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode codeChangeTreeNode = (CodeChangeTreeNode) afterCodeTreeNode;
			SourceCodeChange change = codeChangeTreeNode.getSourceCodeChange();
			CodeTreeNode beforeCodeTreeNode = changeTreeNodeMaps.get(change);
			if ((beforeCodeTreeNode != null)
					&& (beforeCodeTreeNode instanceof CodeChangeTreeNode)
					&& (!(change instanceof Delete))) {
				CodeChangeTreeNode beforeNode = (CodeChangeTreeNode) beforeCodeTreeNode;
				CodeChangeTreeNode afterNode = (CodeChangeTreeNode) afterCodeTreeNode;
				afterNode.setPreContent(beforeNode.getContent());
				afterNode.setPreEndColumn(beforeNode.getPreEndColumn());
				afterNode.setPreEndIndex(beforeNode.getPreEndIndex());
				afterNode.setPreEndLine(beforeNode.getPreEndLine());
				afterNode.setPreNode(beforeNode.getPreNode());
				afterNode.setPreRevisionId(beforeNode.getPreRevisionId());
				afterNode.setPreSimpleType(beforeNode.getPreSimpleType());
				afterNode.setPreStartColumn(beforeNode.getPreStartColumn());
				afterNode.setPreStartIndex(beforeNode.getPreStartIndex());
				afterNode.setPreStartLine(beforeNode.getPreStartLine());
				afterNode.setPreType(beforeNode.getPreType());
			}
		}
		List<CodeTreeNode> childrenNodes = afterCodeTreeNode.getChildren();
		for (CodeTreeNode childNode : childrenNodes) {
			this.merge(childNode);
		}
	}
}

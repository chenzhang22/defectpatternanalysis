/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.merge;

import java.util.HashMap;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class IgnoreDeleteCodeChangeTreeMerger extends ICodeChangeTreeMerger {
	private final static boolean includeDeleteChangeType = false;

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
		HashMap<SourceCodeChange, CodeTreeNode> changeTreeNodeMaps = new HashMap<SourceCodeChange, CodeTreeNode>();
		buildChangeTreeNodeMap(beforeCodeTree, changeTreeNodeMaps,
				includeDeleteChangeType);
		merge(afterCodeTree, changeTreeNodeMaps);
		return afterCodeTree;
	}

	private void merge(CodeTreeNode afterCodeTreeNode,
			HashMap<SourceCodeChange, CodeTreeNode> changeTreeNodeMaps) {
		if (afterCodeTreeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode codeChangeTreeNode = (CodeChangeTreeNode) afterCodeTreeNode;
			SourceCodeChange change = codeChangeTreeNode.getSourceCodeChange();
			CodeTreeNode beforeCodeTreeNode = changeTreeNodeMaps.get(change);
			if (beforeCodeTreeNode != null
					&& beforeCodeTreeNode instanceof CodeChangeTreeNode) {
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
			this.merge(childNode, changeTreeNodeMaps);
		}
	}
}

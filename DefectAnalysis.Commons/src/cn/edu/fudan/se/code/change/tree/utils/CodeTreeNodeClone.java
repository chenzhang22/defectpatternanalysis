/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

/**
 * @author Lotay
 *
 */
@SuppressWarnings("unchecked")
public class CodeTreeNodeClone implements ITreeNodeClone {
	public TreeNode cloneNoChildren(TreeNode treeNode) {
		CodeTreeNode cloneCodeTreeNode = null;
		if (treeNode instanceof CodeTreeNode) {
			CodeTreeNode codeTreeNode = (CodeTreeNode) treeNode;
			if (codeTreeNode instanceof CodeChangeTreeNode) {
				CodeChangeTreeNode cloneCodeChangeTreeNode = new CodeChangeTreeNode();
				CodeChangeTreeNode codeChangeTreeNode = (CodeChangeTreeNode) codeTreeNode;
				cloneCodeChangeTreeNode.setPreRevisionId(codeChangeTreeNode
						.getPreRevisionId());
				cloneCodeChangeTreeNode.setPreType(codeChangeTreeNode
						.getPreType());
				cloneCodeChangeTreeNode.setPreSimpleType(codeChangeTreeNode
						.getPreSimpleType());
				cloneCodeChangeTreeNode.setPreSimpleNameType(codeChangeTreeNode
						.getPreSimpleNameType());
				cloneCodeChangeTreeNode.setPreNode(codeChangeTreeNode
						.getPreNode());
				cloneCodeChangeTreeNode.setPreStartIndex(codeChangeTreeNode
						.getPreStartIndex());
				cloneCodeChangeTreeNode.setPreEndIndex(codeChangeTreeNode
						.getPreEndIndex());
				cloneCodeChangeTreeNode.setPreStartLine(codeChangeTreeNode
						.getPreStartLine());
				cloneCodeChangeTreeNode.setPreStartColumn(codeChangeTreeNode
						.getPreStartColumn());
				cloneCodeChangeTreeNode.setPreEndLine(codeChangeTreeNode
						.getEndLine());
				cloneCodeChangeTreeNode.setPreEndColumn(codeChangeTreeNode
						.getEndColumn());
				cloneCodeChangeTreeNode.setPreContent(codeChangeTreeNode
						.getPreContent());
				cloneCodeChangeTreeNode.setSourceCodeChange(codeChangeTreeNode
						.getSourceCodeChange());
				cloneCodeTreeNode = cloneCodeChangeTreeNode;
			} else {
				cloneCodeTreeNode = new CodeTreeNode();
			}
			cloneCodeTreeNode.setRepoName(codeTreeNode.getRepoName());
			cloneCodeTreeNode.setRevisionId(codeTreeNode.getRevisionId());
			cloneCodeTreeNode.setFileName(codeTreeNode.getFileName());
			cloneCodeTreeNode.setContent(codeTreeNode.getContent());
			cloneCodeTreeNode.setSimpleNameType(codeTreeNode
					.getSimpleNameType());
			cloneCodeTreeNode.setType(codeTreeNode.getType());
			cloneCodeTreeNode.setSimpleType(codeTreeNode.getSimpleType());
			cloneCodeTreeNode.setNode(codeTreeNode.getNode());

			cloneCodeTreeNode.addNameTypes(codeTreeNode.getNameTypes());
			cloneCodeTreeNode.addBugIds(codeTreeNode.getBugIds());

			cloneCodeTreeNode.setEndColumn(codeTreeNode.getEndColumn());
			cloneCodeTreeNode.setEndIndex(codeTreeNode.getEndIndex());
			cloneCodeTreeNode.setEndLine(codeTreeNode.getEndLine());

			cloneCodeTreeNode.setStartColumn(codeTreeNode.getStartColumn());
			cloneCodeTreeNode.setStartIndex(codeTreeNode.getStartIndex());
			cloneCodeTreeNode.setStartLine(codeTreeNode.getStartLine());
			return cloneCodeTreeNode;
		}
		return null;
	}

	public TreeNode clone(TreeNode codeTreeNode) {
		if (codeTreeNode instanceof CodeTreeNode) {

			TreeNode clonedCodeTreeNode = cloneNoChildren(codeTreeNode);
			List<CodeTreeNode> children = codeTreeNode.getChildren();
			for (TreeNode childCodeTreeNode : children) {
				clonedCodeTreeNode.addChild(childCodeTreeNode);
			}
			return clonedCodeTreeNode;
		}
		return null;
	}

	public TreeNode cloneWholeTree(TreeNode codeTreeNode,
			Map<TreeNode, TreeNode> clonedMappedNodes) {
		if (codeTreeNode instanceof CodeTreeNode) {
			CodeTreeNode clonedCodeTreeNode = (CodeTreeNode) this
					.cloneNoChildren(codeTreeNode);
			clonedMappedNodes.put(clonedCodeTreeNode, codeTreeNode);
			for (TreeNode childNode : (List<CodeTreeNode>) codeTreeNode
					.getChildren()) {
				TreeNode clonedChildCodeTreeNode = cloneWholeTree(childNode,
						clonedMappedNodes);
				clonedCodeTreeNode.addChild(clonedChildCodeTreeNode);
			}
			return clonedCodeTreeNode;
		}
		return null;
	}
	@Override
	public TreeNode cloneWholeTree(TreeNode codeTreeNode) {
		return this.cloneWholeTree(codeTreeNode, new HashMap<TreeNode, TreeNode>());
	}
}

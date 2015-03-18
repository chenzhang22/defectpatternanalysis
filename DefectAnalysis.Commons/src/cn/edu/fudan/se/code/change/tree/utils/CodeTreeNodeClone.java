/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.utils;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class CodeTreeNodeClone {
	public static CodeTreeNode cloneNoChildren(CodeTreeNode codeTreeNode) {
		CodeTreeNode cloneCodeTreeNode = null;

		if (codeTreeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode cloneCodeChangeTreeNode = new CodeChangeTreeNode();
			CodeChangeTreeNode codeChangeTreeNode = (CodeChangeTreeNode) codeTreeNode;
			cloneCodeChangeTreeNode.setPreRevisionId(codeChangeTreeNode.getPreRevisionId());
			cloneCodeChangeTreeNode.setPreType(codeChangeTreeNode.getPreType());
			cloneCodeChangeTreeNode.setPreSimpleType(codeChangeTreeNode.getPreSimpleType());
			cloneCodeChangeTreeNode.setPreSimpleNameType(codeChangeTreeNode.getPreSimpleNameType());
			cloneCodeChangeTreeNode.setPreNode(codeChangeTreeNode.getPreNode());
			cloneCodeChangeTreeNode.setPreStartIndex(codeChangeTreeNode.getPreStartIndex());
			cloneCodeChangeTreeNode.setPreEndIndex(codeChangeTreeNode.getPreEndIndex());
			cloneCodeChangeTreeNode.setPreStartLine(codeChangeTreeNode.getPreStartLine());
			cloneCodeChangeTreeNode.setPreStartColumn(codeChangeTreeNode.getPreStartColumn());
			cloneCodeChangeTreeNode.setPreEndLine(codeChangeTreeNode.getEndLine());
			cloneCodeChangeTreeNode.setPreEndColumn(codeChangeTreeNode.getEndColumn());
			cloneCodeChangeTreeNode.setPreContent(codeChangeTreeNode.getPreContent());
			cloneCodeChangeTreeNode.setSourceCodeChange(codeChangeTreeNode.getSourceCodeChange());
			cloneCodeTreeNode = cloneCodeChangeTreeNode;
		}else {
			cloneCodeTreeNode = new CodeTreeNode();
		}
		cloneCodeTreeNode.setRepoName(codeTreeNode.getRepoName());
		cloneCodeTreeNode.setRevisionId(codeTreeNode.getRevisionId());
		cloneCodeTreeNode.setFileName(codeTreeNode.getFileName());
		cloneCodeTreeNode.setContent(codeTreeNode.getContent());
		cloneCodeTreeNode.setSimpleNameType(codeTreeNode.getSimpleNameType());
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
	
	public static CodeTreeNode clone(CodeTreeNode codeTreeNode) {
		CodeTreeNode clonedCodeTreeNode = cloneNoChildren(codeTreeNode);
		for (CodeTreeNode childCodeTreeNode : codeTreeNode.getChildren()) {
			clonedCodeTreeNode.addChild(childCodeTreeNode);
		}
		return clonedCodeTreeNode;
	}
}

/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.utils;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class CodeTreeNodeClone {
	public static CodeTreeNode clone(CodeTreeNode codeTreeNode) {
		CodeTreeNode cloneCodeTreeNode = new CodeTreeNode();
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
}

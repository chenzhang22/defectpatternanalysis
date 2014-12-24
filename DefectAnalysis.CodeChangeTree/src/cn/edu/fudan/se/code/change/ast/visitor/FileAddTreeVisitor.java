package cn.edu.fudan.se.code.change.ast.visitor;

import org.eclipse.jdt.core.dom.ASTNode;

import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

public class FileAddTreeVisitor extends FileTreeVisitor {

	/**
	 * @param codeRangeList
	 */
	public FileAddTreeVisitor(String revisionId, String fileName,
			CodeBlameLineRangeList codeChangeRangeList) {
		super(revisionId, fileName, codeChangeRangeList);
	}

	@Override
	public void preVisit(ASTNode node) {
		int startLine = startLine(node);
		int endLine = endLine(node);

		CodeTreeNode treeNode = null;
		CodeBlameLineRangeList list = this.checkChangeRange(startLine, endLine);
		if (list.isEmpty()) {
			treeNode = new CodeTreeNode();
		} else {
			treeNode = new CodeChangeTreeNode();
		}

		for (CodeBlameLineRange range : list) {
			treeNode.addBugId(range.getBugId());
		}
		this.buildNormalTreeNode(node, startLine, endLine, list, treeNode);
		this.genNameType(node, treeNode);
	}
}

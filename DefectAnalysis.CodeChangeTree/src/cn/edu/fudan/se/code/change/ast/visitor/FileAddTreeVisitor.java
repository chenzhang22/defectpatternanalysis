package cn.edu.fudan.se.code.change.ast.visitor;

import org.eclipse.jdt.core.dom.ASTNode;

import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

public class FileAddTreeVisitor extends FileTreeVisitor {

	/**
	 * @param lineRangeList
	 * @param codeRangeList
	 */
	public FileAddTreeVisitor(String revisionId, String fileName,
			CodeRangeList codeChangeRangeList,
			CodeBlameLineRangeList revBlameLines) {
		super(revisionId, fileName, codeChangeRangeList);
		this.revBlameLines = revBlameLines;
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

		// for (CodeRange range : list) {
		// treeNode.addBugId(range.getBugId());
		// }
		this.buildNormalTreeNode(node, startLine, endLine, list, treeNode);
		this.genNameType(node, treeNode);
	}
}

package cn.edu.fudan.se.code.change.ast.visitor;

import org.eclipse.jdt.core.dom.ASTNode;

import cn.edu.fudan.se.code.change.tree.bean.ChangeLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

public class FileAddTreeVisitor extends FileTreeVisitor {

	/**
	 * @param codeRangeList
	 */
	public FileAddTreeVisitor(String revisionId, String fileName,
			CodeRangeList codeChangeRangeList) {
		super(revisionId,fileName,codeChangeRangeList);
	}

	@Override
	public void preVisit(ASTNode node) {
		int startLine = startLine(node);
		int endLine = endLine(node);

		CodeTreeNode treeNode = null;
		CodeRangeList list = this.checkChangeRange(startLine, endLine);
		if (list.isEmpty()) {
			treeNode = new CodeTreeNode();
		} else {
			treeNode = new CodeChangeTreeNode();
		}

		for (ChangeLineRange range : list) {
			treeNode.addBugId(range.getBugId());
		}
		// astNodes.push(node);
		int startColumn = this.starColumn(node);
		int endColumn = this.endColumn(node);
		treeNode.setEndColumn(endColumn);
		treeNode.setStartColumn(startColumn);
		treeNode.setStartLine(startLine);
		treeNode.setEndLine(endLine);
		treeNode.setStartIndex(node.getStartPosition());
		treeNode.setEndIndex(node.getStartPosition() + node.getLength());
		treeNode.setNode(node);
		treeNode.setRepoName(repoName);
		treeNode.setFileName(fileName);
		treeNode.setRevisionId(revisionId);
		treeNode.setContent(node.toString());
		treeNode.setType(node.getClass().getName());
		treeNode.setSimpleType(node.getClass().getSimpleName());
		if (parentTreeNode == null) {
			parentTreeNode = treeNode;
			rootTreeNode = parentTreeNode;
		} else {
			// Add the node append to its parent...
			ASTNode parentNode = node.getParent();
			if (astTreeNodes.containsKey(parentNode)) {
				CodeTreeNode codeTreeNode = astTreeNodes.get(parentNode);
				codeTreeNode.addChild(treeNode);
			} else {
				// append the node to the root node.
				rootTreeNode.addChild(treeNode);
			}
		}
		astTreeNodes.put(node, treeNode);
	}

	private CodeRangeList checkChangeRange(int startLine, int endLine) {
		CodeRangeList rangeList = new CodeRangeList();
		for (ChangeLineRange range : this.codeChangeRangeList) {
			if (range.getInducedStartLine() <= startLine
					&& range.getInducedEndLine() >= endLine) {
				rangeList.add(range);
			}
		}
		return rangeList;
	}
}

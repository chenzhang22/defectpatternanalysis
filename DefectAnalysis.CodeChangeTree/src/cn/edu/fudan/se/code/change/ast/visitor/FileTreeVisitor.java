/**
 * 
 */
package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.edu.fudan.se.code.change.tree.bean.ChangeLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;

/**
 * @author Lotay
 *
 */
public abstract class FileTreeVisitor extends ASTVisitor {

	protected HashMap<ASTNode, CodeTreeNode> astTreeNodes = new HashMap<ASTNode, CodeTreeNode>();
	protected String repoName = CodeChangeTreeConstants.REPO_NAME;
	protected String fileName = null;
	protected String revisionId = null;
	protected CodeTreeNode parentTreeNode = null;
	protected CodeTreeNode rootTreeNode = null;
	protected CodeRangeList codeChangeRangeList = null;

	/**
	 * @param fileName
	 * @param revisionId
	 * @param codeChangeRangeList
	 */
	public FileTreeVisitor(String fileName, String revisionId,
			CodeRangeList codeChangeRangeList) {
		super();
		this.fileName = fileName;
		this.revisionId = revisionId;
		this.codeChangeRangeList = codeChangeRangeList;
	}

	protected int startLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node
				.getStartPosition());
	}

	protected int endLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node
				.getStartPosition() + node.getLength());
	}

	protected int starColumn(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getColumnNumber(node
				.getStartPosition());
	}

	protected int endColumn(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getColumnNumber(node
				.getStartPosition() + node.getLength());
	}

	public CodeTreeNode getRootTreeNode() {
		return rootTreeNode;
	}

	protected CodeRangeList checkChangeRange(int startLine, int endLine) {
		CodeRangeList rangeList = new CodeRangeList();
		for (ChangeLineRange range : this.codeChangeRangeList) {
			if (range.getInducedStartLine() <= startLine
					&& range.getInducedEndLine() >= endLine) {
				rangeList.add(range);
			}
		}
		return rangeList;
	}

	/**
	 * @param node
	 * @param startLine
	 * @param endLine
	 * @param list
	 * @param treeNode
	 * @return 
	 */
	protected CodeTreeNode buildNormalTreeNode(ASTNode node, int startLine,
			int endLine, CodeRangeList list, CodeTreeNode treeNode) {
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
		for (ChangeLineRange range : list) {
			treeNode.addBugId(range.getBugId());
		}
		return treeNode;
	}

	/**
	 * @param node
	 * @param treeNode
	 */
	protected void buildTree(ASTNode node, CodeTreeNode treeNode) {
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
}

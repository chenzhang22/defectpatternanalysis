package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.edu.fudan.se.code.change.tree.bean.ChangeLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

public class FileAddedTreeVisitor extends ASTVisitor {

	private HashMap<ASTNode, CodeTreeNode> astTreeNodes = new HashMap<ASTNode, CodeTreeNode>();
	private ChangeLineRange codeChangeRange = null;
	private String repoName = null;
	private String fileName = null;
	private String revisionId = null;

	private CodeTreeNode parentTreeNode = null;
	private CodeTreeNode rootTreeNode = null;
	private int sl = -2;
	private int el = -2;
	private int bugId = -2;

	/**
	 * @param codeRangeList
	 */
	public FileAddedTreeVisitor(String repoName, String revisionId,
			String fileName, ChangeLineRange codeChangeRange) {
		super();
		this.repoName = repoName;
		this.revisionId = revisionId;
		this.fileName = fileName;
		this.codeChangeRange = codeChangeRange;
		this.sl = this.codeChangeRange.getInducedStartLine() + 1;
		this.el = this.codeChangeRange.getInducedEndLine();
		this.bugId = this.codeChangeRange.getBugId();
	}

	@Override
	public void postVisit(ASTNode node) {
		// astNodes.pop();
		super.postVisit(node);
	}

	@Override
	public void preVisit(ASTNode node) {

		int startLine = startLine(node);
		int endLine = endLine(node);
		if (sl > endLine || el < startLine) {
			return;
		}
		CodeTreeNode treeNode = null;
		if (sl <= startLine && el >= endLine) {
			treeNode = new CodeChangeTreeNode();
		} else {
			treeNode = new CodeTreeNode();
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
		treeNode.setBugId(bugId);
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

	@Override
	public boolean preVisit2(ASTNode node) {
		return super.preVisit2(node);
	}

	private int startLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node
				.getStartPosition());
	}

	private int endLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node
				.getStartPosition() + node.getLength());
	}

	private int starColumn(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getColumnNumber(node
				.getStartPosition());
	}

	private int endColumn(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getColumnNumber(node
				.getStartPosition() + node.getLength());
	}

	public CodeTreeNode getRootTreeNode() {
		return rootTreeNode;
	}
}

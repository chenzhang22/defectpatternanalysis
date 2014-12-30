/**
 * 
 */
package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay This is a AST visitor for the tree before change.
 */
public class FileBeforeChangedTreeVisitor extends FileChangeTreeVisitor {
	public FileBeforeChangedTreeVisitor(String fileName, String revisionId,
			CodeRangeList codeRangeList,
			List<SourceCodeChange> sourceCodeChanges) {
		super(fileName, revisionId, codeRangeList, sourceCodeChanges);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.dom.ASTVisitor#preVisit2(org.eclipse.jdt.core.dom
	 * .ASTNode)
	 */
	@Override
	public boolean preVisit2(ASTNode node) {
		int startLine = startLine(node);
		int endLine = endLine(node);
		CodeRangeList list = this.checkChangeRange(startLine, endLine);

		CodeTreeNode treeNode = null;
		ValidNodeResult result = this.checkValidNodeLocation(node);
		int flag = result.getFlag();
		SourceCodeChange sourceCodeChange = result.getChange();
		if (flag > 0) {
			if (flag == 2 && sourceCodeChange != null) {
				treeNode = this.buildBeforeTreeNode(node, startLine, endLine,
						list, sourceCodeChange);
			} else {
				treeNode = this.buildNormalTreeNode(node, startLine, endLine,
						list, new CodeTreeNode());
			}
			treeNode.setRepoName(repoName);
			treeNode.setFileName(fileName);
			this.buildTree(node, treeNode);
			return true;
		}
		return false;
	}

	private CodeTreeNode buildBeforeTreeNode(ASTNode node, int startLine,
			int endLine, CodeRangeList list, SourceCodeChange sourceCodeChange) {
		int startColumn = this.starColumn(node);
		int endColumn = this.endColumn(node);
		CodeChangeTreeNode changeTreeNode = new CodeChangeTreeNode();
		changeTreeNode.setPreEndColumn(endColumn);
		changeTreeNode.setPreStartColumn(startColumn);
		changeTreeNode.setPreStartLine(startLine);
		changeTreeNode.setPreEndLine(endLine);
		changeTreeNode.setPreStartIndex(node.getStartPosition());
		changeTreeNode.setPreEndIndex(node.getStartPosition()
				+ node.getLength());
		changeTreeNode.setPreNode(node);
		changeTreeNode.setPreRevisionId(revisionId);
		changeTreeNode.setPreContent(node.toString());
		changeTreeNode.setPreType(node.getClass().getName());
		changeTreeNode.setPreSimpleType(node.getClass().getSimpleName());
//		for (CodeBlameLineRange range : list) {
//			changeTreeNode.addBugId(range.getBugId());
//		}
		changeTreeNode.setSourceCodeChange(sourceCodeChange);
		return changeTreeNode;
	}

	protected ValidNodeResult checkValidNodeLocation(ASTNode node) {
		ValidNodeResult result = new ValidNodeResult();
		if (sourceCodeChanges == null || sourceCodeChanges.isEmpty()) {
			result.setFlag(0);
			return result;
		}
		int nodeStartIndex = node.getStartPosition();
		int nodeEndIndex = nodeStartIndex + node.getLength();
		for (SourceCodeChange change : sourceCodeChanges) {
			int changeLineStart = -1;
			int changeLineEnd = -1;
			// get the line number
			if (change instanceof Insert) {
				continue;
			} else if (change instanceof Move) {
				Move move = (Move) change;
				changeLineStart = (move.getChangedEntity().getStartPosition());
				changeLineEnd = (move.getChangedEntity().getEndPosition());
			} else if (change instanceof Update) {
				Update update = (Update) change;
				changeLineStart = (update.getChangedEntity().getStartPosition());
				changeLineEnd = (update.getChangedEntity().getEndPosition());
			} else if (change instanceof Delete) {
				changeLineStart = (change.getChangedEntity().getStartPosition());
				changeLineEnd = (change.getChangedEntity().getEndPosition());
			}

			/*
			 * Comparing the AST Visitor, The end index of change (from
			 * ChangeDistiller) is less than(1).
			 */
			if (nodeStartIndex >= changeLineStart
					&& nodeEndIndex <= (changeLineEnd + 1)) {
				result.setChange(change);
				result.setFlag(2);
				return result;
			}
		}
		result.setFlag(1);
		return result;
	}

}

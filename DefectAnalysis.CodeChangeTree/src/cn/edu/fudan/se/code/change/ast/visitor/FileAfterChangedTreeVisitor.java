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
 * @author Lotay This is a AST visitor for the tree after change.
 */
public class FileAfterChangedTreeVisitor extends FileChangeTreeVisitor {
	public FileAfterChangedTreeVisitor(String fileName,
			String changeRevisionId, CodeRangeList codeChangeRangeList,
			List<SourceCodeChange> sourceCodeChanges) {
		super(fileName, changeRevisionId, codeChangeRangeList,
				sourceCodeChanges);
	}

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
				// System.out.println("after:"+node);
				CodeChangeTreeNode changeTreeNode = new CodeChangeTreeNode();
				changeTreeNode.setSourceCodeChange(sourceCodeChange);
				treeNode = changeTreeNode;
			} else {
				treeNode = new CodeTreeNode();
			}
			treeNode = buildNormalTreeNode(node, startLine, endLine, list, treeNode);
			buildTree(node, treeNode);
			return true;
		}
		return false;
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
				changeLineStart = (change.getChangedEntity().getStartPosition());
				changeLineEnd = (change.getChangedEntity().getEndPosition());
			} else if (change instanceof Move) {
				Move move = (Move) change;
				changeLineStart = (move.getNewEntity().getStartPosition());
				changeLineEnd = (move.getNewEntity().getEndPosition());
			} else if (change instanceof Update) {
				Update update = (Update) change;
				changeLineStart = (update.getNewEntity().getStartPosition());
				changeLineEnd = (update.getNewEntity().getEndPosition());
			} else if (change instanceof Delete) {
				continue;
			}

			/*
			 * Comparing the AST Visitor, The end index of change (from
			 * ChangeDistiller) is less than(1).
			 */
			if (nodeStartIndex >= changeLineStart
					&& nodeEndIndex <= (changeLineEnd + 1)) {
				result.setFlag(2);
				result.setChange(change);
				return result;
			}
		}
		result.setFlag(1);
		return result;
	}
}

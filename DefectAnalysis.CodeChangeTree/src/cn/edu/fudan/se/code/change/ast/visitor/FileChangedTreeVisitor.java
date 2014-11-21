/**
 * 
 */
package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * @author Lotay
 * This is a AST visitor for the tree after change.
 */
public class FileChangedTreeVisitor extends FileTreeVisitor {
	private List<SourceCodeChange> sourceOldCodeChanges = null;
	private List<SourceCodeChange> filtedNewChanges = null;
	public FileChangedTreeVisitor(String fileName, String changeRevisionId,
			CodeRangeList codeChangeRangeList,
			List<SourceCodeChange> sourceOldCodeChanges, List<SourceCodeChange> filtedNewChanges) {
		super(fileName, changeRevisionId, codeChangeRangeList);
		this.sourceOldCodeChanges = sourceOldCodeChanges;
		this.filtedNewChanges = filtedNewChanges;
	}

	/*
	 * Comparing the AST Visitor, The end index of change (from ChangeDistiller)
	 * is less than(1).
	 */

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);

		int startLine = startLine(node);
		int endLine = endLine(node);

	}

}

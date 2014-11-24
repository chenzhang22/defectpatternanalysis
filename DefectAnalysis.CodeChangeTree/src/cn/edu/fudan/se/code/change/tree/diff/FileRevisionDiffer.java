/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.diff;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.code.change.ast.visitor.ASTBuilder;
import cn.edu.fudan.se.code.change.ast.visitor.FileAddTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileChangedTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileTreeVisitor;
import cn.edu.fudan.se.code.change.tree.bean.ChangeLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;

/**
 * @author Lotay
 *
 */
public abstract class FileRevisionDiffer {
	public abstract void diff();

	/**
	 * @param fileName
	 * @param revision
	 * @param changes
	 */
	protected CodeTreeNode extractTreeNode(String fileName, String revision,
			CodeRangeList lineRangeList, List<SourceCodeChange> changes) {
		ASTBuilder astBuilder = new ASTBuilder(
				CodeChangeTreeConstants.REPO_PATH);
		CompilationUnit compilationUnit = astBuilder.genCompilationUnit(
				revision, fileName);
		if (compilationUnit == null) {
			return null;
		}

		List<SourceCodeChange> filtedNewChanges = null;
		List<SourceCodeChange> filtedOldChanges = null;
		FileTreeVisitor fileTreeVisitor = null;
		if (changes != null && !changes.isEmpty()) {
			filtedNewChanges = filterNewChange(compilationUnit, changes,
					lineRangeList);
			filtedOldChanges = filterOldChange(compilationUnit, changes,
					lineRangeList);
			fileTreeVisitor = new FileChangedTreeVisitor(revision, fileName,
					lineRangeList, filtedNewChanges);
		} else {
			fileTreeVisitor = new FileAddTreeVisitor(revision, fileName,
					lineRangeList);
		}

		compilationUnit.accept(fileTreeVisitor);

		CodeTreeNode rootTreeNode = fileTreeVisitor.getRootTreeNode();
		return rootTreeNode;
	}

	private List<SourceCodeChange> filterOldChange(
			CompilationUnit compilationUnit, List<SourceCodeChange> changes,
			CodeRangeList lineRangeList) {
		List<SourceCodeChange> filtedChanges = new ArrayList<SourceCodeChange>();

		for (SourceCodeChange change : changes) {
			int changeLineStart = -1;
			int changeLineEnd = -1;
			// get the line number
			if (change instanceof Insert) {
				continue;
			} else if (change instanceof Move) {
				Move move = (Move) change;
				changeLineStart = compilationUnit.getLineNumber(move
						.getChangedEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(move
						.getChangedEntity().getEndPosition());
			} else if (change instanceof Update) {
				Update update = (Update) change;
				changeLineStart = compilationUnit.getLineNumber(update
						.getChangedEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(update
						.getChangedEntity().getEndPosition());
			} else if (change instanceof Delete) {
				filtedChanges.add(change);
				continue;
			}
			// check the validity of the change, whether the change is in the
			// blame line.
			for (ChangeLineRange range : lineRangeList) {
				int rangeLineStart = range.getInducedStartLine();
				int rangeLineEnd = range.getInducedEndLine();
				if (rangeLineStart <= changeLineStart
						&& rangeLineEnd >= changeLineEnd) {
					filtedChanges.add(change);
					break;
				} else if (change instanceof Insert) {
					if (rangeLineStart >= changeLineStart
							&& rangeLineEnd <= changeLineEnd) {
						filtedChanges.add(change);
						break;
					}
				}
			}
		}

		return filtedChanges;
	}

	private List<SourceCodeChange> filterNewChange(
			CompilationUnit compilationUnit, List<SourceCodeChange> changes,
			CodeRangeList lineRangeList) {
		List<SourceCodeChange> filtedChanges = new ArrayList<SourceCodeChange>();

		for (SourceCodeChange change : changes) {
			int changeLineStart = -1;
			int changeLineEnd = -1;
			// get the line number
			if (change instanceof Insert) {
				changeLineStart = compilationUnit.getLineNumber(change
						.getChangedEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(change
						.getChangedEntity().getEndPosition());
			} else if (change instanceof Move) {
				Move move = (Move) change;
				changeLineStart = compilationUnit.getLineNumber(move
						.getNewEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(move
						.getNewEntity().getEndPosition());
			} else if (change instanceof Update) {
				Update update = (Update) change;
				changeLineStart = compilationUnit.getLineNumber(update
						.getNewEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(update
						.getNewEntity().getEndPosition());
			} else if (change instanceof Delete) {
				continue;
			}
			// check the validity of the change, whether the change is in the
			// blame line.
			for (ChangeLineRange range : lineRangeList) {
				int rangeLineStart = range.getInducedStartLine();
				int rangeLineEnd = range.getInducedEndLine();
				if (rangeLineStart <= changeLineStart
						&& rangeLineEnd >= changeLineEnd) {
					filtedChanges.add(change);
					break;
				} else if (change instanceof Insert) {
					if (rangeLineStart >= changeLineStart
							&& rangeLineEnd <= changeLineEnd) {
						filtedChanges.add(change);
						break;
					}
				}
			}
		}

		return filtedChanges;
	}
}

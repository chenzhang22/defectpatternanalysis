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
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameRangeList;

/**
 * @author Lotay
 *
 */
public class SourceCodeChangeAfterFilter extends SourceCodeChangeFilter {
	protected List<SourceCodeChange> filtChanges(
			CompilationUnit compilationUnit, List<SourceCodeChange> changes,
			CodeBlameRangeList lineRangeList) {
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
			for (CodeBlameLineRange range : lineRangeList) {
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

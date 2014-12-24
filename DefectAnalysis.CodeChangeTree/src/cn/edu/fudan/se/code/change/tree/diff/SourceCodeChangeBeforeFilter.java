package cn.edu.fudan.se.code.change.tree.diff;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineList;

public class SourceCodeChangeBeforeFilter extends SourceCodeChangeFilter {
	@Override
	protected List<SourceCodeChange> filtChanges(
			CompilationUnit compilationUnit, List<SourceCodeChange> changes,
			CodeBlameLineList lineRangeList) {
		List<SourceCodeChange> filtedChanges = new ArrayList<SourceCodeChange>();
		for (SourceCodeChange change : changes) {
			if(!(change instanceof Insert)){
				filtedChanges.add(change);
			}
//			int changeLineStart = -1;
//			int changeLineEnd = -1;
			// get the line number
//			if (change instanceof Insert) {
//				continue;
//			} else if (change instanceof Move) {
//				Move move = (Move) change;
//				changeLineStart = compilationUnit.getLineNumber(move
//						.getChangedEntity().getStartPosition());
//				changeLineEnd = compilationUnit.getLineNumber(move
//						.getChangedEntity().getEndPosition());
//			} else if (change instanceof Update) {
//				Update update = (Update) change;
//				changeLineStart = compilationUnit.getLineNumber(update
//						.getChangedEntity().getStartPosition());
//				changeLineEnd = compilationUnit.getLineNumber(update
//						.getChangedEntity().getEndPosition());
//			} else if (change instanceof Delete) {
//				filtedChanges.add(change);
//				continue;
//			}
			
			// check the validity of the change, whether the change is in the
			// blame line.
//			for (ChangeLineRange range : lineRangeList) {
//				int rangeLineStart = range.getInducedStartLine();
//				int rangeLineEnd = range.getInducedEndLine();
//				if (rangeLineStart <= changeLineStart
//						&& rangeLineEnd >= changeLineEnd) {
//					filtedChanges.add(change);
//					break;
//				}
//			}
		}

		return filtedChanges;
	}

}

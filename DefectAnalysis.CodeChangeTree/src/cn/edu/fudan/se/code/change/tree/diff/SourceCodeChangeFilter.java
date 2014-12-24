/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.diff;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineList;

/**
 * @author Lotay
 *
 */
public abstract class SourceCodeChangeFilter {
	protected abstract List<SourceCodeChange> filtChanges(
			CompilationUnit compilationUnit, List<SourceCodeChange> changes,
			CodeBlameLineList lineRangeList);
}

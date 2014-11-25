/**
 * 
 */
package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;

/**
 * @author Lotay This is a AST visitor for the tree before change.
 */
public class FileBeforeChangedTreeVisitor extends FileTreeVisitor {
	private List<SourceCodeChange> sourceCodeChanges = null;

	public FileBeforeChangedTreeVisitor(String fileName, String revisionId,
			CodeRangeList codeChangeRangeList,
			List<SourceCodeChange> sourceCodeChanges) {
		super(fileName, revisionId, codeChangeRangeList);
		this.sourceCodeChanges = sourceCodeChanges;

	}

}

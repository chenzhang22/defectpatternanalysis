/**
 * 
 */
package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;

/**
 * @author Lotay
 *
 */
public abstract class FileChangeTreeVisitor extends FileTreeVisitor {

	protected List<SourceCodeChange> sourceCodeChanges = null;

	public FileChangeTreeVisitor(String fileName, String revisionId,
			CodeRangeList codeRangeList,
			List<SourceCodeChange> sourceCodeChanges) {
		super(fileName, revisionId, codeRangeList);
		this.sourceCodeChanges = sourceCodeChanges;
	}

	protected class ValidNodeResult {
		private SourceCodeChange change = null;
		private int flag = -1;

		public SourceCodeChange getChange() {
			return change;
		}

		public void setChange(SourceCodeChange change) {
			this.change = change;
		}

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}
	}
}

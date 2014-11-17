/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.diff;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;

/**
 * @author Lotay
 *
 */
public class DiffMatcher {

	public List<CodeTreeNode> executeDiffAdd(GitSourceFile gitSourceFile) {
		List<CodeTreeNode> codeTreeNodes = new ArrayList<CodeTreeNode>();
		FileDiff differ = new FileDiff();
		String fileName = gitSourceFile.getFileName();
		String revision = gitSourceFile.getRevisionId();

		List<SourceCodeChange> changes = differ.diff(fileName, revision);

		return codeTreeNodes;
	}

	public List<CodeTreeNode> executeDiffChange(GitSourceFile preSourceFile,
			GitSourceFile changedSourceFile) {
		List<CodeTreeNode> codeTreeNodes = new ArrayList<CodeTreeNode>();

		FileDiff differ = new FileDiff();
		String fileName = preSourceFile.getFileName();
		String preRevision = preSourceFile.getRevisionId();
		String changedRevision = changedSourceFile.getRevisionId();
		List<SourceCodeChange> changes = differ.diff(fileName, preRevision,
				changedRevision);
		return codeTreeNodes;
	}
}

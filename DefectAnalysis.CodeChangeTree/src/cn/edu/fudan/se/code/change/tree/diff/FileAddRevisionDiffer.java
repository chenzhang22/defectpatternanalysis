/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.diff;

import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreePrinter;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;

/**
 * @author Lotay
 *
 */
public class FileAddRevisionDiffer extends FileRevisionDiffer {
	private GitSourceFile gitSourceFile;
	private CodeRangeList revBlameLines;

	public FileAddRevisionDiffer(GitSourceFile gitSourceFile,
			CodeRangeList revBlameLines) {
		this.gitSourceFile = gitSourceFile;
		this.revBlameLines = revBlameLines;
	}

	@Override
	public void diff() {
		if (this.gitSourceFile == null || this.revBlameLines == null) {
			return;
		}
		String fileName = gitSourceFile.getFileName();
		String revision = gitSourceFile.getRevisionId();
		CodeTreeNode codeRootNode = extractTreeNode(fileName, revision,
				revBlameLines, null);

		// TODO: Filter the non-bug-blame code.

		CodeTreePrinter.treeNormalPrint(codeRootNode);
		System.out.println("");
	}
}

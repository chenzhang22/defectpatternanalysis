/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.main;

import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.db.LineRangeGenerator;
import cn.edu.fudan.se.code.change.tree.diff.FileAddRevisionDiffer;
import cn.edu.fudan.se.code.change.tree.diff.FileChangeRevisionDiffer;
import cn.edu.fudan.se.code.change.tree.diff.FileRevisionDiffer;
import cn.edu.fudan.se.code.change.tree.git_change.ChangeSourceFileLoader;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.bean.track.BugInduceBlameLine;

/**
 * @author Lotay
 *
 */
public class CodeChangeDistillerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CodeChangeDistillerMain main = new CodeChangeDistillerMain();
		main.execute();
	}

	public void execute() {
		Map<String, List<GitSourceFile>> gitChangeSourceFiles = ChangeSourceFileLoader
				.loadSourceFiles();
		int size = gitChangeSourceFiles.size();
		int i = 0;
		for (String fileName : gitChangeSourceFiles.keySet()) {
			System.out.println((i++) + "/" + size + ":" + fileName);
			List<GitSourceFile> sourceFiles = gitChangeSourceFiles
					.get(fileName);
			if (sourceFiles == null || sourceFiles.isEmpty()) {
				continue;
			}
			executeFile(sourceFiles);
		}
	}

	private void executeFile(List<GitSourceFile> sourceFiles) {
		int i = 0;
		GitSourceFile preSourceFile = null;
		String fileName = sourceFiles.get(0).getFileName();
		Map<String, CodeRangeList> blameLines = LineRangeGenerator
				.genCodeRangList(fileName);
		FileRevisionDiffer fileRevisionDiffer = null;
		for (; i < sourceFiles.size(); i++) {
			GitSourceFile sourceFile = sourceFiles.get(i);
			if (sourceFile == null) {
				continue;
			}
			String revisionId = sourceFile.getRevisionId();
			CodeRangeList revBlameLines = blameLines.get(revisionId);
			if (revBlameLines == null || revBlameLines.isEmpty()) {
				preSourceFile = sourceFile;
				continue;
			}
			String changeType = sourceFile.getChangeType();
			List<CodeTreeNode> codeTreeNodes = null;
			if (preSourceFile == null || "ADD".equals(changeType)) {
				// the first version of file
				fileRevisionDiffer = new FileAddRevisionDiffer(sourceFile,
						revBlameLines);
			} else {
				// the change version.
				fileRevisionDiffer = new FileChangeRevisionDiffer(
						preSourceFile, sourceFile, revBlameLines);
			}
			fileRevisionDiffer.diff();
			preSourceFile = sourceFile;
		}
	}
}

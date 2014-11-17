/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.main;

import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.diff.DiffMatcher;
import cn.edu.fudan.se.code.change.tree.git_change.ChangeSourceFileLoader;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;

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

	DiffMatcher diffMatcher = new DiffMatcher();

	private void executeFile(List<GitSourceFile> sourceFiles) {
		int j = 0;
		GitSourceFile preSourceFile = null;

		for (; j < sourceFiles.size(); j++) {
			GitSourceFile sourceFile = sourceFiles.get(j);
			if (sourceFile == null) {
				continue;
			}
			String changeType = sourceFile.getChangeType();
			List<CodeTreeNode> codeTreeNodes = null;
			if (preSourceFile == null || "ADD".equals(changeType)) {
				// TODO: the first version of file
				codeTreeNodes = diffMatcher.executeDiffAdd(sourceFile);
			} else {
				// TODO: the change version.
				codeTreeNodes = diffMatcher.executeDiffChange(preSourceFile,
						sourceFile);
			}

			System.out.println(codeTreeNodes.size());

			preSourceFile = sourceFile;
		}
	}
}

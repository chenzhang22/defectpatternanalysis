/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.diff;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jgit.api.errors.GitAPIException;

import cn.edu.fudan.se.code.change.ast.visitor.ASTBuilder;
import cn.edu.fudan.se.code.change.ast.visitor.FileAddTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileTreeVisitor;
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.db.LineRangeGenerator;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.git.explore.main.GitExplore;

/**
 * @author Lotay
 *
 */
public class FileAddRevisionDiffer extends FileRevisionDiffer {
	private GitSourceFile gitSourceFile;
	private CodeBlameLineRangeList revBlameLines;

	public FileAddRevisionDiffer(GitSourceFile gitSourceFile,
			CodeBlameLineRangeList revBlameLines) {
		this.gitSourceFile = gitSourceFile;
		this.revBlameLines = revBlameLines;
	}

	@Override
	public CodeTreeNode diff() {
		if (this.gitSourceFile == null || this.revBlameLines == null) {
			return null;
		}
		String fileName = gitSourceFile.getFileName();
		String revision = gitSourceFile.getRevisionId();
		ArrayList<Integer> changeLines;
		try {
			GitExplore gitExplore = new GitExplore("RepoName1");
			changeLines = gitExplore.gitBlame(fileName, revision);
			CodeRangeList codeRangeList = LineRangeGenerator.genCodeRangList(
					fileName, revision, changeLines);
			CodeTreeNode codeRootNode = extractAddTreeNode(fileName, revision,
					codeRangeList);

			// TODO: Filter the non-bug-blame code.
			// CodeTreePrinter.treeNormalPrint(codeRootNode);
			return codeRootNode;
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
		}
		// System.out.println("");
		return null;
	}

	/**
	 * @param fileName
	 * @param revision
	 * @param changes
	 */
	protected CodeTreeNode extractAddTreeNode(String fileName, String revision,
			CodeRangeList lineRangeList) {
		CompilationUnit compilationUnit = ASTBuilder.genCompilationUnit(
				revision, fileName);
		if (compilationUnit == null) {
			return null;
		}

		FileTreeVisitor fileTreeVisitor = null;
		fileTreeVisitor = new FileAddTreeVisitor(revision, fileName,
				lineRangeList,revBlameLines);
		compilationUnit.accept(fileTreeVisitor);

		CodeTreeNode rootTreeNode = fileTreeVisitor.getRootTreeNode();
		return rootTreeNode;
	}

}

/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.diff;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.ast.visitor.ASTBuilder;
import cn.edu.fudan.se.code.change.ast.visitor.FileAddTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileChangedTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileTreeVisitor;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;
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
		CodeTreeNode codeRootNode = extractAddTreeNode(fileName, revision,
				revBlameLines);

		// TODO: Filter the non-bug-blame code.

		CodeTreePrinter.treeNormalPrint(codeRootNode);
		System.out.println("");
	}

	/**
	 * @param fileName
	 * @param revision
	 * @param changes
	 */
	protected CodeTreeNode extractAddTreeNode(String fileName, String revision,
			CodeRangeList lineRangeList) {
		ASTBuilder astBuilder = new ASTBuilder(
				CodeChangeTreeConstants.REPO_PATH);
		CompilationUnit compilationUnit = astBuilder.genCompilationUnit(
				revision, fileName);
		if (compilationUnit == null) {
			return null;
		}

		FileTreeVisitor fileTreeVisitor = null;
		fileTreeVisitor = new FileAddTreeVisitor(revision, fileName,
				lineRangeList);
		compilationUnit.accept(fileTreeVisitor);

		CodeTreeNode rootTreeNode = fileTreeVisitor.getRootTreeNode();
		return rootTreeNode;
	}

}

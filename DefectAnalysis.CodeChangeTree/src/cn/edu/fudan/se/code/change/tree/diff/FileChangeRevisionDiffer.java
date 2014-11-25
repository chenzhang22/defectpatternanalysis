package cn.edu.fudan.se.code.change.tree.diff;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.ast.visitor.ASTBuilder;
import cn.edu.fudan.se.code.change.ast.visitor.FileAfterChangedTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileBeforeChangedTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileTreeVisitor;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreePrinter;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.git.content.extract.JavaFileContentExtractor;

/**
 * @author Lotay
 *
 */
public class FileChangeRevisionDiffer extends FileRevisionDiffer {
	private GitSourceFile preSourceFile;
	private GitSourceFile changedSourceFile;
	private CodeRangeList revBlameLines;

	public FileChangeRevisionDiffer(GitSourceFile preSourceFile,
			GitSourceFile changedSourceFile, CodeRangeList revBlameLines) {
		super();
		this.preSourceFile = preSourceFile;
		this.changedSourceFile = changedSourceFile;
		this.revBlameLines = revBlameLines;
	}

	@Override
	public void diff() {
		if (this.preSourceFile == null || this.changedSourceFile == null
				|| this.revBlameLines == null) {
			return;
		}
		JavaFileContentExtractor fileContentExtractor = new JavaFileContentExtractor(
				CodeChangeTreeConstants.REPO_PATH);
		String fileName = changedSourceFile.getFileName();
		String changedRevision = changedSourceFile.getRevisionId();
		String preRevision = this.preSourceFile.getRevisionId();

		FileDistiller distiller = ChangeDistiller
				.createFileDistiller(Language.JAVA);
		char beforeChangeChars[] = fileContentExtractor.extract(preRevision,
				fileName);
		char afterChangeChars[] = fileContentExtractor.extract(changedRevision,
				fileName);
		distiller.extractClassifiedSourceCodeChanges(beforeChangeChars,
				fileName, afterChangeChars, fileName);
		List<SourceCodeChange> changes = new ArrayList<SourceCodeChange>();
		changes.addAll(distiller.getSourceCodeChanges());

		CodeTreeNode codeAfterRootNode = extractAfterChangeTreeNode(fileName,
				changedRevision, afterChangeChars, revBlameLines, changes);
		CodeTreeNode codeBeforeRootNode = extractBeforeChangeTreeNode(fileName,
				preRevision, beforeChangeChars, revBlameLines, changes);

		// TODO: filter the bug-blame-code.
		if (codeBeforeRootNode != null) {
//			CodeTreePrinter.treeSimpleTypePrint(codeBeforeRootNode);
		}
		if (codeAfterRootNode != null) {
//			CodeTreePrinter.treeSimpleTypePrint(codeAfterRootNode);
		}

		System.out.println("");
	}

	private CodeTreeNode extractAfterChangeTreeNode(String fileName,
			String revision, char afterChangeChars[],
			CodeRangeList lineRangeList, List<SourceCodeChange> changes) {
		CompilationUnit compilationUnit = ASTBuilder
				.genCompilationUnit(afterChangeChars);
		if (compilationUnit == null) {
			return null;
		}

		List<SourceCodeChange> changeEntitiesNew = null;
		if (changes != null && !changes.isEmpty()) {
			SourceCodeChangeFilter sourceCodeChangeFilter = new SourceCodeChangeAfterFilter();
			changeEntitiesNew = sourceCodeChangeFilter.filtChanges(
					compilationUnit, changes, lineRangeList);
			FileTreeVisitor fileAfterChangedTreeVisitor = new FileAfterChangedTreeVisitor(
					fileName, revision, lineRangeList, changeEntitiesNew);
			compilationUnit.accept(fileAfterChangedTreeVisitor);
			CodeTreeNode rootAfterChangeTreeNode = fileAfterChangedTreeVisitor
					.getRootTreeNode();
			return rootAfterChangeTreeNode;
		}
		return null;
	}

	private CodeTreeNode extractBeforeChangeTreeNode(String fileName,
			String revision, char beforeChangeChars[],
			CodeRangeList lineRangeList, List<SourceCodeChange> changes) {
		CompilationUnit compilationUnit = ASTBuilder
				.genCompilationUnit(beforeChangeChars);
		if (compilationUnit == null) {
			return null;
		}

		List<SourceCodeChange> changeEntitiesOld = null;
		if (changes != null && !changes.isEmpty()) {
			SourceCodeChangeFilter sourceCodeChangeFilter = new SourceCodeChangeBeforeFilter();
			changeEntitiesOld = sourceCodeChangeFilter.filtChanges(
					compilationUnit, changes, lineRangeList);
			FileTreeVisitor fileBeforeChangedTreeVisitor = new FileBeforeChangedTreeVisitor(
					fileName,revision,  lineRangeList, changeEntitiesOld);
			compilationUnit.accept(fileBeforeChangedTreeVisitor);
			CodeTreeNode rootBeforeChangeTreeNode = fileBeforeChangedTreeVisitor
					.getRootTreeNode();
			return rootBeforeChangeTreeNode;
		}
		return null;
	}

}

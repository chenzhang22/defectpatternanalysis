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
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;
import cn.edu.fudan.se.code.change.tree.db.LineRangeGenerator;
import cn.edu.fudan.se.code.change.tree.merge.ICodeChangeTreeMerger;
import cn.edu.fudan.se.code.change.tree.merge.NormalCodeChangeTreeMerger;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreePrinter;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.git.content.extract.JavaFileContentExtractor;
import cn.edu.fudan.se.git.explore.main.GitExplore;

/**
 * @author Lotay
 *
 */
public class FileChangeRevisionDiffer extends FileRevisionDiffer {
	private GitSourceFile preSourceFile;
	private GitSourceFile changedSourceFile;
	private CodeBlameLineRangeList revBlameLines;

	public FileChangeRevisionDiffer(GitSourceFile preSourceFile,
			GitSourceFile changedSourceFile,
			CodeBlameLineRangeList revBlameLines) {
		super();
		this.preSourceFile = preSourceFile;
		this.changedSourceFile = changedSourceFile;
		this.revBlameLines = revBlameLines;
	}

	@Override
	public CodeTreeNode diff() {
		if (this.preSourceFile == null || this.changedSourceFile == null
				|| this.revBlameLines == null) {
			return null;
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
		try {
			ArrayList<Integer> changeLines = GitExplore.gitBlame(fileName,
					changedRevision);
			CodeRangeList codeRangeList = LineRangeGenerator.genCodeRangList(
					fileName, changedRevision, changeLines);

			CodeTreeNode codeAfterRootNode = extractAfterChangeTreeNode(
					fileName, changedRevision, afterChangeChars, codeRangeList,
					changes);
			CodeTreeNode codeBeforeRootNode = extractBeforeChangeTreeNode(
					fileName, preRevision, beforeChangeChars, codeRangeList,
					changes);

			if (codeBeforeRootNode != null && codeAfterRootNode != null) {
				ICodeChangeTreeMerger codeChangeTreeMerger = new NormalCodeChangeTreeMerger();
				CodeTreeNode changeTree = codeChangeTreeMerger.merge(
						codeBeforeRootNode, codeAfterRootNode, changes);
//				CodeTreePrinter.treeNormalPrint(codeAfterRootNode);
				return changeTree;
			} else if (codeBeforeRootNode != null) {
				return codeBeforeRootNode;
			} else if (codeAfterRootNode != null) {
				return codeAfterRootNode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CodeTreeNode extractAfterChangeTreeNode(String fileName,
			String revision, char afterChangeChars[],
			CodeRangeList codeRangeList, List<SourceCodeChange> changes) {
		CompilationUnit compilationUnit = ASTBuilder
				.genCompilationUnit(afterChangeChars);
		if (compilationUnit == null) {
			return null;
		}

		List<SourceCodeChange> changeEntitiesNew = null;
		if (changes != null && !changes.isEmpty()) {
			SourceCodeChangeFilter sourceCodeChangeFilter = new SourceCodeChangeAfterFilter();
			changeEntitiesNew = sourceCodeChangeFilter.filtChanges(
					compilationUnit, changes, codeRangeList);
			FileTreeVisitor fileAfterChangedTreeVisitor = new FileAfterChangedTreeVisitor(
					fileName, revision, codeRangeList, changeEntitiesNew,revBlameLines);
			compilationUnit.accept(fileAfterChangedTreeVisitor);
			CodeTreeNode rootAfterChangeTreeNode = fileAfterChangedTreeVisitor
					.getRootTreeNode();
			return rootAfterChangeTreeNode;
		}
		return null;
	}

	private CodeTreeNode extractBeforeChangeTreeNode(String fileName,
			String revision, char beforeChangeChars[],
			CodeRangeList codeRangeList, List<SourceCodeChange> changes) {
		CompilationUnit compilationUnit = ASTBuilder
				.genCompilationUnit(beforeChangeChars);
		if (compilationUnit == null) {
			return null;
		}

		List<SourceCodeChange> changeEntitiesOld = null;
		if (changes != null && !changes.isEmpty()) {
			SourceCodeChangeFilter sourceCodeChangeFilter = new SourceCodeChangeBeforeFilter();
			changeEntitiesOld = sourceCodeChangeFilter.filtChanges(
					compilationUnit, changes, codeRangeList);
			FileTreeVisitor fileBeforeChangedTreeVisitor = new FileBeforeChangedTreeVisitor(
					fileName, revision, codeRangeList, changeEntitiesOld);
			compilationUnit.accept(fileBeforeChangedTreeVisitor);
			CodeTreeNode rootBeforeChangeTreeNode = fileBeforeChangedTreeVisitor
					.getRootTreeNode();
			return rootBeforeChangeTreeNode;
		}
		return null;
	}

}

package cn.edu.fudan.se.code.change.tree.diff;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.code.change.ast.visitor.ASTBuilder;
import cn.edu.fudan.se.code.change.ast.visitor.FileAddTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileAfterChangedTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileTreeVisitor;
import cn.edu.fudan.se.code.change.tree.bean.ChangeLineRange;
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

		CodeTreeNode codeRootNode = extractChangeTreeNode(fileName,
				changedRevision, revBlameLines, changes);

		// TODO: filter the bug-blame-code.
		if (codeRootNode != null) {
			CodeTreePrinter.treeSimpleTypePrint(codeRootNode);
		}
		System.out.println("");
	}

	/**
	 * @param fileName
	 * @param revision
	 * @param changes
	 */
	protected CodeTreeNode extractChangeTreeNode(String fileName,
			String revision, CodeRangeList lineRangeList,
			List<SourceCodeChange> changes) {
		ASTBuilder astBuilder = new ASTBuilder(
				CodeChangeTreeConstants.REPO_PATH);
		CompilationUnit compilationUnit = astBuilder.genCompilationUnit(
				revision, fileName);
		if (compilationUnit == null) {
			return null;
		}

		List<SourceCodeChange> changeEntitiesNew = null;
		List<SourceCodeChange> changeEntitiesOld = null;
		FileTreeVisitor fileTreeVisitor = null;
		if (changes != null && !changes.isEmpty()) {
			changeEntitiesNew = filterAfterChanges(compilationUnit, changes,
					lineRangeList);
			changeEntitiesOld = filterBeforeChanges(compilationUnit, changes,
					lineRangeList);
			fileTreeVisitor = new FileAfterChangedTreeVisitor(revision, fileName,
					lineRangeList, changeEntitiesNew);
			compilationUnit.accept(fileTreeVisitor);

			CodeTreeNode rootTreeNode = fileTreeVisitor.getRootTreeNode();
			return rootTreeNode;
		}
		return null;
	}

	protected List<SourceCodeChange> filterBeforeChanges(
			CompilationUnit compilationUnit, List<SourceCodeChange> changes,
			CodeRangeList lineRangeList) {
		List<SourceCodeChange> filtedChanges = new ArrayList<SourceCodeChange>();
		for (SourceCodeChange change : changes) {
			int changeLineStart = -1;
			int changeLineEnd = -1;
			// get the line number
			if (change instanceof Insert) {
				continue;
			} else if (change instanceof Move) {
				Move move = (Move) change;
				changeLineStart = compilationUnit.getLineNumber(move
						.getChangedEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(move
						.getChangedEntity().getEndPosition());
			} else if (change instanceof Update) {
				Update update = (Update) change;
				changeLineStart = compilationUnit.getLineNumber(update
						.getChangedEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(update
						.getChangedEntity().getEndPosition());
			} else if (change instanceof Delete) {
				filtedChanges.add(change);
				continue;
			}
			// check the validity of the change, whether the change is in the
			// blame line.
			for (ChangeLineRange range : lineRangeList) {
				int rangeLineStart = range.getInducedStartLine();
				int rangeLineEnd = range.getInducedEndLine();
				if (rangeLineStart <= changeLineStart
						&& rangeLineEnd >= changeLineEnd) {
					filtedChanges.add(change);
					break;
				} else if (change instanceof Insert) {
					if (rangeLineStart >= changeLineStart
							&& rangeLineEnd <= changeLineEnd) {
						filtedChanges.add(change);
						break;
					}
				}
			}
		}

		return filtedChanges;
	}

	protected List<SourceCodeChange> filterAfterChanges(
			CompilationUnit compilationUnit, List<SourceCodeChange> changes,
			CodeRangeList lineRangeList) {
		List<SourceCodeChange> filtedChanges = new ArrayList<SourceCodeChange>();
		for (SourceCodeChange change : changes) {
			int changeLineStart = -1;
			int changeLineEnd = -1;
			// get the line number
			if (change instanceof Insert) {
				changeLineStart = compilationUnit.getLineNumber(change
						.getChangedEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(change
						.getChangedEntity().getEndPosition());
			} else if (change instanceof Move) {
				Move move = (Move) change;
				changeLineStart = compilationUnit.getLineNumber(move
						.getNewEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(move
						.getNewEntity().getEndPosition());
			} else if (change instanceof Update) {
				Update update = (Update) change;
				changeLineStart = compilationUnit.getLineNumber(update
						.getNewEntity().getStartPosition());
				changeLineEnd = compilationUnit.getLineNumber(update
						.getNewEntity().getEndPosition());
			} else if (change instanceof Delete) {
				continue;
			}
			// check the validity of the change, whether the change is in the
			// blame line.
			for (ChangeLineRange range : lineRangeList) {
				int rangeLineStart = range.getInducedStartLine();
				int rangeLineEnd = range.getInducedEndLine();
				if (rangeLineStart <= changeLineStart
						&& rangeLineEnd >= changeLineEnd) {
					filtedChanges.add(change);
					break;
				} else if (change instanceof Insert) {
					if (rangeLineStart >= changeLineStart
							&& rangeLineEnd <= changeLineEnd) {
						filtedChanges.add(change);
						break;
					}
				}
			}
		}

		return filtedChanges;
	}
}

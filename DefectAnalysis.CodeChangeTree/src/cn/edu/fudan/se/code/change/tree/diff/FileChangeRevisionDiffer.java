package cn.edu.fudan.se.code.change.tree.diff;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
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

		CodeTreeNode codeRootNode = extractTreeNode(fileName, changedRevision,
				revBlameLines,changes);

		//TODO: filter the bug-blame-code.
		CodeTreePrinter.treeNormalPrint(codeRootNode);
		
		
		System.out.println("");
	}
}

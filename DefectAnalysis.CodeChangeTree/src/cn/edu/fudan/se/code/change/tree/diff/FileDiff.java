/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.diff;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;
import cn.edu.fudan.se.git.content.extract.JavaFileContentExtractor;

/**
 * @author Lotay
 *
 */
public class FileDiff {

	public List<SourceCodeChange> diff(String fileName, String fromRevision,
			String toRevision) {
		JavaFileContentExtractor fileContentExtractor = new JavaFileContentExtractor(
				CodeChangeTreeConstants.REPO_PATH);
		FileDistiller distiller = ChangeDistiller
				.createFileDistiller(Language.JAVA);
		char beforeChangeChars[] = fileContentExtractor.extract(fromRevision,
				fileName);
		char afterChangeChars[] = fileContentExtractor.extract(toRevision,
				fileName);
		distiller.extractClassifiedSourceCodeChanges(beforeChangeChars,
				fileName, afterChangeChars, fileName);
		List<SourceCodeChange> changes = new ArrayList<SourceCodeChange>();
		changes.addAll(distiller.getSourceCodeChanges());
		return changes;
	}

	public List<SourceCodeChange> diff(String fileName, String revision) {
		JavaFileContentExtractor fileContentExtractor = new JavaFileContentExtractor(
				CodeChangeTreeConstants.REPO_PATH);
		FileDistiller distiller = ChangeDistiller
				.createFileDistiller(Language.JAVA);
		char beforeChangeChars[] = new char[1];
		beforeChangeChars[0] = ' ';
		char afterChangeChars[] = fileContentExtractor.extract(revision,
				fileName);
		distiller.extractClassifiedSourceCodeChanges(beforeChangeChars,
				fileName, afterChangeChars, fileName);
		List<SourceCodeChange> changes = new ArrayList<SourceCodeChange>();
		changes.addAll(distiller.getSourceCodeChanges());
		return changes;
	}

}

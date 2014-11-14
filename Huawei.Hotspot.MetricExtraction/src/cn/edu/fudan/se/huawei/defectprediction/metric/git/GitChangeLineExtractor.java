/**
 * 
 */
package cn.edu.fudan.se.huawei.defectprediction.metric.git;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import cn.edu.fudan.se.huawei.defectprediction.metric.constants.GitConstants;

/**
 * @author Lotay
 *
 */
public class GitChangeLineExtractor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String revisionId = "747fb42f4bc1f71c2bb6fc16ceffec0a8cf788d9";
		String fileName = "org.eclipse.jdt.debug/model/org/eclipse/jdt/internal/debug/core/hcr/MethodSearchVisitor.java";
		GitChangeLineExtractor reader = new GitChangeLineExtractor();
		List<Integer> values = null;
		try {
			values = reader.changeLines(revisionId, fileName);
		} catch (RevisionSyntaxException | IOException | GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(values);
	}

	public List<Integer> changeLines(@Nonnull String revisionId,
			@Nonnull String fileName) throws RevisionSyntaxException,
			MissingObjectException, IncorrectObjectTypeException,
			AmbiguousObjectException, IOException, GitAPIException {
		List<Integer> changeLines = new ArrayList<Integer>();
		if (revisionId == null || fileName == null) {
			return changeLines;
		}
		BlameCommand bcmd = GitConstants.git.blame();
		ObjectId objId = GitConstants.repo.resolve(revisionId);
		RevCommit commit = GitConstants.revWalk.parseCommit(objId);
		bcmd.setStartCommit(commit);
		bcmd.setFilePath(fileName);
		BlameResult bresult = bcmd.call();
		bresult.computeAll();

		for (int line = 0; line < bresult.getResultContents().size(); line++) {
			String revision = bresult.getSourceCommit(line).getName();
			if (revisionId.equals(revision)) {
				changeLines.add(line + 1);
			}
		}

		return changeLines;
	}
}

/**
 * 
 */
package cn.edu.fudan.se.huawei.defectprediction.metric.git;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.WorkingTreeOptions;
import org.eclipse.jgit.util.io.AutoCRLFInputStream;

import cn.edu.fudan.se.huawei.defectprediction.metric.constants.GitConstants;

/**
 * @author Lotay
 *
 */
public class GitFileReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String revisionId = "b04602938404af4f8569a399fb36a5018768a262";
		String fileName = "org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/lookup/SourceTypeBinding.java";
		// TODO Auto-generated method stub
		char[] content = new GitFileReader().readContentFromRepo(revisionId,
				fileName);
		System.out.println(new String(content));
	}

	public char[] readContentFromRepo(String revisionId, String fileName) {
		if (revisionId == null || fileName == null) {
			System.err.println("revisionId/fileName is null..");
			return null;
		}

		if (GitConstants.repo == null || GitConstants.git == null
				|| GitConstants.treeWalk == null) {
			System.err.println("git repo is null..");
			return null;
		}
		RevWalk walk = new RevWalk(GitConstants.repo);

		try {
			ObjectId objId = GitConstants.repo.resolve(revisionId);
			if (objId == null) {
				System.err.println("The revision:" + revisionId
						+ " does not exist.");
				return null;
			}
			RevCommit commit = walk.parseCommit(GitConstants.repo
					.resolve(revisionId));
			if (commit != null) {
				RevTree tree = commit.getTree();
				TreeWalk treeWalk = TreeWalk.forPath(GitConstants.repo,
						fileName, tree);
				ObjectId id = treeWalk.getObjectId(0);
				InputStream is = open(id, GitConstants.repo);
				return IOUtils.toCharArray(is);
			} else {
				System.err.println("Cannot found file(" + fileName
						+ ") in revision(" + revisionId + "): "
						+ GitConstants.treeWalk.getPathString());
			}
		} catch (RevisionSyntaxException e) {
			e.printStackTrace();
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (AmbiguousObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static InputStream open(ObjectId blobId, Repository db)
			throws IOException, IncorrectObjectTypeException {
		if (blobId == null)
			return new ByteArrayInputStream(new byte[0]);

		try {
			WorkingTreeOptions workingTreeOptions = db.getConfig().get(
					WorkingTreeOptions.KEY);
			switch (workingTreeOptions.getAutoCRLF()) {
			case INPUT:
			case FALSE:
				return db.open(blobId, Constants.OBJ_BLOB).openStream();
			case TRUE:
			default:
				return new AutoCRLFInputStream(db.open(blobId,
						Constants.OBJ_BLOB).openStream(), true);
			}
		} catch (MissingObjectException notFound) {
			return null;
		}
	}
}

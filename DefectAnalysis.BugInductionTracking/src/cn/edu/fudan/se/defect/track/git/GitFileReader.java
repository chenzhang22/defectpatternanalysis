/**
 * 
 */
package cn.edu.fudan.se.defect.track.git;

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

/**
 * @author Lotay
 * 
 */
public class GitFileReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String revisionId = "bba53a7d9b86041ddcc96b8e42826ecbf3c21464", fileName = "org.eclipse.jdt.compiler.apt/src/org/eclipse/jdt/internal/compiler/apt/dispatch/HookedJavaFileObject.java";
		GitFileReader reader = new GitFileReader();
		String value = new String(reader.readGitFile(revisionId, fileName));
		System.out.println(value);
	}


	public byte[] readGitFile(String revisionId, String fileName) {
		if (revisionId == null || fileName == null) {
			System.err.println("revisionId/fileName is null..");
			return null;
		}

		if (GitUtils.repo == null || GitUtils.git == null || GitUtils.treeWalk == null) {
			System.err.println("git repo is null..");
			return null;
		}
		RevWalk walk = new RevWalk(GitUtils.repo);

		try {
			ObjectId objId = GitUtils.repo.resolve(revisionId);
			if (objId == null) {
				System.err.println("The revision:" + revisionId
						+ " does not exist.");
				return null;
			}
			RevCommit commit = walk.parseCommit(GitUtils.repo.resolve(revisionId));
			if (commit != null) {
				RevTree tree = commit.getTree();
				TreeWalk treeWalk = TreeWalk.forPath(GitUtils.repo, fileName, tree);
				ObjectId id = treeWalk.getObjectId(0);
				InputStream is = open(id, GitUtils.repo);
				byte[] byteArray = IOUtils.toByteArray(is);
				return byteArray;
			} else {
				System.err.println("Cannot found file(" + fileName
						+ ") in revision(" + revisionId + "): "
						+ GitUtils.treeWalk.getPathString());
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
				// When autocrlf == input the working tree could be either CRLF
				// or LF, i.e. the comparison
				// itself should ignore line endings.
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

/**
 * 
 */
package cn.edu.fudan.se.defect.track.blame.execute;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;

/**
 * @author Lotay
 *
 */
public class BlameCMD {
	private static Repository repo;
	private static Git git;
	private static RevWalk walk;

	public BlameResult gitBlame(String revisionId, String fileName) {
		if (revisionId == null || fileName == null) {
			return null;
		}
		try {
			BlameCommand bcmd = git.blame();
			bcmd.setStartCommit(walk.parseCommit(repo.resolve(revisionId)));
			bcmd.setFilePath(fileName);
			BlameResult bresult = bcmd.call();
			return bresult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static {
		try {
			repo = new FileRepository(new File(
					BugTrackingConstants.GIT_REPO_PATH));
			git = new Git(repo);
			walk = new RevWalk(repo);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

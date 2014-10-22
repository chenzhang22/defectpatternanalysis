/**
 * 
 */
package cn.edu.fudan.se.defect.track.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;

/**
 * @author Lotay
 *
 */
public class GitUtils {

	protected static Repository repo = null;
	protected static Git git = null;
	protected static TreeWalk treeWalk = null;
	protected static RevWalk revWalk = null;
	static{
		try {
			repo = new FileRepository(new File(
					BugTrackingConstants.ECLIPSE_CORE_GIT_REPO_PATH));
			git = new Git(repo);
			treeWalk = new TreeWalk(repo);
			revWalk = new RevWalk(repo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

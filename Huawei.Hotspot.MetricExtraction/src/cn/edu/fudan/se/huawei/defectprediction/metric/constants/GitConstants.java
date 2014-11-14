/**
 * 
 */
package cn.edu.fudan.se.huawei.defectprediction.metric.constants;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

/**
 * @author Lotay
 *
 */
public final class GitConstants {
	public static Repository repo = null;
	public static Git git = null;
	public static TreeWalk treeWalk = null;
	public static RevWalk revWalk = null;
	static {
		try {
			repo = new FileRepository(new File(BasicConstants.GIT_REPO_PATH));
			git = new Git(repo);
			treeWalk = new TreeWalk(repo);
			revWalk = new RevWalk(repo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

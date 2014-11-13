/**
 * 
 */
package cn.edu.fudan.se.git.explore.help;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitChange;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommitInfo;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitChangeDao;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitCommitDao;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitSourceFileDao;
import cn.edu.fudan.se.git.explore.constants.GitExploreConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class TimeUpdate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		updateSourceFileTime();
	}

	public static void updateSourceFileTime() {
		GitCommitDao commitDao = new GitCommitDao();
		GitChangeDao changeDao = new GitChangeDao();
		GitSourceFileDao srcDao = new GitSourceFileDao();
		List<GitCommitInfo> commitInfos = commitDao
				.loadAllGitCommitInfo(GitExploreConstants.HIBERNATE_CONF_PATH);
		for (GitCommitInfo commit : commitInfos) {
			if (commit == null) {
				continue;
			}
			System.out.println(commit.getRevisionID());
			List<GitChange> changes = changeDao.loadChangeByRevisionId(
					commit.getRevisionID(),
					GitExploreConstants.HIBERNATE_CONF_PATH);
			for (GitChange c : changes) {
				c.setTime(commit.getTime());
			}

			HibernateUtils.saveAll(changes,
					GitExploreConstants.HIBERNATE_CONF_PATH);
			List<GitSourceFile> srcFiles = srcDao.loadSourceFileByRevisionId(
					commit.getRevisionID(),
					GitExploreConstants.HIBERNATE_CONF_PATH);
			for (GitSourceFile s : srcFiles) {
				s.setTime(commit.getTime());
			}
			HibernateUtils.saveAll(srcFiles,
					GitExploreConstants.HIBERNATE_CONF_PATH);

		}
	}
}

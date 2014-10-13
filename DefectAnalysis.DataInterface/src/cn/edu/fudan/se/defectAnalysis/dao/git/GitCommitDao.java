/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.git;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommitInfo;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 * 
 */
public class GitCommitDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String revisionId = "00000b60f562c2a527973770ffa6c4e4b7e3b76a";
		System.out.println(new GitCommitDao()
				.loadGitCommitInfoByRevisionId(revisionId));
	}

	@SuppressWarnings("unchecked")
	public GitCommitInfo loadGitCommitInfoByRevisionId(String revisionId) {
		if (revisionId == null) {
			return null;
		}
		String hql = "from GitCommitInfo where revisionId='" + revisionId + "'";
		List<GitCommitInfo> commitInfos = HibernateUtils.retrieveObjects(hql,
				DaoConstants.HIBERNATE_LOCATION_PATH);
		return commitInfos == null || commitInfos.size() != 1 ? null
				: commitInfos.get(0);
	}
}

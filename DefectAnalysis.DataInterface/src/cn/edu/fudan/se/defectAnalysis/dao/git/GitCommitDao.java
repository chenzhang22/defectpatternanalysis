/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.git;

import java.sql.Timestamp;
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
		System.out.println(new GitCommitDao().loadGitCommitInfoByRevisionId(
				revisionId, DaoConstants.TOMCAT_HIBERNATE_LOCATION_PATH));
	}

	@SuppressWarnings("unchecked")
	public GitCommitInfo loadGitCommitInfoByRevisionId(String revisionId,
			String hbmConf) {
		if (revisionId == null || hbmConf == null) {
			return null;
		}
		String hql = "from GitCommitInfo where revisionId='" + revisionId + "'";
		List<GitCommitInfo> commitInfos = HibernateUtils.retrieveObjects(hql,
				hbmConf);
		return commitInfos == null || commitInfos.size() != 1 ? null
				: commitInfos.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<GitCommitInfo> loadAllGitCommitInfo(String hbmConf) {
		if (hbmConf == null) {
			return null;
		}
		List<GitCommitInfo> commitInfos = HibernateUtils.retrieveAll(
				GitCommitInfo.class, hbmConf);
		return commitInfos;
	}
	
	@SuppressWarnings("unchecked")
	public List<GitCommitInfo> loadGitCommitInfoWithinTime(Timestamp startTime,
			Timestamp endTime, String hbmConf) {
		if (startTime == null || endTime == null || hbmConf == null) {
			return null;
		}
		String hql = "from GitCommitInfo where time between'" + startTime
				+ "' and '" + endTime + "'";
		List<GitCommitInfo> commitInfos = HibernateUtils.retrieveObjects(hql,
				hbmConf);
		return commitInfos;
	}
}

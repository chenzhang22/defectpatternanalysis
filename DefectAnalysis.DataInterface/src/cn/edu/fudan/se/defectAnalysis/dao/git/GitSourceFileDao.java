/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.git;

import java.sql.Timestamp;
import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 * 
 */
public class GitSourceFileDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new GitSourceFileDao().loadSourceFileByRevisionId(
				"00000b60f562c2a527973770ffa6c4e4b7e3b76a",
				DaoConstants.TOMCAT_HIBERNATE_LOCATION_PATH));
		System.out
				.println(new GitSourceFileDao()
						.loadSourceFileByFileName(
								"org.eclipse.jdt.core/dom/org/eclipse/jdt/core/dom/ASTConverter.java",
								DaoConstants.TOMCAT_HIBERNATE_LOCATION_PATH)
						.size());
	}

	@SuppressWarnings("unchecked")
	public List<GitSourceFile> loadSourceFileByRevisionId(String revisionId,
			String hbmConf) {
		if (revisionId == null || hbmConf == null) {
			return null;
		}
		String hql = "from GitSourceFile where revisionId='" + revisionId + "'";
		return HibernateUtils.retrieveObjects(hql, hbmConf);
	}

	@SuppressWarnings("unchecked")
	public List<GitSourceFile> loadSourceFileByFileName(String fileName,
			String hbmConf) {
		if (fileName == null || hbmConf == null) {
			return null;
		}
		String hql = "from GitSourceFile where fileName='" + fileName + "' order by time";
		return HibernateUtils.retrieveObjects(hql, hbmConf);
	}

	@SuppressWarnings("unchecked")
	public List<GitSourceFile> loadSourceFileNoTest(String hbmPath) {
		if (hbmPath == null) {
			return null;
		}
		String hql = "from GitSourceFile where fileName not like '%test%' order by fileName,time";
		return HibernateUtils.retrieveObjects(hql, hbmPath);
	}
	
	@SuppressWarnings("unchecked")
	public List<GitSourceFile> loadSourceFileNoTestByTime(Timestamp startTime, 
			Timestamp endTime, String hbmPath) {
		if (startTime == null || endTime == null || hbmPath == null) {
			return null;
		}
		String hql = "from GitSourceFile where fileName not like '%test%' and time between '" 
				+ startTime + "' and '" + endTime + "' order by fileName, time";
		return HibernateUtils.retrieveObjects(hql, hbmPath);
	}
	
	@SuppressWarnings("unchecked")
	public List<GitSourceFile> loadSourceFileNoTestWithinTimeOrderByTime(
			Timestamp startTime, Timestamp endTime, String hbmPath) {
		if (startTime == null || endTime == null || hbmPath == null) {
			return null;
		}
		String hql = "from GitSourceFile where fileName not like '%test%' and time between '"
				+ startTime + "' and '" + endTime + "' order by time, fileName";
		return HibernateUtils.retrieveObjects(hql, hbmPath);
	}
}

/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.git;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitChange;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class GitChangeDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public List<GitChange> loadChangeByRevisionId(String revisionId,
			String hbmConf) {
		if (revisionId == null || hbmConf == null) {
			return null;
		}
		String hql = "from GitChange where revisionId='" + revisionId + "'";
		return HibernateUtils.retrieveObjects(hql, hbmConf);
	}

	@SuppressWarnings("unchecked")
	public List<GitChange> loadChangeByFileName(String fileName,
			String hbmConf) {
		if (fileName == null || hbmConf == null) {
			return null;
		}
		String hql = "from GitChange where fileName='" + fileName
				+ "' order by time";
		return HibernateUtils.retrieveObjects(hql, hbmConf);
	}
}

/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.git;

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
		System.out
				.println(new GitSourceFileDao()
						.loadSourceFileByRevisionId("00000b60f562c2a527973770ffa6c4e4b7e3b76a"));
		System.out
		.println(new GitSourceFileDao()
				.loadSourceFileByFileName("org.eclipse.jdt.core/dom/org/eclipse/jdt/core/dom/ASTConverter.java").size());
	}

	@SuppressWarnings("unchecked")
	public List<GitSourceFile> loadSourceFileByRevisionId(String revisionId) {
		if (revisionId == null) {
			return null;
		}
		String hql = "from GitSourceFile where revisionId='" + revisionId + "'";
		return HibernateUtils.retrieveObjects(hql,
				DaoConstants.HIBERNATE_LOCATION_PATH);
	}

	@SuppressWarnings("unchecked")
	public List<GitSourceFile> loadSourceFileByFileName(String fileName) {
		if (fileName == null) {
			return null;
		}	
		String hql = "from GitSourceFile where fileName='" + fileName + "'";
		return HibernateUtils.retrieveObjects(hql,
				DaoConstants.HIBERNATE_LOCATION_PATH);
	}
}

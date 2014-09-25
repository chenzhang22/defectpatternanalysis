/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.git;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitAuthor;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 * 
 */
public class GitAuthorDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(new GitAuthorDao().listAllAuthors().size());
	}

	@SuppressWarnings("unchecked")
	public List<GitAuthor> listAllAuthors() {
		return HibernateUtils.retrieveAll(GitAuthor.class,
				DaoConstants.HIBERNATE_LOCATION_PATH);
	}
}

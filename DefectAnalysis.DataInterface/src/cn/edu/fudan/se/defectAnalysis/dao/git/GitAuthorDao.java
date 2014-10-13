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
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(new GitAuthorDao().listAllAuthors().size());
	}

	@SuppressWarnings("unchecked")
	public List<GitAuthor> listAllAuthors() {
		return HibernateUtils.retrieveAll(GitAuthor.class,
				DaoConstants.HIBERNATE_LOCATION_PATH);
	}

	@SuppressWarnings("unchecked")
	public GitAuthor loadAuthorById(String authorId) {
		if (authorId == null) {
			return null;
		}
		String hql = "from GitAuthor where authorId = '" + authorId + "'";
		List<GitAuthor> authors = HibernateUtils.retrieveObjects(hql,
				DaoConstants.HIBERNATE_LOCATION_PATH);
		return authors == null || authors.size() != 1 ? null : authors.get(0);
	}
}

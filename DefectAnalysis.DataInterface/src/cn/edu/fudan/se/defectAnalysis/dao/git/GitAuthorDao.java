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
	private String hibernateConf = null;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(new GitAuthorDao(DaoConstants.HIBERNATE_LOCATION_PATH).listAllAuthors().size());
	}

	/**
	 * @param hibernateConf
	 * @throws Exception 
	 */
	public GitAuthorDao(String hibernateConf) throws Exception {
		super();
		if(hibernateConf==null){
			throw new Exception("The hibernate configuration file is null.");
		}
		this.hibernateConf = hibernateConf;
	}



	@SuppressWarnings("unchecked")
	public List<GitAuthor> listAllAuthors() {
		if(hibernateConf==null||hibernateConf.isEmpty()){
			return null;
		}
		return HibernateUtils.retrieveAll(GitAuthor.class,hibernateConf);
	}
}

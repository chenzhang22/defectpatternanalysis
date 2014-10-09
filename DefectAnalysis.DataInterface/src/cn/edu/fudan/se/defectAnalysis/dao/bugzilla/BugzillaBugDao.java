/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.bugzilla;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class BugzillaBugDao {
	private String hibernateConf = null;
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		BugzillaBugDao bugDao = new BugzillaBugDao(DaoConstants.HIBERNATE_LOCATION_PATH);
		String product = "Platform";
		List<BugzillaBug> bugs = bugDao.loadBugzillaBugsByProduct(product);
		System.out.println(bugs.get(0)+"\n\n"+bugs.size());
	}

	/**
	 * @param hibernateConf
	 * @throws Exception 
	 */
	public BugzillaBugDao(String hibernateConf) throws Exception {
		super();
		if(hibernateConf==null){
			throw new Exception("The hibernate configuration file is null.");
		}
		this.hibernateConf = hibernateConf;
	}

	public List<BugzillaBug> loadBugzillaBugsByProduct(String product) throws Exception{
		if(hibernateConf==null||hibernateConf.isEmpty()){
			throw new Exception("The hibernate configuration file is null.");
		}
		if(product==null){
			throw new Exception("The product is null.");
		}
		String hql = "from BugzillaBug where product = '"+product+"'";
		return HibernateUtils.retrieveObjects(hql, hibernateConf);
	}
}

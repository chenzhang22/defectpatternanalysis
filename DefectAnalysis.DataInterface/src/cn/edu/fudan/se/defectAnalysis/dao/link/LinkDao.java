/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.link;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitFiltedLink;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class LinkDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new LinkDao().listLinkByBugId(195346));
	}

	@SuppressWarnings("unchecked")
	public List<FixedBugCommitFiltedLink> listLinkByBugId(final int bugId){
		String hql = "from FixedBugCommitFiltedLink where bugId = "+bugId;
		return HibernateUtils.retrieveObjects(hql, DaoConstants.HIBERNATE_LOCATION_PATH);
	}
}

/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.link;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitFiltedLink;
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitLink;
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
		System.out.println(new LinkDao().listLinks(
				DaoConstants.TOMCAT_HIBERNATE_LOCATION_PATH).get(0));
	}

	@SuppressWarnings("unchecked")
	public List<FixedBugCommitFiltedLink> listLinkByBugId(final int bugId,
			final String hbmConf) {
		if (hbmConf == null) {
			return null;
		}
		String hql = "from FixedBugCommitFiltedLink where bugId = " + bugId
				+ " order by bugId";
		return HibernateUtils.retrieveObjects(hql, hbmConf);
	}

	@SuppressWarnings("unchecked")
	public List<FixedBugCommitFiltedLink> listLinks(String hbmPath) {
		if (hbmPath == null) {
			return null;
		}
		String hql = "from FixedBugCommitFiltedLink order by bugId";
		return HibernateUtils.retrieveObjects(hql, hbmPath);
	}
	
	@SuppressWarnings("unchecked")
	public List<FixedBugCommitLink> listUnFiltedLinks(String hbmPath) {
		if (hbmPath == null) {
			return null;
		}
		String hql = "from FixedBugCommitLink order by bugId";
		return HibernateUtils.retrieveObjects(hql, hbmPath);
	}
}

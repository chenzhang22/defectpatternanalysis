/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.track;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.track.BugSurvivalTime;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class BugSurvivalTimeDao {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String fileName = 	"org.eclipse.jdt.apt.core/src/org/eclipse/jdt/apt/core/util/AptPreferenceConstants.java";

		System.out.println(loadAllBugSurvivalTime(DaoConstants.ECLIPSE_CORE_HIBERNATE_LOCATION_PATH).size());
	}

	@SuppressWarnings("unchecked")
	public static List<BugSurvivalTime> loadBugSurvivalTimeForFile(
			String fileName, final String hbmConf) {
		if (fileName == null || hbmConf == null) {
			return null;
		}
		String hql = "from BugSurvivalTime where fileName='" + fileName + "'";
		return HibernateUtils.retrieveObjects(hql, hbmConf);
	}
	
	@SuppressWarnings("unchecked")
	public static List<BugSurvivalTime> loadAllBugSurvivalTime(final String hbmConf) {
		if (hbmConf == null) {
			return null;
		}
		return HibernateUtils.retrieveAll(BugSurvivalTime.class, hbmConf);
	}
}

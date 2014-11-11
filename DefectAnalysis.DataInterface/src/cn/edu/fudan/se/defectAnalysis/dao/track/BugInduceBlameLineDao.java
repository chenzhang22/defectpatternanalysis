/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.track;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.track.BugInduceBlameLine;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class BugInduceBlameLineDao {
	@SuppressWarnings("unchecked")
	public List<BugInduceBlameLine> blameLinesForFile(String hbmConf,
			String fileName) {
		if (hbmConf == null || fileName == null)
			return null;
		String hql = "from BugInduceBlameLine where fileName='" + fileName
				+ "' order by inducedRevisionId,fixedRevisionid,bugId,inducedlineNumber";
		return HibernateUtils.retrieveObjects(hql, hbmConf);
	}

	public static void main(String args[]) {
		String fileName = "org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/lookup/MethodVerifier.java";
		List<BugInduceBlameLine> lines = new BugInduceBlameLineDao()
				.blameLinesForFile(
						DaoConstants.ECLIPSE_CORE_HIBERNATE_LOCATION_PATH,
						fileName);
		System.out.println(lines);
	}
}

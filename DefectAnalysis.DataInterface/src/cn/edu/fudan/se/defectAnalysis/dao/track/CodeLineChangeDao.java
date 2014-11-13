/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.dao.track;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.track.CodeLineChangeBlock;
import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class CodeLineChangeDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/ClassFile.java", revisionId = "092d49846655da2c4e511a256ba551dc4e15c674";
		CodeLineChangeDao dao = new CodeLineChangeDao();
		List<CodeLineChangeBlock> changeLines = dao.loadChangeLines(
				DaoConstants.ECLIPSE_CORE_HIBERNATE_LOCATION_PATH, fileName,
				revisionId);
		System.out.println(changeLines);
	}

	@SuppressWarnings("unchecked")
	public List<CodeLineChangeBlock> loadChangeLines(String hbmConf,
			String fileName, String revisionId) {
		if (hbmConf == null || fileName == null || revisionId == null)
			return null;
		String hql = "from CodeLineChange where fileName='" + fileName
				+ "' and revisionId = '" + revisionId + "' order by lineNumber";
		return HibernateUtils.retrieveObjects(hql, hbmConf);
	}
}

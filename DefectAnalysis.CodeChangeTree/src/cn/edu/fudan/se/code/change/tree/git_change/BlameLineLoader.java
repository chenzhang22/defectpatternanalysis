/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.git_change;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;
import cn.edu.fudan.se.defectAnalysis.bean.track.BugInduceBlameLine;
import cn.edu.fudan.se.defectAnalysis.dao.track.BugInduceBlameLineDao;

/**
 * @author Lotay
 *
 */
public class BlameLineLoader {
	/**
	 * @param fileName
	 * @return the map for different revision.
	 */
	public static Map<String, List<BugInduceBlameLine>> loadBugBlameLines(
			String fileName) {
		BugInduceBlameLineDao blameDao = new BugInduceBlameLineDao();
		List<BugInduceBlameLine> bLines = blameDao.blameLinesForInducedFile(
				CodeChangeTreeConstants.HIBERNATE_CONF_PATH, fileName);
		Map<String, List<BugInduceBlameLine>> blameLines = new HashMap<String, List<BugInduceBlameLine>>();
		for (BugInduceBlameLine l : bLines) {
			if (l == null) {
				continue;
			}
			String revision = l.getInducedRevisionId();
			List<BugInduceBlameLine> bls = blameLines.get(revision);
			if (bls == null) {
				bls = new ArrayList<BugInduceBlameLine>();
				blameLines.put(revision, bls);
			}
			bls.add(l);
		}
		return blameLines;
	}
}

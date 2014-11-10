/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.db;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.ChangeLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangList;
import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;
import cn.edu.fudan.se.defectAnalysis.bean.track.BugInduceBlameLine;
import cn.edu.fudan.se.defectAnalysis.dao.track.BugInduceBlameLineDao;

/**
 * @author Lotay
 *
 */
public class LineRangeGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/lookup/MethodVerifier.java";
		LineRangeGenerator generator = new LineRangeGenerator();
		CodeRangList rangeList = generator.genCodeRangList(fileName);
		System.out.println(rangeList);
	}

	private BugInduceBlameLineDao blameDao = new BugInduceBlameLineDao();

	public CodeRangList genCodeRangList(String fileName) {
		if (fileName == null) {
			return null;
		}
		List<BugInduceBlameLine> lines = blameDao.blameLinesForFile(
				CodeChangeTreeConstants.HIBERNATE_CONF_PATH, fileName);
		CodeRangList codeList = new CodeRangList();
		int bugId = -2;
		String induceRevisionId = null;
		int inducedLine = -2;

		ChangeLineRange range = null;

		for (BugInduceBlameLine line : lines) {
			int lineBugId = line.getBugId();
			String lineRevisionId = line.getInducedRevisionId();
			int lineInduceNum = line.getInducedlineNumber();
			int fixedLineStart = line.getFixedLineStart();
			int fixedLineEnd = line.getFixedLineStart();

			if ((lineBugId != bugId)
					|| (induceRevisionId == null || !lineRevisionId
							.equals(induceRevisionId))
					|| (lineInduceNum != inducedLine + 1)) {
				range = new ChangeLineRange();
				range.setFixedEndLine(fixedLineEnd);
				range.setInducedEndLine(fixedLineStart);
				codeList.add(range);
			}
			bugId = lineBugId;
			induceRevisionId = lineRevisionId;
			inducedLine = lineInduceNum;
		}

		return codeList;
	}
}

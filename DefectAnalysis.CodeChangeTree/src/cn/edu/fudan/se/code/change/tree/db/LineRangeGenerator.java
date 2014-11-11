/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.db;

import java.util.HashMap;
import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.ChangeLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
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
		String fileName = "org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/problem/ProblemReporter.java";
		LineRangeGenerator generator = new LineRangeGenerator();
		HashMap<String, CodeRangeList> codeListMap = generator
				.genCodeRangList(fileName);
		for (CodeRangeList ranges : codeListMap.values()) {
			System.out.println(ranges);
		}
		System.out.println();
	}

	private BugInduceBlameLineDao blameDao = new BugInduceBlameLineDao();

	public HashMap<String, CodeRangeList> genCodeRangList(String fileName) {
		if (fileName == null) {
			return null;
		}
		List<BugInduceBlameLine> lines = blameDao.blameLinesForFile(
				CodeChangeTreeConstants.HIBERNATE_CONF_PATH, fileName);
		HashMap<String, CodeRangeList> codeListMap = new HashMap<String, CodeRangeList>();

		int bugId = -2;
		String induceRevisionId = null;
		String fixedRevisionId = null;
		int inducedLine = -2;
		String changeType = null;
		ChangeLineRange range = null;
		CodeRangeList codeList = null;
		for (BugInduceBlameLine line : lines) {
			int lineBugId = line.getBugId();
			String lineRevisionId = line.getInducedRevisionId();
			String fixedId = line.getFixedRevisionId();
			String ct = line.getChangeType();
			int lineInduceNum = line.getInducedlineNumber();
			int fixedLineStart = line.getFixedLineStart();
			int fixedLineEnd = line.getFixedLineEnd();

			if (codeList == null
					|| !codeList.getRevisionId().equals(lineRevisionId)) {
				codeList = new CodeRangeList();
				codeList.setRepoName(CodeChangeTreeConstants.REPO_NAME);
				codeList.setFileName(fileName);
				codeList.setRevisionId(lineRevisionId);
				codeListMap.put(lineRevisionId, codeList);
			}

			if ((range == null)
					|| (changeType == null)
					|| (fixedRevisionId == null || !fixedRevisionId
							.equals(fixedId)) || (!ct.equals(changeType))
					|| (lineBugId != bugId) || induceRevisionId == null
					|| !lineRevisionId.equals(induceRevisionId)
					|| (lineInduceNum != inducedLine + 1)) {
				range = new ChangeLineRange();
				range.setBugId(lineBugId);
				range.setChangeType(ct);

				range.setInducedStartLine(lineInduceNum);
				range.setFixedEndLine(fixedLineEnd);
				range.setFixedStartLine(fixedLineStart);

				codeList.add(range);
			}
			if (ct.equals("CODE_INSERTED")) {
				range.setInducedEndLine(lineInduceNum);
			} else {
				range.setInducedEndLine(lineInduceNum + 1);
			}

			changeType = ct;
			bugId = lineBugId;
			induceRevisionId = lineRevisionId;
			fixedRevisionId = fixedId;
			inducedLine = lineInduceNum;
		}

		return codeListMap;
	}
}

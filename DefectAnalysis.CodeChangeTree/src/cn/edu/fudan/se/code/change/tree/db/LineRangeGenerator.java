/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.db;

import java.util.HashMap;
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
		HashMap<String, CodeRangList> codeListMap = generator
				.genCodeRangList(fileName);
		for (CodeRangList ranges : codeListMap.values()) {
			System.out.println(ranges);
		}
		System.out.println();
	}

	private BugInduceBlameLineDao blameDao = new BugInduceBlameLineDao();

	public HashMap<String, CodeRangList> genCodeRangList(String fileName) {
		if (fileName == null) {
			return null;
		}
		List<BugInduceBlameLine> lines = blameDao.blameLinesForFile(
				CodeChangeTreeConstants.HIBERNATE_CONF_PATH, fileName);
		HashMap<String, CodeRangList> codeListMap = new HashMap<String, CodeRangList>();

		int bugId = -2;
		String induceRevisionId = null;
		int inducedLine = -2;
		String changeType = null;
		ChangeLineRange range = null;
		CodeRangList codeList = null;
		for (BugInduceBlameLine line : lines) {
			int lineBugId = line.getBugId();
			String lineRevisionId = line.getInducedRevisionId();
			String ct = line.getChangeType();
			int lineInduceNum = line.getInducedlineNumber();
			int fixedLineStart = line.getFixedLineStart();
			int fixedLineEnd = line.getFixedLineEnd();

			if (codeList == null
					|| !codeList.getRevisionId().equals(lineRevisionId)) {
				codeList = new CodeRangList();
				codeList.setRepoName(CodeChangeTreeConstants.REPO_NAME);
				codeList.setFileName(fileName);
				codeList.setRevisionId(lineRevisionId);
				codeListMap.put(lineRevisionId, codeList);
			}

			if ((range == null) || (changeType == null)
					|| (!ct.equals(changeType)) || (lineBugId != bugId)
					|| induceRevisionId == null
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
			inducedLine = lineInduceNum;
		}

		return codeListMap;
	}
}

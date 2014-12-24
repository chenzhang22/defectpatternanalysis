/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.db;

import java.util.HashMap;
import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineList;
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
		HashMap<String, CodeBlameLineList> codeListMap = genCodeRangList(fileName);
		for (CodeBlameLineList ranges : codeListMap.values()) {
			System.out.println(ranges);
		}
		System.out.println();
	}

	public static HashMap<String, CodeBlameLineList> genCodeRangList(String fileName) {
		if (fileName == null) {
			return null;
		}
		BugInduceBlameLineDao blameDao = new BugInduceBlameLineDao();

		List<BugInduceBlameLine> lines = blameDao.blameLinesForFile(
				CodeChangeTreeConstants.HIBERNATE_CONF_PATH, fileName);
		HashMap<String, CodeBlameLineList> codeListMap = new HashMap<String, CodeBlameLineList>();

		int bugId = -2;
		String induceRevisionId = null;
		String fixedRevisionId = null;
		int inducedLine = -2;
		String changeType = null;
		CodeBlameLineRange range = null;
		CodeBlameLineList codeList = null;
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
				codeList = new CodeBlameLineList();
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
				range = new CodeBlameLineRange();
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
	
	public CodeBlameLineList genCodeRangList(String fileName,String revisionId,List<Integer> lines){
		CodeBlameLineList codeChangeList = new CodeBlameLineList();
		codeChangeList.setFileName(fileName);
		codeChangeList.setRevisionId(revisionId);
		codeChangeList.setRepoName(CodeChangeTreeConstants.REPO_NAME);
		
		for(Integer l:lines){
			
		}
		return codeChangeList;
		
	}
}

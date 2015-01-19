/**
 * 
 */
package cn.edu.fudan.se.defect.track.blame.execute;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.blame.BlameResult;

import cn.edu.fudan.se.defect.track.blame.extract.FixBugCommitFiltedLinkManager;
import cn.edu.fudan.se.defect.track.blame.file.SourceFilePreparation;
import cn.edu.fudan.se.defect.track.blame.util.LineChangeHandler;
import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommitInfo;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.bean.track.BugInduceBlameLine;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitCommitDao;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class BlameTrace {

	private Set<BugInduceBlameLine> blameTrace(
			List<GitSourceFile> sourceFileList) {
		Set<BugInduceBlameLine> blameLines = new HashSet<BugInduceBlameLine>();
		BlameCMD blameCom = new BlameCMD();
		LineChangeHandler lineChangeHandler = new LineChangeHandler();
		BlameResult preBlameResult = null;

		Map<Integer, Boolean> preRevisionLineTypes = null;
		for (int i = 0; i < sourceFileList.size(); i++) {
			GitSourceFile sf = sourceFileList.get(i);
			if (sf.getChangeType().equals("DELETE")) {
				continue;
			}
			String fileName = sf.getFileName();
			String revisionId = sf.getRevisionId();
			GitCommitInfo fixedCommitInfo = new GitCommitDao()
					.loadGitCommitInfoByRevisionId(revisionId,
							BugTrackingConstants.HIBERNATE_CONF_PATH);
			if (fixedCommitInfo == null) {
				continue;
			}
			BlameResult blameResult = blameCom.gitBlame(revisionId, fileName);
			if (blameResult == null) {
				continue;
			}
			Map<Integer, Boolean> thisLineTypes = lineChangeHandler
					.lineType(blameResult);
			Map<Integer, Boolean> lineChanges = lineChangeHandler.lineChange(
					blameResult, revisionId);
			if (preBlameResult != null && preRevisionLineTypes != null) {
				Set<Integer> bugIds;

				if (FixBugCommitFiltedLinkManager.getBugfixedlinks()
						.containsKey(revisionId)) {
					bugIds = FixBugCommitFiltedLinkManager.getBugfixedlinks()
							.get(revisionId);

					searchChangeDiffs(blameLines, preBlameResult,
							preRevisionLineTypes, fileName, revisionId,
							fixedCommitInfo, blameResult, lineChanges, bugIds);
				}
			}

//			buildChangeLines(codeLineChangeBlocks, fileName, revisionId, lineChanges);
			// Set the current result and type as last ones.

			preBlameResult = blameResult;
			preRevisionLineTypes = thisLineTypes;
		}
		return blameLines;
	}

	/**
	 * @param blameLines
	 * @param preBlameResult
	 * @param preRevisionLineTypes
	 * @param fileName
	 * @param revisionId
	 * @param fixedCommitInfo
	 * @param blameResult
	 * @param lineChanges
	 * @param bugIds
	 */
	private void searchChangeDiffs(Set<BugInduceBlameLine> blameLines,
			BlameResult preBlameResult,
			Map<Integer, Boolean> preRevisionLineTypes, String fileName,
			String revisionId, GitCommitInfo fixedCommitInfo,
			BlameResult blameResult, Map<Integer, Boolean> lineChanges,
			Set<Integer> bugIds) {
		if (!lineChanges.isEmpty()) {
			int previousVersionLine = 0, thisVersionLine = 0;
			int preVersionStartLine = 0, thisVersionStartLine = 0;
			while (thisVersionLine < blameResult.getResultContents().size()) {

				// search for the not in the change lines.
				while (thisVersionLine < blameResult.getResultContents().size()
						&& !lineChanges.containsKey(thisVersionLine)) {
					String lastContent = blameResult.getResultContents()
							.getString(thisVersionLine);
					String lastRevisionId = blameResult.getSourceCommit(
							thisVersionLine).getName();

					previousVersionLine = searchDeletedCode(blameLines,
							preBlameResult, preRevisionLineTypes, fileName,
							revisionId, fixedCommitInfo, bugIds,
							previousVersionLine, thisVersionLine, lastContent,
							lastRevisionId);

					thisVersionLine++;
					previousVersionLine++;
				}
				preVersionStartLine = previousVersionLine;
				thisVersionStartLine = thisVersionLine;
				thisVersionLine = searchTheChangeCodeForCurrentRevision(
						blameResult, lineChanges, thisVersionLine);

				previousVersionLine = searchUnChangeCodeLineForPreviousVersion(
						preBlameResult, blameResult, previousVersionLine,
						thisVersionLine);

				if (preVersionStartLine == previousVersionLine
						&& previousVersionLine == preBlameResult
								.getResultContents().size()
						&& thisVersionLine == thisVersionStartLine
						&& thisVersionLine == blameResult.getResultContents()
								.size()) {
					// System.out.println("End...");
					break;
				}

				searchBlameLineForBug(blameLines, preBlameResult,
						preRevisionLineTypes, fileName, revisionId,
						fixedCommitInfo, blameResult, lineChanges, bugIds,
						previousVersionLine, thisVersionLine,
						preVersionStartLine, thisVersionStartLine);
			}
		}
	}

	/**
	 * @param blameResult
	 * @param lineChanges
	 * @param thisVersionLine
	 * @return
	 */
	private int searchTheChangeCodeForCurrentRevision(BlameResult blameResult,
			Map<Integer, Boolean> lineChanges, int thisVersionLine) {
		while (thisVersionLine < blameResult.getResultContents().size()
				&& lineChanges.containsKey(thisVersionLine)) {
			thisVersionLine++;
		}
		return thisVersionLine;
	}

	/**
	 * @param preBlameResult
	 * @param blameResult
	 * @param previousVersionLine
	 * @param thisVersionLine
	 * @return
	 */
	private int searchUnChangeCodeLineForPreviousVersion(
			BlameResult preBlameResult, BlameResult blameResult,
			int previousVersionLine, int thisVersionLine) {
		if (thisVersionLine < blameResult.getResultContents().size()) {
			String line = blameResult.getResultContents().getString(
					thisVersionLine);
			String nextLineRevisionId = blameResult.getSourceCommit(
					thisVersionLine).getName();
			for (; previousVersionLine < preBlameResult.getResultContents()
					.size(); previousVersionLine++) {
				String lastContent = preBlameResult.getResultContents()
						.getString(previousVersionLine);
				String lastRevisionId = preBlameResult.getSourceCommit(
						previousVersionLine).getName();
				if (lastRevisionId.equals(nextLineRevisionId)
						&& lastContent.equals(line)) {
					break;
				}
			}
		} else {
			previousVersionLine = preBlameResult.getResultContents().size();
		}
		return previousVersionLine;
	}

	/**
	 * @param blameLines
	 * @param preBlameResult
	 * @param preRevisionLineTypes
	 * @param fileName
	 * @param revisionId
	 * @param fixedCommitInfo
	 * @param bugIds
	 * @param previousVersionLine
	 * @param thisVersionLine
	 * @param lastContent
	 * @param lastRevisionId
	 * @return
	 */
	private int searchDeletedCode(Set<BugInduceBlameLine> blameLines,
			BlameResult preBlameResult,
			Map<Integer, Boolean> preRevisionLineTypes, String fileName,
			String revisionId, GitCommitInfo fixedCommitInfo,
			Set<Integer> bugIds, int previousVersionLine, int thisVersionLine,
			String lastContent, String lastRevisionId) {
		// Search for the delete lines.
		if (lastContent != null && lastRevisionId != null) {
			boolean hasDeleteLine = false;
			int deleteStart = previousVersionLine;
			while (previousVersionLine < preBlameResult.getResultContents()
					.size()
					&& (!preBlameResult.getSourceCommit(previousVersionLine)
							.getName().equals(lastRevisionId) || !preBlameResult
							.getResultContents().getString(previousVersionLine)
							.equals(lastContent))) {
				previousVersionLine++;
				hasDeleteLine = true;
			}
			if (hasDeleteLine) {
				// Add Delete lines....
				for (int ll = deleteStart; ll < previousVersionLine
						&& ll < preRevisionLineTypes.size(); ll++) {
					for (Integer bugId : bugIds) {
						BugInduceBlameLine bl = new BugInduceBlameLine();
						bl.setBugId(bugId);
						bl.setFileName(fileName);
						bl.setFixedLineStart(thisVersionLine);
						bl.setFixedLineEnd(thisVersionLine);
						bl.setInducedlineNumber(preBlameResult
								.getSourceLine(ll));
						bl.setFixedRevisionId(revisionId);
						bl.setInducedRevisionId(preBlameResult.getSourceCommit(
								ll).getName());
						bl.setShouldPreCare(preRevisionLineTypes.get(ll));
						bl.setInducedTime(new Timestamp(((long) preBlameResult
								.getSourceCommit(ll).getCommitTime()) * 1000));
						bl.setFixedTime(fixedCommitInfo.getTime());
						bl.setChangeType(BugTrackingConstants.CODE_DELETED);
						blameLines.add(bl);
					}
				}
			}
		}
		return previousVersionLine;
	}

	/**
	 * @param blameLines
	 * @param preBlameResult
	 * @param preRevisionLineTypes
	 * @param fileName
	 * @param revisionId
	 * @param fixedCommitInfo
	 * @param blameResult
	 * @param lineChanges
	 * @param bugIds
	 * @param previousVersionLine
	 * @param thisVersionLine
	 * @param preVersionStartLine
	 * @param thisVersionStartLine
	 * @return
	 */
	private boolean searchBlameLineForBug(Set<BugInduceBlameLine> blameLines,
			BlameResult preBlameResult,
			Map<Integer, Boolean> preRevisionLineTypes, String fileName,
			String revisionId, GitCommitInfo fixedCommitInfo,
			BlameResult blameResult, Map<Integer, Boolean> lineChanges,
			Set<Integer> bugIds, int previousVersionLine, int thisVersionLine,
			int preVersionStartLine, int thisVersionStartLine) {
		// check whether all change lines in current version
		// are empty or
		// comment.
		boolean shouldCurCare = false;
		for (int ll = thisVersionStartLine; ll < thisVersionLine
				&& ll < blameResult.getResultContents().size(); ll++) {
			if (lineChanges.containsKey(ll) && lineChanges.get(ll)) {
				shouldCurCare = true;
				break;
			}
		}
		if (preVersionStartLine == previousVersionLine) {
			if (preRevisionLineTypes.containsKey(previousVersionLine)) {
				for (Integer bugId : bugIds) {
					BugInduceBlameLine bl = new BugInduceBlameLine();
					bl.setBugId(bugId);
					bl.setFileName(fileName);
					bl.setFixedLineStart(thisVersionStartLine);
					bl.setFixedLineEnd(thisVersionLine);
					bl.setInducedlineNumber(preBlameResult
							.getSourceLine(previousVersionLine));
					bl.setFixedRevisionId(revisionId);
					bl.setInducedRevisionId(preBlameResult.getSourceCommit(
							previousVersionLine).getName());
					bl.setShouldPreCare(preRevisionLineTypes
							.get(previousVersionLine));
					bl.setShouldCurCare(shouldCurCare);
					bl.setInducedTime(new Timestamp(((long) preBlameResult
							.getSourceCommit(previousVersionLine)
							.getCommitTime()) * 1000));
					bl.setFixedTime(fixedCommitInfo.getTime());
					bl.setChangeType(BugTrackingConstants.CODE_INSERTED);

					blameLines.add(bl);
				}
			}
		} else if (preVersionStartLine < previousVersionLine) {
			for (int ll = preVersionStartLine; ll < previousVersionLine
					&& ll < blameResult.getResultContents().size(); ll++) {
				if (preRevisionLineTypes.containsKey(ll)) {
					for (Integer bugId : bugIds) {
						BugInduceBlameLine bl = new BugInduceBlameLine();
						bl.setBugId(bugId);
						bl.setFileName(fileName);
						bl.setFixedLineStart(thisVersionStartLine);
						bl.setFixedLineEnd(thisVersionLine);
						bl.setInducedlineNumber(preBlameResult
								.getSourceLine(ll));
						bl.setFixedRevisionId(revisionId);
						bl.setInducedRevisionId(preBlameResult.getSourceCommit(
								ll).getName());
						bl.setShouldPreCare(preRevisionLineTypes.get(ll));
						bl.setShouldCurCare(shouldCurCare);
						bl.setInducedTime(new Timestamp(((long) preBlameResult
								.getSourceCommit(ll).getCommitTime()) * 1000));
						bl.setFixedTime(fixedCommitInfo.getTime());
						bl.setChangeType(BugTrackingConstants.CODE_MODIFIED);

						blameLines.add(bl);
					}
				}
			}
		}
		return shouldCurCare;
	}

	/**
	 * @param codeLineChangeBlock
	 * @param fileName
	 * @param revisionId
	 * @param lineChanges
	 */
//	private void buildChangeLines(Set<CodeLineChangeBlock> codeLineChangeBlock,
//			String fileName, String revisionId,
//			Map<Integer, Boolean> lineChanges) {
//		// Extracting the change line.
//		for (Integer line : lineChanges.keySet()) {
//			CodeLineChangeBlock lineChange = new CodeLineChangeBlock();
//			lineChange.setFileName(fileName);
//			lineChange.setRevisionId(revisionId);
//			lineChange.setShouldCare(lineChanges.get(line));
//			lineChange.setLineNumber(line);
//			codeLineChangeBlock.add(lineChange);
//		}
//	}

	/**
	 * @param lineLastChangeRevisions
	 * @param blameResult
	 */
	void fillLastChangeForLine(Map<Integer, String[]> lineLastChangeRevisions,
			BlameResult blameResult) {
		lineLastChangeRevisions.clear();
		for (int line = 0; line < blameResult.getResultContents().size(); line++) {
			String[] contents = { blameResult.getSourceCommit(line).getName(),
					blameResult.getResultContents().getString(line) };
			lineLastChangeRevisions.put(line, contents);
		}
	}

	public void blameTrace() {
		SourceFilePreparation preparation = new SourceFilePreparation();
		List<List<GitSourceFile>> sourceFileLists = preparation.getSourcefilelist();
//		System.out.println(sourceFileLists.size());
		for (int i = startIndex; i < startIndex + size
				&& i < sourceFileLists.size(); i++) {
			List<GitSourceFile> sourceFiles = sourceFileLists.get(i);
			if (sourceFiles == null || sourceFiles.size() < 1) {
				continue;
			}
			System.out.println("FileName:" + i + "/" + sourceFileLists.size()
					+ ":" + sourceFiles.get(0).getFileName());
			Set<BugInduceBlameLine> blameLines = this.blameTrace(sourceFiles);
			// Save to DataBase.
			HibernateUtils.saveAll(blameLines,
					BugTrackingConstants.HIBERNATE_CONF_PATH);
		}
	}
	
	public void blameTrace(Timestamp startTime, Timestamp endTime) {
		SourceFilePreparation preparation = new SourceFilePreparation(startTime, endTime);
		List<List<GitSourceFile>> sourceFileLists = preparation.getSourcefilelist();
		for (List<GitSourceFile> sourceFiles : sourceFileLists) {
			if (sourceFiles == null || sourceFiles.size() < 1) {
				continue;
			}
			System.out.println("FileName:" + sourceFileLists.indexOf(sourceFiles)
					+ "/" + sourceFileLists.size()
					+ ":" + sourceFiles.get(0).getFileName());
			Set<BugInduceBlameLine> blameLines = this.blameTrace(sourceFiles);
			// Save to DataBase.
			//HibernateUtils.saveAll(blameLines, BugTrackingConstants.HIBERNATE_CONF_PATH);
		}
	}

	int startIndex = 0;
	int size = 0;
	
	/**
	 * @param startIndex
	 */
	public BlameTrace(int startIndex, int size) {
		super();
		this.startIndex = startIndex;
		this.size = size;
	}
	
	public BlameTrace(){
	}
}

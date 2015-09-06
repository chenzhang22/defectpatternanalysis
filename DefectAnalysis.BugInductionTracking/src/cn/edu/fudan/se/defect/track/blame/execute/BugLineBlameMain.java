/**
 * 
 */
package cn.edu.fudan.se.defect.track.blame.execute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;

import cn.edu.fudan.se.code.io.utils.LoggerUtils;
import cn.edu.fudan.se.defect.track.blame.file.SourceFilePreparation;
import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommitInfo;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitFiltedLink;
import cn.edu.fudan.se.defectAnalysis.bean.track.BugInduceBlameLine;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitCommitDao;
import cn.edu.fudan.se.defectAnalysis.dao.link.LinkDao;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class BugLineBlameMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String fileName =
		// "org.eclipse.jdt.core/model/org/eclipse/jdt/internal/core/LambdaExpression.java";
		// executeDiff(fileName, "c5d3e87bb720e91660c058962146e02390cfc415",
		// "a3a00115caa9dfd2b9b6d5b9fcf61ba0f8479cd9");
		SourceFilePreparation preparation = new SourceFilePreparation();
		String repoName = "Eclipse_Core";
		List<List<GitSourceFile>> sourceFileLists = preparation
				.getSourcefilelist();
		int startIndex = 542;
		for (int i = startIndex; i < sourceFileLists.size(); i++) {
			List<GitSourceFile> sourceFiles = sourceFileLists.get(i);
			if (sourceFiles == null || sourceFiles.size() < 1) {
				continue;
			}
			System.out.println("FileName:" + i + "/" + sourceFileLists.size()
					+ ":" + sourceFiles.get(0).getFileName());
			Set<BugInduceBlameLine> bugInduceBlameLines = calculateBugInduceBlameLines(
					repoName, sourceFiles);
			HibernateUtils.saveAll(bugInduceBlameLines,
					BugTrackingConstants.HIBERNATE_CONF_PATH);
		}
	}

	static Set<BugInduceBlameLine> calculateBugInduceBlameLines(
			String repoName, List<GitSourceFile> gitSourceFiles) {
		Set<BugInduceBlameLine> bugInduceBlameLines = new HashSet<BugInduceBlameLine>();
		BlameCMD blameCmd = new BlameCMD();
		Map<Integer, String> previousRevisionMap = new HashMap<Integer, String>();
		Map<Integer, Integer> previousLineMap = new HashMap<Integer, Integer>();
		BlameResult preBlameResult = null;
		String previousRevision = null;
		for (GitSourceFile gitSourceFile : gitSourceFiles) {
			String fileName = gitSourceFile.getFileName();
			String revisionId = gitSourceFile.getRevisionId();
			String changeType = gitSourceFile.getChangeType();
			GitCommitInfo gitCommitInfo = gitCommitInfoMap.get(revisionId);
			if (changeType.equals(ChangeType.DELETE.name())) {
				continue;
			}
			BlameResult blameResult = blameCmd.gitBlame(revisionId, fileName);
			if (previousRevision != null
					&& fixedBugCommitFiltedLinkMap.containsKey(revisionId)
					&& !changeType.equals(ChangeType.ADD.name())) {
				Set<Integer> bugIds = fixedBugCommitFiltedLinkMap
						.get(revisionId);
				if (bugIds != null && !bugIds.isEmpty()) {

					int preIndex = 0, curIndex = 0;
					while (preIndex < preBlameResult.getResultContents().size()
							&& curIndex < blameResult.getResultContents()
									.size()) {
						while ((preIndex < preBlameResult.getResultContents()
								.size() && curIndex < blameResult
								.getResultContents().size())
								&& (preBlameResult.getSourceCommit(preIndex)
										.equals(blameResult
												.getSourceCommit(curIndex)) && preBlameResult
										.getResultContents()
										.getString(preIndex)
										.equals(blameResult.getResultContents()
												.getString(curIndex)))) {
							preIndex++;
							curIndex++;
						}
						int fixedLineCount = 0;
						int fixedStart = curIndex;
						while ((curIndex < blameResult.getResultContents()
								.size())
								&& (blameResult.getSourceCommit(curIndex)
										.getName().equals(revisionId))) {
							fixedLineCount++;
							curIndex++;
						}
						int inducedLineCount = 0;
						int inducedStart = preIndex;
						while ((preIndex < preBlameResult.getResultContents()
								.size() && curIndex < blameResult
								.getResultContents().size())
								&& (!preBlameResult.getSourceCommit(preIndex)
										.equals(blameResult
												.getSourceCommit(curIndex)) || !preBlameResult
										.getResultContents()
										.getString(preIndex)
										.equals(blameResult.getResultContents()
												.getString(curIndex)))) {
							preIndex++;
							inducedLineCount++;
						}
						if (fixedLineCount > 0 || inducedLineCount > 0) {
							for (int i = inducedStart; i < preIndex; i++) {
								for (Integer bugId : bugIds) {
									BugInduceBlameLine bugInduceBlameLine = new BugInduceBlameLine();
									bugInduceBlameLine.setBugId(bugId);
									bugInduceBlameLine.setFileName(fileName);
									if (fixedLineCount == 0) {
										bugInduceBlameLine
												.setChangeType("CODE_DELETED");
										bugInduceBlameLine
												.setShouldPreCare(true);
									} else if (inducedLineCount == 0) {
										bugInduceBlameLine
												.setChangeType("CODE_INSERTED");
										bugInduceBlameLine
												.setShouldCurCare(true);
									} else {
										bugInduceBlameLine
												.setChangeType("CODE_MODIFIED");
										bugInduceBlameLine
												.setShouldCurCare(true);
										bugInduceBlameLine
												.setShouldPreCare(true);
									}
									bugInduceBlameLine
											.setFixedLineStart(fixedStart);
									bugInduceBlameLine
											.setFixedLineEnd(curIndex);
									bugInduceBlameLine
											.setFixedRevisionId(revisionId);
									bugInduceBlameLine
											.setFixedTime(gitCommitInfo
													.getTime());
									bugInduceBlameLine
											.setInducedlineNumber(previousLineMap
													.get(i));
									bugInduceBlameLine
											.setInducedRevisionId(previousRevisionMap
													.get(i));
									bugInduceBlameLine
											.setInducedTime(gitCommitInfoMap
													.get(previousRevisionMap
															.get(i)).getTime());
									bugInduceBlameLines.add(bugInduceBlameLine);
								}
							}
						}
					}
					// end with removed.
					while (preIndex < preBlameResult.getResultContents().size()) {
						for (Integer bugId : bugIds) {
							BugInduceBlameLine bugInduceBlameLine = new BugInduceBlameLine();
							bugInduceBlameLine.setBugId(bugId);
							bugInduceBlameLine.setFileName(fileName);
							bugInduceBlameLine.setChangeType("CODE_DELETED");
							bugInduceBlameLine.setShouldPreCare(true);
							bugInduceBlameLine.setFixedLineStart(curIndex);
							bugInduceBlameLine.setFixedLineEnd(curIndex);
							bugInduceBlameLine.setFixedRevisionId(revisionId);
							bugInduceBlameLine.setFixedTime(gitCommitInfo
									.getTime());
							bugInduceBlameLine
									.setInducedlineNumber(previousLineMap
											.get(preIndex));
							bugInduceBlameLine
									.setInducedRevisionId(previousRevisionMap
											.get(preIndex));
							bugInduceBlameLine.setInducedTime(gitCommitInfoMap
									.get(previousRevisionMap.get(preIndex))
									.getTime());
							bugInduceBlameLines.add(bugInduceBlameLine);
						}
						preIndex++;
					}
					// end with added.
					if (curIndex < blameResult.getResultContents().size()) {
						for (Integer bugId : bugIds) {
							BugInduceBlameLine bugInduceBlameLine = new BugInduceBlameLine();
							bugInduceBlameLine.setBugId(bugId);
							bugInduceBlameLine.setFileName(fileName);
							bugInduceBlameLine.setChangeType("CODE_INSERTED");
							bugInduceBlameLine.setShouldCurCare(true);
							bugInduceBlameLine.setFixedLineStart(curIndex);
							bugInduceBlameLine.setFixedLineEnd(blameResult
									.getResultContents().size());
							bugInduceBlameLine.setFixedRevisionId(revisionId);
							bugInduceBlameLine.setFixedTime(gitCommitInfo
									.getTime());
							bugInduceBlameLine.setInducedlineNumber(preIndex);
							bugInduceBlameLine
									.setInducedRevisionId(previousRevision);
							bugInduceBlameLine.setInducedTime(gitCommitInfoMap
									.get(previousRevision).getTime());
							bugInduceBlameLines.add(bugInduceBlameLine);
						}
					}
				}
			}

			int preIndex = 0;
			int curIndex = 0;
			if (preBlameResult == null) {
				while (curIndex < blameResult.getResultContents().size()) {
					previousLineMap.put(curIndex, curIndex);
					previousRevisionMap.put(curIndex, revisionId);
					curIndex++;
				}
			} else {
				Map<Integer, Integer> lineMap = new HashMap<Integer, Integer>();
				Map<Integer, String> lineRevisionMap = new HashMap<Integer, String>();
				while (preIndex < preBlameResult.getResultContents().size()
						&& curIndex < blameResult.getResultContents().size()) {
					while ((preIndex < preBlameResult.getResultContents()
							.size() && curIndex < blameResult
							.getResultContents().size())
							&& (preBlameResult.getSourceCommit(preIndex) != null
									&& preBlameResult.getSourceCommit(preIndex)
											.equals(blameResult
													.getSourceCommit(curIndex)) && preBlameResult
									.getResultContents()
									.getString(preIndex)
									.equals(blameResult.getResultContents()
											.getString(curIndex)))) {
						lineMap.put(curIndex, previousLineMap.get(preIndex));
						lineRevisionMap.put(curIndex,
								previousRevisionMap.get(preIndex));
						preIndex++;
						curIndex++;
					}
					while ((curIndex < blameResult.getResultContents().size())
							&& blameResult.getSourceCommit(curIndex) != null
							&& (revisionId.equals(blameResult.getSourceCommit(
									curIndex).getName()))) {
						lineMap.put(curIndex, curIndex);
						lineRevisionMap.put(curIndex, revisionId);
						curIndex++;
					}
					while ((preIndex < preBlameResult.getResultContents()
							.size() && curIndex < blameResult
							.getResultContents().size())
							&& (blameResult.getSourceCommit(curIndex) != null
									&& preBlameResult.getSourceCommit(preIndex) != null
									&& (!preBlameResult.getSourceCommit(
											preIndex).equals(
											blameResult
													.getSourceCommit(curIndex))) || !preBlameResult
									.getResultContents()
									.getString(preIndex)
									.equals(blameResult.getResultContents()
											.getString(curIndex)))) {
						preIndex++;
					}
				}
				// end with added.
				while (curIndex < blameResult.getResultContents().size()) {
					lineMap.put(curIndex, curIndex);
					lineRevisionMap.put(curIndex, revisionId);
					curIndex++;
				}
				previousLineMap = lineMap;
				previousRevisionMap = lineRevisionMap;
			}
			preBlameResult = blameResult;
			previousRevision = revisionId;
		}
		return bugInduceBlameLines;
	}

	static Map<String, GitCommitInfo> gitCommitInfoMap = new HashMap<String, GitCommitInfo>();
	static Map<String, Set<Integer>> fixedBugCommitFiltedLinkMap = new HashMap<String, Set<Integer>>();
	static {
		LoggerUtils.println("loading git and bug link start.");
		List<GitCommitInfo> gitCommitInfos = new GitCommitDao()
				.loadAllGitCommitInfo(BugTrackingConstants.HIBERNATE_CONF_PATH);
		for (GitCommitInfo gitCommitInfo : gitCommitInfos) {
			gitCommitInfoMap.put(gitCommitInfo.getRevisionID(), gitCommitInfo);
		}
		List<FixedBugCommitFiltedLink> fixedBugCommitFiltedLinks = new LinkDao()
				.listLinks(BugTrackingConstants.HIBERNATE_CONF_PATH);
		for (FixedBugCommitFiltedLink fixedBugCommitFiltedLink : fixedBugCommitFiltedLinks) {
			String revisionId = fixedBugCommitFiltedLink.getRevisionId();
			int bugId = fixedBugCommitFiltedLink.getBugId();
			Set<Integer> linkedBugIds = fixedBugCommitFiltedLinkMap
					.get(revisionId);
			if (linkedBugIds == null) {
				linkedBugIds = new HashSet<Integer>();
				fixedBugCommitFiltedLinkMap.put(revisionId, linkedBugIds);
			}
			linkedBugIds.add(bugId);
		}
		LoggerUtils.println("loading git and bug link end.");
	}
}

/**
 * 
 */
package cn.edu.fudan.se.defect.track.execute;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;

import cn.edu.fudan.se.defect.track.bugzilla.BugzillaBugExtractor;
import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defect.track.file.BugFixFileTracker;
import cn.edu.fudan.se.defect.track.file.BugInduceFileTracker;
import cn.edu.fudan.se.defect.track.fileop.TempFileOperator;
import cn.edu.fudan.se.defect.track.git.GitBlameDiffFilter;
import cn.edu.fudan.se.defect.track.git.GitFileReader;
import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommitInfo;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffEntity;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitCommitDao;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 * 
 */
public class BugFileTracker {
	private static final TempFileOperator tempFileOperator = new TempFileOperator();
	private static final GitFileReader gitFileReader = new GitFileReader();

	public void diffJTrack(int bugId) {
		diffExecute(bugId, new DiffJDiffExecutor());
	}

	public void changeDistillerTrack(int bugId) {
		diffExecute(bugId, new ChangeDistillerExecutor());
	}

	/**
	 * @param bugId
	 */
	private void diffExecute(int bugId, DiffExecutor executor) {
		BugzillaBugExtractor bugExtractor = new BugzillaBugExtractor();
		BugzillaBug bug = bugExtractor.loadBug(bugId);
		if (bug == null || executor == null) {
			return;
		}

		BugFixFileTracker tracker = new BugFixFileTracker();
		Set<GitSourceFile> sourceFiles = tracker.track2SourceFile(bugId);

		if (sourceFiles == null) {
			return;
		}
		Timestamp bugReportTime = bug.getCreation_time();

		BugInduceFileTracker bugInduceFileTracker = new BugInduceFileTracker();
		GitCommitDao gitCommitDao = new GitCommitDao();
		Map<String,GitCommitInfo> bugReportFixedCommits = new HashMap<String,GitCommitInfo>();

		Set<DiffEntity> diffJDiffEntities = new HashSet<DiffEntity>();

		for (GitSourceFile srcFile : sourceFiles) {
			String fileName = srcFile.getFileName();
			String fixedRevisionId = srcFile.getRevisionId();

			GitCommitInfo fixedBugCommitInfo = gitCommitDao
					.loadGitCommitInfoByRevisionId(srcFile.getRevisionId());

			GitCommitInfo lastCommitInfo = bugInduceFileTracker.bugInduceTrack(
					bugReportTime, fixedBugCommitInfo, fileName,
					bugReportFixedCommits);

			if (lastCommitInfo != null) {
				String lastRevisionId = lastCommitInfo.getRevisionID();
				// If the bug induce commit does not exist.
				String bugFixedFileName = saveTempFile(fixedRevisionId,
						fileName);
				String bugInducedFileName = saveTempFile(lastRevisionId,
						fileName);
				if (bugInducedFileName == null || bugFixedFileName == null) {
					if (bugInducedFileName != null) {
						tempFileOperator.deleteTempFile(bugInducedFileName);
					}
					if (bugFixedFileName != null) {
						tempFileOperator.deleteTempFile(bugFixedFileName);
					}
					continue;
				}

				List<DiffEntity> fileDiffs = executor.execute(bugId,
						lastRevisionId, fixedRevisionId, fileName,
						bugInducedFileName, bugFixedFileName);
				if (bugReportFixedCommits != null
						&& !bugReportFixedCommits.isEmpty()) {
//					System.out
//							.println("fileDiffs (before):" + fileDiffs.size());
					try {
						GitBlameDiffFilter blameFilter = new GitBlameDiffFilter();
						fileDiffs = blameFilter.blameFilte(fileDiffs,
								bugReportFixedCommits.keySet(), lastRevisionId,
								fileName);
					} catch (RevisionSyntaxException | IOException
							| GitAPIException e) {
						System.out.println("Class:" + this.getClass().getName()
								+ ", message:" + e.getMessage());
					}
//					System.out.println("fileDiffs (after):" + fileDiffs.size());
				}

				if (fileDiffs != null) {
					diffJDiffEntities.addAll(fileDiffs);
				}
				tempFileOperator.deleteTempFile(bugInducedFileName);
				tempFileOperator.deleteTempFile(bugFixedFileName);
			}
		}
//		System.out.println("Saving Tracking Diff-Entities for Bug:" + bugId);
//		HibernateUtils.saveAll(diffJDiffEntities,
//				BugTrackingConstants.HIBERNATE_CONF_PATH);
	}

	private String saveTempFile(String fixedRevisionId, String fileName) {
		byte[] bugFixedFileData = gitFileReader.readGitFile(fixedRevisionId,
				fileName);
		if (bugFixedFileData == null) {
			return null;
		}
		String bugFixedFileName = tempFileOperator.byte2File(bugFixedFileData);
		return bugFixedFileName;
	}

}

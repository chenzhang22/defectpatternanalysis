/**
 * 
 */
package cn.edu.fudan.se.defect.track.main;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.incava.analysis.FileDiffs;

import cn.edu.fudan.se.defect.track.bugzilla.BugzillaBugExtractor;
import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defect.track.diff.DiffEntityFactory;
import cn.edu.fudan.se.defect.track.diff.DiffJMain;
import cn.edu.fudan.se.defect.track.file.BugFixFileTracker;
import cn.edu.fudan.se.defect.track.file.BugInduceFileTracker;
import cn.edu.fudan.se.defect.track.fileop.TempFileOperator;
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
	public void track(int bugId) {
		BugzillaBugExtractor bugExtractor = new BugzillaBugExtractor();
		BugzillaBug bug = bugExtractor.loadBug(bugId);
		if (bug == null) {
			return;
		}

		BugFixFileTracker tracker = new BugFixFileTracker();
		Set<GitSourceFile> sourceFiles = tracker.track2SourceFile(bugId);

		if (sourceFiles == null) {
			return;
		}
		System.out.println("Bug Tracking for bugId:" + bugId);
		Timestamp bugReportTime = bug.getCreation_time();

		BugInduceFileTracker bugInduceFileTracker = new BugInduceFileTracker();
		GitCommitDao gitCommitDao = new GitCommitDao();
		List<GitCommitInfo> bugInduceFixedCommits = new ArrayList<GitCommitInfo>();

		GitFileReader gitFileReader = new GitFileReader();
		TempFileOperator tempFileOperator = new TempFileOperator();

		List<DiffEntity> diffEntities = new ArrayList<DiffEntity>();

		for (GitSourceFile srcFile : sourceFiles) {
			String fileName = srcFile.getFileName();
			String fixedRevisionId = srcFile.getRevisionId();
			GitCommitInfo fixedBugCommitInfo = gitCommitDao
					.loadGitCommitInfoByRevisionId(srcFile.getRevisionId());
			GitCommitInfo bugInduceCommitInfo = bugInduceFileTracker
					.bugInduceTrack(bugReportTime, fixedBugCommitInfo,
							fileName, bugInduceFixedCommits);
			// If the bug induce commit does not exist.
			if (bugInduceCommitInfo != null) {
				byte[] bugFixedFileData = gitFileReader.readGitFile(
						fixedRevisionId, fileName);
				if (bugFixedFileData == null) {
					continue;
				}
				byte[] bugIndecedFileData = gitFileReader.readGitFile(
						bugInduceCommitInfo.getRevisionID(), fileName);
				if (bugIndecedFileData == null) {
					continue;
				}
				String bugFixedFileName = tempFileOperator
						.byte2File(bugFixedFileData);
				String bugInducedFileName = tempFileOperator
						.byte2File(bugIndecedFileData);
				if (bugInducedFileName == null || bugFixedFileName == null) {
					continue;
				}
				FileDiffs fileDiffs = diffjExecute(bugFixedFileName,
						bugInducedFileName);

				diffEntities.addAll(DiffEntityFactory.build(bugId,
						bugInduceCommitInfo.getRevisionID(), fixedRevisionId,
						fileName, fileDiffs));
				if (bugInducedFileName != null) {
					tempFileOperator.deleteTempFile(bugInducedFileName);
				}
				if (bugFixedFileName != null) {
					tempFileOperator.deleteTempFile(bugFixedFileName);
				}
			}
		}
		System.out.println("Saving Tracking Diff-Entities for Bug:" + bugId);
		HibernateUtils.saveAll(diffEntities,
				BugTrackingConstants.HIBERNATE_CONF_PATH);
	}

	/**
	 * @param bugFixedFileName
	 * @param bugInducedFileName
	 * @return
	 */
	private FileDiffs diffjExecute(String bugFixedFileName,
			String bugInducedFileName) {
		DiffJMain diffj = new DiffJMain();
		diffj.diffExecute(bugInducedFileName, bugFixedFileName);
		FileDiffs fileDiffs = diffj.getFileDiffs();
		return fileDiffs;
	}
}

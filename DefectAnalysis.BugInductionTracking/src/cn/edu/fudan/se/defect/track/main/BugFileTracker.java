/**
 * 
 */
package cn.edu.fudan.se.defect.track.main;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.fudan.se.defect.track.bugzilla.BugzillaBugExtractor;
import cn.edu.fudan.se.defect.track.file.BugFixFileTracker;
import cn.edu.fudan.se.defect.track.file.BugInduceFileTracker;
import cn.edu.fudan.se.defect.track.fileop.TempFileOperator;
import cn.edu.fudan.se.defect.track.git.GitFileReader;
import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommitInfo;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitCommitDao;

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
		for (GitSourceFile srcFile : sourceFiles) {
			System.out.println(srcFile);
		}

		Timestamp bugReportTime = bug.getCreation_time();

		BugInduceFileTracker bugInduceFileTracker = new BugInduceFileTracker();
		GitCommitDao gitCommitDao = new GitCommitDao();
		List<GitCommitInfo> bugInduceFixedCommits = new ArrayList<GitCommitInfo>();

		GitFileReader gitFileReader = new GitFileReader();
		TempFileOperator tempFileOperator = new TempFileOperator();
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
				System.out.println(fixedRevisionId);
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
				System.out.println("bugFixedFileName:" + bugFixedFileName);
				System.out.println("bugInducedFileName:" + bugInducedFileName);
				break;
			}
		}
	}
}

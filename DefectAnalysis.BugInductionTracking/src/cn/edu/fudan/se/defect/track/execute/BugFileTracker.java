/**
 * 
 */
package cn.edu.fudan.se.defect.track.execute;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;

import cn.edu.fudan.se.defect.track.bugzilla.BugzillaBugExtractor;
import cn.edu.fudan.se.defect.track.file.BugFixFileTracker;
import cn.edu.fudan.se.defect.track.file.BugInduceFileTracker;
import cn.edu.fudan.se.defect.track.fileop.TempFileOperator;
import cn.edu.fudan.se.defect.track.git.GitBlameDiffFilter;
import cn.edu.fudan.se.defect.track.git.GitFileReader;
import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommitInfo;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.BugInduceBlameLine;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffEntity;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitCommitDao;

/**
 * @author Lotay
 * 
 */
public class BugFileTracker {
	private static final TempFileOperator tempFileOperator = new TempFileOperator();
	private static final GitFileReader gitFileReader = new GitFileReader();

	public Set<DiffEntity> diffJTrack(int bugId,
			Collection<BugInduceBlameLine> inducedBlameLines) {
		return diffExecute(bugId, new DiffJDiffExecutor(), inducedBlameLines);
	}

	public Set<DiffEntity> changeDistillerTrack(int bugId,
			Collection<BugInduceBlameLine> inducedBlameLines) {
		return diffExecute(bugId, new ChangeDistillerExecutor(),
				inducedBlameLines);
	}

	/**
	 * @param bugId
	 */
	private Set<DiffEntity> diffExecute(int bugId, DiffExecutor executor,
			Collection<BugInduceBlameLine> inducedBlameLines) {
		BugzillaBugExtractor bugExtractor = new BugzillaBugExtractor();
		BugzillaBug bug = bugExtractor.loadBug(bugId);
		Set<DiffEntity> diffJDiffEntities = new HashSet<DiffEntity>();

		if (bug == null || executor == null) {
			return diffJDiffEntities;
		}

		BugFixFileTracker tracker = new BugFixFileTracker();
		Set<GitSourceFile> sourceFiles = tracker.track2SourceFile(bugId);

		if (sourceFiles == null) {
			return diffJDiffEntities;
		}
		Timestamp bugReportTime = bug.getCreation_time();

		BugInduceFileTracker bugInduceFileTracker = new BugInduceFileTracker();
		GitCommitDao gitCommitDao = new GitCommitDao();
		Map<String, GitCommitInfo> bugReportFixedCommits = new HashMap<String, GitCommitInfo>();

		Set<BugInduceBlameLine> tmpInducedBlameLines = new HashSet<BugInduceBlameLine>();

		for (GitSourceFile srcFile : sourceFiles) {
			String fileName = srcFile.getNewPath();
			String fixedRevisionId = srcFile.getRevisionId();

			GitCommitInfo fixedBugCommitInfo = gitCommitDao
					.loadGitCommitInfoByRevisionId(fixedRevisionId);

			String previousRevisionId = fixedBugCommitInfo.getPreviousRID();
			if(previousRevisionId==null){
				continue;
			}
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
						previousRevisionId, fixedRevisionId, fileName,
						bugInducedFileName, bugFixedFileName);
				GitBlameDiffFilter blameFilter = new GitBlameDiffFilter();

				try {
					tmpInducedBlameLines.clear();

					fileDiffs = blameFilter.blameFilte(fileDiffs,
							bugReportFixedCommits.keySet(), lastRevisionId,
							fileName, tmpInducedBlameLines);
					inducedBlameLines.addAll(tmpInducedBlameLines);

				} catch (RevisionSyntaxException | IOException
						| GitAPIException e) {
					System.out.println("Class:" + this.getClass().getName()
							+ ", message:" + e.getMessage());
				}

				if (fileDiffs != null) {
					diffJDiffEntities.addAll(fileDiffs);
				}
				tempFileOperator.deleteTempFile(bugInducedFileName);
				tempFileOperator.deleteTempFile(bugFixedFileName);
			}
		}
		return diffJDiffEntities;
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

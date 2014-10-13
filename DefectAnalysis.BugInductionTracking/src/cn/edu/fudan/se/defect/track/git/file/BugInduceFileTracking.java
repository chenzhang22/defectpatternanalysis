/**
 * 
 */
package cn.edu.fudan.se.defect.track.git.file;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommitInfo;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitCommitDao;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitSourceFileDao;

/**
 * @author Lotay
 * 
 */
public class BugInduceFileTracking {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/lookup/Scope.java";
		
		//02d4f1049999a6639faf6572ec12176fbdb0f122
		//report: 2014-03-02 23:29:00
		//fix: 2014-03-04 18:08:17
		//bugId: 429424
		Timestamp bugReportTime = new Timestamp(0);
		bugReportTime.setYear(114);
		bugReportTime.setMonth(2);
		bugReportTime.setDate(2);
		bugReportTime.setHours(23);
		bugReportTime.setMinutes(29);
		bugReportTime.setSeconds(0);
		bugReportTime.setNanos(0);
		System.out.println("bugReportTime:"+bugReportTime);
		
		BugInduceFileTracking track = new BugInduceFileTracking();
		GitCommitInfo commitInfo = track.bugExistedTrack(bugReportTime, fileName);
		System.out.println("commitInfo:"+commitInfo);
	}

	public GitCommitInfo bugExistedTrack(Timestamp bugReportTime,
			String fileName) {
		if (bugReportTime == null || fileName == null) {
			return null;
		}
		GitSourceFileDao srcFileDao = new GitSourceFileDao();
		List<GitSourceFile> srcFiles = srcFileDao
				.loadSourceFileByFileName(fileName);
		GitCommitDao gitCommitDao = new GitCommitDao();
		GitCommitInfo lastestGitCommitInfo = null;

		for (GitSourceFile gsf : srcFiles) {
			GitCommitInfo gitCommitInfo = gitCommitDao
					.loadGitCommitInfoByRevisionId(gsf.getRevisionId());
			if (gitCommitInfo != null
					&& gitCommitInfo.getTime().before(bugReportTime)) {
				if (lastestGitCommitInfo == null
						|| gitCommitInfo.getTime().after(
								lastestGitCommitInfo.getTime())) {
					lastestGitCommitInfo = gitCommitInfo;
				}
			}
		}
		return lastestGitCommitInfo;
	}
}

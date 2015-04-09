/**
 * 
 */
package cn.edu.fudan.se.defect.track.blame.file;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitSourceFileDao;

/**
 * @author Lotay
 *
 */
public class SourceFilePreparation {
	private List<List<GitSourceFile>> sourceFileList = new ArrayList<List<GitSourceFile>>();

	/**
	 * @return the sourcefilelist
	 */
	public List<List<GitSourceFile>> getSourcefilelist() {
		return sourceFileList;
	}

	public SourceFilePreparation() {
		//if (sourceFileList.isEmpty()) {
		GitSourceFileDao dao = new GitSourceFileDao();
		List<GitSourceFile> sourceFiles = dao
				.loadSourceFileNoTest(BugTrackingConstants.HIBERNATE_CONF_PATH);
		String lastFileName = null;
		List<GitSourceFile> fileBaseList = null;
		for (int i = 0; i < sourceFiles.size(); i++) {
			GitSourceFile sf = sourceFiles.get(i);
			if (sf == null) {
				continue;
			}
			if (lastFileName == null || !lastFileName.equals(sf.getFileName())) {
				fileBaseList = new ArrayList<GitSourceFile>();
				sourceFileList.add(fileBaseList);
			}
			fileBaseList.add(sf);
			lastFileName = sf.getFileName();
		}
		//}
	}
	
	public SourceFilePreparation(Timestamp startTime, Timestamp endTime, String hiberConf){
		GitSourceFileDao dao = new GitSourceFileDao();
		List<GitSourceFile> sourceFiles = dao.loadSourceFileNoTestByTime(startTime, 
				endTime, hiberConf);
		String lastFileName = null;
		List<GitSourceFile> fileBaseList = null;
		for (GitSourceFile sf : sourceFiles) {
			if (sf == null) {
				continue;
			}
			if (lastFileName == null || !lastFileName.equals(sf.getFileName())) {
				fileBaseList = new ArrayList<GitSourceFile>();
				sourceFileList.add(fileBaseList);
			}
			fileBaseList.add(sf);
			lastFileName = sf.getFileName();
		}
	}
}

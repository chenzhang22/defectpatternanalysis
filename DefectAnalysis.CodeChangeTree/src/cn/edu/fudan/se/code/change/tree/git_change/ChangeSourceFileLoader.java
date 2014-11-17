/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.git_change;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitSourceFileDao;

/**
 * @author Lotay
 *
 */
public class ChangeSourceFileLoader {
	public static Map<String, List<GitSourceFile>> loadSourceFiles() {
		GitSourceFileDao sourceFilesDao = new GitSourceFileDao();
		List<GitSourceFile> sourceFiles = sourceFilesDao
				.loadSourceFileNoTest(CodeChangeTreeConstants.HIBERNATE_CONF_PATH);
		Map<String, List<GitSourceFile>> gitChangeSourceFiles = new HashMap<String, List<GitSourceFile>>();

		for (int i = 0; i < sourceFiles.size(); i++) {
			GitSourceFile file = sourceFiles.get(i);
			if (file == null) {
				continue;
			}
			String fileName = file.getFileName();
			List<GitSourceFile> files = gitChangeSourceFiles.get(fileName);
			if (files == null) {
				files = new ArrayList<GitSourceFile>();
				gitChangeSourceFiles.put(fileName, files);
			}
			files.add(file);
		}

		return gitChangeSourceFiles;
	}

}

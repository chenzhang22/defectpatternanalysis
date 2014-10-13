/**
 * 
 */
package cn.edu.fudan.se.defect.track.link;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import cn.edu.fudan.se.defect.track.git.constants.BugTrackingConstants;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;

/**
 * @author Lotay
 * 
 */
public class FileFilter {
	public static List<GitSourceFile> filter(List<GitSourceFile> files) {
		List<GitSourceFile> gitSourceFiles = new ArrayList<GitSourceFile>();
		for (GitSourceFile file : files) {
			String fileName = file.getFileName();
			Matcher matcher = BugTrackingConstants.FILTED_SOURCEFILE_PATTERN
					.matcher(fileName);
			if (matcher.find()) {
				gitSourceFiles.add(file);
			}
		}

		for (GitSourceFile file : gitSourceFiles) {
			files.remove(file);
		}
		return files;
	}

	public static void main(String[] args) {
		String fileName = "org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/lookup/SyntheticMethodBinding.java";
		Matcher matcher = BugTrackingConstants.FILTED_SOURCEFILE_PATTERN
		.matcher(fileName);
		System.out.println((matcher.find()));
	}
}

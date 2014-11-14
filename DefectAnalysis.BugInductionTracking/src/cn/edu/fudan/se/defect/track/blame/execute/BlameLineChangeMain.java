/**
 * 
 */
package cn.edu.fudan.se.defect.track.blame.execute;

import java.util.List;

import cn.edu.fudan.se.defect.track.blame.file.SourceFilePreparation;
import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.bean.track.CodeLineChangeBlock;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

/**
 * @author Lotay
 *
 */
public class BlameLineChangeMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int startIndex = 542;
		int size = -1;
		executeBlame(startIndex, size);
	}

	/**
	 * @param startIndex
	 * @param size
	 */
	private static void executeBlame(int startIndex, int size) {
		List<List<GitSourceFile>> sourceFileLists = SourceFilePreparation
				.getSourcefilelist();
		if (size < 0) {
			size = sourceFileLists.size();
		}
		BlameLineChange changeLineBlame = new BlameLineChange();
		for (int i = startIndex; i < startIndex + size
				&& i < sourceFileLists.size(); i++) {
			List<GitSourceFile> sourceFiles = sourceFileLists.get(i);
			if (sourceFiles == null || sourceFiles.size() < 1) {
				continue;
			}
//			if(!"org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/lookup/SourceTypeBinding.java".equals(sourceFiles.get(0).getFileName())){
//				continue;
//			}
			System.out.println("FileName:" + i + "/" + sourceFileLists.size()
					+ ":" + sourceFiles.get(0).getFileName());
			List<CodeLineChangeBlock> changeBlocks = changeLineBlame
					.extractChangeLine(sourceFiles);
			// Save to DataBase.
			HibernateUtils.saveAll(changeBlocks,
					BugTrackingConstants.HIBERNATE_CONF_PATH);
		}
	}

}

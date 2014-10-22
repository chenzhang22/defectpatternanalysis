/**
 * 
 */
package cn.edu.fudan.se.defect.track.execute;

import java.util.List;

import org.incava.analysis.FileDiffs;

import cn.edu.fudan.se.defect.track.diff.DiffJMain;
import cn.edu.fudan.se.defect.track.factory.DiffJEntityFactory;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffEntity;

/**
 * @author Lotay
 *
 */
public class DiffJDiffExecutor implements DiffExecutor{

	@Override
	public List<DiffEntity> execute(int bugId, String inducedRevisionId, String fixedRevisionId,String fileName,String leftFileName, String rightFileName) {
		DiffJMain diffj = new DiffJMain();
		diffj.diffExecute(leftFileName, rightFileName);
		FileDiffs fileDiffs = diffj.getFileDiffs();
		DiffJEntityFactory factory = new DiffJEntityFactory();
		List<DiffEntity> fileDiffEntities = factory.build(bugId, inducedRevisionId, fixedRevisionId, fileName, fileDiffs);
		return fileDiffEntities;
	}
}

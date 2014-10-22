/**
 * 
 */
package cn.edu.fudan.se.defect.track.execute;

import java.io.File;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import cn.edu.fudan.se.defect.track.factory.ChangeDistillerDiffEntityFactory;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffEntity;

/**
 * @author Lotay
 * 
 */
public class ChangeDistillerExecutor implements DiffExecutor {
	public List<DiffEntity> execute(int bugId, String inducedRevisionId,
			String fixedRevisionId, String fileName, String leftFileName,
			String rightFileName) {
		List<DiffEntity> diffEntities = null;

		FileDistiller distiller = ChangeDistiller
				.createFileDistiller(Language.JAVA);
		System.out.println("leftFileName:" + leftFileName);
		System.out.println("rightFileName:" + rightFileName);
		File left = new File(leftFileName);
		File right = new File(rightFileName);
		distiller.extractClassifiedSourceCodeChanges(left, right);
		ChangeDistillerDiffEntityFactory changeDistillerFactory = new ChangeDistillerDiffEntityFactory();
		diffEntities = changeDistillerFactory.build(bugId, inducedRevisionId,
				fixedRevisionId, fileName, distiller, leftFileName,
				rightFileName);

		return diffEntities;
	}
}

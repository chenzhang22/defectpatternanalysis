/**
 * 
 */
package cn.edu.fudan.se.defect.track.factory;

import java.util.ArrayList;
import java.util.List;

import org.incava.analysis.FileDiff;
import org.incava.analysis.FileDiffs;
import org.incava.ijdk.text.Location;
import org.incava.ijdk.text.LocationRange;

import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffEntity;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffJDiffEntity;

/**
 * @author Lotay
 * 
 */
public class DiffJEntityFactory {
	public List<DiffEntity> build(int bugId, String inducedRevisionId,
			String fixedRevisionId, String fileName, FileDiffs fileDiffs) {
		List<DiffEntity> diffJDiffEntities = new ArrayList<DiffEntity>();
		if (inducedRevisionId != null && fileDiffs != null) {
			for (FileDiff fileDiff : fileDiffs) {
				DiffJDiffEntity diffJDiffEntity = new DiffJDiffEntity();
				diffJDiffEntity.setBugId(bugId);
				diffJDiffEntity.setInducedRevisionId(inducedRevisionId);
				diffJDiffEntity.setFixedRevisionId(fixedRevisionId);
				diffJDiffEntity.setFileName(fileName);
				if (fileDiff.getType() != null) {
					diffJDiffEntity.setBasicType(fileDiff.getType().toString());
				}
				if (fileDiff.getChangeType() != null) {
					diffJDiffEntity.setChangeType(fileDiff.getChangeType()
							.toString());
				}
				diffJDiffEntity.setMessage(fileDiff.getMessage());
				LocationRange locationRange = fileDiff.getFirstLocation();
				if (locationRange == null) {
					continue;
				}
				Location loc = locationRange.getStart();
				if (loc != null) {
					diffJDiffEntity.setInducedStartLineNumber(loc.getLine());
					diffJDiffEntity
							.setInducedStartcolumnNumber(loc.getColumn());
				} else {
					continue;
				}
				loc = locationRange.getEnd();
				if (loc != null) {
					diffJDiffEntity.setInducedEndLineNumber(loc.getLine());
					diffJDiffEntity.setInducedEndcolumnNumber(loc.getColumn());
				} else {
					continue;
				}
				locationRange = fileDiff.getSecondLocation();
				if (locationRange != null) {
					loc = locationRange.getStart();
					if (loc != null) {
						diffJDiffEntity.setFixedStartLineNumber(loc.getLine());
						diffJDiffEntity.setFixedStartcolumnNumber(loc
								.getColumn());
					} else {
						continue;
					}
					loc = locationRange.getEnd();
					if (loc != null) {
						diffJDiffEntity.setFixedEndLineNumber(loc.getLine());
						diffJDiffEntity
								.setFixedEndcolumnNumber(loc.getColumn());
					} else {
						continue;
					}
				}

				diffJDiffEntities.add(diffJDiffEntity);
			}
		}
		return diffJDiffEntities;
	}
}

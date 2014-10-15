/**
 * 
 */
package cn.edu.fudan.se.defect.track.diff;

import java.util.ArrayList;
import java.util.List;

import org.incava.analysis.FileDiff;
import org.incava.analysis.FileDiffs;
import org.incava.ijdk.text.Location;
import org.incava.ijdk.text.LocationRange;

import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffEntity;

/**
 * @author Lotay
 * 
 */
public class DiffEntityFactory {
	public static List<DiffEntity> build(int bugId, String inducedRevisionId,
			String fixedRevisionId, String fileName, FileDiffs fileDiffs) {
		List<DiffEntity> diffEntities = new ArrayList<DiffEntity>();
		if (inducedRevisionId != null && fileDiffs != null) {
			for (FileDiff fileDiff : fileDiffs) {
				DiffEntity diffEntity = new DiffEntity();
				diffEntity.setBugId(bugId);
				diffEntity.setInducedRevisionId(inducedRevisionId);
				diffEntity.setFixedRevisionId(fixedRevisionId);
				diffEntity.setFileName(fileName);
				if (fileDiff.getType() != null) {
					diffEntity.setBasicType(fileDiff.getType().toString());
				}
				if (fileDiff.getChangeType() != null) {
					diffEntity.setChangeType(fileDiff.getChangeType()
							.toString());
				}
				diffEntity.setMessage(fileDiff.getMessage());
				LocationRange locationRange = fileDiff.getFirstLocation();
				if (locationRange == null) {
					continue;
				}
				Location loc = locationRange.getStart();
				if (loc != null) {
					diffEntity.setInducedStartLineNumber(loc.getLine());
					diffEntity.setInducedStartcolumnNumber(loc.getColumn());
				} else {
					continue;
				}
				loc = locationRange.getEnd();
				if (loc != null) {
					diffEntity.setInducedEndLineNumber(loc.getLine());
					diffEntity.setInducedEndcolumnNumber(loc.getColumn());
				} else {
					continue;
				}
				locationRange = fileDiff.getSecondLocation();
				if (locationRange != null) {
					loc = locationRange.getStart();
					if (loc != null) {
						diffEntity.setFixedStartLineNumber(loc.getLine());
						diffEntity.setFixedStartcolumnNumber(loc.getColumn());
					} else {
						continue;
					}
					loc = locationRange.getEnd();
					if (loc != null) {
						diffEntity.setFixedEndLineNumber(loc.getLine());
						diffEntity.setFixedEndcolumnNumber(loc.getColumn());
					} else {
						continue;
					}
				}

				diffEntities.add(diffEntity);
			}
		}
		return diffEntities;
	}
}

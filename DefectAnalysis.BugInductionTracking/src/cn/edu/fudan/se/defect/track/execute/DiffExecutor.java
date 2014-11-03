/**
 * 
 */
package cn.edu.fudan.se.defect.track.execute;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.track.DiffEntity;

/**
 * @author Lotay
 * 
 */
public interface DiffExecutor {
	public List<DiffEntity> execute(int bugId,
			String inducedRevisionId, String fixedRevisionId, String fileName,
			String leftFileName, String rightFileName);
}

/**
 * 
 */
package cn.edu.fudan.se.huawei.defectprediction.metric.main;

import java.util.List;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitSourceFileDao;
import cn.edu.fudan.se.huawei.defectprediction.metric.ast.MetricExtractionExecution;
import cn.edu.fudan.se.huawei.defectprediction.metric.constants.BasicConstants;

import com.huawei.data.bean.analysis.filedata.ElementInputMetric;
import com.huawei.data.database.constants.DataBaseConstants;

/**
 * @author Lotay
 *
 */
public class MetricExtractionMain {

	private static List<GitSourceFile> gitSourceFiles = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int start = 0;
		int num = -1;
		MetricExtractionMain main = new MetricExtractionMain();
		main.execute(start, num);
	}

	public void execute(int start, int num) {
		int size = gitSourceFiles.size();
		int end = start + num;
		if (num < 0 || end < size) {
			end = size;
		}

		for (int index = start; index < end; index++) {
			GitSourceFile sourceFile = gitSourceFiles.get(index);
			String changeType = sourceFile.getChangeType();
			// The delete file
			if (BasicConstants.DELETE_FILE_STR.equals(changeType)) {
				System.out.println(sourceFile);
				continue;
			}
			MetricExtractionExecution executor = new MetricExtractionExecution();
			executor.extract(sourceFile);
			List<ElementInputMetric> metrics = executor.extract(sourceFile);

			if (metrics != null) {
				// TODO: Save the metrics.

			}
		}
	}

	static {
		GitSourceFileDao sourceFileDao = new GitSourceFileDao();
		gitSourceFiles = sourceFileDao
				.loadSourceFileNoTestOrderByTime(DataBaseConstants.HIBERNATE_CONF_PATH);
	}
}

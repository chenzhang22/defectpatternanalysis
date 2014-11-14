/**
 * 
 */
package cn.edu.fudan.se.huawei.defectprediction.metric.fact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huawei.data.bean.analysis.filedata.ElementInputMetric;

/**
 * @author Lotay
 *
 */
public class ElementMetricFactory {
	public static List<ElementInputMetric> build(
			Map<String, Map<String, Double>> metricValues,
			Map<String, String> eleType) {
		List<ElementInputMetric> eleMetric = new ArrayList<ElementInputMetric>();
		// TODO: Build the element metric with values.

		return eleMetric;
	}
}

/**
 * 
 */
package cn.edu.fudan.se.huawei.defectprediction.metric.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 * @author Lotay
 *
 */
public class MetricExtractionVisitor extends ASTVisitor {
	private List<Integer> changeLines = null;

	private Map<String, Map<String, Double>> metricValues = new HashMap<String, Map<String, Double>>();
	private Map<String, String> elementTypes = new HashMap<String, String>();

	public MetricExtractionVisitor(List<Integer> changeLines) {
		super();
		this.changeLines = changeLines;
	}

	public Map<String, Map<String, Double>> getMetricValues() {
		return metricValues;
	}

	public Map<String, String> getElementTypes() {
		return elementTypes;
	}

	public List<Integer> getChangeLines() {
		return changeLines;
	}
	// TODO: finish the metric calculator.
	
}

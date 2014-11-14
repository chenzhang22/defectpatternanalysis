/**
 * 
 */
package cn.edu.fudan.se.huawei.defectprediction.metric.ast;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;

import com.huawei.data.bean.analysis.filedata.ElementInputMetric;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.huawei.defectprediction.metric.fact.ElementMetricFactory;
import cn.edu.fudan.se.huawei.defectprediction.metric.git.GitChangeLineExtractor;
import cn.edu.fudan.se.huawei.defectprediction.metric.git.GitFileReader;

/**
 * @author Lotay
 *
 */
public class MetricExtractionExecution {
	public List<ElementInputMetric> extract(GitSourceFile sourceFile) {
		String revisionId = sourceFile.getRevisionId();
		String fileName = sourceFile.getFileName();
		GitFileReader gitFileReader = new GitFileReader();
		GitChangeLineExtractor changeLineExtractor = new GitChangeLineExtractor();
		try {
			char contents[] = gitFileReader.readContentFromRepo(revisionId,
					fileName);
			List<Integer> changeLines = changeLineExtractor.changeLines(
					revisionId, fileName);
			CompilationUnit compilationUnit = this.genCompilationUnit(contents);
			MetricExtractionVisitor metricVisitor = new MetricExtractionVisitor(
					changeLines);
			compilationUnit.accept(metricVisitor);
			Map<String, Map<String, Double>> metricValues = metricVisitor
					.getMetricValues();
			Map<String, String> eleTypes = metricVisitor.getElementTypes();
			return ElementMetricFactory.build(metricValues, eleTypes);
		} catch (RevisionSyntaxException | IOException | GitAPIException e) {
			e.printStackTrace();
		}
		return null;
	}

	private CompilationUnit genCompilationUnit(char[] contents) {
		if (contents == null) {
			return null;
		}
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(contents);
		parser.setCompilerOptions(getCompilerOption("1.8"));
		CompilationUnit compilationUnit = (CompilationUnit) parser
				.createAST(null);
		return compilationUnit;
	}

	private Map<String, String> getCompilerOption(String javaVersion) {
		@SuppressWarnings("unchecked")
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, javaVersion);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, javaVersion);
		options.put(JavaCore.COMPILER_COMPLIANCE, javaVersion);
		return options;
	}
}

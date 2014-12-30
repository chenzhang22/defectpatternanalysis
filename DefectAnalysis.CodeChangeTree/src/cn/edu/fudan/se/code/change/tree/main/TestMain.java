/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.main;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.edu.fudan.se.code.change.ast.visitor.FileAddTreeVisitor;

/**
 * @author Lotay
 *
 */
public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestMain test = new TestMain();
		String fileName = "data/java/BugzillaAttachmentFactory.java";
		test.analysis(fileName);
	}

	public void analysis(String fileName) {
		InputStream is = null;
		char[] contents = null;
		try {
			is = new FileInputStream(fileName);
			contents = IOUtils.toCharArray(is);

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(contents);
		parser.setCompilerOptions(getCompilerOption("1.7"));
		CompilationUnit compilationUnit = (CompilationUnit) parser
				.createAST(null);
		compilationUnit.accept(new FileAddTreeVisitor("test", fileName, null, null));

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

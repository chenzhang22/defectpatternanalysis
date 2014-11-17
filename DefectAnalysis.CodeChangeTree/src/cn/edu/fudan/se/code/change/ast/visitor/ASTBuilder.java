package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;
import cn.edu.fudan.se.git.content.extract.JavaFileContentExtractor;

/**
 * @author Lotay
 *
 */
public class ASTBuilder {
	public static void main(String[] args) {
		String revisionId = "b1f709bd1fd47a690112f71bf976691c90f21c2e", fileName = "org.eclipse.jdt.core/model/org/eclipse/jdt/internal/core/DeltaProcessor.java";

		ASTBuilder builder = new ASTBuilder(CodeChangeTreeConstants.REPO_PATH);
		CompilationUnit cu = builder.genCompilationUnit(revisionId, fileName);
		cu.accept(new FileAddedTreeVisitor(CodeChangeTreeConstants.REPO_NAME,
				revisionId, fileName, null));
	}

	private JavaFileContentExtractor javaFileContentExtractor = null;

	public ASTBuilder(String repoPath) {
		javaFileContentExtractor = new JavaFileContentExtractor(repoPath);
	}

	public CompilationUnit genCompilationUnit(String revisionId, String fileName) {
		if (revisionId == null || fileName == null) {
			return null;
		}
		char contents[] = javaFileContentExtractor
				.extract(revisionId, fileName);
		// System.out.println(new String(contents));
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

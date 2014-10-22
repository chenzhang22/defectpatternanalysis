package cn.edu.fudan.se.defect.track.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTNodeFileLocation {
	private CompilationUnit leftCompilationUnit = null;
	private CompilationUnit rightCompilationUnit = null;

	public ASTNodeFileLocation(String leftFileName, String rightFileName)
			throws Exception {
		leftCompilationUnit = this.parseFile(leftFileName);
		rightCompilationUnit = this.parseFile(rightFileName);
		if (leftCompilationUnit == null || rightCompilationUnit == null) {
			throw new Exception("CompilationUnit parse error.");
		}
	}

	private char[] readCharFromFile(String filePath) {
		if (filePath != null) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(filePath);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				StringBuffer strBuf = new StringBuffer();
				strBuf.append(new String(bytes));
				return strBuf.toString().toCharArray();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	private Map<String, String> getCompilerOption(String javaVersion) {
		@SuppressWarnings("unchecked")
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, javaVersion);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, javaVersion);
		options.put(JavaCore.COMPILER_COMPLIANCE, javaVersion);
		return options;
	}

	private CompilationUnit parseFile(String filePath) {
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		char[] content = readCharFromFile(filePath);
		if (content == null) {
			return null;
		}
		parser.setSource(content);
		parser.setCompilerOptions(getCompilerOption("1.6"));
		return (CompilationUnit) parser.createAST(new NullProgressMonitor());
	}

	public int readLineForLeftFile(int position) {
		return readLineNumber(position, this.leftCompilationUnit);
	}

	public int readColumnForLeftFile(int position) {
		return readColumnNumber(position, this.leftCompilationUnit);
	}

	public int readLineForRightFile(int position) {
		return readLineNumber(position, this.rightCompilationUnit);
	}

	public int readColumnForRightFile(int position) {
		return readColumnNumber(position, this.rightCompilationUnit);
	}

	private int readLineNumber(int position, CompilationUnit compilationUnit) {
		if (compilationUnit != null) {
			return compilationUnit.getLineNumber(position);
		}
		return -1;
	}

	private int readColumnNumber(int position, CompilationUnit compilationUnit) {
		if (compilationUnit != null) {
			return compilationUnit.getColumnNumber(position);
		}
		return -1;
	}

}

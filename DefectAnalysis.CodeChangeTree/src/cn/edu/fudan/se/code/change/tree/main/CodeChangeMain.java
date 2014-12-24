/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.main;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.edu.fudan.se.code.change.ast.visitor.ASTBuilder;
import cn.edu.fudan.se.code.change.ast.visitor.FileAddTreeVisitor;
import cn.edu.fudan.se.code.change.ast.visitor.FileTreeVisitor;
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.db.LineRangeGenerator;
import cn.edu.fudan.se.code.change.tree.utils.CodeTree2String;
import cn.edu.fudan.se.code.change.tree.utils.TreeSaver;

/**
 * @author Lotay
 *
 */
public class CodeChangeMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName[] = {
				"org.eclipse.jdt.core/dom/org/eclipse/jdt/core/dom/ASTConverter.java",
				"org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/problem/ProblemReporter.java",
				"org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/lookup/Scope.java",
				"org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/parser/Parser.java",
				"org.eclipse.jdt.core/codeassist/org/eclipse/jdt/internal/codeassist/CompletionEngine.java",
				"org.eclipse.jdt.core/model/org/eclipse/jdt/internal/core/ClassFile.java" };
		new CodeChangeMain().buildChangeTree(fileName[5]);
	}

	public void buildChangeTree(String fileName) {
		if (fileName == null) {
			return;
		}
		HashMap<String, CodeBlameLineList> codeChangeList = LineRangeGenerator
				.genCodeRangList(fileName);

		for (String revisionId : codeChangeList.keySet()) {
			CompilationUnit cu = ASTBuilder.genCompilationUnit(revisionId,
					fileName);
			CodeBlameLineList rangeList = codeChangeList.get(revisionId);
			FileTreeVisitor treeVisitor = new FileAddTreeVisitor(revisionId,
					fileName, rangeList);
			cu.accept(treeVisitor);
			CodeTreeNode treeNode = treeVisitor.getRootTreeNode();
			// CodeTreePrinter.treeTypePrint(treeNode);
			String toStr = (CodeTree2String.treeSimpleType2String(treeNode));
			TreeSaver.save(
					"data/tree/"
							+ fileName.substring(fileName.lastIndexOf("/") + 1,
									fileName.lastIndexOf(".")) + ".txt",
					revisionId + "\t" + "\n" + toStr);
		}
	}

}

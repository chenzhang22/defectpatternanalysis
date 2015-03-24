/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.main;

import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.aggregate.AbsTreeNodeAggregation;
import cn.edu.fudan.se.code.change.tree.aggregate.NormalTreeNodeAggregation;
import cn.edu.fudan.se.code.change.tree.bean.AggregateTypeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.db.LineRangeGenerator;
import cn.edu.fudan.se.code.change.tree.diff.FileAddRevisionDiffer;
import cn.edu.fudan.se.code.change.tree.diff.FileChangeRevisionDiffer;
import cn.edu.fudan.se.code.change.tree.diff.FileRevisionDiffer;
import cn.edu.fudan.se.code.change.tree.git_change.ChangeSourceFileLoader;
import cn.edu.fudan.se.code.change.tree.replace.AbsNodeTypeReplaceStrategy;
import cn.edu.fudan.se.code.change.tree.replace.DirectNodeTypeReplaceStrategy;
import cn.edu.fudan.se.code.change.tree.split.AbsCodeTreeSpliter;
import cn.edu.fudan.se.code.change.tree.split.MethodLevelCodeTreeSpliter;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.tree.pattern.mining.AbsTreeNodePatternMiner;
import cn.edu.fudan.se.tree.pattern.mining.CodeTreeNodePatternMinerImpl;
import cn.edu.fudan.se.tree.pattern.similarility.CodeTreeNodeSimilarityImpl;

/**
 * @author Lotay
 *
 */
public class CodeChangeDistillerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CodeChangeDistillerMain main = new CodeChangeDistillerMain();
		main.execute();
	}

	public void execute() {
		Map<String, List<GitSourceFile>> gitChangeSourceFiles = ChangeSourceFileLoader
				.loadSourceFiles();
		int size = gitChangeSourceFiles.size();
		int i = 0;
		for (String fileName : gitChangeSourceFiles.keySet()) {
			System.out.println((i++) + "/" + size + ":" + fileName);
			List<GitSourceFile> sourceFiles = gitChangeSourceFiles
					.get(fileName);
			if (sourceFiles == null || sourceFiles.isEmpty()) {
				continue;
			}
			executeFile(sourceFiles);
		}
	}

	private void executeFile(List<GitSourceFile> sourceFiles) {
		int i = 0;
		GitSourceFile preSourceFile = null;
		String fileName = sourceFiles.get(0).getFileName();
		Map<String, CodeBlameLineRangeList> blameLines = LineRangeGenerator
				.genCodeRangList(fileName);
		FileRevisionDiffer fileRevisionDiffer = null;
		AbsNodeTypeReplaceStrategy replaceStrategy = new DirectNodeTypeReplaceStrategy();
		AbsTreeNodeAggregation aggregationStrategy = new NormalTreeNodeAggregation();
		AbsCodeTreeSpliter splitStrategy = new MethodLevelCodeTreeSpliter();
		AbsTreeNodePatternMiner codeTreeNodePatternMiner = new CodeTreeNodePatternMinerImpl(
				new CodeTreeNodeSimilarityImpl());
		for (; i < sourceFiles.size(); i++) {
			GitSourceFile sourceFile = sourceFiles.get(i);
			if (sourceFile == null) {
				continue;
			}
			String revisionId = sourceFile.getRevisionId();
			CodeBlameLineRangeList revBlameLines = blameLines.get(revisionId);
			if (revBlameLines == null || revBlameLines.isEmpty()) {
				preSourceFile = sourceFile;
				continue;
			}
			String changeType = sourceFile.getChangeType();
			if (preSourceFile == null || "ADD".equals(changeType)) {
				// the first version of file
				fileRevisionDiffer = new FileAddRevisionDiffer(sourceFile,
						revBlameLines);
			} else {
				// the change version.
				fileRevisionDiffer = new FileChangeRevisionDiffer(
						preSourceFile, sourceFile, revBlameLines);
			}
			CodeTreeNode codeTree = fileRevisionDiffer.diff();
			// reference to the code tree node after split the change node.....
			List<CodeTreeNode> splitedCodeTreeNode = null;

			if (codeTree != null) {
				// replace the code tree simpleName with corresponding Type...
				codeTree = replaceStrategy.replace(codeTree);
			}
			if (codeTree != null) {
				// split the changed code tree node from the normal node..
				splitedCodeTreeNode = splitStrategy.split(codeTree);
			}

			// CodeTreePrinter.treeNormalPrint(codeTree);
			if (splitedCodeTreeNode != null && !splitedCodeTreeNode.isEmpty()) {

				Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> treePatterns = codeTreeNodePatternMiner
						.mine(splitedCodeTreeNode);

				// aggregate the splited code tree node
				// (NormalTreeNodeAggregation).....
				AggregateTypeNode aggregateTypeNode = aggregationStrategy
						.aggregate(splitedCodeTreeNode.get(0));
				System.out.println(aggregateTypeNode);
			}
			if (codeTree != null) {
				// codeTree = replaceStrategy.replace(codeTree);
			}
			preSourceFile = sourceFile;
		}
	}
}

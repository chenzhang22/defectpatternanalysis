/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.aggregate.AbsTreeNodeAggregation;
import cn.edu.fudan.se.code.change.tree.aggregate.NormalTreeNodeAggregation;
import cn.edu.fudan.se.code.change.tree.bean.CodeBlameLineRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.db.LineRangeGenerator;
import cn.edu.fudan.se.code.change.tree.diff.FileAddRevisionDiffer;
import cn.edu.fudan.se.code.change.tree.diff.FileChangeRevisionDiffer;
import cn.edu.fudan.se.code.change.tree.diff.FileRevisionDiffer;
import cn.edu.fudan.se.code.change.tree.git_change.ChangeSourceFileLoader;
import cn.edu.fudan.se.code.change.tree.replace.AbsNodeTypeReplaceStrategy;
import cn.edu.fudan.se.code.change.tree.replace.DirectNodeTypeReplaceStrategy;
import cn.edu.fudan.se.code.change.tree.split.AbsCodeTreeSpliter;
import cn.edu.fudan.se.code.change.tree.split.MethodLevelCodeTreeSpliter;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreeNodeClone;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.tree.pattern.bean.TreePattern;
import cn.edu.fudan.se.tree.pattern.mining.AbsTreeNodePatternMiner;
import cn.edu.fudan.se.tree.pattern.mining.CodeTreeNodePatternMinerImpl;
import cn.edu.fudan.se.tree.pattern.mining.TreeNodePatternMinerImpl;
import cn.edu.fudan.se.tree.pattern.similarility.CodeTreeNodeSimilarityImpl;
import cn.edu.fudan.se.tree.pattern.similarility.TreePatternSimilarityImpl;

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
		int i = 0,base = 50;
		for (String fileName : gitChangeSourceFiles.keySet()) {
			System.out.println((i++) + "/" + size + ":" + fileName);
			if (i<base) {
				continue;
			}
			List<GitSourceFile> sourceFiles = gitChangeSourceFiles
					.get(fileName);
			if (sourceFiles == null || sourceFiles.isEmpty()) {
				continue;
			}
			executeFile(sourceFiles);
		}
	}
	List<TreeNode> treeNodesInstanceList = new ArrayList<TreeNode>();

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

//			 CodeTreePrinter.treeNormalPrint(codeTree);
			if (splitedCodeTreeNode != null && !splitedCodeTreeNode.isEmpty()) {
//				List<TreeNode> treeNodes = new ArrayList<TreeNode>();
//				treeNodes.addAll((Collection<? extends TreeNode>) splitedCodeTreeNode);
//				Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> treePatterns = codeTreeNodePatternMiner
//						.mine(treeNodes);
//				
				for (CodeTreeNode codeTreeNode : splitedCodeTreeNode) {
					if (codeTreeNode.getBugIds().isEmpty()&& !(codeTreeNode.getNode() instanceof org.eclipse.jdt.core.dom.FieldDeclaration)) {
						treeNodesInstanceList.add(codeTreeNode);
						if (treeNodesInstanceList.size()>20) {
							break;
						}
					}
				}
				if (treeNodesInstanceList.size()>20) {
					break;
				}
				// aggregate the splited code tree node
				// (NormalTreeNodeAggregation).....
//				AggregateTypeNode aggregateTypeNode = aggregationStrategy
//						.aggregate(splitedCodeTreeNode.get(0));
//				System.out.println(aggregateTypeNode);
			}
			if (codeTree != null) {
				// codeTree = replaceStrategy.replace(codeTree);
			}
			preSourceFile = sourceFile;
		}
		
		try {
			System.out.println("treeNodesInstanceList size:"+treeNodesInstanceList.size());
			if (treeNodesInstanceList.size()>20) {
				if (treeNodesInstanceList.size()>50) {
					treeNodesInstanceList = treeNodesInstanceList.subList(0, 50);
				}
				TreeNodePatternMinerImpl treePatternSimilarityImpl = new TreeNodePatternMinerImpl(
						new TreePatternSimilarityImpl(new CodeTreeNodeSimilarityImpl()),new CodeTreeNodeClone());
				treePatternSimilarityImpl.setFrequencyThreshold(3);
				List<TreePattern> treePatterns = treePatternSimilarityImpl.mineTreePattern(treeNodesInstanceList);

				System.out.println(treePatterns);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

package cn.edu.fudan.se.tree.pattern.test;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.bean.TreePattern;
import cn.edu.fudan.se.tree.pattern.mining.TreeNodePatternMinerImpl;
import cn.edu.fudan.se.tree.pattern.similarility.TreePatternSimilarityImpl;

public class GroupPatternMiningTest {
	public static void main(String args[]) {
		TreeNodeTest buildTree1 = buildTree("1");
		buildTree1.setLabel("1");
		TreeNodeTest buildTree2 = buildTree("2");
		buildTree2.setLabel("2");
		TreeNodeTest buildTree31 = buildTree("3");
		buildTree31.setLabel("3.1");
		TreeNodeTest buildTree32 = buildTree("3");
		buildTree32.setLabel("3.2");
		TreeNodeTest buildTree4 = buildTree("4");
		buildTree4.setLabel("4");
		TreeNodeTest buildTree5 = buildTree("5");
		buildTree5.setLabel("5");
		TreeNodeTest buildTree6 = buildTree("6");
		buildTree6.setLabel("6");
		TreeNodeTest buildTree7 = buildTree("7");
		buildTree7.setLabel("7");
		
		buildTree1.addChild(buildTree2);
		buildTree1.addChild(buildTree31);
		buildTree31.addChild(buildTree32);
		buildTree32.addChild(buildTree7);
		buildTree2.addChild(buildTree4);
		buildTree2.addChild(buildTree5);
		buildTree2.addChild(buildTree6);

		TreeNodeTest tree0 = buildTree("0");
		tree0.setLabel("0");
		TreeNodeTest tree1 = buildTree("1");
		tree1.setLabel("1");
		TreeNodeTest tree21 = buildTree("2");
		tree21.setLabel("2.1");
		TreeNodeTest tree31 = buildTree("3");
		tree31.setLabel("3.1");
		TreeNodeTest tree32 = buildTree("3");
		tree32.setLabel("3.2");
		TreeNodeTest tree4 = buildTree("4");
		tree4.setLabel("4");
		TreeNodeTest tree5 = buildTree("5");
		tree5.setLabel("5");
		TreeNodeTest tree6 = buildTree("6");
		tree6.setLabel("6");
		TreeNodeTest tree7 = buildTree("7");
		tree7.setLabel("7");
		TreeNodeTest tree8 = buildTree("8");
		tree8.setLabel("8");
		TreeNodeTest tree9 = buildTree("9");
		tree9.setLabel("9");
		TreeNodeTest tree10 = buildTree("10");
		tree10.setLabel("10");
		TreeNodeTest tree22 = buildTree("2");
		tree22.setLabel("2.2");
		TreeNodeTest tree33 = buildTree("3");
		tree33.setLabel("3.3");
		
		tree0.addChild(tree31);
		tree0.addChild(tree1);
		tree1.addChild(tree8);
		tree1.addChild(tree21);
		tree1.addChild(tree22);
		tree1.addChild(tree32);

		tree21.addChild(tree4);
		tree21.addChild(tree5);
		tree21.addChild(tree6);
		tree4.addChild(tree10);

		tree32.addChild(tree33);
		tree33.addChild(tree9);
		tree9.addChild(tree7);

		TreeNodeTest root31 = new TreeNodeTest();
		root31.setNodeId("1");
		root31.setLabel("1-3");
		TreeNodeTest root32 = new TreeNodeTest();
		root32.setNodeId("2");
		root32.setLabel("2-3");
		TreeNodeTest root33 = new TreeNodeTest();
		root33.setNodeId("3");
		root33.setLabel("3-3");
		TreeNodeTest root331 = new TreeNodeTest();
		root331.setNodeId("3");
		root331.setLabel("3-3-1");
		TreeNodeTest root34 = new TreeNodeTest();
		root34.setNodeId("4");
		root34.setLabel("4-3");
		TreeNodeTest root35= new TreeNodeTest();
		root35.setNodeId("5");
		root35.setLabel("5-3");
		TreeNodeTest root36 = new TreeNodeTest();
		root36.setNodeId("6");
		root36.setLabel("6-3");
		TreeNodeTest root37 = new TreeNodeTest();
		root37.setNodeId("7");
		root37.setLabel("7-3");
		TreeNodeTest root38 = new TreeNodeTest();
		root38.setNodeId("8");
		root38.setLabel("8-3");
		
		root31.addChild(root32);
		root32.addChild(root34);
		root32.addChild(root35);
		root32.addChild(root36);
		root31.addChild(root33);
		root33.addChild(root331);
		root331.addChild(root38);
		root38.addChild(root37);
		
		
		List<TreeNode> trees = new ArrayList<TreeNode>();
		trees.add(buildTree1);
		trees.add(tree0);
		trees.add(root31);
		try {
			TreeNodePatternMinerImpl minerImpl = new TreeNodePatternMinerImpl(
					new TreePatternSimilarityImpl(new TreeNodeTestSimilarity()), new TreeNodeTestClone());
			List<TreePattern> treePatterns = minerImpl.mineTreePattern(trees);
			System.out.println("\n");
			for (TreePattern treePattern : treePatterns) {
				System.out.println(treePattern);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		AbsTreeNodePatternMiner patternMiner = new CodeTreeNodePatternMinerImpl(
//				new TreeNodeTestSimilarity());
//		patternMiner.setTreeNodeClone(new TreeNodeTestClone());
//		Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> patterns = patternMiner
//				.mine(trees);
//		System.out.println(patterns);
	}

	/**
	 * 
	 */
	private static TreeNodeTest buildTree(String id) {
		TreeNodeTest patterNodeTest = new TreeNodeTest();
		patterNodeTest.setNodeId(id);
		return patterNodeTest;
	}
}

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
		TreeNodeTest buildTree2 = buildTree("2");
		TreeNodeTest buildTree3 = buildTree("3");
		TreeNodeTest buildTree33 = buildTree("3");
		TreeNodeTest buildTree4 = buildTree("4");
		TreeNodeTest buildTree5 = buildTree("5");
		TreeNodeTest buildTree6 = buildTree("6");
		TreeNodeTest buildTree7 = buildTree("7");
		buildTree1.addChild(buildTree33);
		buildTree33.addChild(buildTree3);
		buildTree3.addChild(buildTree7);
		buildTree1.addChild(buildTree2);
		buildTree2.addChild(buildTree4);
		buildTree2.addChild(buildTree5);
		buildTree2.addChild(buildTree6);

		TreeNodeTest tree0 = buildTree("0");
		tree0.addChild(buildTree("3"));
		TreeNodeTest tree1 = buildTree("1");
		TreeNodeTest tree2 = buildTree("2");
		TreeNodeTest tree3 = buildTree("3");
		TreeNodeTest tree33 = buildTree("3");
		TreeNodeTest tree4 = buildTree("4");
		TreeNodeTest tree5 = buildTree("5");
		TreeNodeTest tree6 = buildTree("6");
		TreeNodeTest tree7 = buildTree("7");
		TreeNodeTest tree8 = buildTree("8");
		TreeNodeTest tree9 = buildTree("9");
		TreeNodeTest tree10 = buildTree("10");
		TreeNodeTest tree22 = buildTree("2");
		tree0.addChild(tree1);
		tree1.addChild(tree8);
		tree1.addChild(tree22);
		tree1.addChild(tree2);
		tree1.addChild(tree33);

		tree2.addChild(tree4);
		tree4.addChild(tree10);
		tree2.addChild(tree5);
		tree2.addChild(tree6);
		tree33.addChild(tree3);
		tree3.addChild(tree9);
		tree9.addChild(tree7);


		List<TreeNode> trees = new ArrayList<TreeNode>();
		trees.add(buildTree1);
		trees.add(tree0);
		try {
			TreeNodePatternMinerImpl minerImpl = new TreeNodePatternMinerImpl(
					new TreePatternSimilarityImpl(new TreeNodeTestSimilarity()), new TreeNodeTestClone());
			List<TreePattern> treePatterns = minerImpl.mineTreePattern(trees);
			System.out.println(treePatterns);
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

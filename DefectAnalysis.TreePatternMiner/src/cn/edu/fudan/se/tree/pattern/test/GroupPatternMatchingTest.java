/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.match.ImplGroupPatternInstanceMatching;

/**
 * @author Lotay
 *
 */
public class GroupPatternMatchingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeNodeTest buildTree1 = buildTree("1");
		TreeNodeTest buildTree2 = buildTree("2");
		TreeNodeTest buildTree3 = buildTree("3");
		TreeNodeTest buildTree4 = buildTree("4");
		TreeNodeTest buildTree5 = buildTree("5");
		TreeNodeTest buildTree6 = buildTree("6");
		TreeNodeTest buildTree7 = buildTree("7");
		buildTree1.addChild(buildTree3);
		buildTree3.addChild(buildTree7);
		buildTree2.addChild(buildTree4);
		buildTree2.addChild(buildTree5);
		buildTree2.addChild(buildTree6);

		TreeNodeTest tree0 = buildTree("0");
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
		tree2.addChild(tree4);
		tree4.addChild(tree10);
		tree2.addChild(tree5);
		tree2.addChild(tree6);
		tree1.addChild(tree33);
		tree33.addChild(tree3);
		tree3.addChild(tree9);
		tree9.addChild(tree7);

		// ImplSinglePatternInstanceMatching singlePatternInstanceMatching = new
		// ImplSinglePatternInstanceMatching(
		// buildTree1, new TreeNodeTestSimilarity());
		//
		// Map<TreeNode, TreeNode> patternMatch = singlePatternInstanceMatching
		// .patternMatch(buildTree1, tree0);
		// System.out.println(patternMatch);
		// patternMatch = singlePatternInstanceMatching.patternMatch(buildTree2,
		// tree0);
		// System.out.println(patternMatch);

		List<TreeNode> groupPattern = new ArrayList<TreeNode>();
		groupPattern.add(buildTree1);
		groupPattern.add(buildTree2);
		ImplGroupPatternInstanceMatching groupPatternInstanceMatching = new ImplGroupPatternInstanceMatching(
				groupPattern, new TreeNodeTestSimilarity());
		Map<TreeNode, Map<TreeNode, TreeNode>> patternMatchAllNodes = groupPatternInstanceMatching
				.patternMatchAll(tree0);

		System.out.println(patternMatchAllNodes);
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

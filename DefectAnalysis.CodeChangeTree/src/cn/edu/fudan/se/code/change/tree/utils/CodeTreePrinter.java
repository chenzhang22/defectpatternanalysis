/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.utils;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class CodeTreePrinter {
	public static void treeFullPrint(CodeTreeNode treeNode) {
		printTree(treeNode, 0, ToStringType.FULL);
	}

	public static void treeTypePrint(CodeTreeNode treeNode) {
		printTree(treeNode, 0, ToStringType.TYPE);
	}

	public static void treeNormalPrint(CodeTreeNode treeNode) {
		printTree(treeNode, 0, ToStringType.NORMAL);
	}

	public static void treeSimpleTypePrint(CodeTreeNode treeNode) {
		printTree(treeNode, 0, ToStringType.SIMPLETYPE);
	}

	public static void printTree(CodeTreeNode treeNode, int depth,
			ToStringType printType) {
		if (treeNode == null) {
			System.err.println("The tree is empty.");
			return;
		}
		if (treeNode.getType().equals("org.eclipse.jdt.core.dom.SimpleName")) {
			return;
		}
		String blank = "";
		for (int i = 0; i < depth; i++) {
			blank += "\t";
		}
		String toStr = null;
		switch (printType) {
		case NORMAL:
			toStr = treeNode.toNormalString();
			break;
		case TYPE:
			toStr = treeNode.toTypeString();
			break;
		case SIMPLETYPE:
			toStr = treeNode.toSimpleTypeString();
			break;
		default:
			toStr = treeNode.toString();
			break;
		}
		System.out.println(blank + toStr);

		for (CodeTreeNode child : treeNode.getChildren()) {
			printTree(child, depth + 1, printType);
		}
	}
}

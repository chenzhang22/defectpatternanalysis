/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.utils;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class CodeTree2String {
	public static String treeType2String(CodeTreeNode treeNode) {
		StringBuffer toStrBuf = new StringBuffer();
		toString(toStrBuf, treeNode, 0, ToStringType.TYPE);
		return toStrBuf.toString();
	}

	public static String treeSimpleType2String(CodeTreeNode treeNode) {
		StringBuffer toStrBuf = new StringBuffer();
		toString(toStrBuf, treeNode, 0, ToStringType.SIMPLETYPE);
		return toStrBuf.toString();
	}

	public static String treeNormal2String(CodeTreeNode treeNode) {
		StringBuffer toStrBuf = new StringBuffer();
		toString(toStrBuf, treeNode, 0, ToStringType.NORMAL);
		return toStrBuf.toString();
	}

	public static String treeFull2String(CodeTreeNode treeNode) {
		StringBuffer toStrBuf = new StringBuffer();
		toString(toStrBuf, treeNode, 0, ToStringType.FULL);
		return toStrBuf.toString();
	}

	public static void toString(StringBuffer toStrBuf, CodeTreeNode treeNode,
			int depth, ToStringType printType) {
		if (treeNode == null) {
			System.err.println("The tree is empty.");
			return;
		}
		if (!(treeNode.getType().equals("org.eclipse.jdt.core.dom.SimpleName") || treeNode
				.getType().equals("org.eclipse.jdt.core.dom.Block"))) {
			for (int i = 0; i < depth; i++) {
				toStrBuf.append("  ");
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
			toStrBuf.append(toStr + "\n");
		}

		if (!treeNode.getType().equals("org.eclipse.jdt.core.dom.Block")) {
			depth = depth + 1;
		}
		for (CodeTreeNode child : treeNode.getChildren()) {
			toString(toStrBuf, child, depth, printType);
		}
	}
}

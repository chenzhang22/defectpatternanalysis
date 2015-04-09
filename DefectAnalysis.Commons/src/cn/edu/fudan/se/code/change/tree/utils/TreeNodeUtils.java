/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.utils;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;

/**
 * @author Lotay
 *
 */
public class TreeNodeUtils {
	@SuppressWarnings("unchecked")
	public static int countNumberOfNode(TreeNode treeNode) {
		if (treeNode == null) {
			return 0;
		}
		int num = 1;
		List<TreeNode> children = treeNode.getChildren();
		for (TreeNode childNode : children) {
			num += countNumberOfNode(childNode);
		}
		return num;
	}
	
	public static TreeNode rootOf(TreeNode treeNode) {
		if (treeNode==null) {
			return null;
		}
		TreeNode parentTreeNode = treeNode.getParentTreeNode();
		if (parentTreeNode==null) {
			return treeNode;
		}
		return rootOf(parentTreeNode);
	}
}

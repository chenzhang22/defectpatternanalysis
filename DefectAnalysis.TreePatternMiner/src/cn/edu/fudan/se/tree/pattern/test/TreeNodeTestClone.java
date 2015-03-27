package cn.edu.fudan.se.tree.pattern.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.utils.ITreeNodeClone;

@SuppressWarnings("unchecked")
public class TreeNodeTestClone implements ITreeNodeClone {

	@Override
	public TreeNode cloneNoChildren(TreeNode codeTreeNode) {
		if (codeTreeNode instanceof TreeNodeTest) {
			TreeNodeTest clonedNodeTest = new TreeNodeTest();
			clonedNodeTest.setNodeId(((TreeNodeTest) codeTreeNode).getNodeId());
			return clonedNodeTest;
		}
		return null;
	}

	@Override
	public TreeNode clone(TreeNode codeTreeNode) {
		TreeNode clonedNode = this.cloneNoChildren(codeTreeNode);
		if (clonedNode != null) {
			for (TreeNode childNode : (List<TreeNode>) codeTreeNode
					.getChildren()) {
				clonedNode.addChild(childNode);
			}
		}
		return clonedNode;
	}

	@Override
	public TreeNode cloneWholeTree(TreeNode codeTreeNode,
			Map<TreeNode, TreeNode> clonedMappedNodes) {
		TreeNode clonedNode = this.cloneNoChildren(codeTreeNode);
		clonedMappedNodes.put(clonedNode, codeTreeNode);
		if (clonedNode != null) {
			for (TreeNode childNode : (List<TreeNode>) codeTreeNode
					.getChildren()) {
				TreeNode clonedSubTreeNode = this.cloneWholeTree(childNode,
						clonedMappedNodes);
				if (clonedSubTreeNode != null) {
					clonedNode.addChild(clonedSubTreeNode);
				}
			}
		}
		return clonedNode;
	}

	@Override
	public TreeNode cloneWholeTree(TreeNode codeTreeNode) {
		return this.cloneWholeTree(codeTreeNode, new HashMap<TreeNode, TreeNode>());
	}
}

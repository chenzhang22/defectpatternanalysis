/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.split;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreeNodeClone;

/**
 * @author Lotay
 *
 *
 *         split rules: change node parent nodes/ sibling nodes/ child nodes.
 */
public class NormalCodeTreeSpliter implements AbsCodeTreeSpliter {
	@Override
	public List<CodeTreeNode> split(CodeTreeNode treeNode) {
		// TODO Auto-generated method stub
		parentNode = null;
		this.splits(treeNode, parentNode);
		ArrayList<CodeTreeNode> treeForest = new ArrayList<CodeTreeNode>();
		treeForest.add(parentNode);
		return treeForest;
	}

	private CodeTreeNode parentNode = null;

	private boolean splits(CodeTreeNode treeNode,
			CodeTreeNode parentCodeTreeNode) {
		CodeTreeNodeClone treeNodeClone = new CodeTreeNodeClone();
		if (parentCodeTreeNode == null) {
			parentCodeTreeNode = (CodeTreeNode) treeNodeClone.cloneNoChildren(treeNode);
			parentNode = parentCodeTreeNode;
		}
		if (treeNode instanceof CodeChangeTreeNode) {
			parentCodeTreeNode.getParentTreeNode().addChild(treeNode);
			return true;
		} else {
			boolean isChildHasModified = false;
			for (CodeTreeNode node : treeNode.getChildren()) {
				CodeTreeNode cloneCodeTreeNode = (CodeTreeNode) treeNodeClone.cloneNoChildren(node);
				parentCodeTreeNode.addChild(cloneCodeTreeNode);
				boolean isChanged = this.splits(node, cloneCodeTreeNode);
				isChildHasModified |= isChanged;
			}
			if (!isChildHasModified) {
				parentCodeTreeNode.removeAllChildren();
			}
			return isChildHasModified;
		}
	}
}

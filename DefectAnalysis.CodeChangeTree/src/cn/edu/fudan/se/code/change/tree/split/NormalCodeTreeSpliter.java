/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.split;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreeNodeClone;

/**
 * @author Lotay
 *
 *
 *         split rules: change node parent nodes/ sibling nodes/ child nodes.
 */
public class NormalCodeTreeSpliter extends AbsCodeTreeSpliter {
	@Override
	public CodeTreeNode split(CodeTreeNode treeNode) {
		// TODO Auto-generated method stub
		parentNode = null;
		this.splits(treeNode, parentNode);
		return parentNode;
	}

	private CodeTreeNode parentNode = null;

	private boolean splits(CodeTreeNode treeNode,
			CodeTreeNode parentCodeTreeNode) {
		if (parentCodeTreeNode == null) {
			parentCodeTreeNode = CodeTreeNodeClone.clone(treeNode);
			parentNode = parentCodeTreeNode;
		}
		if (treeNode instanceof CodeChangeTreeNode) {
			parentCodeTreeNode.getParentTreeNode().addChild(treeNode);
			return true;
		} else {
			boolean isChildHasModified = false;
			for (CodeTreeNode node : treeNode.getChildren()) {
				CodeTreeNode cloneCodeTreeNode = CodeTreeNodeClone.clone(node);
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

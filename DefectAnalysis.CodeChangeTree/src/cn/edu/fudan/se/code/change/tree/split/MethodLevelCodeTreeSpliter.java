/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.split;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class MethodLevelCodeTreeSpliter implements AbsCodeTreeSpliter {
	@Override
	public List<CodeTreeNode> split(CodeTreeNode treeNode) {
		NormalCodeTreeSpliter normalCodeTreeSpliter = new NormalCodeTreeSpliter();
		List<CodeTreeNode> removeUnchangedNodeTree = normalCodeTreeSpliter
				.split(treeNode);
		List<CodeTreeNode> splitedCodeTreeNodes = new ArrayList<CodeTreeNode>();
		for (CodeTreeNode codeTreeNode : removeUnchangedNodeTree) {
			ArrayList<CodeTreeNode> children = codeTreeNode.getChildren();
			if (!children.isEmpty()) {
				this.visiTreeNodes(children.get(children.size() - 1),
						splitedCodeTreeNodes);
			}
		}
		return splitedCodeTreeNodes;
	}

	private boolean visiTreeNodes(CodeTreeNode codeTreeNode,
			List<CodeTreeNode> splitedCodeTreeNodes) {

		if (codeTreeNode.getNodeType() != ASTNode.TYPE_DECLARATION) {
			if (codeTreeNode instanceof CodeChangeTreeNode) {
				return true;
			}
		}
		ArrayList<CodeTreeNode> children = codeTreeNode.getChildren();
		boolean result = false;
		for (int i = 0; children != null && i < children.size(); i++) {
			CodeTreeNode childNode = children.get(i);
			boolean isChanged = this.visiTreeNodes(childNode,
					splitedCodeTreeNodes);
			result |= isChanged;
			if (isChanged) {
				if (childNode.getParentTreeNode().getNodeType() == ASTNode.TYPE_DECLARATION) {
					splitedCodeTreeNodes.add(childNode);
				} else {
					return isChanged;
				}
			}
		}
		return result;
	}
}

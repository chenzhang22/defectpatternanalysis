/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.replace;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.utils.NameTypeUtils;

/**
 * @author Lotay
 * 
 *         This strategy will directly replace the simple name with its
 *         corresponding Type.
 */
public class DirectNodeTypeReplaceStrategy implements
		AbsNodeTypeReplaceStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.fudan.se.code.change.tree.type.AbsCodeTreeNodeTypeReplace#replace
	 * (cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode)
	 */
	@Override
	public CodeTreeNode replace(CodeTreeNode codeTree) {
		CodeTreeNode codeTreeNode = codeTree;

		ArrayList<CodeTreeNode> children = codeTreeNode.getChildren();
		if (!children.isEmpty()) {
			replaceSimpleName(children.get(children.size() - 1));
		}
		return codeTree;
	}

	private void replaceSimpleName(CodeTreeNode codeTreeNode) {
		CodeTreeNode parentTreeNode = codeTreeNode.getParentTreeNode();
		String parentNodeType = null;
		if (parentTreeNode != null) {
			parentNodeType = parentTreeNode.getSimpleType();
		}
		if (codeTreeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode codeChangeTreeNode = (CodeChangeTreeNode) codeTreeNode;
			ASTNode preNode = codeChangeTreeNode.getPreNode();
			resolveSimpleName(codeTreeNode, parentNodeType, preNode);
		}
		ASTNode astNode = codeTreeNode.getNode();
		resolveSimpleName(codeTreeNode, parentNodeType, astNode);
		ArrayList<CodeTreeNode> children = codeTreeNode.getChildren();
		for (CodeTreeNode node : children) {
			this.replaceSimpleName(node);
		}
	}

	/**
	 * @param codeTreeNode
	 * @param parentNodeType
	 * @param astNode
	 */
	private void resolveSimpleName(CodeTreeNode codeTreeNode,
			String parentNodeType, ASTNode astNode) {

		if (astNode instanceof SimpleName) {
			/**
			 * ignore the simplename with parent type: SingleVariableDeclaration
			 * or VariableDeclarationFragment
			 */
			if ((parentNodeType != null)
					&& (!parentNodeType.equals("SingleVariableDeclaration"))
					&& (!parentNodeType.equals("VariableDeclarationFragment"))) {
				SimpleName simName = (SimpleName) astNode;
				String labelStr = simName.getFullyQualifiedName();
				String typeName = NameTypeUtils.genNameType(codeTreeNode,
						labelStr);
				if (typeName != null) {
//					System.out.println(codeTreeNode.getStartLine() + ":"
//							+ codeTreeNode.getEndLine() + ":" + labelStr
//							+ "-->" + typeName);
				}
			}
		}
	}

	private boolean shouldEnd(ASTNode astNode) {
		return false;
	}
}

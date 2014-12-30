/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.replace;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 * 
 * This strategy will directly replace the simple name with its corresponding Type.
 */
public class DirectNodeTypeReplaceStrategy implements AbsNodeTypeReplaceStrategy {

	/* (non-Javadoc)
	 * @see cn.edu.fudan.se.code.change.tree.type.AbsCodeTreeNodeTypeReplace#replace(cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode)
	 */
	@Override
	public CodeTreeNode replace(CodeTreeNode codeTree) {
		CodeTreeNode codeTreeNode = codeTree;
		replaceSimpleName(codeTreeNode);
		return codeTree;
	}

	private void replaceSimpleName(CodeTreeNode codeTreeNode) {
		// TODO Auto-generated method stub
		if(codeTreeNode instanceof CodeChangeTreeNode){
			CodeChangeTreeNode codeChangeTreeNode = (CodeChangeTreeNode) codeTreeNode;
			ASTNode preNode = codeChangeTreeNode.getPreNode();
			ASTNode postNode = codeChangeTreeNode.getNode();
			int preNodeType = preNode.getNodeType();
		}else{
			ASTNode astNode = codeTreeNode.getNode();
			if(astNode instanceof SimpleName){
				SimpleName simName = (SimpleName) astNode;
				
			}
		}
	}
	
	private boolean shouldEnd(ASTNode astNode){
		return false;
	}
}

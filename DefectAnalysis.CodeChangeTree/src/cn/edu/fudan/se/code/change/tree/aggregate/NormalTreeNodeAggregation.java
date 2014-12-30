/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.aggregate;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.code.change.tree.bean.AggregateTypeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class NormalTreeNodeAggregation implements AbsTreeNodeAggregation {

	@Override
	public AggregateTypeNode aggregate(CodeTreeNode treeNode) {
		if (treeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode changeTreeNode = (CodeChangeTreeNode) treeNode;
			SourceCodeChange sourceCodeChange = changeTreeNode
					.getSourceCodeChange();

			ASTNode preAstNode = changeTreeNode.getPreNode();
			ASTNode postAstNode = changeTreeNode.getNode();
			if (sourceCodeChange instanceof Insert) {
				postAstNode = changeTreeNode.getNode();
			} else if (sourceCodeChange instanceof Delete) {

			} else if (sourceCodeChange instanceof Move) {

			} else if (sourceCodeChange instanceof Update) {

			}

		} else {
			ASTNode postAstNode = treeNode.getNode();

		}

		return null;
	}

	@Override
	public String aggregate2Value(ASTNode astNode) {
		int nodeType = astNode.getNodeType();
		String value = "";
		switch (nodeType) {
		case ASTNode.TYPE_DECLARATION:
			TypeDeclaration tyDecl = (TypeDeclaration)astNode;
			List mods= tyDecl.modifiers();
			for(Object m:mods){
				value+=m.toString()+" ";
			}
			value += tyDecl.getName().getFullyQualifiedName()+" ";
			break;
		}
		return value;
	}
}

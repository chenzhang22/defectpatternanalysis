/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.aggregate;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.AggregateTypeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 *	This aggregation just simple abstract the change change as simple.
 */
public class TypeTreeNodeAggregation implements AbsTreeNodeAggregation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.fudan.se.code.change.tree.aggregate.AbsTreeNodeAggregation#aggregate
	 * (cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode)
	 */
	@Override
	public AggregateTypeNode aggregate(CodeTreeNode treeNode) {
		AggregateTypeNode aggregateTypeNode = new AggregateTypeNode(treeNode);
		if (treeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode changeTreeNode = (CodeChangeTreeNode) treeNode;
			
			/**Generate the change Type for the tree node.*/
			SourceCodeChange sourceCodeChange = changeTreeNode
					.getSourceCodeChange();
			aggregateTypeNode.setChangeType(sourceCodeChange.getChangeType()
					.name());
			
			/** generate the previous node type and value.*/
			ASTNode preAstNode = changeTreeNode.getPreNode();
			if (preAstNode != null) {
				String preValue = this.aggregate2Value(preAstNode);
				aggregateTypeNode.setPreNodeValue(preValue);
			}

		}
		
		/** generate the post node type and value..*/
		ASTNode postAstNode = treeNode.getNode();
		if (postAstNode != null) {
			String postValue = this.aggregate2Value(postAstNode);
			aggregateTypeNode.setPostNodeValue(postValue);
		}
		
		ArrayList<CodeTreeNode> treeNodeChildren = treeNode.getChildren();
		for(CodeTreeNode childNode:treeNodeChildren){
			AggregateTypeNode childTypeNode = this.aggregate(childNode);
			aggregateTypeNode.addChildNode(childTypeNode);
		}
		
		return aggregateTypeNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.edu.fudan.se.code.change.tree.aggregate.AbsTreeNodeAggregation#
	 * aggregate2Value(org.eclipse.jdt.core.dom.ASTNode)
	 */
	@Override
	public String aggregate2Value(ASTNode treeNode) {
		return ASTNode.nodeClassForType(treeNode.getNodeType()).getName();
	}
}

/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.aggregate;

import org.eclipse.jdt.core.dom.ASTNode;

import cn.edu.fudan.se.code.change.tree.bean.AggregateTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public interface AbsTreeNodeAggregation {
	public AggregateTreeNode aggregate(CodeTreeNode treeNode);
	
	public String aggregate2Value(ASTNode treeNode);
}

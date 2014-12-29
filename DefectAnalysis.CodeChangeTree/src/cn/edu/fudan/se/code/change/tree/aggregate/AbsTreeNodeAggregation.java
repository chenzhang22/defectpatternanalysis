/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.aggregate;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public interface AbsTreeNodeAggregation {
	public CodeTreeNode aggregate(CodeTreeNode treeNode);
	
	public String aggregate2Value(CodeTreeNode treeNode);
}

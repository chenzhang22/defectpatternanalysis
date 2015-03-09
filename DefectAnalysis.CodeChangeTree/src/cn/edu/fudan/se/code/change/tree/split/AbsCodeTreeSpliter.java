/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.split;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public interface AbsCodeTreeSpliter {
	public List<CodeTreeNode> split(CodeTreeNode treeNode);
}

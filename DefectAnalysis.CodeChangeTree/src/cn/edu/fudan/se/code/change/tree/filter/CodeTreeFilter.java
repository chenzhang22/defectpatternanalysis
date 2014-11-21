/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.filter;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public abstract class CodeTreeFilter {
	protected abstract CodeTreeNode filter(CodeTreeNode codeRootNode);
}

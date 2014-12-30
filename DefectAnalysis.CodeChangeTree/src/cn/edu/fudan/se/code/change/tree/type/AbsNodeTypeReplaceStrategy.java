/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.type;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public interface AbsNodeTypeReplaceStrategy {
	public abstract CodeTreeNode replace(CodeTreeNode codeTree);
}

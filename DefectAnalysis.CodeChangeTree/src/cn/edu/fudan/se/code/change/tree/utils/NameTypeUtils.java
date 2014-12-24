/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.utils;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class NameTypeUtils {
	/**
	 * read the name type for the 'name'
	 * 
	 * @param codeTreeNode
	 * @param name
	 * @return
	 */
	public static String genNameType(CodeTreeNode codeTreeNode, String name) {
		if (codeTreeNode == null) {
			return null;
		}
		CodeTreeNode codeNode = codeTreeNode;
		while (codeNode != null) {
			if (codeNode.hasTypeName(name)) {
				return codeNode.getNameType(name);
			}
			codeNode = codeNode.getParentTreeNode();
		}
		return null;
	}
}

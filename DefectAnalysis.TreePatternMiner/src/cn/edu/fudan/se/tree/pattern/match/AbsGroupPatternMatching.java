/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.match;

import java.util.List;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public abstract class AbsGroupPatternMatching implements IPatternMatching {
	protected List<CodeTreeNode> groupPatterns = null;

	public AbsGroupPatternMatching(List<CodeTreeNode> groupPattern) {
		super();
		this.groupPatterns = groupPattern;
	}
	
	public abstract CodeTreeNode matchOne(CodeTreeNode codeTreeNode);
}

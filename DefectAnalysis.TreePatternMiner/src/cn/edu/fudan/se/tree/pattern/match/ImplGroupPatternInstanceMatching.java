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
public class ImplGroupPatternInstanceMatching extends AbsGroupPatternMatching {

	public ImplGroupPatternInstanceMatching(List<CodeTreeNode> groupPattern) {
		super(groupPattern);
	}

	/* (non-Javadoc)
	 * @see cn.edu.fudan.se.tree.pattern.match.IPatternMatching#match(cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode)
	 */
	@Override
	public boolean match(CodeTreeNode instanceCodeTreeNode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CodeTreeNode matchOne(CodeTreeNode codeTreeNode) {
		// TODO Auto-generated method stub
		return null;
	}

}

package cn.edu.fudan.se.tree.pattern.match;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

public abstract class AbsSinglePatternMatching implements IPatternMatching {
	protected CodeTreeNode pattern = null;

	public AbsSinglePatternMatching(CodeTreeNode pattern) {
		super();
		this.pattern = pattern;
	}
}

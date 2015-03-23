package cn.edu.fudan.se.tree.pattern.match;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

public abstract class AbsSinglePatternMatching extends AbsPatternMatching {

	public AbsSinglePatternMatching(TreeNode pattern) {
		super();
		this.pattern = pattern;
	}

	public AbsSinglePatternMatching(TreeNode pattern,
			ITreeNodeSimilarity similarityFunction) {
		super();
		this.pattern = pattern;
		this.similarityFunction = similarityFunction;
	}

	public AbsSinglePatternMatching(TreeNode pattern,
			double similarityThredhold) {
		super();
		this.pattern = pattern;
		this.similarityThredhold = similarityThredhold;
	}
	public AbsSinglePatternMatching(TreeNode pattern,
			double similarityThredhold,
			ITreeNodeSimilarity similarityFunction) {
		super();
		this.pattern = pattern;
		this.similarityThredhold = similarityThredhold;
		this.similarityFunction = similarityFunction;
	}
}

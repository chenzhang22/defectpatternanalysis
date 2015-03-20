package cn.edu.fudan.se.tree.pattern.match;

import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.tree.pattern.similarility.CodeTreeNodeSimilarityImpl;
import cn.edu.fudan.se.tree.pattern.similarility.ICodeTreeNodeSimilarity;

public abstract class AbsSinglePatternMatching implements IPatternMatching {
	protected CodeTreeNode pattern = null;
	private double similarityThredhold = 1.0;
	private ICodeTreeNodeSimilarity similarityFunction = new CodeTreeNodeSimilarityImpl();

	public AbsSinglePatternMatching(CodeTreeNode pattern) {
		super();
		this.pattern = pattern;
	}

	public AbsSinglePatternMatching(CodeTreeNode pattern,
			ICodeTreeNodeSimilarity similarityFunction) {
		super();
		this.pattern = pattern;
		this.similarityFunction = similarityFunction;
	}

	public AbsSinglePatternMatching(CodeTreeNode pattern,
			double similarityThredhold) {
		super();
		this.pattern = pattern;
		this.similarityThredhold = similarityThredhold;
	}
	public AbsSinglePatternMatching(CodeTreeNode pattern,
			double similarityThredhold,
			ICodeTreeNodeSimilarity similarityFunction) {
		super();
		this.pattern = pattern;
		this.similarityThredhold = similarityThredhold;
		this.similarityFunction = similarityFunction;
	}


}

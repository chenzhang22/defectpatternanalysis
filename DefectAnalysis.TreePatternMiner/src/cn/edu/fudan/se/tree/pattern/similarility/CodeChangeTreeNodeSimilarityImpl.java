/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.similarility;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class CodeChangeTreeNodeSimilarityImpl extends
		CodeTreeNodeSimilarityImpl {

	@Override
	public double similarity(CodeTreeNode aggreTreeNode1,
			CodeTreeNode aggreTreeNode2) {
		// both node should be CodeChangeTreeNode
		if (!(aggreTreeNode1 instanceof CodeChangeTreeNode && aggreTreeNode2 instanceof CodeChangeTreeNode)) {
			return 0;
		}
		CodeChangeTreeNode changeTreeNode1 = (CodeChangeTreeNode) aggreTreeNode1;
		CodeChangeTreeNode changeTreeNode2 = (CodeChangeTreeNode) aggreTreeNode2;
		SourceCodeChange sourceCodeChange1 = changeTreeNode1
				.getSourceCodeChange();
		SourceCodeChange sourceCodeChange2 = changeTreeNode2
				.getSourceCodeChange();

		Class<? extends SourceCodeChange> sourceCodeClass1 = sourceCodeChange1
				.getClass();
		Class<? extends SourceCodeChange> sourceCodeClass2 = sourceCodeChange2
				.getClass();
		ASTNode preAstNode1 = changeTreeNode1.getPreNode();
		ASTNode preAstNode2 = changeTreeNode2.getPreNode();
		ASTNode postAstNode1 = changeTreeNode1.getNode();
		ASTNode postAstNode2 = changeTreeNode2.getNode();
		if (sourceCodeClass1.equals(sourceCodeClass2)) {
			if (sourceCodeChange1 instanceof Insert) {
				// Insert statement/node.
				if (postAstNode1 != null && postAstNode2 != null) {
					return super.similarity(postAstNode1, postAstNode2);
				}
			} else if (sourceCodeChange1 instanceof Update
					|| sourceCodeChange1 instanceof Move) {
				// update or move...
				if ((postAstNode1 != null && postAstNode2 != null)
						&& (preAstNode1 != null && preAstNode2 != null)) {
					return super.similarity(preAstNode1, preAstNode2)
							* super.similarity(postAstNode1, postAstNode2);
				}
			} else if (sourceCodeChange1 instanceof Delete) {
				// Delete statement/node..
				if (preAstNode1 != null && preAstNode2 != null) {
					return super.similarity(preAstNode1, preAstNode2);
				}
			}
		}

		return 0;
	}

	public static void main(String args[]) {
		SourceCodeChange sourceCodeChange = new Insert(null, null, null);
		System.out.println(sourceCodeChange.getClass());
	}

}

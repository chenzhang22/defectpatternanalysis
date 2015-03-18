/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.similarility;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;

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
public class CodeTreeNodeSimilarityImpl implements ICodeTreeNodeSimilarity {

	/*
	 * (non-Javadoc) If all child nodes of node1 and node2 are equal, then node1
	 * and node2 are equal.
	 */
	@Override
	public double treeNodeSimilarity(CodeTreeNode node1, CodeTreeNode node2) {
		double similarity = this.similarity(node1, node2);
		double childSimilarity = 1;
		for (int i = 0; i < node1.getChildren().size(); i++) {
			CodeTreeNode childNode1 = node1.getChildren().get(i);
			if (i < node2.getChildren().size()) {
				CodeTreeNode childNode2 = node2.getChildren().get(i);
				childSimilarity *= this.similarity(childNode1, childNode2);
			} else {
				childSimilarity *= 0;
				break;
			}
		}
		return similarity * childSimilarity;
	}

	@Override
	public double similarity(CodeTreeNode codeNode1, CodeTreeNode codeNode2) {
		if (codeNode1 == null || codeNode2 == null) {
			return 0;
		}
		if (codeNode1 instanceof CodeChangeTreeNode
				&& codeNode2 instanceof CodeChangeTreeNode) {
			return this.similarity((CodeChangeTreeNode) codeNode1,
					(CodeChangeTreeNode) codeNode2);
		} else if (codeNode1 instanceof CodeChangeTreeNode
				|| codeNode2 instanceof CodeChangeTreeNode) {
			return 0;
		}
		ASTNode node1 = codeNode1.getNode();
		ASTNode node2 = codeNode2.getNode();
		return similarity(node1, node2);
	}

	@Override
	public double similarity(CodeChangeTreeNode aggreTreeNode1,
			CodeChangeTreeNode aggreTreeNode2) {
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
					return this.similarity(postAstNode1, postAstNode2);
				}
			} else if (sourceCodeChange1 instanceof Update
					|| sourceCodeChange1 instanceof Move) {
				// update or move...
				if ((postAstNode1 != null && postAstNode2 != null)
						&& (preAstNode1 != null && preAstNode2 != null)) {
					return this.similarity(preAstNode1, preAstNode2)
							* this.similarity(postAstNode1, postAstNode2);
				}
			} else if (sourceCodeChange1 instanceof Delete) {
				// Delete statement/node..
				if (preAstNode1 != null && preAstNode2 != null) {
					return this.similarity(preAstNode1, preAstNode2);
				}
			}
		}

		return 0;
	}

	/* calculate the similarity between the node1 and node3 */
	private double similarity(ASTNode node1, ASTNode node2) {
		if (node1 == null || node2 == null) {
			return 0;
		}
		int nodeType1 = node1.getNodeType();
		int nodeType2 = node2.getNodeType();
		if (nodeType1 != nodeType2) {
			return 0;
		}
		switch (nodeType1) {
		case ASTNode.ANNOTATION_TYPE_DECLARATION:
		case ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION:
		case ASTNode.BLOCK_COMMENT:
		case ASTNode.LINE_COMMENT:
		case ASTNode.MARKER_ANNOTATION:
		case ASTNode.MEMBER_REF:
		case ASTNode.MEMBER_VALUE_PAIR:
		case ASTNode.METHOD_REF:
		case ASTNode.METHOD_REF_PARAMETER:
		case ASTNode.NORMAL_ANNOTATION:
		case ASTNode.SINGLE_MEMBER_ANNOTATION:
		case ASTNode.TAG_ELEMENT:
		case ASTNode.TEXT_ELEMENT:
			return 0;

		case ASTNode.BOOLEAN_LITERAL:
			BooleanLiteral booleanLiteral1 = (BooleanLiteral) node1;

			BooleanLiteral booleanLiteral2 = (BooleanLiteral) node2;
			if (booleanLiteral1.booleanValue() == booleanLiteral2
					.booleanValue()) {
				return 1;
			}
			return 0;

		case ASTNode.CHARACTER_LITERAL:
			CharacterLiteral characterLiteral1 = (CharacterLiteral) node1;
			CharacterLiteral characterLiteral2 = (CharacterLiteral) node2;
			if (characterLiteral1.charValue() == characterLiteral2.charValue()) {
				return 1;
			}
			return 0;

		case ASTNode.LABELED_STATEMENT:
			LabeledStatement labeledStatement1 = (LabeledStatement) node1;
			LabeledStatement labeledStatement2 = (LabeledStatement) node2;
			if (labeledStatement1.getLabel().getIdentifier()
					.equals(labeledStatement2.getLabel().getIdentifier())) {
				return 1;
			}
			return 0;

		case ASTNode.MODIFIER:
			Modifier modifier1 = (Modifier) node1;
			Modifier modifier2 = (Modifier) node2;
			if (modifier1.getKeyword().toString()
					.equals(modifier2.getKeyword().toString())) {
				return 1;
			}
			return 0;

		case ASTNode.NUMBER_LITERAL:
			NumberLiteral numberLiteral1 = (NumberLiteral) node1;
			NumberLiteral numberLiteral2 = (NumberLiteral) node2;
			if (numberLiteral1.getToken().equals(numberLiteral2.getToken())) {
				return 1;
			}
			return 0;

		case ASTNode.PRIMITIVE_TYPE:
			PrimitiveType primitiveType1 = (PrimitiveType) node1;
			PrimitiveType primitiveType2 = (PrimitiveType) node2;
			if (primitiveType1.getPrimitiveTypeCode() == primitiveType2
					.getPrimitiveTypeCode()) {
				return 1;
			}
			return 0;

		case ASTNode.SIMPLE_NAME:
			SimpleName simpleName1 = (SimpleName) node1;
			SimpleName simpleName2 = (SimpleName) node2;
			if (simpleName1.getIdentifier().equals(simpleName2.getIdentifier())) {
				return 1;
			}
			return 0;

		case ASTNode.STRING_LITERAL:
			StringLiteral stringLiteral1 = (StringLiteral) node1;
			StringLiteral stringLiteral2 = (StringLiteral) node2;
			String literalValue1 = stringLiteral1.getLiteralValue();
			String literalValue2 = stringLiteral2.getLiteralValue();
			int length1 = literalValue1.length();
			int length2 = literalValue2.length();
			return 1 - EditDistance.minDistance(literalValue1, literalValue2)
					* 1.0 / Math.max(length1, length2);

		case ASTNode.TYPE_LITERAL:
			TypeLiteral typeLiteral1 = (TypeLiteral) node1;
			TypeLiteral typeLiteral2 = (TypeLiteral) node2;

			if (typeLiteral1.getType().toString()
					.equals(typeLiteral2.getType().toString())) {
				return 1;
			}
			return 0;

		default:
			return nodeType1 == nodeType2 ? 1 : 0;
		}
	}
}

/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.aggregate;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.code.change.tree.bean.AggregateTypeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class NormalTreeNodeAggregation implements AbsTreeNodeAggregation {

	@Override
	public AggregateTypeNode aggregate(CodeTreeNode treeNode) {
		AggregateTypeNode aggregateTypeNode = null;
		if (treeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode changeTreeNode = (CodeChangeTreeNode) treeNode;
			SourceCodeChange sourceCodeChange = changeTreeNode
					.getSourceCodeChange();

			ASTNode preAstNode = changeTreeNode.getPreNode();
			ASTNode postAstNode = changeTreeNode.getNode();
			if (sourceCodeChange instanceof Insert) {
				postAstNode = changeTreeNode.getNode();
			} else if (sourceCodeChange instanceof Delete) {
				preAstNode = changeTreeNode.getPreNode();
			} else if (sourceCodeChange instanceof Move) {
				preAstNode = changeTreeNode.getPreNode();
				postAstNode = changeTreeNode.getNode();
			} else if (sourceCodeChange instanceof Update) {
				preAstNode = changeTreeNode.getPreNode();
				postAstNode = changeTreeNode.getNode();
			}

			// //aggregate to string.
			String preAstValue = null;
			String postAstValue = null;
			if (preAstNode != null) {
				preAstValue = this.aggregate2Value(preAstNode);

			}
			if (postAstNode != null) {
				postAstValue = this.aggregate2Value(postAstNode);
			}
			if (preAstValue != null || postAstValue != null) {
				aggregateTypeNode = new AggregateTypeNode(treeNode);
				aggregateTypeNode.setPostNodeValue(postAstValue);
				aggregateTypeNode.setPreNodeValue(preAstValue);
				aggregateTypeNode.setChangeType(sourceCodeChange.getLabel());
				// System.out.println(preAstValue+"->"+postAstNode);
			} else {
				aggregateTypeNode = new AggregateTypeNode(treeNode);
				for (CodeTreeNode chiCodeTreeNode : treeNode.getChildren()) {
					AggregateTypeNode childAggregatedTypeNode = this
							.aggregate(chiCodeTreeNode);
					aggregateTypeNode.addChildNode(childAggregatedTypeNode);
					childAggregatedTypeNode.setParentNode(aggregateTypeNode);
				}
			}
		} else {
			ASTNode astNodestNode = treeNode.getNode();
			String astValue = null;
			if (astNodestNode != null) {
				astValue = this.aggregate2Value(astNodestNode);
			}
			if (astValue != null) {
				aggregateTypeNode = new AggregateTypeNode(treeNode);
				aggregateTypeNode.setPostNodeValue(astValue);
				// System.out.println(astValue);

			} else {
				aggregateTypeNode = new AggregateTypeNode(treeNode);
				for (CodeTreeNode chiCodeTreeNode : treeNode.getChildren()) {
					AggregateTypeNode childAggregatedTypeNode = this
							.aggregate(chiCodeTreeNode);
					aggregateTypeNode.addChildNode(childAggregatedTypeNode);
					childAggregatedTypeNode.setParentNode(aggregateTypeNode);
				}
			}
		}
		return aggregateTypeNode;
	}

	@Override
	public String aggregate2Value(ASTNode astNode) {
		if (astNode == null) {
			return null;
		}
		switch (astNode.getNodeType()) {
		case ASTNode.EMPTY_STATEMENT:
		case ASTNode.JAVADOC:
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
		case ASTNode.TYPE_METHOD_REFERENCE:
			return "";
		case ASTNode.NULL_LITERAL:
			return "null";
		case ASTNode.CHARACTER_LITERAL:
			CharacterLiteral characterLiteral = (CharacterLiteral) astNode;
			return "'" + characterLiteral.charValue() + "'";
		case ASTNode.MODIFIER:
			Modifier modifier = (Modifier) astNode;
			return modifier.getKeyword().toString();
		case ASTNode.NUMBER_LITERAL:
			NumberLiteral numberLiteral = (NumberLiteral) astNode;
			return numberLiteral.getToken();
		case ASTNode.PRIMITIVE_TYPE:
			PrimitiveType primitiveType = (PrimitiveType) astNode;
			return primitiveType.toString();
		case ASTNode.QUALIFIED_NAME:
			QualifiedName qualifiedName = (QualifiedName) astNode;
			return qualifiedName.getFullyQualifiedName().toString();
		case ASTNode.QUALIFIED_TYPE:
			QualifiedType qualifiedType = (QualifiedType) astNode;
			return qualifiedType.toString();
		case ASTNode.RETURN_STATEMENT:
			String retStr = "return";
			return retStr;
		case ASTNode.SIMPLE_NAME:
			SimpleName simpleName = (SimpleName) astNode;
			return simpleName.getFullyQualifiedName();
		case ASTNode.SIMPLE_TYPE:
			SimpleType simpleType = (SimpleType) astNode;
			return simpleType.getName().toString();
		case ASTNode.STRING_LITERAL:
			StringLiteral stringLiteral = (StringLiteral) astNode;
			return "\"" + stringLiteral.getLiteralValue() + "\"";
		case ASTNode.THIS_EXPRESSION:
			return "this";
		case ASTNode.TYPE_LITERAL:
			TypeLiteral typeLiteral = (TypeLiteral) astNode;
			return typeLiteral.getType().toString();

		case ASTNode.NAME_QUALIFIED_TYPE:
			NameQualifiedType nameQualifiedType = (NameQualifiedType) astNode;
			return nameQualifiedType.toString();

		case ASTNode.UNION_TYPE:
			// UnionType unionType = (UnionType) astNode;
			// for (Object itype: unionType.types()) {
			//
			// };
			break;
		}
		return null;
	}
}

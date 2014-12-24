/**
 * 
 */
package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WildcardType;

import cn.edu.fudan.se.code.change.tree.bean.ChangeLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.constant.CodeChangeTreeConstants;

/**
 * @author Lotay
 *
 */
public abstract class FileTreeVisitor extends ASTVisitor {

	protected HashMap<ASTNode, CodeTreeNode> astTreeNodes = new HashMap<ASTNode, CodeTreeNode>();
	protected String repoName = CodeChangeTreeConstants.REPO_NAME;
	protected String fileName = null;
	protected String revisionId = null;
	protected CodeTreeNode parentTreeNode = null;
	protected CodeTreeNode rootTreeNode = null;
	protected CodeRangeList codeChangeRangeList = null;

	/**
	 * @param fileName
	 * @param revisionId
	 * @param codeChangeRangeList
	 */
	public FileTreeVisitor(String fileName, String revisionId,
			CodeRangeList codeChangeRangeList) {
		super();
		this.fileName = fileName;
		this.revisionId = revisionId;
		this.codeChangeRangeList = codeChangeRangeList;
	}

	protected int startLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node
				.getStartPosition());
	}

	protected int endLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node
				.getStartPosition() + node.getLength());
	}

	protected int starColumn(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getColumnNumber(node
				.getStartPosition());
	}

	protected int endColumn(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getColumnNumber(node
				.getStartPosition() + node.getLength());
	}

	public CodeTreeNode getRootTreeNode() {
		return rootTreeNode;
	}

	protected CodeRangeList checkChangeRange(int startLine, int endLine) {
		CodeRangeList rangeList = new CodeRangeList();
		for (ChangeLineRange range : this.codeChangeRangeList) {
			if (range.getInducedStartLine() <= startLine
					&& range.getInducedEndLine() >= endLine) {
				rangeList.add(range);
			}
		}
		return rangeList;
	}

	/**
	 * @param node
	 * @param startLine
	 * @param endLine
	 * @param list
	 * @param treeNode
	 * @return
	 */
	protected CodeTreeNode buildNormalTreeNode(ASTNode node, int startLine,
			int endLine, CodeRangeList list, CodeTreeNode treeNode) {
		int startColumn = this.starColumn(node);
		int endColumn = this.endColumn(node);
		treeNode.setEndColumn(endColumn);
		treeNode.setStartColumn(startColumn);
		treeNode.setStartLine(startLine);
		treeNode.setEndLine(endLine);
		treeNode.setStartIndex(node.getStartPosition());
		treeNode.setEndIndex(node.getStartPosition() + node.getLength());
		treeNode.setNode(node);
		treeNode.setRepoName(repoName);
		treeNode.setFileName(fileName);
		treeNode.setRevisionId(revisionId);
		treeNode.setContent(node.toString());
		treeNode.setType(node.getClass().getName());
		treeNode.setSimpleType(node.getClass().getSimpleName());
		for (ChangeLineRange range : list) {
			treeNode.addBugId(range.getBugId());
		}
		return treeNode;
	}

	/**
	 * @param node
	 * @param treeNode
	 */
	protected void buildTree(ASTNode node, CodeTreeNode treeNode) {
		if (parentTreeNode == null) {
			parentTreeNode = treeNode;
			rootTreeNode = parentTreeNode;
		} else {
			// Add the node append to its parent...
			ASTNode parentNode = node.getParent();
			if (astTreeNodes.containsKey(parentNode)) {
				CodeTreeNode codeTreeNode = astTreeNodes.get(parentNode);
				codeTreeNode.addChild(treeNode);
			} else {
				// append the node to the root node.
				rootTreeNode.addChild(treeNode);
			}
		}
		astTreeNodes.put(node, treeNode);

		// Save the name type of the field, variable declaration.
		genNameType(node, treeNode);
	}

	/**
	 * The belong code is used to resolve the type and for resolving the
	 * variable name.
	 * */
	private HashSet<String> importList = new HashSet<String>();
	private HashSet<String> typeDecl = new HashSet<String>();

	@SuppressWarnings("unchecked")
	protected void genNameType(ASTNode node, CodeTreeNode belongCodeTreeNode) {
		if (node instanceof SingleVariableDeclaration) {
			SingleVariableDeclaration svd = (SingleVariableDeclaration) node;
			Type ty = svd.getType();
			String name = svd.getName().toString();
			String tyName = resolveType(ty);
			addNameType(belongCodeTreeNode, name, tyName);
		} else if (node instanceof FieldDeclaration) {
			FieldDeclaration fd = (FieldDeclaration) node;
			Type ty = fd.getType();
			String tyName = resolveType(ty);

			for (VariableDeclarationFragment var : (List<VariableDeclarationFragment>) fd
					.fragments()) {
				String name = var.getName().toString();
				addNameType(belongCodeTreeNode, name, tyName);
			}
		} else if (node instanceof VariableDeclarationStatement) {
			VariableDeclarationStatement varDeclStatement = (VariableDeclarationStatement) node;
			Type ty = varDeclStatement.getType();
			String tyName = resolveType(ty);

			for (VariableDeclarationFragment var : (List<VariableDeclarationFragment>) varDeclStatement
					.fragments()) {
				String name = var.getName().toString();
				addNameType(belongCodeTreeNode, name, tyName);
			}
		} else if (node instanceof PackageDeclaration) {
			// PackageDeclaration packDecl = (PackageDeclaration) node;
			// String packDeclStr = packDecl.getName().getFullyQualifiedName();
		} else if (node instanceof ImportDeclaration) {
			ImportDeclaration impDecl = (ImportDeclaration) node;
			importList.add(impDecl.getName().getFullyQualifiedName());
		} else if (node instanceof AbstractTypeDeclaration) {
			AbstractTypeDeclaration absTypeDecl = (AbstractTypeDeclaration) node;
			typeDecl.add(absTypeDecl.getName().getFullyQualifiedName());
		}
	}

	void addNameType(CodeTreeNode belongCodeTreeNode, String name, String type) {
		if (belongCodeTreeNode.getParentTreeNode() != null) {
			belongCodeTreeNode.getParentTreeNode().addNameType(name, type);
		} else {
			belongCodeTreeNode.addNameType(name, type);
		}
	}

	/**
	 * resolve the simple type string to name.
	 * */
	private String resolveTypeQualifierString(String tyStr) {
		for (String impStr : importList) {
			if (impStr.endsWith(tyStr)) {
				int ind = impStr.lastIndexOf(tyStr);
				if (ind > 0 && impStr.charAt(ind - 1) == '.') {
					return impStr;
				}
			}
		}
		for (String ty : typeDecl) {
			if (ty.endsWith(tyStr)) {
				int ind = ty.lastIndexOf(tyStr);
				if (ind > 0 && ty.charAt(ind - 1) == '.') {
					return ty;
				}
			}
		}
		return tyStr;
	}

	/**
	 * This method used to resolve from type to type string.
	 * 
	 */
	@SuppressWarnings("unchecked")
	private String resolveType(Type ty) {
		if (ty == null) {
			return "null";
		}
		int tyInd = ty.getNodeType();
		String typeStr = "";
		switch (tyInd) {
		case ASTNode.ARRAY_TYPE:
			ArrayType arrTypes = (ArrayType) ty;
			int dimensionsNum = arrTypes.getDimensions();
			Type eleTy = arrTypes.getElementType();
			typeStr += this.resolveType(eleTy);
			for (int i = 0; i < dimensionsNum; i++) {
				typeStr += "[]";
			}
			break;
		case ASTNode.INTERSECTION_TYPE:
			IntersectionType interSectType = (IntersectionType) ty;

			List<Type> interListTys = interSectType.types();
			for (int i = 0; i < interListTys.size(); i++) {
				if (i > 0) {
					typeStr += "&";
				}
				typeStr += this.resolveType(interListTys.get(i));
			}
			break;
		case ASTNode.NAME_QUALIFIED_TYPE:
			NameQualifiedType nameQualifiedType = (NameQualifiedType) ty;
			typeStr += nameQualifiedType.getName().getFullyQualifiedName();
			break;
		case ASTNode.PARAMETERIZED_TYPE:
			ParameterizedType paraType = (ParameterizedType) ty;
			String typeString = this.resolveType(paraType.getType());
			typeStr += this.resolveTypeQualifierString(typeString);
			List<Type> pTypes = paraType.typeArguments();
			typeStr += "<";
			for (int i = 0; i < pTypes.size(); i++) {
				if (i > 0) {
					typeStr += ",";
				}
				typeStr += this.resolveType(pTypes.get(i));
			}
			typeStr += ">";
			break;
		case ASTNode.PRIMITIVE_TYPE:
			PrimitiveType primitiveType = (PrimitiveType) ty;
			typeStr += primitiveType.toString();
			break;
		case ASTNode.QUALIFIED_TYPE:
			QualifiedType qualifiedType = (QualifiedType) ty;
			Type qualifierTy = qualifiedType.getQualifier();
			typeStr += this.resolveType(qualifierTy);

			String quaName = qualifiedType.getName().getFullyQualifiedName();
			typeStr += "." + this.resolveTypeQualifierString(quaName);
			break;
		case ASTNode.SIMPLE_TYPE:
			SimpleType simpleType = (SimpleType) ty;
			typeStr += this.resolveTypeQualifierString(simpleType.getName()
					.getFullyQualifiedName());
			break;
		case ASTNode.UNION_TYPE:
			UnionType unionType = (UnionType) ty;
			List<Type> unionListTys = unionType.types();
			for (int i = 0; i < unionListTys.size(); i++) {
				if (i > 0) {
					typeStr += "|";
				}
				typeStr += this.resolveType(unionListTys.get(i));
			}
			break;
		case ASTNode.WILDCARD_TYPE:
			WildcardType wildcardType = (WildcardType) ty;
			Type bType = wildcardType.getBound();
			typeStr += "? extends " + this.resolveType(bType);
			break;
		default:
			return ty.toString();
		}
		return typeStr;
	}
}

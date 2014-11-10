package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class VisitorTest extends ASTVisitor {

	Stack<ASTNode> astNodes = new Stack<ASTNode>();

}

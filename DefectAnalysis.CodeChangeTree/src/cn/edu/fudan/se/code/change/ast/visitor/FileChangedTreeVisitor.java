/**
 * 
 */
package cn.edu.fudan.se.code.change.ast.visitor;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * @author Lotay
 *
 */
public class FileChangedTreeVisitor extends ASTVisitor {
	private List<SourceCodeChange> sourceCodeChanges = null;
	private List<Integer> changeLineNumbers = null;
	
}

/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.diff;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.code.change.tree.bean.ChangeLineRange;
import cn.edu.fudan.se.code.change.tree.bean.CodeRangeList;

/**
 * @author Lotay
 *
 */
public abstract class FileRevisionDiffer {
	public abstract void diff();

}

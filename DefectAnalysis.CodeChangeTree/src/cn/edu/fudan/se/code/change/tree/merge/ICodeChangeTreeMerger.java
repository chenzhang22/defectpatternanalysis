/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.merge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public abstract class ICodeChangeTreeMerger {
	public abstract CodeTreeNode merge(CodeTreeNode beforeCodeTree,
			CodeTreeNode afterCodeTree, List<SourceCodeChange> changes);

	protected HashMap<SourceCodeChange, CodeTreeNode> changeTreeNodeMaps = new HashMap<SourceCodeChange, CodeTreeNode>();

	/**
	 * @param codeTreeNode
	 * @param changeTreeNodeMaps
	 * @param includeDeleteChangeType
	 *            -- false: ignore delete, true: does not ignore
	 *            delete(included).
	 */

	protected void buildChangeTreeNodeMap(CodeTreeNode codeTreeNode) {
		changeTreeNodeMaps.clear();
		buildChangeTreeNodeLocation(codeTreeNode,0);
		System.out.println(changeLocations);
	}
	
	private Stack<Integer> locationStack = new Stack<Integer>();
	protected HashMap<SourceCodeChange,List<Integer>> changeLocations = new HashMap<SourceCodeChange,List<Integer>>();
	private void buildChangeTreeNodeLocation(CodeTreeNode codeTreeNode,int index) {
		locationStack.push(index);
		if (codeTreeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode codeChangeTreeNode = (CodeChangeTreeNode) codeTreeNode;
			SourceCodeChange change = codeChangeTreeNode.getSourceCodeChange();
			changeTreeNodeMaps.put(change, codeChangeTreeNode);
			if(change instanceof Delete){
				if(!changeLocations.containsKey(change)){
					List<Integer> locations = new ArrayList<Integer>();
					locations.addAll(locationStack);
					changeLocations.put(change, locations);
				}
			}
		}
		List<CodeTreeNode> childrenNodes = codeTreeNode.getChildren();
		int i=0;
		for (CodeTreeNode childNode : childrenNodes) {
			buildChangeTreeNodeLocation(childNode,i);
			i++;
		}
		locationStack.pop();
	}
}

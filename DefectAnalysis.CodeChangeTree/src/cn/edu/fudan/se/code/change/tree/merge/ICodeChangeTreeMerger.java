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

	protected HashMap<SourceCodeChange, CodeChangeTreeNode> changeTreeNodeMaps = new HashMap<SourceCodeChange, CodeChangeTreeNode>();

	/**
	 * @param codeTreeNode
	 * @param changeTreeNodeMaps
	 * @param includeDeleteChangeType
	 *            -- false: ignore delete, true: does not ignore
	 *            delete(included).
	 */
	protected void buildChangeTreeNodeMap(CodeTreeNode codeTreeNode) {
		changeTreeNodeMaps.clear();
		buildChangeTreeNodeLocation(codeTreeNode, 0);
		System.out.println(deleteCodeChangeTreeLocations);
	}

	private Stack<Integer> locationStack = new Stack<Integer>();
	protected HashMap<List<Integer>,SourceCodeChange> deleteCodeChangeTreeLocations = new HashMap<List<Integer>,SourceCodeChange>();
	/**
	 * Add new deleteCodeChangeTreeNodeMap
	 * */
	protected HashMap<SourceCodeChange, CodeChangeTreeNode> deleteCodeChangeTreeNodeMap = new HashMap<SourceCodeChange, CodeChangeTreeNode>();

	/**
	 * add the delete list order recorder.
	 * */
	protected ArrayList<List<Integer>> deleteListOrder = new ArrayList<List<Integer>>();

	/**
	 * @param codeTreeNode
	 * @param index
	 */
	private void buildChangeTreeNodeLocation(CodeTreeNode codeTreeNode,
			int index) {
		locationStack.push(index);
		if (codeTreeNode instanceof CodeChangeTreeNode) {
			CodeChangeTreeNode codeChangeTreeNode = (CodeChangeTreeNode) codeTreeNode;
			SourceCodeChange change = codeChangeTreeNode.getSourceCodeChange();
			changeTreeNodeMaps.put(change, codeChangeTreeNode);
			if (change instanceof Delete) {
				if (!deleteCodeChangeTreeLocations.containsValue(change)) {
					List<Integer> locations = new ArrayList<Integer>();
					locations.addAll(locationStack);
					deleteListOrder.add(locations);
					deleteCodeChangeTreeLocations.put(locations,change);
					/** Add the code delete change node to the delete map. */
					deleteCodeChangeTreeNodeMap.put(change, codeChangeTreeNode);
				}
			}
		}
		List<CodeTreeNode> childrenNodes = codeTreeNode.getChildren();
		int i = 0;
		for (CodeTreeNode childNode : childrenNodes) {
			buildChangeTreeNodeLocation(childNode, i);
			i++;
		}
		locationStack.pop();
	}
}

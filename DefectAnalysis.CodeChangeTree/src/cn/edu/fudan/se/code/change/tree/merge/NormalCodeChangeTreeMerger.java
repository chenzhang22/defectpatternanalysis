/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.merge;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;

/**
 * @author Lotay
 *
 */
public class NormalCodeChangeTreeMerger extends
		IgnoreDeleteCodeChangeTreeMerger {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.fudan.se.code.change.tree.merge.ICodeChangeTreeMerger#merge(cn
	 * .edu.fudan.se.code.change.tree.bean.CodeTreeNode,
	 * cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode, java.util.List)
	 */
	@Override
	public CodeTreeNode merge(CodeTreeNode beforeCodeTree,
			CodeTreeNode afterCodeTree, List<SourceCodeChange> changes) {
		super.merge(beforeCodeTree, afterCodeTree, changes);
		// System.out.println("deleteListOrder:" + deleteListOrder);
		if ((!deleteListOrder.isEmpty())
				&& (!super.deleteCodeChangeTreeLocations.isEmpty())
				&& (!super.deleteCodeChangeTreeNodeMap.isEmpty())) {
			merge(afterCodeTree);
		}
		return afterCodeTree;
	}

	private void merge(CodeTreeNode afterCodeTreeNode) {

		// CountTree deleteNodeIndexTreeRoot = new CountTree(0);
		for (List<Integer> deleteLocation : deleteListOrder) {
			// System.out.println("deleteLocation:" + deleteLocation);
			SourceCodeChange change = this.deleteCodeChangeTreeLocations
					.get(deleteLocation);
			CodeChangeTreeNode changeTreeNode = this.changeTreeNodeMaps
					.get(change);

			// CountTree deleteNodeIndexTree = searchAndAddDeleteIndex(
			// deleteNodeIndexTreeRoot, deleteLocation);
			if (change != null && changeTreeNode != null) {
				searchAndInsertDeleteNode(
						afterCodeTreeNode.getChildren().get(
								afterCodeTreeNode.getChildren().size() - 1),
						deleteLocation, 2, changeTreeNode);
			}
			// deleteNodeIndexTree.increaseCount();
		}
	}

	private void searchAndInsertDeleteNode(CodeTreeNode afterCodeTreeNode,
			List<Integer> deleteLocation, int curLoc,
			CodeChangeTreeNode changeTreeNode) {
		CodeTreeNode codeNode = afterCodeTreeNode;
		ArrayList<CodeTreeNode> children = (ArrayList<CodeTreeNode>) codeNode.getChildren();
		// search to the

		for (; curLoc < deleteLocation.size() - 1; curLoc++) {
			children = (ArrayList<CodeTreeNode>) codeNode.getChildren();
			int index = searchNextLevelChangeNode(children, deleteLocation,
					curLoc);
			// System.out.print(index+"-->");
			if (index < children.size()) {
				codeNode = children.get(index);
			}
		}
		int index = searchNextLevelChangeNode(children, deleteLocation, curLoc);
		// System.out.print(index+"\n");
		if (index <= children.size()) {
			children.add(index, changeTreeNode);
		} else {
			System.out.println("Not found:" + deleteLocation + ",index="
					+ index);
		}

	}

	/**
	 * @param afterCodeTreeNode
	 * @param deleteNodeIndexTreeRoot
	 * @param deleteLocation
	 * @param curLoc
	 * @return
	 */
	private int searchNextLevelChangeNode(ArrayList<CodeTreeNode> children,
			List<Integer> deleteLocation, int curLoc) {
		int oldIndex = deleteLocation.get(curLoc);
		// CountTree corTree = this.searchParentDeleteIndex(
		// deleteNodeIndexTreeRoot, deleteLocation, curLoc);

		int newIndex = oldIndex;
		int count = 0;
		for (; count < newIndex && count < children.size(); count++) {
			CodeTreeNode node = children.get(count);
			if (node instanceof CodeChangeTreeNode) {
				CodeChangeTreeNode n = (CodeChangeTreeNode) node;
				SourceCodeChange change = n.getSourceCodeChange();
				if ((change instanceof Insert)) {
					newIndex++;
				}
			}
		}

		return newIndex;
	}

	/**
	 * Search the delete tree to get the number of delete for the same parent.
	 * and add the
	 * 
	 * @param deleteNodeIndexTreeRoot
	 * @param deleteLocation
	 * @return
	 */
//	private CountTree searchAndAddDeleteIndex(CountTree deleteNodeIndexTree,
//			List<Integer> deleteLocation) {
//		for (int i = 1; i < deleteLocation.size() - 1; i++) {
//			CountTree tree = deleteNodeIndexTree.findChild(deleteLocation
//					.get(i));
//			if (tree == null) {
//				tree = new CountTree(deleteLocation.get(i));
//				deleteNodeIndexTree.addChild(tree);
//			}
//			deleteNodeIndexTree = tree;
//		}
//		return deleteNodeIndexTree;
//	}

	/**
	 * Search the delete tree to get the number of delete for the same parent.
	 * 
	 * @param deleteNodeIndexTreeRoot
	 * @param deleteLocation
	 * @return
	 */
//	private CountTree searchParentDeleteIndex(CountTree deleteNodeIndexTree,
//			List<Integer> deleteLocation, int endIndex) {
//		if (endIndex < 1) {
//			return null;
//		}
//		for (int i = 1; i < endIndex; i++) {
//			CountTree tree = deleteNodeIndexTree.findChild(deleteLocation
//					.get(i));
//			deleteNodeIndexTree = tree;
//		}
//		return deleteNodeIndexTree;
//	}
}

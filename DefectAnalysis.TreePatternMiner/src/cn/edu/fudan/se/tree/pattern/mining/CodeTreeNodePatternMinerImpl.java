/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.mining;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreeNodeClone;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreePrinter;
import cn.edu.fudan.se.tree.pattern.similarility.ICodeTreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
public class CodeTreeNodePatternMinerImpl extends AbsCodeTreeNodePatternMiner {
	// The first level map is used for the CodeChangeTreeNode, and the second
	// Map is a change/unchange codeTreeNode identifier/(the root), the last
	// list store the same
	// change/unchange node list
	// occur in current codeTreeNode
	private Map<CodeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> codeCodeTreeNodeGroup = new HashMap<CodeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>>();
	private Map<CodeTreeNode, List<CodeChangeTreeNode>> treeCodeChangeNodesMap = new HashMap<CodeTreeNode, List<CodeChangeTreeNode>>();

	public CodeTreeNodePatternMinerImpl(ICodeTreeNodeSimilarity nodeSimilary) {
		super(nodeSimilary);
		codeCodeTreeNodeGroup.clear();
		treeCodeChangeNodesMap.clear();
	}

	/*
	 * mining the code tree node
	 */
	@Override
	public List<CodeTreeNode> mine(List<CodeTreeNode> codeTreeNodeList) {
		// group the code tree node into a map with same change node.
		codeCodeTreeNodeGroup.clear();
		treeCodeChangeNodesMap.clear();
		for (CodeTreeNode codeTreeNodeInstance : codeTreeNodeList) {
			treeCodeChangeNodesMap.put(codeTreeNodeInstance,
					new ArrayList<CodeChangeTreeNode>());
			groupCodeTreeNode(codeTreeNodeInstance, codeTreeNodeInstance);
		}

		Map<CodeChangeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> oneElementFreCodeComponents = mineOneCodeChangeElement();
		Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> curFreChangeNodeComponents = new HashMap<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>>();

		copyOneElementFreCodeNodeComponents2CurrentFrequentComponents(
				oneElementFreCodeComponents, curFreChangeNodeComponents);

		while (!curFreChangeNodeComponents.isEmpty()) {

			/*
			 * All the key List<CodeChangeTreeNode> in
			 * curFreChangeNodeComponents and nextFreChangeNodeComponents are
			 * cloned node.
			 */
			Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> nextFreChangeNodeComponents = new HashMap<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>>();
			/*
			 * @changeClusterComponent: cluster component
			 */
			for (List<CodeChangeTreeNode> changeClusterComponent : curFreChangeNodeComponents
					.keySet()) {
				/*
				 * The cluster instance of the changeClusterComponent *
				 * 
				 * @clusterInstances: all instances of the cluster.
				 * 
				 * Map<CodeTreeNode, List<Map.Entry<CodeTreeNode, Boolean>>>
				 * (The key is the CodeTreeNode instance, the List is the change
				 * node of the instance, whether already as a component.)
				 */
				Map<CodeTreeNode, List<CodeTreeNode>> clusterInstances = curFreChangeNodeComponents
						.get(changeClusterComponent);
				double maxFrequency = 0;
				double curFrequency = clusterInstances.size();
				// for each @codeTreeNodeInstance in clusterInstances
				for (CodeTreeNode codeTreeNodeInstance : clusterInstances
						.keySet()) {
					// for @codeTreeNodeInstance: the changeNode in cluster
					// instance, and @instanceChangeNodes are the change node in
					// @codeTreeNodeInstance
					List<CodeTreeNode> instanceChangeNodes = clusterInstances
							.get(codeTreeNodeInstance);
					for (CodeTreeNode instanceCodeTreeNode : instanceChangeNodes) {

						/*
						 * for each change node @codeChangeTreeNodeEntry<Change
						 * Node, Have been used> in clusterInstances
						 * 
						 * @instanceCodeTreeNode in oneElementFreNodeComponents
						 */
						if (instanceCodeTreeNode instanceof CodeChangeTreeNode
								&& /*
									 * check @instanceCodeChangeTreeNode not in
									 * {changeClusterComponent}
									 */
								(this.checkWhetherChangeNodeInOneFreComponent(
										oneElementFreCodeComponents,
										codeTreeNodeInstance,
										(CodeChangeTreeNode) instanceCodeTreeNode))) {
							CodeChangeTreeNode instanceCodeChangeTreeNode = (CodeChangeTreeNode) instanceCodeTreeNode;

							boolean isCodeTreeNodeInComponent = false;
							/*
							 * check whether the the @instanceCodeChangeTreeNode
							 * already as the component in the current frequent
							 * component set @changeClusterComponent
							 */
							for (CodeTreeNode entry : instanceChangeNodes) {
								if (instanceCodeChangeTreeNode == entry) {
									isCodeTreeNodeInComponent = true;
									break;
								}
							}

							if (!isCodeTreeNodeInComponent) {
								/*
								 * check the @instanceCodeChangeTreeNode has
								 * parent relationship with any nodes in
								 * {changeClusterComponent}
								 */
								CodeChangeTreeNode instParentOfFrequentNode = exploreInstanceParentOfRelation(
										changeClusterComponent,
										instanceCodeTreeNode);

								/*
								 * check the @instanceCodeChangeTreeNode has
								 * child relationship with any nodes in
								 * {changeClusterComponent}
								 */
								Map.Entry<CodeChangeTreeNode, CodeChangeTreeNode> instChildFrequentNodeEntry = exploreInstanceChildOfRelation(
										changeClusterComponent,
										instParentOfFrequentNode);
								if (instParentOfFrequentNode != null) {
									// current node as a parent node of frequent
									// node.
									// TODO:

								}
								if (instChildFrequentNodeEntry != null) {
									// Current Node as a child node of the
									// frequent node.

								}
								/*
								 * if changeClusterComponent’ =
								 * {changeClusterComponent}U{
								 * instanceCodeChangeTreeNode } is in
								 * nextFreChangeNodeComponent, continue;
								 */
								if (this.checkWhetherAlreadyInClusterComponent(
										nextFreChangeNodeComponents,
										changeClusterComponent,
										instanceCodeChangeTreeNode,
										instParentOfFrequentNode,
										instChildFrequentNodeEntry)) {
									continue;
								}

								/*
								 * calculate the frequency’ of
								 * changeClusterComponent’, frequent instances
								 * clusterFrequentInstances’ of
								 * changeClusterComponent’
								 */

							}
						}
					}
				}
				if (maxFrequency < curFrequency) {
					// TODO:add (changeClusterComponent, clusterInstances,
					// frequency) in freChangeNodeComponents
				}
			}

			// Update the curFreChangeNodeComponents for next level..
			curFreChangeNodeComponents = nextFreChangeNodeComponents;
		}

		printNodeChangeInfo(oneElementFreCodeComponents);
		return null;
	}

	/**
	 * @param oneElementFreCodeComponents
	 * @param curFreChangeNodeComponents
	 */
	private void copyOneElementFreCodeNodeComponents2CurrentFrequentComponents(
			Map<CodeChangeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> oneElementFreCodeComponents,
			Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> curFreChangeNodeComponents) {
		for (CodeChangeTreeNode codeChangeTreeNode : oneElementFreCodeComponents
				.keySet()) {
			List<CodeChangeTreeNode> oneElementCodeChangeTreeComponent = new ArrayList<CodeChangeTreeNode>();
			CodeTreeNode clonedNode = CodeTreeNodeClone
					.clone(codeChangeTreeNode);
			if (clonedNode instanceof CodeChangeTreeNode) {
				oneElementCodeChangeTreeComponent
						.add((CodeChangeTreeNode) clonedNode);
			}
			if (oneElementCodeChangeTreeComponent.isEmpty()) {
				continue;
			}
			Map<CodeTreeNode, List<CodeTreeNode>> oneElementFreCodeComponentInstances = oneElementFreCodeComponents
					.get(codeChangeTreeNode);
			Map<CodeTreeNode, List<CodeTreeNode>> copyOneElementFreCodeComponentInstances = this
					.copyOneElementFreCodeComponentInstances(oneElementFreCodeComponentInstances);
			curFreChangeNodeComponents.put(oneElementCodeChangeTreeComponent,
					copyOneElementFreCodeComponentInstances);
		}
	}

	/**
	 * @param oneElementFreCodeComponents
	 * @param curFreChangeNodeComponents
	 */
	@SuppressWarnings("unused")
	private void copyFreCodeNodeComponents2CurrentFrequentComponents(
			Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> freElementFreCodeComponents,
			Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> curFreChangeNodeComponents) {
		for (List<CodeChangeTreeNode> codeChangeTreeNode : freElementFreCodeComponents
				.keySet()) {
			List<CodeChangeTreeNode> clonedElementCodeChangeTreeComponent = new ArrayList<CodeChangeTreeNode>();
			for (CodeChangeTreeNode codeChangeTreeNode2 : codeChangeTreeNode) {
				CodeTreeNode clonedNode = CodeTreeNodeClone
						.clone(codeChangeTreeNode2);
				if (clonedNode instanceof CodeChangeTreeNode) {
					clonedElementCodeChangeTreeComponent
							.add((CodeChangeTreeNode) clonedNode);
				}
			}

			if (clonedElementCodeChangeTreeComponent.isEmpty()) {
				continue;
			}
			Map<CodeTreeNode, List<CodeTreeNode>> oneElementFreCodeComponentInstances = freElementFreCodeComponents
					.get(codeChangeTreeNode);
			Map<CodeTreeNode, List<CodeTreeNode>> copyOneElementFreCodeComponentInstances = this
					.copyOneElementFreCodeComponentInstances(oneElementFreCodeComponentInstances);
			curFreChangeNodeComponents.put(
					clonedElementCodeChangeTreeComponent,
					copyOneElementFreCodeComponentInstances);
		}
	}

	/**
	 * check whether the next generate-ing frequent component is already in
	 * nextFreChangeNodeComponents,
	 * */
	private boolean checkWhetherAlreadyInClusterComponent(
			Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> nextFreChangeNodeComponents,
			List<CodeChangeTreeNode> changeClusterComponent,
			CodeChangeTreeNode instCodeChangeTreeNode,
			CodeChangeTreeNode instParentOfFrequentNode,
			Map.Entry<CodeChangeTreeNode, CodeChangeTreeNode> instChildFrequentNodeEntry) {

		// TODO: modify the child and parent node of the @instCodeChangeTreeNode
		// with @instParentOfFrequentNode and @parentFrequentNodeEntry.

		for (List<CodeChangeTreeNode> frequentComponent : nextFreChangeNodeComponents
				.keySet()) {
			ArrayList<CodeChangeTreeNode> componentNodes = new ArrayList<CodeChangeTreeNode>(
					frequentComponent);
			// TODO:check the size for componentNodes and
			// changeClusterComponent...

			// TODO: add the code to check whether the component is the same.
			for (CodeChangeTreeNode codeChangeTreeNode : changeClusterComponent) {
				// TODO：Attention: since the treeNode tree just as the original
				// tree, this can
				// not be used.....
				this.nodeSimilarity.treeNodeSimilarity(instCodeChangeTreeNode,
						codeChangeTreeNode);

			}

		}

		return false;
	}

	// Map.Entry<CodeChangeTreeNode, CodeChangeTreeNode>: key is the frequent
	// node, value is the direct value..
	private Map.Entry<CodeChangeTreeNode, CodeChangeTreeNode> exploreInstanceChildOfRelation(
			List<CodeChangeTreeNode> changeClusterComponent,
			CodeChangeTreeNode instanceCodeTreeNode) {
		for (CodeChangeTreeNode codeChangeTreeNode : changeClusterComponent) {
			CodeTreeNode parentNode = this.findParentNodeOfInstanceNode(
					codeChangeTreeNode, instanceCodeTreeNode);
			if (parentNode != null && parentNode instanceof CodeChangeTreeNode) {
				return new AbstractMap.SimpleEntry<CodeChangeTreeNode, CodeChangeTreeNode>(
						codeChangeTreeNode, (CodeChangeTreeNode) parentNode);
			}
		}
		return null;
	}

	private CodeTreeNode findParentNodeOfInstanceNode(
			CodeTreeNode clusterCodeChangeTreeNode,
			CodeChangeTreeNode instanceCodeTreeNode) {
		for (CodeTreeNode node : clusterCodeChangeTreeNode.getChildren()) {
			if (node == instanceCodeTreeNode) {
				return clusterCodeChangeTreeNode;
			}
		}
		for (CodeTreeNode node : clusterCodeChangeTreeNode.getChildren()) {
			CodeTreeNode parentNodeOfInstanceNode = this
					.findParentNodeOfInstanceNode(node, instanceCodeTreeNode);
			if (parentNodeOfInstanceNode != null) {
				return parentNodeOfInstanceNode;
			}
		}
		return null;
	}

	private Map<CodeChangeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> mineOneCodeChangeElement() {
		Map<CodeChangeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> freOneCodeTreeNodeFrequentComponent = new HashMap<CodeChangeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>>();
		for (CodeTreeNode codeTreeNode : codeCodeTreeNodeGroup.keySet()) {
			if (codeTreeNode instanceof CodeChangeTreeNode) {
				Map<CodeTreeNode, List<CodeTreeNode>> codeTrees = codeCodeTreeNodeGroup
						.get(codeTreeNode);
				while (true) {
					CodeChangeTreeNode frequentNode = null;
					Map<CodeTreeNode, List<CodeTreeNode>> frequentInstances = new HashMap<CodeTreeNode, List<CodeTreeNode>>();
					for (CodeTreeNode changeNodeTreeInstance : codeTrees
							.keySet()) {
						List<CodeTreeNode> instanceCodeChangeNodes = codeTrees
								.get(changeNodeTreeInstance);
						if (!instanceCodeChangeNodes.isEmpty()) {
							CodeTreeNode instanceFreChangeNode = instanceCodeChangeNodes
									.remove(0);
							if (instanceFreChangeNode instanceof CodeChangeTreeNode) {
								if (frequentNode == null) {
									frequentNode = ((CodeChangeTreeNode) instanceFreChangeNode);
								}
								ArrayList<CodeTreeNode> arrayList = new ArrayList<CodeTreeNode>();
								arrayList.add(instanceFreChangeNode);
								frequentInstances.put(changeNodeTreeInstance,
										arrayList);
							}
						}
					}

					boolean meetThrehold = frequentInstances.size() >= minFrequencyThredhold;
					if (meetThrehold) {
						freOneCodeTreeNodeFrequentComponent.put(frequentNode,
								frequentInstances);
					} else {
						break;
					}
				}
			}
		}
		return freOneCodeTreeNodeFrequentComponent;
	}

	private Map<CodeTreeNode, List<CodeTreeNode>> copyOneElementFreCodeComponentInstances(
			Map<CodeTreeNode, List<CodeTreeNode>> oneElementFreCodeComponentInstances) {
		Map<CodeTreeNode, List<CodeTreeNode>> copyFrequentInstances = new HashMap<CodeTreeNode, List<CodeTreeNode>>();
		for (CodeTreeNode codeTreeNode : oneElementFreCodeComponentInstances
				.keySet()) {
			List<CodeTreeNode> copyFrequentNodesForInstance = new ArrayList<CodeTreeNode>();
			copyFrequentInstances.put(codeTreeNode,
					copyFrequentNodesForInstance);
			for (CodeTreeNode instanceNodeEntry : oneElementFreCodeComponentInstances
					.get(codeTreeNode)) {
				copyFrequentNodesForInstance.add(instanceNodeEntry);
			}
		}
		return copyFrequentInstances;
	}

	private CodeChangeTreeNode exploreInstanceParentOfRelation(
			List<CodeChangeTreeNode> changeClusterComponent,
			CodeTreeNode instanceCodeTreeNode) {
		for (CodeChangeTreeNode frequentChangeTreeNode : changeClusterComponent) {
			if (this.exploreParentRelation(instanceCodeTreeNode,
					frequentChangeTreeNode)) {
				// The current analysis node is the parent node of the frequent
				// node.
				return frequentChangeTreeNode;
			}
		}
		return null;
	}

	private boolean exploreParentRelation(CodeTreeNode potentialChildNode,
			CodeTreeNode potentialParentNode) {
		if (potentialChildNode.getParentTreeNode() != null
				&& potentialChildNode.getParentTreeNode() == potentialParentNode) {
			return true;
		}
		return false;
	}

	/**
	 * Chech whether the @param codeChangeTreeNode in the tree( @param
	 * codeTreeNodeInstance ) is in the @param oneElementFreCodeComponents
	 * */
	private boolean checkWhetherChangeNodeInOneFreComponent(
			Map<CodeChangeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> oneElementFreCodeComponents,
			CodeTreeNode codeTreeNodeInstance,
			CodeChangeTreeNode codeChangeTreeNode) {
		for (CodeChangeTreeNode freCodeChangeTreeNodes : oneElementFreCodeComponents
				.keySet()) {
			if (this.nodeSimilarity.similarity(codeChangeTreeNode,
					(CodeTreeNode) freCodeChangeTreeNodes) >= this.minSimilarityThrehold) {
				Map<CodeTreeNode, List<CodeTreeNode>> freInstances = oneElementFreCodeComponents
						.get(freCodeChangeTreeNodes);
				if (freInstances != null && !freInstances.isEmpty()) {
					List<CodeTreeNode> instanceFrequencyNodes = freInstances
							.get(codeTreeNodeInstance);
					if (instanceFrequencyNodes != null
							&& !instanceFrequencyNodes.isEmpty()) {
						if (instanceFrequencyNodes.contains(codeChangeTreeNode)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/*
	 * */
	/**
	 * @param rootNode
	 *            ： the root of the current node..
	 * @param codeTreeNode
	 *            : the current visited node.
	 */
	private void groupCodeTreeNode(CodeTreeNode rootNode,
			CodeTreeNode codeTreeNode) {
		// store the change treenode of each tree...
		if (codeTreeNode instanceof CodeChangeTreeNode) {
			treeCodeChangeNodesMap.get(rootNode).add(
					(CodeChangeTreeNode) codeTreeNode);
		}

		Set<CodeTreeNode> codeTreeNodes = codeCodeTreeNodeGroup.keySet();
		CodeTreeNode groupHeadNode = codeTreeNode;
		// search whether the codeTreeNode has the same node in the group.
		for (CodeTreeNode groupTreeNode : codeTreeNodes) {
			if (this.nodeSimilarity.similarity(codeTreeNode, groupTreeNode) == 1) {
				groupHeadNode = groupTreeNode;
				break;
			}
		}
		Map<CodeTreeNode, List<CodeTreeNode>> groupNodesMap = this.codeCodeTreeNodeGroup
				.get(groupHeadNode);
		if (groupNodesMap == null) {
			groupNodesMap = new HashMap<CodeTreeNode, List<CodeTreeNode>>();
			this.codeCodeTreeNodeGroup.put(groupHeadNode, groupNodesMap);
		}

		List<CodeTreeNode> groupNodeTreeInstanceList = groupNodesMap
				.get(rootNode);
		if (groupNodeTreeInstanceList == null) {
			groupNodeTreeInstanceList = new ArrayList<CodeTreeNode>();
			groupNodesMap.put(rootNode, groupNodeTreeInstanceList);
		}
		groupNodeTreeInstanceList.add(codeTreeNode);

		for (CodeTreeNode childTreeNode : codeTreeNode.getChildren()) {
			this.groupCodeTreeNode(rootNode, childTreeNode);
		}
	}

	/**
	 * @param freECdeCodeTreeNodeGroup
	 */
	private void printNodeChangeInfo(
			Map<CodeChangeTreeNode, Map<CodeTreeNode, List<CodeTreeNode>>> freECdeCodeTreeNodeGroup) {
		for (Map<CodeTreeNode, List<CodeTreeNode>> codeTreeNode : freECdeCodeTreeNodeGroup
				.values()) {
			if (codeTreeNode.size() > 1) {
				for (List<CodeTreeNode> codeTreeNode1 : codeTreeNode.values()) {
					for (int i = 0; i < codeTreeNode1.size(); i++) {
						System.out.print(codeTreeNode1.get(i).getType() + "\t");
					}
					System.out.println();
				}
			}
		}
	}

}

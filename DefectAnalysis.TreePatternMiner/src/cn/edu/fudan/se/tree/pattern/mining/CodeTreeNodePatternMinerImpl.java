/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.mining;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.fudan.se.code.change.tree.bean.CodeChangeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.CodeTreeNode;
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.utils.CodeTreeNodeClone;
import cn.edu.fudan.se.tree.pattern.match.ImplGroupPatternInstanceMatching;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

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

	public CodeTreeNodePatternMinerImpl(ITreeNodeSimilarity nodeSimilary) {
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

		List<CodeTreeNode> frequentComponents = new ArrayList<CodeTreeNode>();

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
					List<CodeTreeNode> instanceFreChangeNodes = clusterInstances
							.get(codeTreeNodeInstance);
					/**
					 * The changeNode of the instance
					 * */
					List<CodeChangeTreeNode> instanceChangeNodes = this.treeCodeChangeNodesMap
							.get(codeTreeNodeInstance);
					for (CodeChangeTreeNode instanceCodeChangeTreeNode : instanceChangeNodes) {

						/*
						 * for each change node @codeChangeTreeNodeEntry<Change
						 * Node, Have been used> in clusterInstances
						 * 
						 * @instanceCodeTreeNode in oneElementFreNodeComponents
						 */
						if (/*
							 * check @instanceCodeChangeTreeNode not in
							 * {changeClusterComponent}
							 */
						this.checkWhetherChangeNodeInFreComponent(instanceCodeChangeTreeNode)) {
							boolean isCodeTreeNodeInComponent = false;
							/*
							 * check whether the the @instanceCodeChangeTreeNode
							 * already as the component in the current frequent
							 * component set @changeClusterComponent
							 */
							for (CodeTreeNode entry : instanceFreChangeNodes) {
								if (instanceCodeChangeTreeNode == entry) {
									isCodeTreeNodeInComponent = true;
									break;
								}
							}

							if (!isCodeTreeNodeInComponent) {
								Map.Entry<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> newFrequentComponents = this
										.copyFrequentComponent(
												changeClusterComponent,
												clusterInstances);
								/*
								 * check the @instanceCodeChangeTreeNode has
								 * parent relationship with any nodes in
								 * {changeClusterComponent} The parent of the
								 * frequent node @instParentOfFrequentNode list
								 * is instanceCodeTreeNode.
								 */
								List<CodeChangeTreeNode> instParentOfFrequentNode = exploreInstanceParentOfFrequentNodes(
										changeClusterComponent,
										instanceCodeChangeTreeNode);
								// if (instParentOfFrequentNode != null
								// && !instParentOfFrequentNode.isEmpty()) {
								// // current node as a parent node of frequent
								// // node.
								//
								// System.out.println();
								// }
								/*
								 * check the @instanceCodeChangeTreeNode has
								 * child relationship with any nodes in
								 * {changeClusterComponent} The
								 * instanceCodeTreeNode node is a child of the
								 * Map.Entry<CodeChangeTreeNode,
								 * CodeChangeTreeNode>. where The key the
								 * frequent component, and the value is the
								 * direct parent of instanceCodeTreeNode
								 */
								Map.Entry<CodeChangeTreeNode, CodeChangeTreeNode> instChildFrequentNodeEntry = exploreInstanceChildOfRelation(
										changeClusterComponent,
										instanceCodeChangeTreeNode);
								//
								// if (instChildFrequentNodeEntry != null) {
								// // Current Node as a child node of the
								// // frequent node.
								// System.out.println();
								// }

								/*
								 * Build the new frequent components based on
								 * the relationship between the
								 * instanceCodeChangeTreeNode and component's
								 * nodes.
								 */
								this.buildNewCurrentComponents(
										newFrequentComponents,
										instanceCodeChangeTreeNode,
										instParentOfFrequentNode,
										instChildFrequentNodeEntry);

								/*
								 * if changeClusterComponent’ =
								 * {changeClusterComponent}U{
								 * instanceCodeChangeTreeNode } is in
								 * nextFreChangeNodeComponent, continue;
								 */
								if (this.checkWhetherAlreadyInClusterComponent(
										nextFreChangeNodeComponents,
										newFrequentComponents)) {
									continue;
								}

								/**
								 * TODO:calculate the frequency’ of
								 * 
								 * @newFrequentComponents: frequent instances
								 *                         clusterFrequentInstances
								 *                         ’ of
								 *                         changeClusterComponent
								 *                         ’
								 */

								filterTheChangeNodeNotMeetNewFrequentComponent(newFrequentComponents);

								/**
								 * TODO: check whether the frequency' of
								 * 
								 * @newFrequentComponents: is larger than
								 * @minFrequencyThredhold, then add @newFrequentComponents
								 *                         into the @nextFreChangeNodeComponents
								 * */

							}
						}
					}
				}
				if (maxFrequency < curFrequency) {
					// TODO:add (changeClusterComponent, clusterInstances,
					// frequency) in freChangeNodeComponents
				}
			}
			printFrequentComponents(curFreChangeNodeComponents);

			// Update the curFreChangeNodeComponents for next level..
			curFreChangeNodeComponents = nextFreChangeNodeComponents;
		}

		printNodeChangeInfo(oneElementFreCodeComponents);
		return frequentComponents;
	}

	private void filterTheChangeNodeNotMeetNewFrequentComponent(
			Entry<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> newFrequentComponents) {
		Map<CodeTreeNode, List<CodeTreeNode>> patternInstances = newFrequentComponents
				.getValue();
		List<CodeChangeTreeNode> frequentComponent = newFrequentComponents
				.getKey();
		List<TreeNode> groupPatterns = new ArrayList<TreeNode>(frequentComponent);
		ImplGroupPatternInstanceMatching groupPatternMatching = new ImplGroupPatternInstanceMatching(groupPatterns);
		for (CodeTreeNode patternInstance : patternInstances.keySet()) {
			Map<TreeNode, Map<TreeNode, TreeNode>> patternMatchedNodes = groupPatternMatching.patternMatchAll(patternInstance);
			if (patternMatchedNodes.size()==frequentComponent.size()) {
				patternInstances.remove(patternInstance);
			}
		}
	}

	/**
	 * @param newFrequentComponents
	 *            : the new frequent components to be updated..
	 * @param instanceCodeChangeTreeNode
	 *            : the instance code change node
	 * @param instanceParentOfFrequentNode
	 *            : the instanceCodeChangeTreeNode is the parent node of
	 *            instParentOfFrequentNode
	 * @param instanceChildFrequentNodeEntry
	 *            : the instanceCodeChangeTreeNode is a child node of
	 *            instChildFrequentNodeEntry(key: component, value: direct
	 *            parent node)
	 */
	private void buildNewCurrentComponents(
			Map.Entry<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> newFrequentComponents,
			CodeChangeTreeNode instanceCodeChangeTreeNode,
			List<CodeChangeTreeNode> instanceParentOfFrequentNode,
			Map.Entry<CodeChangeTreeNode, CodeChangeTreeNode> instanceChildFrequentNodeEntry) {
		List<CodeChangeTreeNode> frequentComponents = newFrequentComponents
				.getKey();
		boolean alreadyAdd2NewFreComponents = false;
		if (instanceParentOfFrequentNode != null
				&& !instanceParentOfFrequentNode.isEmpty()) {
			/*
			 * Remove the child node from the @frequentComponents. And add the
			 * corresponding child node @codeChangeTreeNode as child of
			 * instanceCodeChangeTreeNode
			 */
			for (CodeChangeTreeNode codeChangeTreeNode : instanceParentOfFrequentNode) {
				frequentComponents.remove(codeChangeTreeNode);
				instanceCodeChangeTreeNode.addChild(codeChangeTreeNode);
			}
			frequentComponents.add(instanceCodeChangeTreeNode);
			alreadyAdd2NewFreComponents = true;
		}

		/**
		 * the node @instanceCodeChangeTreeNode as a child of
		 * 
		 * @instanceChildFrequentNodeEntry
		 * */
		if (instanceChildFrequentNodeEntry != null) {
			CodeChangeTreeNode directParentChangeTreeNode = instanceChildFrequentNodeEntry
					.getValue();
			directParentChangeTreeNode.addChild(instanceCodeChangeTreeNode);
			alreadyAdd2NewFreComponents = true;
		}

		// If there are no child/parnet relationship, then add into the
		// frequentComponents directly.
		if (!alreadyAdd2NewFreComponents) {
			frequentComponents.add(instanceCodeChangeTreeNode);
		}
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
	 * check whether the next generating frequent component is already in
	 * nextFreChangeNodeComponents,
	 * */
	private boolean checkWhetherAlreadyInClusterComponent(
			Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> nextFreChangeNodeComponents,
			Entry<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> newFrequentComponentsEntry) {
		List<CodeChangeTreeNode> newFrequentComponents = newFrequentComponentsEntry
				.getKey();
		for (List<CodeChangeTreeNode> frequentComponent : nextFreChangeNodeComponents
				.keySet()) {
			ArrayList<CodeChangeTreeNode> nextFrequentComponents = new ArrayList<CodeChangeTreeNode>(
					frequentComponent);

			/**
			 * check the size for @newFrequentComponents and
			 * 
			 * @nextFrequentComponents, this is the basic requirement for the @nextFreChangeNodeComponents
			 *                          and @nextFrequentComponents be equal.
			 */
			if (nextFrequentComponents.size() != newFrequentComponents.size()) {
				continue;
			}

			/**
			 * the code used to check whether the @newFrequentComponents has the
			 * same component in @nextFreChangeNodeComponents
			 */
			for (CodeChangeTreeNode newFreComponentNode : newFrequentComponents) {
				CodeChangeTreeNode similarNextChangeTreeNode = null;
				for (CodeChangeTreeNode nextFreChangeTreeNode : nextFrequentComponents) {
					if (this.nodeSimilarity.treeNodeSimilarity(
							nextFreChangeTreeNode, newFreComponentNode) >= this.minSimilarityThrehold) {
						similarNextChangeTreeNode = nextFreChangeTreeNode;
						break;
					}
				}
				if (similarNextChangeTreeNode != null) {
					nextFrequentComponents.remove(similarNextChangeTreeNode);
				} else {
					break;
				}
			}
			if (nextFrequentComponents.isEmpty()) {
				return true;
			}
		}

		return false;
	}

	private Map.Entry<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> copyFrequentComponent(
			List<CodeChangeTreeNode> freComponents,
			Map<CodeTreeNode, List<CodeTreeNode>> frequentInstances) {

		List<CodeChangeTreeNode> copyComponents = new ArrayList<CodeChangeTreeNode>();
		for (CodeChangeTreeNode codeChangeTreeNode : freComponents) {
			CodeTreeNode cloneNoChildren = CodeTreeNodeClone
					.cloneNoChildren(codeChangeTreeNode);
			if (cloneNoChildren instanceof CodeChangeTreeNode) {
				copyComponents.add((CodeChangeTreeNode) cloneNoChildren);
			}
		}

		Map<CodeTreeNode, List<CodeTreeNode>> componentInstances = new HashMap<CodeTreeNode, List<CodeTreeNode>>();
		for (CodeTreeNode codeTreeNode : frequentInstances.keySet()) {
			componentInstances.put(codeTreeNode, new ArrayList<CodeTreeNode>(
					frequentInstances.get(codeTreeNode)));
		}

		Map.Entry<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> copyComponentsEntry = new AbstractMap.SimpleEntry<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>>(
				copyComponents, componentInstances);
		return copyComponentsEntry;
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

	// The current analysis node is the parent node of the frequent
	// node.
	private List<CodeChangeTreeNode> exploreInstanceParentOfFrequentNodes(
			List<CodeChangeTreeNode> changeClusterComponent,
			CodeTreeNode instanceCodeTreeNode) {
		List<CodeChangeTreeNode> childrenNodes = new ArrayList<CodeChangeTreeNode>();
		for (CodeChangeTreeNode frequentChangeTreeNode : changeClusterComponent) {
			if (this.exploreParentRelation(frequentChangeTreeNode,
					instanceCodeTreeNode)) {
				childrenNodes.add(frequentChangeTreeNode);
			}
		}
		return childrenNodes;
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
	private boolean checkWhetherChangeNodeInFreComponent(
			CodeChangeTreeNode codeChangeTreeNode) {
		for (CodeTreeNode codeTreeNode : this.codeCodeTreeNodeGroup.keySet()) {
			if (codeTreeNode instanceof CodeChangeTreeNode) {
				if (super.nodeSimilarity.similarity(codeChangeTreeNode,
						codeTreeNode) >= this.minSimilarityThrehold) {
					Map<CodeTreeNode, List<CodeTreeNode>> instances = this.codeCodeTreeNodeGroup
							.get(codeTreeNode);
					if (instances != null
							&& instances.size() >= this.minFrequencyThredhold) {
						return true;
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

	/**
	 * @param freECdeCodeTreeNodeGroup
	 */
	private void printFrequentComponents(
			Map<List<CodeChangeTreeNode>, Map<CodeTreeNode, List<CodeTreeNode>>> freECdeCodeTreeNodeGroup) {
		for (List<CodeChangeTreeNode> keyChangeTreeNodes : freECdeCodeTreeNodeGroup
				.keySet()) {
			Map<CodeTreeNode, List<CodeTreeNode>> codeTreeNode = freECdeCodeTreeNodeGroup
					.get(keyChangeTreeNodes);
			for (CodeChangeTreeNode codeChangeTreeNode : keyChangeTreeNodes) {
				System.out.println(codeChangeTreeNode.getPreSimpleType() + ":"
						+ codeChangeTreeNode.getSimpleType());
			}
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

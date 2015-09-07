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
import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.tree.pattern.match.ImplGroupPatternInstanceMatching;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;

/**
 * @author Lotay
 *
 */
@SuppressWarnings("unchecked")
public class CodeTreeNodePatternMinerImpl extends AbsTreeNodePatternMiner {
	// The first level map is used for the CodeChangeTreeNode, and the second
	// Map is a change/unchange codeTreeNode identifier/(the root), the last
	// list store the same
	// change/unchange node list
	// occur in current codeTreeNode
	private Map<TreeNode, Map<TreeNode, List<TreeNode>>> codeCodeTreeNodeGroup = new HashMap<TreeNode, Map<TreeNode, List<TreeNode>>>();
	private Map<TreeNode, List<TreeNode>> treeCodeChangeNodesMap = new HashMap<TreeNode, List<TreeNode>>();

	public CodeTreeNodePatternMinerImpl(ITreeNodeSimilarity nodeSimilary) {
		super(nodeSimilary);
		codeCodeTreeNodeGroup.clear();
		treeCodeChangeNodesMap.clear();
	}

	/*
	 * mining the code tree node
	 */
	@Override
	public Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> mine(
			List<TreeNode> codeTreeNodeList) {
		// group the code tree node into a map with same change node.
		codeCodeTreeNodeGroup.clear();
		treeCodeChangeNodesMap.clear();
		for (TreeNode codeTreeNodeInstance : codeTreeNodeList) {
			treeCodeChangeNodesMap.put(codeTreeNodeInstance,
					new ArrayList<TreeNode>());
			groupCodeTreeNode(codeTreeNodeInstance, codeTreeNodeInstance);
		}

		List<TreeNode> frequentComponents = new ArrayList<TreeNode>();

		Map<TreeNode, Map<TreeNode, List<TreeNode>>> oneElementFreCodeComponents = mineOneCodeChangeElement();
		Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> curFreChangeNodeComponents = new HashMap<List<TreeNode>, Map<TreeNode, List<TreeNode>>>();

		Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> minedFrequentChangeNodeComponents = new HashMap<List<TreeNode>, Map<TreeNode, List<TreeNode>>>();

		copyOneElementFreCodeNodeComponents2CurrentFrequentComponents(
				oneElementFreCodeComponents, curFreChangeNodeComponents);

		while (!curFreChangeNodeComponents.isEmpty()) {

			/*
			 * All the key List<CodeChangeTreeNode> in
			 * curFreChangeNodeComponents and nextFreChangeNodeComponents are
			 * cloned node.
			 */
			Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> nextFreChangeNodeComponents = new HashMap<List<TreeNode>, Map<TreeNode, List<TreeNode>>>();
			/*
			 * @changeClusterComponent: cluster component
			 */
			for (List<TreeNode> changeClusterComponent : curFreChangeNodeComponents
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
				Map<TreeNode, List<TreeNode>> clusterInstances = curFreChangeNodeComponents
						.get(changeClusterComponent);
				double nextMaxFrequency = 0;
				double curFrequency = clusterInstances.size();
				// for each @codeTreeNodeInstance in clusterInstances
				for (TreeNode codeTreeNodeInstance : clusterInstances.keySet()) {
					// for @codeTreeNodeInstance: the changeNode in cluster
					// instance, and @instanceChangeNodes are the change node in
					// @codeTreeNodeInstance
					List<TreeNode> instanceFreChangeNodes = clusterInstances
							.get(codeTreeNodeInstance);
					/**
					 * The changeNode of the instance
					 * */
					List<TreeNode> instanceChangeNodes = this.treeCodeChangeNodesMap
							.get(codeTreeNodeInstance);
					for (TreeNode instanceCodeChangeTreeNode : instanceChangeNodes) {

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
							for (TreeNode entry : instanceFreChangeNodes) {
								if (instanceCodeChangeTreeNode == entry) {
									isCodeTreeNodeInComponent = true;
									break;
								}
							}

							if (!isCodeTreeNodeInComponent) {
								Map.Entry<List<TreeNode>, Map<TreeNode, List<TreeNode>>> newFrequentComponents = this
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
								List<TreeNode> instParentOfFrequentNode = exploreInstanceParentOfFrequentNodes(
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
								Map.Entry<TreeNode, TreeNode> instChildFrequentNodeEntry = exploreInstanceChildOfRelation(
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
								int newFrequentSize = newFrequentComponents
										.getValue().size();
								if (newFrequentSize >= this.minFrequencyThredhold) {
									nextFreChangeNodeComponents.put(
											newFrequentComponents.getKey(),
											newFrequentComponents.getValue());
								}

								/**
								 * update the nextMaxFrequency value.
								 * */
								if (newFrequentSize > nextMaxFrequency) {
									nextMaxFrequency = newFrequentSize;
								}
							}
						}
					}
				}
				if (nextMaxFrequency < curFrequency) {
					// TODO:add (changeClusterComponent, clusterInstances,
					// frequency) in freChangeNodeComponents
					minedFrequentChangeNodeComponents.put(
							changeClusterComponent, clusterInstances);
				}
			}
			printFrequentComponents(curFreChangeNodeComponents);

			// Update the curFreChangeNodeComponents for next level..
			curFreChangeNodeComponents = nextFreChangeNodeComponents;
		}

		printNodeChangeInfo(oneElementFreCodeComponents);
		return minedFrequentChangeNodeComponents;
	}

	private void filterTheChangeNodeNotMeetNewFrequentComponent(
			Entry<List<TreeNode>, Map<TreeNode, List<TreeNode>>> newFrequentComponents) {
		Map<TreeNode, List<TreeNode>> patternInstances = newFrequentComponents
				.getValue();
		List<TreeNode> frequentComponent = newFrequentComponents.getKey();
		List<TreeNode> groupPatterns = new ArrayList<TreeNode>(
				frequentComponent);
		ImplGroupPatternInstanceMatching groupPatternMatching = new ImplGroupPatternInstanceMatching(
				groupPatterns);
		List<TreeNode> filtedInstances = new ArrayList<TreeNode>();
		for (TreeNode patternInstance : patternInstances.keySet()) {
			Map<TreeNode, Map<TreeNode, TreeNode>> patternMatchedNodes = groupPatternMatching
					.patternMatchAll(patternInstance);
			if (patternMatchedNodes.size() != frequentComponent.size()) {
				filtedInstances.add(patternInstance);
			}
		}

		for (TreeNode filtedInstance : filtedInstances) {
			patternInstances.remove(filtedInstance);
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
			Map.Entry<List<TreeNode>, Map<TreeNode, List<TreeNode>>> newFrequentComponents,
			TreeNode instanceCodeChangeTreeNode,
			List<TreeNode> instanceParentOfFrequentNode,
			Map.Entry<TreeNode, TreeNode> instanceChildFrequentNodeEntry) {
		List<TreeNode> frequentComponents = newFrequentComponents.getKey();
		boolean alreadyAdd2NewFreComponents = false;
		if (instanceParentOfFrequentNode != null
				&& !instanceParentOfFrequentNode.isEmpty()) {
			/*
			 * Remove the child node from the @frequentComponents. And add the
			 * corresponding child node @codeChangeTreeNode as child of
			 * instanceCodeChangeTreeNode
			 */
			for (TreeNode codeChangeTreeNode : instanceParentOfFrequentNode) {
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
			TreeNode directParentChangeTreeNode = instanceChildFrequentNodeEntry
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
			Map<TreeNode, Map<TreeNode, List<TreeNode>>> oneElementFreCodeComponents,
			Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> curFreChangeNodeComponents) {
		for (TreeNode codeChangeTreeNode : oneElementFreCodeComponents.keySet()) {
			List<TreeNode> oneElementCodeChangeTreeComponent = new ArrayList<TreeNode>();
			TreeNode clonedNode = treeNodeClone.clone(codeChangeTreeNode);
			if (clonedNode instanceof CodeChangeTreeNode) {
				oneElementCodeChangeTreeComponent
						.add((CodeChangeTreeNode) clonedNode);
			}
			if (oneElementCodeChangeTreeComponent.isEmpty()) {
				continue;
			}
			Map<TreeNode, List<TreeNode>> oneElementFreCodeComponentInstances = oneElementFreCodeComponents
					.get(codeChangeTreeNode);
			Map<TreeNode, List<TreeNode>> copyOneElementFreCodeComponentInstances = this
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
			Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> freElementFreCodeComponents,
			Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> curFreChangeNodeComponents) {
		for (List<TreeNode> codeChangeTreeNode : freElementFreCodeComponents
				.keySet()) {
			List<TreeNode> clonedElementCodeChangeTreeComponent = new ArrayList<TreeNode>();
			for (TreeNode codeChangeTreeNode2 : codeChangeTreeNode) {
				TreeNode clonedNode = treeNodeClone.clone(codeChangeTreeNode2);
				if (clonedNode instanceof CodeChangeTreeNode) {
					clonedElementCodeChangeTreeComponent
							.add((CodeChangeTreeNode) clonedNode);
				}
			}

			if (clonedElementCodeChangeTreeComponent.isEmpty()) {
				continue;
			}
			Map<TreeNode, List<TreeNode>> oneElementFreCodeComponentInstances = freElementFreCodeComponents
					.get(codeChangeTreeNode);
			Map<TreeNode, List<TreeNode>> copyOneElementFreCodeComponentInstances = this
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
			Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> nextFreChangeNodeComponents,
			Entry<List<TreeNode>, Map<TreeNode, List<TreeNode>>> newFrequentComponentsEntry) {
		List<TreeNode> newFrequentComponents = newFrequentComponentsEntry
				.getKey();
		for (List<TreeNode> frequentComponent : nextFreChangeNodeComponents
				.keySet()) {
			ArrayList<TreeNode> nextFrequentComponents = new ArrayList<TreeNode>(
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
			for (TreeNode newFreComponentNode : newFrequentComponents) {
				TreeNode similarNextChangeTreeNode = null;
				for (TreeNode nextFreChangeTreeNode : nextFrequentComponents) {
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

	private Map.Entry<List<TreeNode>, Map<TreeNode, List<TreeNode>>> copyFrequentComponent(
			List<TreeNode> freComponents,
			Map<TreeNode, List<TreeNode>> frequentInstances) {

		List<TreeNode> copyComponents = new ArrayList<TreeNode>();
		for (TreeNode codeChangeTreeNode : freComponents) {
			TreeNode cloneNoChildren = super.treeNodeClone
					.cloneNoChildren(codeChangeTreeNode);
			if (cloneNoChildren instanceof CodeChangeTreeNode) {
				copyComponents.add((CodeChangeTreeNode) cloneNoChildren);
			} else {
				System.err.println("Clone Error.");
				System.exit(0);
			}
		}

		Map<TreeNode, List<TreeNode>> componentInstances = new HashMap<TreeNode, List<TreeNode>>();
		for (TreeNode codeTreeNode : frequentInstances.keySet()) {
			componentInstances.put(codeTreeNode, new ArrayList<TreeNode>(
					frequentInstances.get(codeTreeNode)));
		}

		Map.Entry<List<TreeNode>, Map<TreeNode, List<TreeNode>>> copyComponentsEntry = new AbstractMap.SimpleEntry<List<TreeNode>, Map<TreeNode, List<TreeNode>>>(
				copyComponents, componentInstances);
		return copyComponentsEntry;
	}

	// Map.Entry<CodeChangeTreeNode, CodeChangeTreeNode>: key is the frequent
	// node, value is the direct value..
	private Map.Entry<TreeNode, TreeNode> exploreInstanceChildOfRelation(
			List<TreeNode> changeClusterComponent, TreeNode instanceCodeTreeNode) {
		for (TreeNode codeChangeTreeNode : changeClusterComponent) {
			TreeNode parentNode = this.findParentNodeOfInstanceNode(
					codeChangeTreeNode, instanceCodeTreeNode);
			if (parentNode != null && parentNode instanceof CodeChangeTreeNode) {
				return new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
						codeChangeTreeNode, (CodeChangeTreeNode) parentNode);
			}
		}
		return null;
	}

	private TreeNode findParentNodeOfInstanceNode(
			TreeNode clusterCodeChangeTreeNode, TreeNode instanceCodeTreeNode) {
		for (TreeNode node : (List<TreeNode>) clusterCodeChangeTreeNode
				.getChildren()) {
			if (node == instanceCodeTreeNode) {
				return clusterCodeChangeTreeNode;
			}
		}
		for (TreeNode node : (List<TreeNode>) clusterCodeChangeTreeNode
				.getChildren()) {
			TreeNode parentNodeOfInstanceNode = this
					.findParentNodeOfInstanceNode(node, instanceCodeTreeNode);
			if (parentNodeOfInstanceNode != null) {
				return parentNodeOfInstanceNode;
			}
		}
		return null;
	}

	private Map<TreeNode, Map<TreeNode, List<TreeNode>>> mineOneCodeChangeElement() {
		Map<TreeNode, Map<TreeNode, List<TreeNode>>> freOneCodeTreeNodeFrequentComponent = new HashMap<TreeNode, Map<TreeNode, List<TreeNode>>>();
		for (TreeNode codeTreeNode : codeCodeTreeNodeGroup.keySet()) {
			if (codeTreeNode instanceof CodeChangeTreeNode) {
				Map<TreeNode, List<TreeNode>> codeTrees = codeCodeTreeNodeGroup
						.get(codeTreeNode);
				while (true) {
					TreeNode frequentNode = null;
					Map<TreeNode, List<TreeNode>> frequentInstances = new HashMap<TreeNode, List<TreeNode>>();
					for (TreeNode changeNodeTreeInstance : codeTrees.keySet()) {
						List<TreeNode> instanceCodeChangeNodes = codeTrees
								.get(changeNodeTreeInstance);
						if (!instanceCodeChangeNodes.isEmpty()) {
							TreeNode instanceFreChangeNode = instanceCodeChangeNodes
									.remove(0);
							if (instanceFreChangeNode instanceof CodeChangeTreeNode) {
								if (frequentNode == null) {
									frequentNode = instanceFreChangeNode;
								}
								ArrayList<TreeNode> arrayList = new ArrayList<TreeNode>();
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

	private Map<TreeNode, List<TreeNode>> copyOneElementFreCodeComponentInstances(
			Map<TreeNode, List<TreeNode>> oneElementFreCodeComponentInstances) {
		Map<TreeNode, List<TreeNode>> copyFrequentInstances = new HashMap<TreeNode, List<TreeNode>>();
		for (TreeNode codeTreeNode : oneElementFreCodeComponentInstances
				.keySet()) {
			List<TreeNode> copyFrequentNodesForInstance = new ArrayList<TreeNode>();
			copyFrequentInstances.put(codeTreeNode,
					copyFrequentNodesForInstance);
			for (TreeNode instanceNodeEntry : oneElementFreCodeComponentInstances
					.get(codeTreeNode)) {
				copyFrequentNodesForInstance.add(instanceNodeEntry);
			}
		}
		return copyFrequentInstances;
	}

	// The current analysis node is the parent node of the frequent
	// node.
	private List<TreeNode> exploreInstanceParentOfFrequentNodes(
			List<TreeNode> changeClusterComponent, TreeNode instanceCodeTreeNode) {
		List<TreeNode> childrenNodes = new ArrayList<TreeNode>();
		for (TreeNode frequentChangeTreeNode : changeClusterComponent) {
			if (this.exploreParentRelation(frequentChangeTreeNode,
					instanceCodeTreeNode)) {
				childrenNodes.add(frequentChangeTreeNode);
			}
		}
		return childrenNodes;
	}

	private boolean exploreParentRelation(TreeNode potentialChildNode,
			TreeNode potentialParentNode) {
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
			TreeNode codeChangeTreeNode) {
		for (TreeNode codeTreeNode : this.codeCodeTreeNodeGroup.keySet()) {
			if (codeTreeNode instanceof CodeChangeTreeNode) {
				if (super.nodeSimilarity.similarity(codeChangeTreeNode,
						codeTreeNode) >= this.minSimilarityThrehold) {
					Map<TreeNode, List<TreeNode>> instances = this.codeCodeTreeNodeGroup
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
	private void groupCodeTreeNode(TreeNode rootNode, TreeNode codeTreeNode) {
		// store the change treenode of each tree...
		if (codeTreeNode instanceof CodeChangeTreeNode) {
			treeCodeChangeNodesMap.get(rootNode).add(
					(CodeChangeTreeNode) codeTreeNode);
		}

		Set<TreeNode> codeTreeNodes = codeCodeTreeNodeGroup.keySet();
		TreeNode groupHeadNode = codeTreeNode;
		// search whether the codeTreeNode has the same node in the group.
		for (TreeNode groupTreeNode : codeTreeNodes) {
			if (this.nodeSimilarity.similarity(codeTreeNode, groupTreeNode) == 1) {
				groupHeadNode = groupTreeNode;
				break;
			}
		}
		Map<TreeNode, List<TreeNode>> groupNodesMap = this.codeCodeTreeNodeGroup
				.get(groupHeadNode);
		if (groupNodesMap == null) {
			groupNodesMap = new HashMap<TreeNode, List<TreeNode>>();
			this.codeCodeTreeNodeGroup.put(groupHeadNode, groupNodesMap);
		}

		List<TreeNode> groupNodeTreeInstanceList = groupNodesMap.get(rootNode);
		if (groupNodeTreeInstanceList == null) {
			groupNodeTreeInstanceList = new ArrayList<TreeNode>();
			groupNodesMap.put(rootNode, groupNodeTreeInstanceList);
		}
		groupNodeTreeInstanceList.add(codeTreeNode);

		List<TreeNode> children = (List<TreeNode>) codeTreeNode.getChildren();
		for (TreeNode childTreeNode : children) {
			this.groupCodeTreeNode(rootNode, childTreeNode);
		}
	}

	/**
	 * @param freECdeCodeTreeNodeGroup
	 */
	private void printNodeChangeInfo(
			Map<TreeNode, Map<TreeNode, List<TreeNode>>> freECdeCodeTreeNodeGroup) {
		for (Map<TreeNode, List<TreeNode>> codeTreeNode : freECdeCodeTreeNodeGroup
				.values()) {
			if (codeTreeNode.size() > 1) {
				for (List<TreeNode> codeTreeNode1 : codeTreeNode.values()) {
					for (int i = 0; i < codeTreeNode1.size(); i++) {
						System.out.print(codeTreeNode1.get(i) + "\t");
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
			Map<List<TreeNode>, Map<TreeNode, List<TreeNode>>> freECdeCodeTreeNodeGroup) {
		for (List<TreeNode> keyChangeTreeNodes : freECdeCodeTreeNodeGroup
				.keySet()) {
			Map<TreeNode, List<TreeNode>> codeTreeNode = freECdeCodeTreeNodeGroup
					.get(keyChangeTreeNodes);
			for (TreeNode codeChangeTreeNode : keyChangeTreeNodes) {
				System.out.println(codeChangeTreeNode);
			}
			if (codeTreeNode.size() > 1) {
				for (List<TreeNode> codeTreeNode1 : codeTreeNode.values()) {
					for (int i = 0; i < codeTreeNode1.size(); i++) {
						System.out.print(codeTreeNode1.get(i) + "\t");
					}
					System.out.println();
				}
			}
		}
	}
}

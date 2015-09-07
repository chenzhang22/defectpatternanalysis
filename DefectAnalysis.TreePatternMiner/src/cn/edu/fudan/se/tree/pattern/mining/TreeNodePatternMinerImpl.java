/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.mining;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.fudan.se.code.change.tree.bean.TreeNode;
import cn.edu.fudan.se.code.change.tree.utils.ITreeNodeClone;
import cn.edu.fudan.se.tree.pattern.bean.TreePattern;
import cn.edu.fudan.se.tree.pattern.bean.TreePatternInstance;
import cn.edu.fudan.se.tree.pattern.filter.IFrequentPatternFilter;
import cn.edu.fudan.se.tree.pattern.filter.NodeChangePatternFilter;
import cn.edu.fudan.se.tree.pattern.similarility.ITreeNodeSimilarity;
import cn.edu.fudan.se.tree.pattern.similarility.ITreePatternSimilarity;
import cn.edu.fudan.se.tree.pattern.utils.TreePatternClone;

/**
 * @author Lotay
 *
 */
public class TreeNodePatternMinerImpl {
	private ITreePatternSimilarity treePatternSimilarity = null;
	private ITreeNodeSimilarity nodeSimilarity = null;
	private int frequencyThreshold = 2;
	private double similarityThreshold = 1.0;
	private TreeNodeGrouping groupingStrategy = null;
	private ITreeNodeClone treeNodeClone = null;
	private TreePatternClone treePatternClone = null;
	private IFrequentPatternFilter frequentPatternFilter = new NodeChangePatternFilter();
	public TreeNodePatternMinerImpl(
			ITreePatternSimilarity treePatternSimilarity,
			ITreeNodeClone treeNodeClone) {
		this.treePatternSimilarity = treePatternSimilarity;
		this.nodeSimilarity = this.treePatternSimilarity
				.getNodeSimilarityCalculator();
		this.treeNodeClone = treeNodeClone;
		this.groupingStrategy = new TreeNodeGrouping(this.nodeSimilarity,
				this.similarityThreshold, this.treeNodeClone);
		this.treePatternClone = new TreePatternClone(this.treeNodeClone);
	}

	public TreeNodePatternMinerImpl(
			ITreePatternSimilarity treePatternSimilarity,
			ITreeNodeClone treeNodeClone, int frequencyThreshold) {
		this(treePatternSimilarity, treeNodeClone);
		this.frequencyThreshold = frequencyThreshold;
	}

	public TreeNodePatternMinerImpl(
			ITreePatternSimilarity treePatternSimilarity,
			ITreeNodeClone treeNodeClone, double similarityThreshold) {
		this(treePatternSimilarity, treeNodeClone);
		this.similarityThreshold = similarityThreshold;
	}

	public TreeNodePatternMinerImpl(
			ITreePatternSimilarity treePatternSimilarity,
			ITreeNodeClone treeNodeClone, int frequencyThreshold,
			double similarityThreshold) {
		this(treePatternSimilarity, treeNodeClone, similarityThreshold);
		this.frequencyThreshold = frequencyThreshold;
	}

	public List<TreePattern> mineTreePattern(List<TreeNode> treeInstances) {

		Map<TreeNode, Map<TreeNode, List<TreeNode>>> treeNodeGroups = this.groupingStrategy
				.group(treeInstances);
		List<TreePattern> oneTreePatterns = this
				.mineOneSamePattern(treeNodeGroups);
		List<TreePattern> cloneTreePatterns = new ArrayList<TreePattern>();
		for (TreePattern treePattern : oneTreePatterns) {
			cloneTreePatterns.add(this.treePatternClone.clone(treePattern));
		}
		Map<TreeNode, Map<TreeNode, List<TreePatternInstance>>> treeOneNodeFrequentPatterns = buildTreePatternInstances(oneTreePatterns);

		List<TreePattern> frequentPatterns = mineTreePattern(cloneTreePatterns,
				treeOneNodeFrequentPatterns);

		return frequentPatterns;
	}

	private List<TreePattern> mineTreePattern(
			List<TreePattern> currentTreePatterns,
			Map<TreeNode, Map<TreeNode, List<TreePatternInstance>>> treeOneNodeFrequentPatterns) {
		List<TreePattern> frequentPatterns = new ArrayList<TreePattern>();
//		System.out.println(treeOneNodeFrequentPatterns);
		Collection<TreePattern> builtTreePatternsList = new ArrayList<TreePattern>();
		List<TreePattern> nextFrequentPatterns = new ArrayList<TreePattern>();

		for (TreePattern treePattern : currentTreePatterns) {
			builtTreePatternsList.add(treePattern);
		}
//		System.out.println(builtTreePatternsList);
		while (!currentTreePatterns.isEmpty()
				|| !nextFrequentPatterns.isEmpty()) {
			// If the currentTreePatterns is empty and nextFrequentPatterns is
			// not empty,
			// then extend for the nextFrequentPatterns
			if (currentTreePatterns.isEmpty()) {
				currentTreePatterns = nextFrequentPatterns;
				nextFrequentPatterns = new ArrayList<TreePattern>();
			}

			TreePattern treePattern = currentTreePatterns.remove(0);
			int currentFrequency = treePattern.getFrequency();
			int maxFrequency = 0;
			Map<TreeNode, List<TreePatternInstance>> treePatternInstancesMap = treePattern
					.getPatternInstances();
//			System.out.println("previous pattern: " + treePattern);
			// For each tree instance in current frequent pattern
			for (TreeNode treeInstance : treePatternInstancesMap.keySet()) {
				List<TreePatternInstance> treePatternInstances = treePatternInstancesMap
						.get(treeInstance);
				Map<TreeNode, List<TreePatternInstance>> oneNodeFrequentPatternInstanceMap = treeOneNodeFrequentPatterns
						.get(treeInstance);

				// for each treePatternInstance in the current tree instance.
				for (TreePatternInstance treePatternInstance : treePatternInstances) {
					// For the one node pattern for current treeInstance
//					System.out.println("treePatternInstance:"+treePatternInstance);
					for (TreeNode oneNodePattern : oneNodeFrequentPatternInstanceMap
							.keySet()) {
						// for each frequent tree instance of one node
						// pattern in current tree instance.
						// Actually, there is only one node in the
						// oneNodeFrequentTreePatternInstance
						for (TreePatternInstance oneNodeFrequentTreePatternInstance : oneNodeFrequentPatternInstanceMap
								.get(oneNodePattern)) {
							// check whether the
							// oneNodeFrequentTreePatternInstance already in
							// treePatternInstance
							TreeNode oneTreePatternFrequentNode = oneNodeFrequentTreePatternInstance
									.getMatchedNodes().get(oneNodePattern)
									.get(oneNodePattern);
							// if (treePattern.getTreePatterns().size()>2) {
							// }

							Map.Entry<TreeNode, TreeNode> existedEntry = this
									.isExistInCurrentTreePatternInstance(
											treePatternInstance,
											oneTreePatternFrequentNode);
							if (existedEntry != null) {
								continue;
							}
							

							// check the relationship(child of) between the
							// treePatternInstance and
							// oneNodeFrequentTreePatternInstance
							Map.Entry<TreeNode, Map.Entry<TreeNode, TreeNode>> parentNodeEntry = null;
							if (oneTreePatternFrequentNode.getParentTreeNode() != null) {
								try {
									parentNodeEntry = this
											.checkParentOfOneFrequentInstanceNode(
													treePatternInstance,
													oneTreePatternFrequentNode);
								} catch (Exception e) {
//									System.err
//											.println("oneTreePatternFrequentNode："
//													+ oneTreePatternFrequentNode
//													+ ",root:"
//													+ TreeNodeUtils
//															.rootOf(oneTreePatternFrequentNode));
//									e.printStackTrace();
									continue;
								}
							}

							// check the relationship(parent of) between the
							// treePatternInstance and
							// oneNodeFrequentTreePatternInstance
							Map<TreeNode, TreeNode> childrenNodesEntries = null;
							if (!oneTreePatternFrequentNode.getChildren()
									.isEmpty()) {
								childrenNodesEntries = this
										.checkChildRelationOfFrequentInstanceNode(
												treePatternInstance,
												oneTreePatternFrequentNode);
							}
							
							if (parentNodeEntry!=null) {
//								System.out.println("oneTreePatternFrequentNode:"+oneTreePatternFrequentNode+",root:"+TreeNodeUtils.rootOf(oneTreePatternFrequentNode)
//										+"\nparentNodeEntry:"+parentNodeEntry+",childrenNodesEntries:"+childrenNodesEntries);
//								System.out.println("oneTreePatternFrequentNode:"+treePatternInstance.getMatchedNodes().get(parentNodeEntry.getKey()).get(parentNodeEntry.getValue().getKey()));
							}
							

							
//							System.out.println("oneTreePatternFrequentNode:"+oneTreePatternFrequentNode+",root:"+TreeNodeUtils.rootOf(oneTreePatternFrequentNode));
//							System.out.println("parentNodeEntry:"+parentNodeEntry+",childrenNodesEntries:"+childrenNodesEntries);
							
							// Build the new tree pattern and filte the not meet
							// pattern instance/tree.
							// check whether the new tree pattern already in
							// builtTreePatternsList

							TreePattern newBuiltTreePattern = null;
							try {
								newBuiltTreePattern = generateNewTreePatternAndFilterPatternInstance(
										treePattern, treeOneNodeFrequentPatterns,
										treeInstance, treePatternInstance,
										parentNodeEntry, childrenNodesEntries,
										oneTreePatternFrequentNode,builtTreePatternsList);
							} catch (Exception e) {
								//Here that the newBuiltTreePattern already in the @builtTreePatternsList
								continue;
							}

							// check whether newBuilTreePattern already in the
							// builtTreePatternsList
							

//							System.out.println("newBuiltTreePattern:"
//									+ newBuiltTreePattern);

//
//							System.out.println("newBuiltTreePattern:"
//									+ newBuiltTreePattern);
							if (newBuiltTreePattern!=null) {
								builtTreePatternsList.add(newBuiltTreePattern);
							}
							// System.out.println(newBuiltTreePattern);

							// TODO: check the frequency of the new tree
							// pattern, update maxFrequency,
							// builtTreePatternsList and nextFrequentPatterns
							if (newBuiltTreePattern.getFrequency() >= this.frequencyThreshold) {
								nextFrequentPatterns.add(newBuiltTreePattern);
							}
							if (newBuiltTreePattern.getFrequency() > maxFrequency) {
								maxFrequency = newBuiltTreePattern
										.getFrequency();
							}
						}
					}
				}
			}

			if (currentFrequency > maxFrequency) {
				System.out.println("pattern num:"+frequentPatterns.size());
				frequentPatterns.add(treePattern);
			}
			if (frequentPatterns.size()>390) {
				break;
			}
		}

		//filter.
		frequentPatternFilter.filter(frequentPatterns);
		
		return frequentPatterns;
	}
	


	/**
	 * build the new TreePattern based on the new relation(parent/children of
	 * the oneTreePatternFrequentNode)
	 * 
	 * @param treePattern
	 * @param parentNodeEntry
	 * @param childrenNodesEntries
	 * @param oneTreePatternFrequentNode
	 * @param builtTreePatternsList 
	 * @throws Exception 
	 * 
	 * 
	 */
	private TreePattern generateNewTreePatternAndFilterPatternInstance(
			TreePattern treePattern,
			Map<TreeNode, Map<TreeNode, List<TreePatternInstance>>> oneNodeFrequentPatterns,
			TreeNode treeInstance, TreePatternInstance treePatternInstance,
			Entry<TreeNode, Entry<TreeNode, TreeNode>> parentNodeEntry,
			Map<TreeNode, TreeNode> childrenNodesEntries,
			TreeNode oneTreePatternFrequentNode, Collection<TreePattern> builtTreePatternsList) throws Exception {
		HashMap<TreeNode, TreeNode> matchedPatternNodes = new HashMap<TreeNode, TreeNode>();
		TreePattern clonedTreePattern = this.treePatternClone.clone(
				treePattern, matchedPatternNodes);
		TreeNode cloneNewPatternNode = this.treeNodeClone
				.cloneNoChildren(oneTreePatternFrequentNode);

		// / Bug: should add the cloneNewPatternTree in the right location.....

		// If the new pattern tree node has other pattern as children.
		List<TreeNode> clonedChildPatternTreeNodes = new ArrayList<TreeNode>();

		if (childrenNodesEntries != null) {
			List<TreeNode> childPatternTreeNodes = new ArrayList<TreeNode>();
			this.arrangeChildPatterns(oneTreePatternFrequentNode,
					childPatternTreeNodes, childrenNodesEntries);
			for (TreeNode childPatternNodeTree : childPatternTreeNodes) {
				if (childPatternNodeTree != null
						&& matchedPatternNodes
								.containsKey(childPatternNodeTree)) {
					TreeNode clonedChildPatternNodeTree = matchedPatternNodes
							.get(childPatternNodeTree);
					if (clonedTreePattern.getTreePatterns().contains(
							clonedChildPatternNodeTree)) {
						clonedTreePattern.getTreePatterns().remove(
								clonedChildPatternNodeTree);
						clonedChildPatternTreeNodes
								.add(clonedChildPatternNodeTree);
					} else {
						System.err
								.println("TreeNodePatternMinerImpl: Clone Error 1...");
					}
					cloneNewPatternNode.addChild(clonedChildPatternNodeTree);
				} else {
					System.err
							.println("TreeNodePatternMinerImpl: Clone Error 2...");
				}
			}
		}

		// If the other pattern node is the parent node of the new pattern node
		Entry<TreeNode, Entry<TreeNode, TreeNode>> clonedParentNodeEntry = null;
		TreeNode cloneParentPatternNodeTree = null;
		TreeNode cloneDirectParentPatternTreeNode = null;
		if (parentNodeEntry != null) {
			TreeNode patternNodeTree = parentNodeEntry.getKey();
			cloneParentPatternNodeTree = matchedPatternNodes
					.get(patternNodeTree);
			Entry<TreeNode, TreeNode> relatedPatternNodes = parentNodeEntry
					.getValue();
			TreeNode directPatternNode = relatedPatternNodes.getKey();
			cloneDirectParentPatternTreeNode = matchedPatternNodes
					.get(directPatternNode);
			TreeNode previousPatternNode = relatedPatternNodes.getValue();
			Entry<TreeNode, TreeNode> clonedRelatedPatternNodes = null;
			if (previousPatternNode == null) {
				// System.err.println("Find Parent Node Entry Wrong.");
				cloneDirectParentPatternTreeNode.addChild(0,
						cloneNewPatternNode);
				clonedRelatedPatternNodes = new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
						cloneDirectParentPatternTreeNode, null);
			} else {
				TreeNode clonePreviousPatternNode = matchedPatternNodes
						.get(previousPatternNode);
				clonedRelatedPatternNodes = new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
						cloneDirectParentPatternTreeNode,
						clonePreviousPatternNode);

				if (clonePreviousPatternNode != null) {
					int childIndex = directPatternNode
							.getChildren().indexOf(previousPatternNode);
					if (childIndex >= 0) {
						cloneDirectParentPatternTreeNode.addChild(
								childIndex + 1, cloneNewPatternNode);
					} else {
						System.err
								.println("TreeNodePatternMinerImpl: Clone Error 4: cloneDirectPatterNode does not have clonePreviousPatternNode as child");
					}
				} else {
					System.err
							.println("TreeNodePatternMinerImpl: Clone Error 3: directPatternNode does not contain the previousPatternNode");
				}
			}
			clonedParentNodeEntry = new AbstractMap.SimpleEntry<TreeNode, Entry<TreeNode, TreeNode>>(
					cloneParentPatternNodeTree, clonedRelatedPatternNodes);
			// remove the new pattern node in the pattern tree.
			// if (clonedTreePattern.getTreePatterns().contains(
			// cloneNewPatternTree)) {
			// clonedTreePattern.getTreePatterns().remove(cloneNewPatternTree);
			// }
//			System.out.println("clonedTreePattern:\n"+clonedTreePattern);
		}

		// TODO: add the cloneNewPattern Tree in the pattern.
		if (clonedParentNodeEntry == null) {
			int index = this.selectTheIndexOfPatternNode(
					treePattern.getTreePatterns(), treePatternInstance,
					oneTreePatternFrequentNode);
			clonedTreePattern.getTreePatterns().add(index, cloneNewPatternNode);
		}
		
		
		if (this.checkPatternInCollection(
				builtTreePatternsList, clonedTreePattern) != null) {
			throw new Exception("Already BuildTreePattern.");
		}

		// TODO: make sure other PatternTreeInstance and TreeInstance meet the
		// new pattern...
		this.filtedPatternTreeInstance(clonedTreePattern, cloneNewPatternNode,
				oneNodeFrequentPatterns, treeInstance, treePatternInstance,
				clonedChildPatternTreeNodes, clonedParentNodeEntry);
		return clonedTreePattern;
	}

	private int selectTheIndexOfPatternNode(List<TreeNode> patternList,
			TreePatternInstance treePatternInstance,
			TreeNode treePatternFrequentNode) {
		ArrayList<String> paths = new ArrayList<String>();
		for (TreeNode treeNode : patternList) {
			TreeNode treeMatchedNode = treePatternInstance.getMatchedNodes()
					.get(treeNode).get(treeNode);
			String path = this.path2Root(treeMatchedNode);
			paths.add(path);
		}
		String path = this.path2Root(treePatternFrequentNode);
		for (int i = 0; i < paths.size(); i++) {
			if (paths.get(i).compareTo(path) > 0) {
				return i;
			}
		}

		return paths.size();
	}

	@SuppressWarnings("unchecked")
	private void arrangeChildPatterns(TreeNode instanceNode,
			List<TreeNode> childInstanceNodes,
			Map<TreeNode, TreeNode> childrenNodesEntries) {
		if (childrenNodesEntries.containsKey(instanceNode)) {
			childInstanceNodes.add(childrenNodesEntries.get(instanceNode));
			return;
		}
		for (TreeNode treeNode : (List<TreeNode>) instanceNode.getChildren()) {
			this.arrangeChildPatterns(treeNode, childInstanceNodes,
					childrenNodesEntries);
		}
	}

	/**
	 * filter the pattern tree instance not meet new pattern
	 */
	private void filtedPatternTreeInstance(
			TreePattern cloneTreePattern,
			TreeNode cloneNewPatternNode,
			Map<TreeNode, Map<TreeNode, List<TreePatternInstance>>> oneNodeFrequentPatterns,
			TreeNode currentTreeInstance,
			TreePatternInstance currentTreePatternInstance,
			List<TreeNode> removePatternTrees,
			Entry<TreeNode, Entry<TreeNode, TreeNode>> clonedParentNodeEntry) {
		Map<TreeNode, List<TreePatternInstance>> patternTreeInstances = cloneTreePattern
				.getPatternInstances();
		Map<TreeNode, List<TreePatternInstance>> newPatternTreeIntstances = new HashMap<TreeNode, List<TreePatternInstance>>();

		TreeNode cloneParentPatternNodeTree = null;
		TreeNode cloneDirectParentPatternTreeNode = null;
		TreeNode clonePreviousPatternNode = null;
		if (clonedParentNodeEntry != null) {
			cloneParentPatternNodeTree = (clonedParentNodeEntry.getKey());
			cloneDirectParentPatternTreeNode = clonedParentNodeEntry.getValue()
					.getKey();
			clonePreviousPatternNode = clonedParentNodeEntry.getValue()
					.getValue();
		}
		for (TreeNode treeInstance : patternTreeInstances.keySet()) {
			List<TreePatternInstance> treePatternInstances = patternTreeInstances
					.get(treeInstance);
			Map<TreeNode, List<TreePatternInstance>> oneNodeTreePatternInstances = oneNodeFrequentPatterns
					.get(treeInstance);
			List<TreePatternInstance> onePatternNodeInstances = this
					.getOneNodeTreePatternInstances(
							oneNodeTreePatternInstances, cloneNewPatternNode);
			// There are no onePatternNodeInstances for pattern node
			// cloneNewPatternTree in treeInstance
			if (onePatternNodeInstances == null) {
				treePatternInstances.clear();
				continue;
			} else {
				// relocate the matched instance node
				List<TreeNode> onePatternNodeMatchedInstanceNodes = new ArrayList<TreeNode>();
				for (TreePatternInstance onePatternNodeInstance : onePatternNodeInstances) {
					for (Map<TreeNode, TreeNode> onePatternNodeMatchedInstance : onePatternNodeInstance
							.getMatchedNodes().values()) {
						onePatternNodeMatchedInstanceNodes
								.addAll(onePatternNodeMatchedInstance.values());
					}
				}
//				List<TreePatternInstance> notMatchedPatternInstances = new ArrayList<TreePatternInstance>();
				List<TreePatternInstance> newTreePatternInstances = new ArrayList<TreePatternInstance>();
				for (TreePatternInstance treePatternInstance : treePatternInstances) {
					boolean hasInstanceNodeMatched = false;
					treePatternInstance.getMatchedNodes();
					for (TreeNode onePatternTreeInstanceNode : onePatternNodeMatchedInstanceNodes) {
						boolean patternInstanceMatched = false;

						// check whether the onePatternTreeInstanceNode
						// matched the cloneNewPatternTree position.
						if (this.isExistInCurrentTreePatternInstance(
								treePatternInstance, onePatternTreeInstanceNode) == null) {

							// add all the child instance node in the
							// matchedTreeNodes
							// check whether child instance match the children
							// node list
							Set<TreeNode> matchedTreeInstanceNodes = new HashSet<TreeNode>();
							boolean allChildrenMeet = true;
							Map<TreeNode, Map<TreeNode, TreeNode>> patternInstanceMatchedNodes = treePatternInstance
									.getMatchedNodes();
							for (TreeNode patternNode : removePatternTrees) {
								Map<TreeNode, TreeNode> map = patternInstanceMatchedNodes
										.get(patternNode);
								if (map != null && map.containsKey(patternNode)) {
									TreeNode patternMatchedInstanceNode = map
											.get(patternNode);
									matchedTreeInstanceNodes
											.add(patternMatchedInstanceNode);
									if (!this.checkChildRelation(
											onePatternTreeInstanceNode,
											patternMatchedInstanceNode,
											matchedTreeInstanceNodes)) {
										allChildrenMeet = false;
										break;
									}
								} else {
									allChildrenMeet = false;
									break;
								}
							}

							boolean notParentOf = checkShouldNotAsParentOfFrequentInstance(
									removePatternTrees,
									onePatternTreeInstanceNode,
									patternInstanceMatchedNodes);

							allChildrenMeet = allChildrenMeet && notParentOf;

							if (!allChildrenMeet) {
								// should remove the patternInstance
								continue;
							}

							// now all child patterns are matched.
							// matched parent..
							if (cloneParentPatternNodeTree != null
									&& cloneDirectParentPatternTreeNode != null) {
								Map<TreeNode, TreeNode> instanceMatchedNodes = patternInstanceMatchedNodes
										.get(cloneParentPatternNodeTree);
								if (instanceMatchedNodes != null
										&& instanceMatchedNodes
												.containsKey(cloneDirectParentPatternTreeNode)) {
									TreeNode treeInstanceParentNode = instanceMatchedNodes
											.get(cloneDirectParentPatternTreeNode);
									if (this.checkChildRelation(
											treeInstanceParentNode,
											onePatternTreeInstanceNode,
											null)) {
										patternInstanceMatched = true;
									}
								}
								//TODO:Fix bug that other interleaving relation.
//								System.out.println("cloneNewPatternNode:"
//										+ cloneNewPatternNode);
//								System.out
//										.println("patternInstanceMatchedNodes"
//												+ patternInstanceMatchedNodes
//												+ ",onePatternTreeInstanceNode:"
//												+ onePatternTreeInstanceNode
//												+ ",root of:"
//												+ TreeNodeUtils
//														.rootOf(onePatternTreeInstanceNode));
//								System.out.println("patternInstanceMatched 1:"+patternInstanceMatched);
								if (patternInstanceMatched
										&& !this.checkTheRelationshipOfClonedPattern(
												cloneParentPatternNodeTree,
												cloneNewPatternNode,
												onePatternTreeInstanceNode,
												instanceMatchedNodes)) {
									patternInstanceMatched = false;
								}
//								System.out.println("patternInstanceMatched 2:"+patternInstanceMatched);
								if (patternInstanceMatched
										&& this.hasInterleavingRelation(
												onePatternTreeInstanceNode,
												instanceMatchedNodes)) {
									patternInstanceMatched = false;
								}
//								System.out.println("patternInstanceMatched 3:"+patternInstanceMatched);
							} else if (cloneParentPatternNodeTree == null
									&& cloneDirectParentPatternTreeNode == null) {
								patternInstanceMatched = true;
								for (TreeNode treeNode : patternInstanceMatchedNodes
										.keySet()) {
									TreeNode instanceTreeNode = patternInstanceMatchedNodes
											.get(treeNode).get(treeNode);
									if (this.checkChildRelation(
											instanceTreeNode,
											onePatternTreeInstanceNode,
											new HashSet<TreeNode>())) {
										patternInstanceMatched = false;
									}
								}
							} else {
								System.err
										.println("TreeNodePatternMinerImpl: Find parent node error.");
							}

							if (patternInstanceMatched) {
								hasInstanceNodeMatched = true;
								TreePatternInstance newTreePatternInstance = new TreePatternInstance();
								HashMap<TreeNode, TreeNode> matchedNodes = new HashMap<TreeNode, TreeNode>();
								matchedNodes.put(cloneNewPatternNode,
										onePatternTreeInstanceNode);
								for (TreeNode treePatternNode2 : removePatternTrees) {
									matchedNodes.putAll(treePatternInstance
											.getMatchedNodes().get(
													treePatternNode2));
								}

								for (TreeNode patternTreeNode : patternInstanceMatchedNodes
										.keySet()) {
									if (!removePatternTrees
											.contains(patternTreeNode)) {
										newTreePatternInstance.addMatchedNode(
												patternTreeNode,
												patternInstanceMatchedNodes
														.get(patternTreeNode));
									}
								}

								if (cloneParentPatternNodeTree != null
										&& cloneDirectParentPatternTreeNode != null) {
									Map<TreeNode, TreeNode> map = treePatternInstance
											.getMatchedNodes().get(
													cloneParentPatternNodeTree);
									matchedNodes.putAll(map);
									newTreePatternInstance.addMatchedNode(
											cloneParentPatternNodeTree,
											matchedNodes);
								} else if (cloneParentPatternNodeTree == null
										&& cloneDirectParentPatternTreeNode == null) {
									newTreePatternInstance.addMatchedNode(
											cloneNewPatternNode, matchedNodes);
								} else {
									System.err
											.println("TreeNodePatternMinerImpl:cloneParentPattern error.");
									continue;
								}
//								System.out.println("newTreePatternInstance:"+newTreePatternInstance);
								newTreePatternInstances
										.add(newTreePatternInstance);
							}
						}
					}
					if (!hasInstanceNodeMatched) {
//						notMatchedPatternInstances.add(treePatternInstance);
					}
					// TODO: handling that pattern trees have no relationship,
					// but the pattern instance have some relationship.

				}

				// for (TreePatternInstance treePatternInstance2 :
				// notMatchedPatternInstances) {
				// newTreePatternInstances.remove(treePatternInstance2);
				// }
//				System.out.println("treeInstance:"+treeInstance+",newTreePatternInstances 1:"+newTreePatternInstances);
				removeDuplicatedPatternInstances(newTreePatternInstances);
				if (cloneTreePattern.getTreePatterns().size() > 1) {
					removeOutOfOrderPatternInstances(
							cloneTreePattern.getTreePatterns(),
							newTreePatternInstances);
				}
//				System.out.println("treeInstance:"+treeInstance+",newTreePatternInstances 2:"+newTreePatternInstances);

				if (!newTreePatternInstances.isEmpty()) {
					newPatternTreeIntstances.put(treeInstance,
							newTreePatternInstances);
				}
			}

		}
//		System.out.println("newPatternTreeIntstances:"+newPatternTreeIntstances);
		cloneTreePattern.setPatternInstances(newPatternTreeIntstances);
	}

	private boolean checkTheRelationshipOfClonedPattern(
			TreeNode cloneParentPatternNodeTree, TreeNode cloneNewPatternNode,
			TreeNode onePatternTreeInstanceNode,
			Map<TreeNode, TreeNode> instanceMatchedNodes) {
		List<TreeNode> patternNodeOrders = new ArrayList<TreeNode>();
		Map<TreeNode, String> instanceMatchedNodePaths = new HashMap<TreeNode, String>();
		this.indexOfPatternNodeTrees(cloneParentPatternNodeTree,
				instanceMatchedNodes, patternNodeOrders,
				instanceMatchedNodePaths);
		instanceMatchedNodePaths.put(cloneNewPatternNode, this.path2Root(onePatternTreeInstanceNode));
		List<String> orderPaths = new ArrayList<String>();
		for (TreeNode treeNode : patternNodeOrders) {
			if (instanceMatchedNodePaths.containsKey(treeNode)) {
				orderPaths.add(instanceMatchedNodePaths.get(treeNode));
			}else {
				System.err.println("Path Order Error...");
			}
		}
//		System.out.println("orderPaths:"+orderPaths);
		for (int i = 0; i < orderPaths.size()-1; i++) {
			if (orderPaths.get(i).compareTo(orderPaths.get(i+1))>0) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void indexOfPatternNodeTrees(TreeNode currentNode,
			Map<TreeNode, TreeNode> instanceMatchedNodes,
			List<TreeNode> orderOfPatternNodes,
			Map<TreeNode, String> instanceMatchedNodePaths) {
		if (currentNode == null) {
			return;
		}
		orderOfPatternNodes.add(currentNode);
		TreeNode matchedNode = instanceMatchedNodes.get(currentNode);
		if (matchedNode!=null) {
			instanceMatchedNodePaths.put(currentNode, this.path2Root(matchedNode));
		}
		for (TreeNode childNode : (List<TreeNode>) currentNode.getChildren()) {
			this.indexOfPatternNodeTrees(childNode,instanceMatchedNodes,orderOfPatternNodes,instanceMatchedNodePaths);
		}
	};

	private void removeOutOfOrderPatternInstances(List<TreeNode> patternTrees,
			List<TreePatternInstance> treePatternInstances) {
		List<TreePatternInstance> outOfOrderPatternInstances = new ArrayList<TreePatternInstance>();

		for (TreePatternInstance treePatternInstance : treePatternInstances) {
			List<String> squenceList = new ArrayList<String>();
			for (TreeNode treePatternTree : patternTrees) {
				boolean flag = false;
				TreeNode treeNode = null;
				try {
					treeNode = treePatternInstance.getMatchedNodes()
							.get(treePatternTree).get(treePatternTree);
					String path = path2Root(treeNode);
					squenceList.add(path);
				} catch (Exception e) {
					e.printStackTrace();
					flag = true;
				}
				if (flag) {
					System.out.println();
				}
			}
			for (int i = 0; i < squenceList.size() - 1; i++) {
				if (squenceList.get(i).compareTo(squenceList.get(i + 1)) > 0) {
					outOfOrderPatternInstances.add(treePatternInstance);
					break;
				}
			}
		}
		for (TreePatternInstance treePatternInstance : outOfOrderPatternInstances) {
			if (treePatternInstances.contains(treePatternInstance)) {
				treePatternInstances.remove(treePatternInstance);
			}
		}
	}

	private String path2Root(TreeNode treeNode) {
		if (treeNode == null) {
			return "";
		}
		TreeNode parentTreeNode = treeNode.getParentTreeNode();
		if (parentTreeNode != null) {
			int index = parentTreeNode.getChildren().indexOf(treeNode);
			return this.path2Root(parentTreeNode) + index;
		}
		return "";
	}

	/**
	 * @param removePatternTrees
	 * @param onePatternTreeInstanceNode
	 * @param patternInstanceMatchedNodes
	 * @return
	 */
	private boolean checkShouldNotAsParentOfFrequentInstance(
			List<TreeNode> removePatternTrees,
			TreeNode onePatternTreeInstanceNode,
			Map<TreeNode, Map<TreeNode, TreeNode>> patternInstanceMatchedNodes) {
		// check when there are child relationship in
		// pattern, while the instances have the
		// parent/child relation.
		for (TreeNode treeNode : patternInstanceMatchedNodes.keySet()) {
			if (!removePatternTrees.contains(treeNode)) {
				TreeNode instanceTreeNode = patternInstanceMatchedNodes.get(
						treeNode).get(treeNode);
				if (this.checkChildRelation(onePatternTreeInstanceNode,
						instanceTreeNode, null)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param treePatternInstances
	 */
	private void removeDuplicatedPatternInstances(
			List<TreePatternInstance> treePatternInstances) {
		// TODO: remove duplicated TreePatternInstance for @treePatternInstances
		List<Set<TreeNode>> duplicatedTreePatternInstanceNodes = new ArrayList<Set<TreeNode>>();
		List<TreePatternInstance> duplicatedPatternInstances = new ArrayList<TreePatternInstance>();
		for (TreePatternInstance treePatternInstance2 : treePatternInstances) {
			Set<TreeNode> treePatternInstanceNodes = new HashSet<TreeNode>();
			for (Map<TreeNode, TreeNode> treeNode : treePatternInstance2
					.getMatchedNodes().values()) {
				treePatternInstanceNodes.addAll(treeNode.values());
			}
			if (this.checkDuplicatedTreePatternInstance(
					duplicatedTreePatternInstanceNodes,
					treePatternInstanceNodes) != null) {
				duplicatedPatternInstances.add(treePatternInstance2);
			} else {
				duplicatedTreePatternInstanceNodes
						.add(treePatternInstanceNodes);
			}
		}
		for (TreePatternInstance treePatternInstance2 : duplicatedPatternInstances) {
			treePatternInstances.remove(treePatternInstance2);
		}
	}

	private Set<TreeNode> checkDuplicatedTreePatternInstance(
			List<Set<TreeNode>> duplicatedTreePatternInstances,
			Set<TreeNode> treePatternInstanceNodes) {
		for (Set<TreeNode> treeNode : duplicatedTreePatternInstances) {
			if (treeNode.size() != treePatternInstanceNodes.size()) {
				continue;
			}
			boolean isDuplicated = true;
			for (TreeNode treeNode2 : treePatternInstanceNodes) {
				if (!treeNode.contains(treeNode2)) {
					isDuplicated = false;
					break;
				}
			}
			if (isDuplicated) {
				return treeNode;
			}
		}
		return null;
	}

	/**
	 * @param parentNode
	 * @param childNode
	 * @param mediumNodes
	 * @return
	 * 
	 *         check whether the parentNode is the parent of childNode, and make
	 *         sure that not crossing the any mediumNodes
	 */
	private boolean checkChildRelation(TreeNode parentNode, TreeNode childNode,
			Set<TreeNode> mediumNodes) {
		TreeNode currentNode = childNode.getParentTreeNode();
		while (currentNode != null) {
			if (mediumNodes != null && mediumNodes.contains(currentNode)) {
				return false;
			}
			if (currentNode == parentNode) {
				return true;
			}
			currentNode = currentNode.getParentTreeNode();
		}
		return false;
	}

	private List<TreePatternInstance> getOneNodeTreePatternInstances(
			Map<TreeNode, List<TreePatternInstance>> oneNodeTreePatternInstances,
			TreeNode cloneNewPatternTree) {
		if (oneNodeTreePatternInstances == null
				|| oneNodeTreePatternInstances.isEmpty()
				|| cloneNewPatternTree == null) {
			return null;
		}
		for (TreeNode treePatternNode : oneNodeTreePatternInstances.keySet()) {
			if (this.nodeSimilarity.similarity(treePatternNode,
					cloneNewPatternTree) >= this.similarityThreshold) {
				return oneNodeTreePatternInstances.get(treePatternNode);
			}
		}
		return null;
	}

	/**
	 * @param treePatternInstance
	 * @param oneTreePatternFrequentNode
	 * @return
	 * 
	 *         check whether the oneTreePatternFrequentNode is the parent node
	 *         of frequent pattern node(corresponding to matched node). return
	 *         Map of the oneTreePatternFrequentNode's children, where the key
	 *         is the matched instance node, and the corresponding pattern node.
	 */
	Map<TreeNode, TreeNode> checkChildrenOfFrequentInstanceNode(
			TreePatternInstance treePatternInstance,
			TreeNode oneTreePatternFrequentNode) {
		Map<TreeNode, TreeNode> childrenTreeNodes = null;
		Map<TreeNode, Map<TreeNode, TreeNode>> patternMatchedNodeTrees = treePatternInstance
				.getMatchedNodes();
		for (TreeNode treePattern : patternMatchedNodeTrees.keySet()) {
			TreeNode patternMatchedRootNodes = patternMatchedNodeTrees.get(
					treePattern).get(treePattern);
			if (patternMatchedRootNodes != null
					&& patternMatchedRootNodes.getParentTreeNode() != null
					&& patternMatchedRootNodes.getParentTreeNode() == oneTreePatternFrequentNode) {

				if (childrenTreeNodes == null) {
					childrenTreeNodes = new HashMap<TreeNode, TreeNode>();
				}
				childrenTreeNodes.put(patternMatchedRootNodes, treePattern);
			}
		}
		return childrenTreeNodes;
	}

	/**
	 * @param treePatternInstance
	 * @param oneTreePatternFrequentNode
	 * @return
	 * 
	 *         check whether the oneTreePatternFrequentNode is the parent node
	 *         of frequent pattern node(corresponding to matched node). return
	 *         Map of the oneTreePatternFrequentNode's children, where the key
	 *         is the matched instance node, and the corresponding pattern node.
	 */
	private Map<TreeNode, TreeNode> checkChildRelationOfFrequentInstanceNode(
			TreePatternInstance treePatternInstance,
			TreeNode oneTreePatternFrequentNode) {
		Map<TreeNode, TreeNode> childrenTreeNodes = new HashMap<TreeNode, TreeNode>();
		Map<TreeNode, Map<TreeNode, TreeNode>> patternMatchedNodeTrees = treePatternInstance
				.getMatchedNodes();
		for (TreeNode treePattern : patternMatchedNodeTrees.keySet()) {
			TreeNode patternMatchedRootNodes = patternMatchedNodeTrees.get(
					treePattern).get(treePattern);
			if (patternMatchedRootNodes != null
					&& patternMatchedRootNodes.getParentTreeNode() != null) {
				if (this.checkChildRelation(oneTreePatternFrequentNode,
						patternMatchedRootNodes, null)) {
					childrenTreeNodes.put(patternMatchedRootNodes, treePattern);
				}
			}
		}
		return childrenTreeNodes;

	}

	/**
	 * check whether the oneTreePatternFrequentNode is a child of
	 * treePatternInstance matched nodes.
	 * 
	 * if yes return Map.Entry<TreeNode, Map.Entry<TreeNode, TreeNode>>: where
	 * the first key is the pattern node tree, the value is the direct parent
	 * and the previous pattern child of the oneTreePatternFrequentNode.
	 * 
	 * if no, return null.
	 * 
	 * @throws Exception
	 * **/
	@SuppressWarnings("unchecked")
	private Map.Entry<TreeNode, Map.Entry<TreeNode, TreeNode>> checkParentOfOneFrequentInstanceNode(
			TreePatternInstance treePatternInstance,
			TreeNode oneTreePatternFrequentNode) throws Exception {
		Map<TreeNode, Map<TreeNode, TreeNode>> matchedNodeTrees = treePatternInstance
				.getMatchedNodes();

		for (TreeNode patternNodeTree : matchedNodeTrees.keySet()) {
			Map<TreeNode, TreeNode> matchedNodes = matchedNodeTrees
					.get(patternNodeTree);
			TreeNode closestParentNode = null;
			int minDepth = Integer.MAX_VALUE;

			for (TreeNode patternNode : matchedNodes.keySet()) {
				TreeNode treeInstanceMatchedNode = matchedNodes
						.get(patternNode);
				int depth = 0;
				TreeNode tmpNode = oneTreePatternFrequentNode;

				boolean asChildOf = false;
				while (tmpNode != null) {
					if (tmpNode == treeInstanceMatchedNode) {
						asChildOf = true;
						break;
					}
					depth++;
					tmpNode = tmpNode.getParentTreeNode();
				}
				if (asChildOf && depth < minDepth) {
					closestParentNode = patternNode;
					minDepth = depth;
				}
			}

			boolean hasParentRelation = hasInterleavingRelation(
					oneTreePatternFrequentNode, matchedNodes);

			if (closestParentNode!=null&&hasParentRelation) {
				throw new Exception(
						"TreeNodePatternMinerImpl.java:Illegal pattern relation.");
			}
			if (closestParentNode != null) {
//				System.out.println("closestParentNode:"+treePatternInstance.getMatchedNodes().get(patternNodeTree).get(closestParentNode));
				if (closestParentNode.getChildren().isEmpty()) {
//					System.out.println("1....");
					return new AbstractMap.SimpleEntry<TreeNode, Map.Entry<TreeNode, TreeNode>>(
							patternNodeTree,
							new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
									closestParentNode, null));
				}

				List<String> childPathList = new ArrayList<String>();

				for (TreeNode childNode : (List<TreeNode>) closestParentNode
						.getChildren()) {
					if (matchedNodes.containsKey(childNode)) {
						String path = this.path2Root(matchedNodes
								.get(childNode));
						childPathList.add(path);
					} else {
						System.err
								.println("TreeNodePatternMinerImpl.java>checkParentOfOneFrequentInstanceNode:Clone Error");
					}
				}
				int i = -1;
				String nodePath = this.path2Root(oneTreePatternFrequentNode);
				for (; i < childPathList.size() - 1; i++) {
					if (nodePath.compareTo(childPathList.get(i + 1)) < 0) {
						break;
					}
				}
				if (i >= 0) {
//					System.out.println("2....");
					return new AbstractMap.SimpleEntry<TreeNode, Map.Entry<TreeNode, TreeNode>>(
							patternNodeTree,
							new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
									closestParentNode,
									(TreeNode) closestParentNode.getChildren()
											.get(i)));
				} else {
//					System.out.println("3....");
					return new AbstractMap.SimpleEntry<TreeNode, Map.Entry<TreeNode, TreeNode>>(
							patternNodeTree,
							new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
									closestParentNode, null));
				}
			}

			/*
			 * for (TreeNode patternNode : matchedNodes.keySet()) { boolean
			 * isParentOf = matchedNodes.get(patternNode) ==
			 * oneTreePatternFrequentNode .getParentTreeNode(); if (isParentOf)
			 * { if (patternNode.getChildren().isEmpty()) { return new
			 * AbstractMap.SimpleEntry<TreeNode, Map.Entry<TreeNode, TreeNode>>(
			 * patternNodeTree, new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
			 * patternNode, null)); } // search the index child of the
			 * oneTreePatternNode int childIndexOfOneTreePatternFrequentNode =
			 * oneTreePatternFrequentNode .getParentTreeNode().getChildren()
			 * .indexOf(oneTreePatternFrequentNode); if
			 * (childIndexOfOneTreePatternFrequentNode < 0) { System.err
			 * .println(
			 * "TreePatternSimilarityImpl.java: index error... some logic errors occur 1."
			 * ); } List<TreeNode> childrenOfPatternNodes = patternNode
			 * .getChildren(); int indexOfChild = -2; int i = 0; for (i = 0; i <
			 * childrenOfPatternNodes.size(); i++) { TreeNode patternChildNode =
			 * childrenOfPatternNodes .get(i); TreeNode treeInstanceMatchedNode
			 * = matchedNodes .get(patternChildNode); int
			 * childIndexOfPatternChildNode = oneTreePatternFrequentNode
			 * .getParentTreeNode().getChildren()
			 * .indexOf(treeInstanceMatchedNode); if
			 * (childIndexOfPatternChildNode >
			 * childIndexOfOneTreePatternFrequentNode) { indexOfChild = i - 1;
			 * break; } else if (childIndexOfPatternChildNode ==
			 * childIndexOfOneTreePatternFrequentNode) { System.err .println(
			 * "TreePatternSimilarityImpl.java: index error... some logic errors occur 2."
			 * ); } }
			 * 
			 * // Not Found, all child pattern node are before the //
			 * oneTreePatternFrequentNode if (i == childrenOfPatternNodes.size()
			 * && indexOfChild == -2) { indexOfChild =
			 * childrenOfPatternNodes.size() - 1; } // as the first child of
			 * patternNode if (indexOfChild == -1) { return new
			 * AbstractMap.SimpleEntry<TreeNode, Map.Entry<TreeNode, TreeNode>>(
			 * patternNodeTree, new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
			 * patternNode, null)); } // as normal child. return new
			 * AbstractMap.SimpleEntry<TreeNode, Map.Entry<TreeNode, TreeNode>>(
			 * patternNodeTree, new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
			 * patternNode, childrenOfPatternNodes.get(indexOfChild))); } }
			 */
		}
		return null;
	}

	/**
	 * @param oneTreePatternFrequentNode
	 * @param matchedNodes
	 * @return
	 */
	private boolean hasInterleavingRelation(TreeNode oneTreePatternFrequentNode,
			Map<TreeNode, TreeNode> matchedNodes) {
		// Bug: when the oneTreePatternFrequentNode is between the
		// patternTreeNode and other pattern Node, And this is illegal.
		boolean hasParentRelation = false;
		for (TreeNode patternMatchedInstanceNode : matchedNodes.values()) {
			if (this.checkChildRelation(oneTreePatternFrequentNode,
					patternMatchedInstanceNode, null)) {
				hasParentRelation = true;
				break;
			}
		}
		return hasParentRelation;
	}

	/**
	 * check whether the oneTreePatternFrequentNode already in the
	 * treePatternInstance. where the return is an entry, where the first key is
	 * the pattern sub tree, the value is the direct parent
	 * */
	private Map.Entry<TreeNode, TreeNode> isExistInCurrentTreePatternInstance(
			TreePatternInstance treePatternInstance,
			TreeNode oneTreePatternFrequentNode) {
		Map<TreeNode, Map<TreeNode, TreeNode>> matchedNodeTrees = treePatternInstance
				.getMatchedNodes();
		for (TreeNode patternNodeTree : matchedNodeTrees.keySet()) {
			Map<TreeNode, TreeNode> patternMatchedNodes = matchedNodeTrees
					.get(patternNodeTree);
			for (TreeNode patternNode : patternMatchedNodes.keySet()) {
				if (patternMatchedNodes.get(patternNode) == oneTreePatternFrequentNode) {
					return new AbstractMap.SimpleEntry<TreeNode, TreeNode>(
							patternNodeTree, patternNode);
				}
			}
		}
		return null;
	}

	/**
	 * This method used to generate the one node frequent pattern for each tree
	 * instance with the oneTreePatterns as inputs
	 * 
	 * return Map: the key is the TreeInstance. value is a map with key:
	 * patternNode, and value: List of TreeInstance.;
	 * 
	 * @param oneTreePatterns
	 */
	private Map<TreeNode, Map<TreeNode, List<TreePatternInstance>>> buildTreePatternInstances(
			List<TreePattern> oneTreePatterns) {
		Map<TreeNode, Map<TreeNode, List<TreePatternInstance>>> treeOneNodeFrequentPatterns = new HashMap<TreeNode, Map<TreeNode, List<TreePatternInstance>>>();

		for (TreePattern treePattern : oneTreePatterns) {
			List<TreeNode> patternNodes = treePattern.getTreePatterns();

			for (Entry<TreeNode, List<TreePatternInstance>> treePatternInstance : treePattern
					.getPatternInstances().entrySet()) {
				TreeNode treeInstance = treePatternInstance.getKey();
				Map<TreeNode, List<TreePatternInstance>> treePatternInstanceListMap = treeOneNodeFrequentPatterns
						.get(treeInstance);
				if (treePatternInstanceListMap == null) {
					treePatternInstanceListMap = new HashMap<TreeNode, List<TreePatternInstance>>();

					treeOneNodeFrequentPatterns.put(treeInstance,
							treePatternInstanceListMap);
				}
				List<TreePatternInstance> treePatternInstanceList = treePatternInstanceListMap
						.get(patternNodes.get(0));
				if (treePatternInstanceList == null) {
					treePatternInstanceList = new ArrayList<TreePatternInstance>();
					treePatternInstanceListMap.put(patternNodes.get(0),
							treePatternInstanceList);
				}
				treePatternInstanceList.addAll(treePatternInstance.getValue());
			}
		}
		return treeOneNodeFrequentPatterns;
	}

	/**
	 * @param treeNodeGroups
	 * @return
	 * 
	 *         mining one frequent node pattern,
	 */
	private List<TreePattern> mineOneSamePattern(
			Map<TreeNode, Map<TreeNode, List<TreeNode>>> treeNodeGroups) {
		List<TreePattern> oneTreePatterns = new ArrayList<TreePattern>();

		for (TreeNode candidateOneNode : treeNodeGroups.keySet()) {
			Map<TreeNode, List<TreeNode>> instanceMatchedNodes = treeNodeGroups
					.get(candidateOneNode);
			if (instanceMatchedNodes.size() >= this.frequencyThreshold) {
				List<TreeNode> oneNodePattern = new ArrayList<TreeNode>();
				oneNodePattern.add(candidateOneNode);

				TreePattern treePattern = new TreePattern();
				treePattern.addTreePatterns(oneNodePattern);

				for (TreeNode treeInstance : instanceMatchedNodes.keySet()) {
					for (TreeNode matchedNodesMap : instanceMatchedNodes
							.get(treeInstance)) {
						TreePatternInstance patternInstance = new TreePatternInstance();
						Map<TreeNode, TreeNode> matchedInstanceNodesMap = new HashMap<TreeNode, TreeNode>();
						matchedInstanceNodesMap.put(candidateOneNode,
								matchedNodesMap);
						patternInstance.addMatchedNode(candidateOneNode,
								matchedInstanceNodesMap);
						treePattern.addPatternInstance(treeInstance,
								patternInstance);
					}
				}
				oneTreePatterns.add(treePattern);
			}
		}

		// Set<TreePattern> builtPatterns = new HashSet<TreePattern>();
		// ListPermutation<Map<TreeNode, TreeNode>> treeNodePermutation = new
		// ListPermutation<Map<TreeNode, TreeNode>>();

		/**
		 * For each treePattern in oneTreePatterns;
		 * */
		// for (TreePattern treePattern : oneTreePatterns) {
		// TreeNode patternRootNode = treePattern.getTreePatterns().get(0);
		// Map<TreeNode, List<PatternTreeInstance>> patternInstances =
		// treePattern
		// .getPatternInstances();
		// for (TreeNode matchedInstance : patternInstances.keySet()) {
		// List<PatternTreeInstance> patternTreeInstances = patternInstances
		// .get(matchedInstance);
		// for (PatternTreeInstance patternTreeInstance : patternTreeInstances)
		// {
		//
		// /**
		// * Get every one pattern-matched node for each instance,
		// * */
		// Map<TreeNode, List<Map<TreeNode, TreeNode>>> patternMatchedNode =
		// patternTreeInstance
		// .getMatchedNodes();
		// List<Map<TreeNode, TreeNode>> matchedNodes = patternMatchedNode
		// .get(patternRootNode);
		//
		// if (matchedNodes.size() > MIN_SAME_EXTENSION_SIZE) {
		// List<List<Map<TreeNode, TreeNode>>> permutedTreeNodeList =
		// treeNodePermutation
		// .permuteSorted(matchedNodes,
		// MIN_SAME_EXTENSION_SIZE + 1,
		// matchedNodes.size());
		//
		// // check relation among the tree node list.
		//
		// for (List<Map<TreeNode, TreeNode>> permutedTreeNodes :
		// permutedTreeNodeList) {
		// List<TreeNode> extendedPatternsList = new ArrayList<TreeNode>();
		// int permutedTreeNodesSize = permutedTreeNodes
		// .size();
		//
		// for (int i = 0; i < permutedTreeNodesSize; i++) {
		// extendedPatternsList.add(this.treeNodeClone
		// .cloneNoChildren(patternRootNode));
		// }
		// List<TreeNode> shouldRemoveNodes = new ArrayList<TreeNode>();
		// for (int i = 0; i < permutedTreeNodesSize - 1; i++) {
		// Map<TreeNode, TreeNode> permutedMatchedNodeMap = permutedTreeNodes
		// .get(i);
		// TreeNode nodei = permutedMatchedNodeMap
		// .get(patternRootNode);
		// for (int j = i + 1; j < permutedTreeNodesSize; j++) {
		// TreeNode nodej = permutedTreeNodes.get(j)
		// .get(patternRootNode);
		// if (nodej.getParentTreeNode() == nodei) {
		// shouldRemoveNodes
		// .add(extendedPatternsList
		// .get(j));
		// extendedPatternsList.get(i).addChild(
		// extendedPatternsList.get(j));
		// }
		// }
		// }
		//
		// for (TreeNode removedNode : shouldRemoveNodes) {
		// extendedPatternsList.remove(removedNode);
		// }
		//
		// // check the new pattern whether in the
		// // builtPatterns
		// boolean hasBuilt = this.checkPatternInCollection(builtPatterns,
		// extendedPatternsList) == null;
		// if (hasBuilt) {
		// TreePattern newPattern = new TreePattern();
		//
		// // count the the frequency and the
		// // corresponding matched instance node....
		//
		// newPattern
		// .addTreePatterns(extendedPatternsList);
		// builtPatterns.add(newPattern);
		//
		//
		//
		//
		// System.out.println();
		// }
		// }
		// }
		// }
		// }
		// }

		return oneTreePatterns;
	}

	private TreePattern checkPatternInCollection(
			Collection<TreePattern> treePatterns, List<TreeNode> checkedPattern) {
		if (checkedPattern != null && treePatterns != null
				&& !treePatterns.isEmpty()) {
			for (TreePattern treePattern : treePatterns) {
				if (this.treePatternSimilarity.similar(
						treePattern.getTreePatterns(), checkedPattern) >= this.similarityThreshold) {
					return treePattern;
				}
			}
		}
		return null;
	}

	private TreePattern checkPatternInCollection(
			Collection<TreePattern> treePatterns, TreePattern checkedPattern) {
		return this.checkPatternInCollection(treePatterns,
				checkedPattern.getTreePatterns());
	}

	public ITreePatternSimilarity getTreePatternSimilarity() {
		return treePatternSimilarity;
	}

	public void setTreePatternSimilarity(
			ITreePatternSimilarity treePatternSimilarity) {
		this.treePatternSimilarity = treePatternSimilarity;
	}

	public ITreeNodeSimilarity getNodeSimilarity() {
		return nodeSimilarity;
	}

	public void setNodeSimilarity(ITreeNodeSimilarity nodeSimilarity) {
		this.nodeSimilarity = nodeSimilarity;
	}

	public double getFrequencyThreshold() {
		return frequencyThreshold;
	}

	public void setFrequencyThreshold(int frequencyThreshold) {
		this.frequencyThreshold = frequencyThreshold;
	}

	public double getSimilarityThreshold() {
		return similarityThreshold;
	}

	public void setSimilarityThreshold(double similarityThreshold) {
		this.similarityThreshold = similarityThreshold;
	}
}

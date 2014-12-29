/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import java.util.ArrayList;

/**
 * @author Lotay
 *
 */
public class AggregateTreeNode {
	private String preValue;
	private String postValue;
	private AggregateTreeNodeType changeNodeType = AggregateTreeNodeType.NORMAL;
	private ArrayList<AggregateTreeNode> children = new ArrayList<AggregateTreeNode>();

	public String getPreValue() {
		return preValue;
	}

	public void setPreValue(String preValue) {
		this.preValue = preValue;
	}

	public String getPostValue() {
		return postValue;
	}

	public void setPostValue(String postValue) {
		this.postValue = postValue;
	}

	public AggregateTreeNodeType getChangeNodeType() {
		return changeNodeType;
	}

	public void setChangeNodeType(AggregateTreeNodeType changeNodeType) {
		this.changeNodeType = changeNodeType;
	}

	public ArrayList<AggregateTreeNode> getChildren() {
		return children;
	}

	public void addChildren(AggregateTreeNode child) {
		this.children.add(child);
	}
}

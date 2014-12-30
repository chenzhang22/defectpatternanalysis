/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import java.util.ArrayList;

/**
 * @author Lotay
 *
 */
public class AggregateTypeNode {
	private String changeType = "NORMAL";
	private String postNodeType = null;
	private String preNodeType = null;
	private AggregateTypeNode parentNode = null;
	private ArrayList<AggregateTypeNode> children = new ArrayList<AggregateTypeNode>();

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getPostNodeType() {
		return postNodeType;
	}

	public void setPostNodeType(String postNodeType) {
		this.postNodeType = postNodeType;
	}

	public String getPreNodeType() {
		return preNodeType;
	}

	public void setPreNodeType(String preNodeType) {
		this.preNodeType = preNodeType;
	}

	public AggregateTypeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(AggregateTypeNode parentNode) {
		this.parentNode = parentNode;
	}

	public ArrayList<AggregateTypeNode> getChildren() {
		return children;
	}

	public void addChildNode(AggregateTypeNode child) {
		if(child!=null){
			this.children.add(child);
			child.setParentNode(this);
		}
	}

	public double similar(AggregateTypeNode treeNode) {
		double similarity = 0;
		if (treeNode != null) {
			if (this.changeType == null || treeNode.getChangeType() == null) {
				return similarity;
			}
			if (treeNode.getChangeType().equals(this.changeType)) {
				String postType = treeNode.getPostNodeType();
				String preType = treeNode.getPreNodeType();
				if (preType != null && postType != null) {
					if (preType.equals(this.preNodeType)) {
						similarity += 0.5;
					}
					if (postType.equals(this.postNodeType)) {
						similarity += 0.5;
					}
				} else if (preType != null) {
					if (preType.equals(this.preNodeType)) {
						similarity += 0.5;
					}
					if (this.postNodeType == null) {
						similarity += 0.3;
					} else {
						similarity += 0.5;
					}
				} else if (postType != null) {
					if (postType.equals(this.postNodeType)) {
						similarity += 0.5;
					}
					if (this.preNodeType == null) {
						similarity += 0.3;
					} else {
						similarity += 0.5;
					}
				} else {
					similarity = 0;
				}
			}
		}
		return 0;
	}
}

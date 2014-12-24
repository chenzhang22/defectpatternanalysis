/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import java.util.ArrayList;

/**
 * @author Lotay
 *
 */
@SuppressWarnings("serial")
public class CodeBlameLineRangeList extends ArrayList<CodeBlameLineRange> {
	private String repoName;
	private String revisionId;
	private String fileName;
	private ArrayList<Integer> bugList = new ArrayList<Integer>();

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ArrayList<Integer> getBugList() {
		return bugList;
	}

	public void addBugId(Integer bugId) {
		if (!this.bugList.contains(bugId)) {
			this.bugList.add(bugId);
		}
	}

	@Override
	public String toString() {
		StringBuffer toStr = new StringBuffer("CodeRangList [repoName="
				+ repoName + ", fileName=" + fileName + ",revision="
				+ this.revisionId + "]\n");
		for (int i = 0; i < this.size(); i++) {
			toStr.append(this.get(i) + "\n");
		}
		return toStr.toString();
	}
}

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
public class CodeRangList extends ArrayList<ChangeLineRange> {
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
		return "CodeRangList [repoName=" + repoName + ", revisionId="
				+ revisionId + ", fileName=" + fileName + ", toString()="
				+ super.toString() + "]";
	}
}

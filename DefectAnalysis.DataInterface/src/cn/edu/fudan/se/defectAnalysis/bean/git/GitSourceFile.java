/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.git;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Lotay
 *
 */
@SuppressWarnings("serial")
public class GitSourceFile implements Serializable {
	private String repoName;
	private String revisionId;
	private String fileName;
	private String newPath;
	private String oldPath;
	private String changeType;
	private int score;
	private Timestamp time;

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

	public String getNewPath() {
		return newPath;
	}

	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}

	public String getOldPath() {
		return oldPath;
	}

	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((repoName == null) ? 0 : repoName.hashCode());
		result = prime * result
				+ ((revisionId == null) ? 0 : revisionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GitSourceFile)) {
			return false;
		}
		GitSourceFile other = (GitSourceFile) obj;
		if (fileName == null) {
			if (other.fileName != null) {
				return false;
			}
		} else if (!fileName.equals(other.fileName)) {
			return false;
		}
		if (repoName == null) {
			if (other.repoName != null) {
				return false;
			}
		} else if (!repoName.equals(other.repoName)) {
			return false;
		}
		if (revisionId == null) {
			if (other.revisionId != null) {
				return false;
			}
		} else if (!revisionId.equals(other.revisionId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GitSourceFile [revisionId=" + revisionId + ", fileName="
				+ fileName + ", newPath=" + newPath + ", oldPath=" + oldPath
				+ ", changeType=" + changeType + ", score=" + score + ", time="
				+ time + "]";
	}
}

/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.git;

/**
 * @author Lotay
 *
 */
public class GitAuthor {
	private String authorID;
	private String authorName;
	/**
	 * @return the authorID
	 */
	public String getAuthorID() {
		return authorID;
	}
	/**
	 * @param authorID the authorID to set
	 */
	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}
	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}
	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GitAuthor [authorID=" + authorID + ", authorName=" + authorName
				+ "]";
	}
}

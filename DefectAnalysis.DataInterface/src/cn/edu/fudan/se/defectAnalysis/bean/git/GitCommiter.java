/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.git;

import java.sql.Timestamp;

/**
 * @author Lotay
 *
 */
public class GitCommiter {
	private String committerId;
	private String committerName;
	private Timestamp when;
	public String getCommitterId() {
		return committerId;
	}
	public void setCommitterId(String committerId) {
		this.committerId = committerId;
	}
	public String getCommitterName() {
		return committerName;
	}
	public void setCommitterName(String commiterName) {
		this.committerName = commiterName;
	}
	public Timestamp getWhen() {
		return when;
	}
	public void setWhen(Timestamp when) {
		this.when = when;
	}
}

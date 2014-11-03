/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.track;

import java.io.Serializable;

/**
 * @author Lotay
 * 
 */
@SuppressWarnings("serial")
public class DiffJDiffEntity extends DiffEntity implements Serializable {
	private int bugId; // primary keys
	private String inducedRevisionId; // primary keys
	private String fixedRevisionId; // primary keys
	private String fileName; // primary keys
	private String basicType; // primary keys
	private String changeType;
	private String message;
	private int inducedStartLineNumber; // primary keys
	private int inducedStartcolumnNumber; // primary keys
	private int inducedEndLineNumber; // primary keys
	private int inducedEndcolumnNumber; // primary keys
	private int fixedStartLineNumber = -1;
	private int fixedStartcolumnNumber = -1;
	private int fixedEndLineNumber = -1;
	private int fixedEndcolumnNumber = -1;

	/**
	 * @return the bugId
	 */
	public int getBugId() {
		return bugId;
	}

	/**
	 * @param bugId
	 *            the bugId to set
	 */
	public void setBugId(int bugId) {
		this.bugId = bugId;
	}

	/**
	 * @return the inducedRevisionId
	 */
	public String getInducedRevisionId() {
		return inducedRevisionId;
	}

	/**
	 * @param inducedRevisionId
	 *            the inducedRevisionId to set
	 */
	public void setInducedRevisionId(String inducedRevisionId) {
		this.inducedRevisionId = inducedRevisionId;
	}

	/**
	 * @return the fixedRevisionId
	 */
	public String getFixedRevisionId() {
		return fixedRevisionId;
	}

	/**
	 * @param fixedRevisionId
	 *            the fixedRevisionId to set
	 */
	public void setFixedRevisionId(String fixedRevisionId) {
		this.fixedRevisionId = fixedRevisionId;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the basicType
	 */
	public String getBasicType() {
		return basicType;
	}

	/**
	 * @param basicType
	 *            the basicType to set
	 */
	public void setBasicType(String basicType) {
		this.basicType = basicType;
	}

	/**
	 * @return the changeType
	 */
	public String getChangeType() {
		return changeType;
	}

	/**
	 * @param changeType
	 *            the changeType to set
	 */
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the inducedStartLineNumber
	 */
	public int getInducedStartLineNumber() {
		return inducedStartLineNumber;
	}

	/**
	 * @param inducedStartLineNumber
	 *            the inducedStartLineNumber to set
	 */
	public void setInducedStartLineNumber(int inducedStartLineNumber) {
		this.inducedStartLineNumber = inducedStartLineNumber;
	}

	/**
	 * @return the inducedStartcolumnNumber
	 */
	public int getInducedStartcolumnNumber() {
		return inducedStartcolumnNumber;
	}

	/**
	 * @param inducedStartcolumnNumber
	 *            the inducedStartcolumnNumber to set
	 */
	public void setInducedStartcolumnNumber(int inducedStartcolumnNumber) {
		this.inducedStartcolumnNumber = inducedStartcolumnNumber;
	}

	/**
	 * @return the inducedEndLineNumber
	 */
	public int getInducedEndLineNumber() {
		return inducedEndLineNumber;
	}

	/**
	 * @param inducedEndLineNumber
	 *            the inducedEndLineNumber to set
	 */
	public void setInducedEndLineNumber(int inducedEndLineNumber) {
		this.inducedEndLineNumber = inducedEndLineNumber;
	}

	/**
	 * @return the inducedEndcolumnNumber
	 */
	public int getInducedEndcolumnNumber() {
		return inducedEndcolumnNumber;
	}

	/**
	 * @param inducedEndcolumnNumber
	 *            the inducedEndcolumnNumber to set
	 */
	public void setInducedEndcolumnNumber(int inducedEndcolumnNumber) {
		this.inducedEndcolumnNumber = inducedEndcolumnNumber;
	}

	/**
	 * @return the fixedStartLineNumber
	 */
	public int getFixedStartLineNumber() {
		return fixedStartLineNumber;
	}

	/**
	 * @param fixedStartLineNumber
	 *            the fixedStartLineNumber to set
	 */
	public void setFixedStartLineNumber(int fixedStartLineNumber) {
		this.fixedStartLineNumber = fixedStartLineNumber;
	}

	/**
	 * @return the fixedStartcolumnNumber
	 */
	public int getFixedStartcolumnNumber() {
		return fixedStartcolumnNumber;
	}

	/**
	 * @param fixedStartcolumnNumber
	 *            the fixedStartcolumnNumber to set
	 */
	public void setFixedStartcolumnNumber(int fixedStartcolumnNumber) {
		this.fixedStartcolumnNumber = fixedStartcolumnNumber;
	}

	/**
	 * @return the fixedEndLineNumber
	 */
	public int getFixedEndLineNumber() {
		return fixedEndLineNumber;
	}

	/**
	 * @param fixedEndLineNumber
	 *            the fixedEndLineNumber to set
	 */
	public void setFixedEndLineNumber(int fixedEndLineNumber) {
		this.fixedEndLineNumber = fixedEndLineNumber;
	}

	/**
	 * @return the fixedEndcolumnNumber
	 */
	public int getFixedEndcolumnNumber() {
		return fixedEndcolumnNumber;
	}

	/**
	 * @param fixedEndcolumnNumber
	 *            the fixedEndcolumnNumber to set
	 */
	public void setFixedEndcolumnNumber(int fixedEndcolumnNumber) {
		this.fixedEndcolumnNumber = fixedEndcolumnNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((basicType == null) ? 0 : basicType.hashCode());
		result = prime * result + bugId;
		result = prime * result
				+ ((diffMethod == null) ? 0 : diffMethod.hashCode());
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((fixedRevisionId == null) ? 0 : fixedRevisionId.hashCode());
		result = prime * result + inducedEndLineNumber;
		result = prime * result + inducedEndcolumnNumber;
		result = prime
				* result
				+ ((inducedRevisionId == null) ? 0 : inducedRevisionId
						.hashCode());
		result = prime * result + inducedStartLineNumber;
		result = prime * result + inducedStartcolumnNumber;
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
		if (!(obj instanceof DiffJDiffEntity)) {
			return false;
		}
		DiffJDiffEntity other = (DiffJDiffEntity) obj;
		if (basicType == null) {
			if (other.basicType != null) {
				return false;
			}
		} else if (!basicType.equals(other.basicType)) {
			return false;
		}
		if (bugId != other.bugId) {
			return false;
		}
		if (diffMethod == null) {
			if (other.diffMethod != null) {
				return false;
			}
		} else if (!diffMethod.equals(other.diffMethod)) {
			return false;
		}
		if (fileName == null) {
			if (other.fileName != null) {
				return false;
			}
		} else if (!fileName.equals(other.fileName)) {
			return false;
		}
		if (fixedRevisionId == null) {
			if (other.fixedRevisionId != null) {
				return false;
			}
		} else if (!fixedRevisionId.equals(other.fixedRevisionId)) {
			return false;
		}
		if (inducedEndLineNumber != other.inducedEndLineNumber) {
			return false;
		}
		if (inducedEndcolumnNumber != other.inducedEndcolumnNumber) {
			return false;
		}
		if (inducedRevisionId == null) {
			if (other.inducedRevisionId != null) {
				return false;
			}
		} else if (!inducedRevisionId.equals(other.inducedRevisionId)) {
			return false;
		}
		if (inducedStartLineNumber != other.inducedStartLineNumber) {
			return false;
		}
		if (inducedStartcolumnNumber != other.inducedStartcolumnNumber) {
			return false;
		}
		return true;
	}
}

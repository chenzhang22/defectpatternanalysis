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
public class ChangeDistillerDiffEntity extends DiffEntity implements
		Serializable {
	private int bugId = -1; // primary keys
	private String lastRevisionId; // primary keys	inducedRevisionId
	private String fixedRevisionId; // primary keys
	private String fileName; // primary keys
	private String basicChangeType; // primary keys
	private String changeEntityType; // primary keys
	private int startCode = -1; // primary keys
	private int endCode = -1; // primary keys
	private int modifiers = -1;
	private int startLine = -1;
	private int startColumn = -1;
	private int endLine = -1;
	private int endColumn = -1;
	private int startNewCode = -1;
	private int endNewCode = -1;
	private int startNewLine = -1;
	private int startNewColumn = -1;
	private int endNewLine = -1;
	private int endNewColumn = -1;
	private String uniqueName;
	private String newUniqueName;
	private String changeType;
	private String newEntityType;
	private boolean bodyChange;
	private boolean declarationChange;
	private String significance;
	private int newModifiers = -1;
	private String parentEntity;
	private int pStartCode = -1;
	private int pEndCode = -1;
	private int pStartLine = -1;
	private int pStartColumn = -1;
	private int pEndLine = -1;
	private int pEndColumn = -1;
	private int pStartNewCode = -1;
	private int pEndNewCode = -1;
	private int pStartNewLine = -1;
	private int pStartNewColumn = -1;
	private int pEndNewLine = -1;
	private int pEndNewColumn = -1;
	private String pType;
	private String pNewType;
	private String pUniqueName;
	private String pNewUniqueName;

	public int getBugId() {
		return bugId;
	}

	public void setBugId(int bugId) {
		this.bugId = bugId;
	}

	public String getLastRevisionId() {
		return lastRevisionId;
	}

	public void setLastRevisionId(String inducedRevisionId) {
		this.lastRevisionId = inducedRevisionId;
	}

	public String getFixedRevisionId() {
		return fixedRevisionId;
	}

	public void setFixedRevisionId(String fixedRevisionId) {
		this.fixedRevisionId = fixedRevisionId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBasicChangeType() {
		return basicChangeType;
	}

	public void setBasicChangeType(String basicChangeType) {
		this.basicChangeType = basicChangeType;
	}

	public String getChangeEntityType() {
		return changeEntityType;
	}

	public void setChangeEntityType(String changeEntityType) {
		this.changeEntityType = changeEntityType;
	}

	public int getStartCode() {
		return startCode;
	}

	public void setStartCode(int startCode) {
		this.startCode = startCode;
	}

	public int getEndCode() {
		return endCode;
	}

	public void setEndCode(int endCode) {
		this.endCode = endCode;
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	public int getStartNewCode() {
		return startNewCode;
	}

	public void setStartNewCode(int startNewCode) {
		this.startNewCode = startNewCode;
	}

	public int getEndNewCode() {
		return endNewCode;
	}

	public void setEndNewCode(int endNewCode) {
		this.endNewCode = endNewCode;
	}

	public int getStartNewLine() {
		return startNewLine;
	}

	public void setStartNewLine(int startNewLine) {
		this.startNewLine = startNewLine;
	}

	public int getStartNewColumn() {
		return startNewColumn;
	}

	public void setStartNewColumn(int startNewColumn) {
		this.startNewColumn = startNewColumn;
	}

	public int getEndNewLine() {
		return endNewLine;
	}

	public void setEndNewLine(int endNewLine) {
		this.endNewLine = endNewLine;
	}

	public int getEndNewColumn() {
		return endNewColumn;
	}

	public void setEndNewColumn(int endNewColumn) {
		this.endNewColumn = endNewColumn;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getNewUniqueName() {
		return newUniqueName;
	}

	public void setNewUniqueName(String newUniqueName) {
		this.newUniqueName = newUniqueName;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getNewEntityType() {
		return newEntityType;
	}

	public void setNewEntityType(String newEntityType) {
		this.newEntityType = newEntityType;
	}

	public boolean isBodyChange() {
		return bodyChange;
	}

	public void setBodyChange(boolean bodyChange) {
		this.bodyChange = bodyChange;
	}

	public boolean isDeclarationChange() {
		return declarationChange;
	}

	public void setDeclarationChange(boolean declarationChange) {
		this.declarationChange = declarationChange;
	}

	public String getSignificance() {
		return significance;
	}

	public void setSignificance(String significance) {
		this.significance = significance;
	}

	public int getNewModifiers() {
		return newModifiers;
	}

	public void setNewModifiers(int newModifiers) {
		this.newModifiers = newModifiers;
	}

	public String getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(String parentEntity) {
		this.parentEntity = parentEntity;
	}

	public int getpStartCode() {
		return pStartCode;
	}

	public void setpStartCode(int pStartCode) {
		this.pStartCode = pStartCode;
	}

	public int getpEndCode() {
		return pEndCode;
	}

	public void setpEndCode(int pEndCode) {
		this.pEndCode = pEndCode;
	}

	public int getpStartLine() {
		return pStartLine;
	}

	public void setpStartLine(int pStartLine) {
		this.pStartLine = pStartLine;
	}

	public int getpStartColumn() {
		return pStartColumn;
	}

	public void setpStartColumn(int pStartColumn) {
		this.pStartColumn = pStartColumn;
	}

	public int getpEndLine() {
		return pEndLine;
	}

	public void setpEndLine(int pEndLine) {
		this.pEndLine = pEndLine;
	}

	public int getpEndColumn() {
		return pEndColumn;
	}

	public void setpEndColumn(int pEndColumn) {
		this.pEndColumn = pEndColumn;
	}

	public int getpStartNewCode() {
		return pStartNewCode;
	}

	public void setpStartNewCode(int pStartNewCode) {
		this.pStartNewCode = pStartNewCode;
	}

	public int getpEndNewCode() {
		return pEndNewCode;
	}

	public void setpEndNewCode(int pEndNewCode) {
		this.pEndNewCode = pEndNewCode;
	}

	public int getpStartNewLine() {
		return pStartNewLine;
	}

	public void setpStartNewLine(int pStartNewLine) {
		this.pStartNewLine = pStartNewLine;
	}

	public int getpStartNewColumn() {
		return pStartNewColumn;
	}

	public void setpStartNewColumn(int pStartNewColumn) {
		this.pStartNewColumn = pStartNewColumn;
	}

	public int getpEndNewLine() {
		return pEndNewLine;
	}

	public void setpEndNewLine(int pEndNewLine) {
		this.pEndNewLine = pEndNewLine;
	}

	public int getpEndNewColumn() {
		return pEndNewColumn;
	}

	public void setpEndNewColumn(int pEndNewColumn) {
		this.pEndNewColumn = pEndNewColumn;
	}

	public String getpType() {
		return pType;
	}

	public void setpType(String pType) {
		this.pType = pType;
	}

	public String getpNewType() {
		return pNewType;
	}

	public void setpNewType(String pNewType) {
		this.pNewType = pNewType;
	}

	public String getpUniqueName() {
		return pUniqueName;
	}

	public void setpUniqueName(String pUniqueName) {
		this.pUniqueName = pUniqueName;
	}

	public String getpNewUniqueName() {
		return pNewUniqueName;
	}

	public void setpNewUniqueName(String pNewUniqueName) {
		this.pNewUniqueName = pNewUniqueName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((basicChangeType == null) ? 0 : basicChangeType.hashCode());
		result = prime * result + bugId;
		result = prime
				* result
				+ ((changeEntityType == null) ? 0 : changeEntityType.hashCode());
		result = prime * result
				+ ((diffMethod == null) ? 0 : diffMethod.hashCode());
		result = prime * result + endCode;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((fixedRevisionId == null) ? 0 : fixedRevisionId.hashCode());
		result = prime
				* result
				+ ((lastRevisionId == null) ? 0 : lastRevisionId.hashCode());
		result = prime * result + startCode;
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
		if (!(obj instanceof ChangeDistillerDiffEntity)) {
			return false;
		}
		ChangeDistillerDiffEntity other = (ChangeDistillerDiffEntity) obj;
		if (basicChangeType == null) {
			if (other.basicChangeType != null) {
				return false;
			}
		} else if (!basicChangeType.equals(other.basicChangeType)) {
			return false;
		}
		if (bugId != other.bugId) {
			return false;
		}
		if (changeEntityType == null) {
			if (other.changeEntityType != null) {
				return false;
			}
		} else if (!changeEntityType.equals(other.changeEntityType)) {
			return false;
		}
		if (diffMethod == null) {
			if (other.diffMethod != null) {
				return false;
			}
		} else if (!diffMethod.equals(other.diffMethod)) {
			return false;
		}
		if (endCode != other.endCode) {
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
		if (lastRevisionId == null) {
			if (other.lastRevisionId != null) {
				return false;
			}
		} else if (!lastRevisionId.equals(other.lastRevisionId)) {
			return false;
		}
		if (startCode != other.startCode) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChangeDistillerDiffEntity [diffMethod=" + diffMethod
				+ ", bugId=" + bugId + ", induceRevisionId=" + lastRevisionId
				+ ", fixedRevisionId=" + fixedRevisionId + ", fileName="
				+ fileName + ", basicChangeType=" + basicChangeType
				+ ", changeEntityType=" + changeEntityType + ", modifiers="
				+ modifiers + ", startCode=" + startCode + ", endCode="
				+ endCode + ", startLine=" + startLine + ", startColumn="
				+ startColumn + ", endLine=" + endLine + ", endColumn="
				+ endColumn + ", startNewCode=" + startNewCode
				+ ", endNewCode=" + endNewCode + ", startNewLine="
				+ startNewLine + ", startNewColumn=" + startNewColumn
				+ ", endNewLine=" + endNewLine + ", endNewColumn="
				+ endNewColumn + ", uniqueName=" + uniqueName + ", changeType="
				+ changeType + ", isBodyChange=" + bodyChange
				+ ", significance=" + significance + ", parentEntity="
				+ parentEntity + ", pStartCode=" + pStartCode + ", pEndCode="
				+ pEndCode + ", pStartLine=" + pStartLine + ", pStartColumn="
				+ pStartColumn + ", pEndLine=" + pEndLine + ", pEndColumn="
				+ pEndColumn + ", pStartNewCode=" + pStartNewCode
				+ ", pEndNewCode=" + pEndNewCode + ", pStartNewLine="
				+ pStartNewLine + ", pStartNewColumn=" + pStartNewColumn
				+ ", pEndNewLine=" + pEndNewLine + ", pEndNewColumn="
				+ pEndNewColumn + ", pType=" + pType + ", pUniqueName="
				+ pUniqueName + "]";
	}
}

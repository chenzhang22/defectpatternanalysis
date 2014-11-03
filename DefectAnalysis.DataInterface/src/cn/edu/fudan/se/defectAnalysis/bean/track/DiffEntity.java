/**
 * 
 */
package cn.edu.fudan.se.defectAnalysis.bean.track;

/**
 * @author Lotay
 *
 */
public abstract class DiffEntity {
	protected String diffMethod;	// primary keys
	
	/**
	 * @param diffMethod the diffMethod to set
	 */
	public void setDiffMethod(String diffMethod) {
		this.diffMethod = diffMethod;
	}

	/**
	 * @return the diffMethod
	 */
	public String getDiffMethod() {
		return this.getClass().getSimpleName();
	}
}

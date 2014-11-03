/**
 * 
 */
package cn.edu.fudan.se.defect.bugzilla.get;

import java.util.HashMap;
import java.util.Map;

import com.j2bugzilla.base.BugzillaMethod;

/**
 * @author Lotay
 * 
 */
public class GetBugzilla implements BugzillaMethod {
	private String get_method = null;

	private Map<Object, Object> resultMap = new HashMap<Object, Object>();
	private Map<Object, Object> params = new HashMap<Object, Object>();

	public GetBugzilla(int id, String get_method) {
		this.params.put("ids", id);
		this.get_method = get_method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2bugzilla.base.BugzillaMethod#getMethodName()
	 */
	@Override
	public String getMethodName() {
		return this.get_method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2bugzilla.base.BugzillaMethod#getParameterMap()
	 */
	@Override
	public Map<Object, Object> getParameterMap() {
		return params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.j2bugzilla.base.BugzillaMethod#setResultMap(java.util.Map)
	 */
	@Override
	public void setResultMap(Map<Object, Object> resultMap) {
		this.resultMap = resultMap;
	}

	/**
	 * @return the resultMap
	 */
	public Map<Object, Object> getResultMap() {
		return resultMap;
	}
}

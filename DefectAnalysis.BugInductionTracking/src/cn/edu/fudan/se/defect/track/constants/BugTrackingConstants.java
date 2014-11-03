/**
 * 
 */
package cn.edu.fudan.se.defect.track.constants;

import java.util.regex.Pattern;

import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;

/**
 * @author Lotay
 * 
 */
public class BugTrackingConstants {
	public static final String ECLIPSE_CORE_GIT_REPO_PATH = "E:/Codes/libs/Eclipse/eclipse.jdt.core/.git";
	public static final String FILTED_SOURCEFILE_STRING = "^(.*)test(.*)$";
	public static final Pattern FILTED_SOURCEFILE_PATTERN = Pattern.compile(
			FILTED_SOURCEFILE_STRING, Pattern.CASE_INSENSITIVE);
	public static final String BUG_FIXED_STATUS[] = { "RESOLVED", "VERIFIED" };
	public static final String HIBERNATE_CONF_PATH = DaoConstants.ECLIPSE_HIBERNATE_LOCATION_PATH;
	
	public static final String CODE_INSERTED = "CODE_INSERTED";
	public static final String CODE_MODIFIED = "CODE_MODIFIED";
	public static final String CODE_DELETED = "CODE_DELETED";
}

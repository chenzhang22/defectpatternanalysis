/**
 * 
 */
package cn.edu.fudan.se.git.explore.constants;

import java.util.regex.Pattern;

import cn.edu.fudan.se.defectAnalysis.constants.dao.DaoConstants;

/**
 * @author Lotay
 *
 */
public class GitExploreConstants {
	public static final String ECLIPSE_CORE_GIT_REPO_PATH = "E:/Codes/libs/Eclipse/eclipse.jdt.core/.git";
	public static final String TOMCAT_GIT_REPO_PATH = "E:/Codes/libs/Apache/tomcat/.git";
	public static final String FILTED_SOURCEFILE_STRING = "^(.*)test(.*)$";
	public static final Pattern FILTED_SOURCEFILE_PATTERN = Pattern.compile(
			FILTED_SOURCEFILE_STRING, Pattern.CASE_INSENSITIVE);

	public static final String SOURCE_FILE_SUFFIX = ".java";
	public static final String ECLIPSE_BUG_PATTERN_MATCH = "(.*\\[[0-9]+\\].*)|([0-9]+)|(.*bug.*[0-9]+.*)|(.*fix.*[0-9]+.*)|(.*patch.*[0-9]+.*)|([0-9]+.*)|(.*[0-9]+)|(.*#[0-9].*)";
	public static final String TOMCAT_BUG_PATTERN_MATCH = "https.*show_bug.*id=[0-9]+";

	public static final String HIBERNATE_CONF_PATH = DaoConstants.TOMCAT_HIBERNATE_LOCATION_PATH;
	public static final String BUG_PATTERN_MATCH = TOMCAT_BUG_PATTERN_MATCH;
	public static final String GIT_REPO_PATH = TOMCAT_GIT_REPO_PATH;

	public static final String COMPONENT_NAME = "Apache_Tomcat";

}

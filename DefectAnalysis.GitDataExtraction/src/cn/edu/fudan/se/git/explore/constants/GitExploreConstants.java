/**
 * 
 */
package cn.edu.fudan.se.git.explore.constants;

import java.util.regex.Pattern;

/**
 * @author Lotay
 *
 */
public class GitExploreConstants {
	public static final String ECLIPSE_CORE_GIT_REPO_PATH = "E:/Codes/libs/Eclipse/eclipse.jdt.core/.git";
	public static final String FILTED_SOURCEFILE_STRING = "^(.*)test(.*)$";
	public static final Pattern FILTED_SOURCEFILE_PATTERN = Pattern.compile(
			FILTED_SOURCEFILE_STRING, Pattern.CASE_INSENSITIVE);
	public static final String HIBERNATE_CONF_PATH = "conf/hibernate.cfg.xml";
	public static final String SOURCE_FILE_SUFFIX = ".java";
	public static final String BUG_PATTERN_MATCH = "(.*\\[[0-9]+\\].*)|([0-9]+)|(.*bug.*[0-9]+.*)|(.*fix.*[0-9]+.*)|(.*patch.*[0-9]+.*)|([0-9]+.*)|(.*[0-9]+)|(.*#[0-9].*)";
}

/**
 * 
 */
package cn.edu.fudan.se.defect.track.git.constants;

import java.util.regex.Pattern;

/**
 * @author Lotay
 *
 */
public class BugTrackingConstants {
	public static final String ECLIPSE_CORE_GIT_REPO_PATH = "E:/Codes/libs/Eclipse/eclipse.jdt.core/.git";
	public static final String FILTED_SOURCEFILE_STRING = "^(.*)test(.*)$";
	public static final Pattern FILTED_SOURCEFILE_PATTERN = Pattern.compile(FILTED_SOURCEFILE_STRING, Pattern.CASE_INSENSITIVE);
}

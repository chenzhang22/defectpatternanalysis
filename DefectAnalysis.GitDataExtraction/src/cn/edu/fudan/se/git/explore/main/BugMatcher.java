package cn.edu.fudan.se.git.explore.main;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.fudan.se.git.explore.constants.GitExploreConstants;

public class BugMatcher {
	private static final Pattern pattern = Pattern
			.compile(GitExploreConstants.TOMCAT_BUG_PATTERN_MATCH);
	private static final Pattern NumberPattern = Pattern.compile("[0-9]+");

	public static Set<Integer> fixedBugLink(String log) {
		Set<Integer> bugNumbers = new HashSet<Integer>();
		if (log != null) {
			log = log.toLowerCase();
			Matcher matcher = pattern.matcher(log);
			while (matcher.find()) {
				String matStr = matcher.group();
				Matcher bugMatcher = NumberPattern.matcher(matStr);
				while (bugMatcher.find()) {
					try {
						int bugId = Integer.parseInt(bugMatcher.group());
						bugNumbers.add(bugId);
					} catch (Exception e) {

					}
				}
			}
		}
		return bugNumbers;
	}

	public static void main(String[] args) {
		String log = "#34745: Incoming changes get in twice [345745]";
		fixedBugLink(log);
	}
}

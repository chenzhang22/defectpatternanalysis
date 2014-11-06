/**
 * 
 */
package cn.edu.fudan.se.defect.bugzilla.main;

import cn.edu.fudan.se.defect.bugzilla.extract.BugzillaMain;


/**
 * @author Lotay
 * 
 */
public class BugzillaTest {
	private static String url = "https://issues.apache.org/bugzilla";
	private static int bugId = 47919;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BugzillaMain main = new BugzillaMain();
		System.out.println(main.extractBugzilla(url, bugId));
	}

}

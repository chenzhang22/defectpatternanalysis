/**
 * 
 */
package cn.edu.fudan.se.defect.bugzilla.extract;

import com.j2bugzilla.base.BugzillaConnector;
import com.j2bugzilla.base.BugzillaException;
import com.j2bugzilla.base.BugzillaMethod;

/**
 * @author Lotay
 * 
 */
public class BugzillaExtractor {
	private BugzillaConnector conn;

	public BugzillaExtractor(String url) throws Exception {
		if (url == null || url.isEmpty()) {
			throw new Exception("The URL is null/empty.");
		}
		conn = new BugzillaConnector();
		conn.connectTo(url);
	}

	public BugzillaMethod executMethod(BugzillaMethod method) {
		try {
			conn.executeMethod(method);
		} catch (BugzillaException e) {
			e.printStackTrace();
			System.out.println("invalidate bug id:" + e.getCause());
			if (!(e.getCause().toString().endsWith("does not exist.")||e.getCause().toString().endsWith("was found in the element content of the document."))) {
				System.exit(0);
			}
			return null;
		}
		return method;
	}
}

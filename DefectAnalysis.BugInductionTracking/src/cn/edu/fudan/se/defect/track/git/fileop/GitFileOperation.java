/**
 * 
 */
package cn.edu.fudan.se.defect.track.git.fileop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Lotay
 * 
 */
public class GitFileOperation {
	private static final String suffix = "java";

	public static void main(String[] args){
		String tempFileName = "tempFileName";
		byte[] data =tempFileName.getBytes();
		
		System.out.println(new GitFileOperation().byte2File(data, tempFileName));
		new GitFileOperation().deleteTempFile("C:\\Users\\Lotay\\AppData\\Local\\Temp\\tempFileName8890886761654397178java");
	}
		
	public String byte2File(final byte data[], String tempFileName) {
		File f = null;
		FileOutputStream o = null;
		try {
			f = File.createTempFile(tempFileName, suffix);
			System.out.println("f :"+f .getCanonicalPath());
			o = new FileOutputStream(f);
			o.write(data);
			o.close();
			return f.getAbsolutePath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (o != null) {
				try {
					o.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public void deleteTempFile(String tempFileName) {
		if (tempFileName != null) {
			File f = new File(tempFileName);
			if (f.exists()) {
				f.delete();
			}
		}
	}
}

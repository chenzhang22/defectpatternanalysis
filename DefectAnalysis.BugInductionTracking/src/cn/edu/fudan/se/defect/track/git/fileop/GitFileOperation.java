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
	private static final String tempFileName = "tempFileName";
	public static void main(String[] args){
		byte[] data =tempFileName.getBytes();
		
		System.out.println(new GitFileOperation().byte2File(data));
		new GitFileOperation().deleteTempFile("C:\\Users\\Lotay\\AppData\\Local\\Temp\\tempFileName8890886761654397178java");
	}
		
	public String byte2File(final byte data[]) {
		File f = null;
		FileOutputStream output = null;
		try {
			f = File.createTempFile(tempFileName, suffix);
			System.out.println("f :"+f .getCanonicalPath());
			output = new FileOutputStream(f);
			output.write(data);
			output.close();
			return f.getAbsolutePath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
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

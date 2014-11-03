package cn.edu.fudan.se.defect.track.blame.execute;

public class BlameMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		int startIndex = 0;
		int size = 10000;
		BlameTrace blameTrace = new BlameTrace(startIndex,size);
		blameTrace.blameTrace();
	}
}

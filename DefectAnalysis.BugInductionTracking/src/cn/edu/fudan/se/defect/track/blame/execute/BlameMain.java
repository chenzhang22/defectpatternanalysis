package cn.edu.fudan.se.defect.track.blame.execute;

public class BlameMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		int startIndex = 0+410+410+410;
		int size = 410;
		BlameTrace blameTrace = new BlameTrace(startIndex,size);
		blameTrace.blameTrace();
	}
}

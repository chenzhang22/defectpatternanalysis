/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.similarility;

/**
 * @author Lotay
 *
 */
public class EditDistance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String test1 = "插入一个字符，删除";
		String test2 = "字符，删除一个字符";
		System.out.println(EditDistance.minDistance(test1, test2));
	}

	public static int minDistance(String word1, String word2) {
		if (word1 == null && word2 == null) {
			return 0;
		}
		if (word1 == null) {
			return word2.length();
		}
		if (word2 == null) {
			return word1.length();
		}
		char ch1[] = word1.toCharArray();
		char ch2[] = word2.toCharArray();

		int len1 = ch1.length;
		int len2 = ch2.length;
		int preVectors[] = new int[len1 + 1];
		int curVectors[] = new int[len1 + 1];
		for (int i = 0; i < len1 + 1; i++) {
			preVectors[i] = i;
		}
		for (int i = 0; i < len2; i++) {
			curVectors[0] = i + 1;
			for (int j = 0; j < len1; j++) {
				if (ch1[j] == ch2[i]) {
					curVectors[j + 1] = preVectors[j];
				} else {
					int min = preVectors[j] < preVectors[j + 1] ? preVectors[j]
							: preVectors[j + 1];
					min = min < curVectors[j] ? min : curVectors[j];
					curVectors[j + 1] = min + 1;
				}
			}
			int tmp[] = preVectors;
			preVectors = curVectors;
			curVectors = tmp;
		}
		return preVectors[len1];
	}
}

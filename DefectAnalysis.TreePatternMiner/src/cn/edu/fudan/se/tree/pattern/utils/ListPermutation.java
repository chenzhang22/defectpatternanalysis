/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lotay
 *
 */
public class ListPermutation<T> {
	public static void main(String args[]) {
		List<String> elements = new ArrayList<String>();
		elements.add("A");
		elements.add("B");
		elements.add("C");
		elements.add("D");
		System.out.println(new ListPermutation<String>()
				.permuteSorted(elements));
	}

	public List<List<T>> permuteSorted(List<T> elements, int minSize,
			int maxSize) {
		List<List<T>> permutedSortedElements = new ArrayList<List<T>>();
		if (minSize < 0) {
			return permutedSortedElements;
		}
		for (int size = minSize; size <= elements.size() && size <= maxSize; size++) {
			this.permuteSorted(new ArrayList<T>(), elements, 0, size,
					permutedSortedElements);
		}
		return permutedSortedElements;
	}

	public List<List<T>> permuteSorted(List<T> elements) {
		return permuteSorted(elements, 0, elements.size());
	}

	public void permuteSorted(List<T> curElements, List<T> elements, int next,
			int left, List<List<T>> permutedSortedElements) {
		if (left == 0) {
			permutedSortedElements.add(curElements);
			return;
		}

		for (int index = next; index < elements.size() - left + 1
				&& index < elements.size(); index++) {
			List<T> nextElements = new ArrayList<T>(curElements);
			nextElements.add(elements.get(index));
			this.permuteSorted(nextElements, elements, index + 1, left - 1,
					permutedSortedElements);
		}
	}
}

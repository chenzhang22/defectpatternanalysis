/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Lotay
 * @param <K>
 * @param <V>
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapDecreasingComparator<K, V extends Comparable> implements
		Comparator<Map.Entry<K, V>> {
	@Override
	public int compare(Entry<K, V> entry1, Entry<K, V> entry2) {
		if (entry1.getValue().compareTo(entry2.getValue()) > 0) {
			return -1;
		} else if (entry1.getValue().compareTo(entry2.getValue()) < 0) {
			return 1;
		}
		return 0;
	}

	public static void main(String args[]) {
		HashMap<String, Integer> testMap = new HashMap<String, Integer>();
		testMap.put("A", 2);
		testMap.put("B", 1);
		testMap.put("C", 6);
		testMap.put("D", 4);
		testMap.put("E", 9);
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
				testMap.entrySet());
		System.out.println(list);
		Collections.sort(list, new MapDecreasingComparator<String, Integer>());
		System.out.println(list);
	}
}

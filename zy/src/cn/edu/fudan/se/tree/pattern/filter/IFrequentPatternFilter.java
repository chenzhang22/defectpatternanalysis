/**
 * 
 */
package cn.edu.fudan.se.tree.pattern.filter;

import java.util.List;

import cn.edu.fudan.se.tree.pattern.bean.TreePattern;

/**
 * @author Lotay
 *
 */
public interface IFrequentPatternFilter {
	public boolean filter(TreePattern treePattern);
	public void filter(List<TreePattern> treePatterns);
}

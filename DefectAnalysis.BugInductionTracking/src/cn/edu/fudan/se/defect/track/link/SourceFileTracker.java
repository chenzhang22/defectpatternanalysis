/**
 * 
 */
package cn.edu.fudan.se.defect.track.link;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitFiltedLink;
import cn.edu.fudan.se.defectAnalysis.dao.git.GitSourceFileDao;
import cn.edu.fudan.se.defectAnalysis.dao.link.LinkDao;

/**
 * @author Lotay
 * 
 */
public class SourceFileTracker {

	public void track2SourceFile() {
		LinkDao linkDao = new LinkDao();
		List<FixedBugCommitFiltedLink> filtedLinks = linkDao.listLinks();
		Map<Integer, Set<String>> mergedLinks = this.mergeLinks(filtedLinks);
		if (mergedLinks == null) {
			return;
		}
	}

	public Set<GitSourceFile> track2SourceFile(int bugId) {
		LinkDao linkDao = new LinkDao();
		List<FixedBugCommitFiltedLink> filtedLinks = linkDao
				.listLinkByBugId(bugId);
		Set<String> revisions = this.mergeOneBugLinks(filtedLinks);

		if (revisions != null) {
			return this.loadSourceFile(revisions);
		}
		return null;
	}

	private Set<String> mergeOneBugLinks(
			List<FixedBugCommitFiltedLink> filtedLinks) {
		if (filtedLinks == null || filtedLinks.isEmpty()) {
			return null;
		}
		Set<String> mergeLinks = new HashSet<String>();
		for (FixedBugCommitFiltedLink link : filtedLinks) {
			mergeLinks.add(link.getRevisionId());
		}
		return mergeLinks;
	}

	private Map<Integer, Set<String>> mergeLinks(
			List<FixedBugCommitFiltedLink> filtedLinks) {
		if (filtedLinks == null)
			return null;
		Map<Integer, Set<String>> mergedLinks = new HashMap<Integer, Set<String>>();

		for (FixedBugCommitFiltedLink link : filtedLinks) {
			Set<String> commits = mergedLinks.get(link.getBugId());
			if (commits == null) {
				commits = new HashSet<String>();
				mergedLinks.put(link.getBugId(), commits);
			}
			commits.add(link.getRevisionId());
		}
		return mergedLinks;
	}

	private Set<GitSourceFile> loadSourceFile(Set<String> bugRevisions) {
		if (bugRevisions == null)
			return null;
		GitSourceFileDao gitSourceFileDao = new GitSourceFileDao();
		Set<GitSourceFile> sourceFiles = new HashSet<GitSourceFile>();
		for (String rId : bugRevisions) {
			if (rId == null || rId.isEmpty()) {
				continue;
			}
			List<GitSourceFile> srcFiles = gitSourceFileDao
					.loadSourceFileByRevisionId(rId);
			sourceFiles.addAll(FileFilter.filter(srcFiles));
		}
		return sourceFiles;
	}

}

package cn.edu.fudan.se.git.explore.main;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import cn.edu.fudan.se.defectAnalysis.bean.bugzilla.BugzillaBug;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitAuthor;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitChange;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommitInfo;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitCommiter;
import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.bean.link.FixedBugCommitLink;
import cn.edu.fudan.se.defectAnalysis.dao.bugzilla.BugzillaBugDao;
import cn.edu.fudan.se.git.explore.constants.GitExploreConstants;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

public class GitExplore {
	static Repository repo;
	static Git git;
	static RevWalk walk;

	public static void main(String[] args) throws NoHeadException,
			GitAPIException {
		try {
			repo = new FileRepository(new File(
					GitExploreConstants.GIT_REPO_PATH));
			git = new Git(repo);
			walk = new RevWalk(repo);
			// allBugIds = loadBugIDs();

			track(GitExploreConstants.COMPONENT_NAME);

			// gitBlame();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @throws RevisionSyntaxException
	 */
	static void track(String component) throws RevisionSyntaxException {
		try {

			// List<Ref> branches = git.branchList().setListMode(ListMode.ALL)
			// .call();

			// Ref branch = branches.get(0);
			String postCommit = null;
			boolean hasStarted = true;
			for (RevCommit commit : git.log().all().call()) {

//				if (commit.getName().equals(
//						"bce35fda5537a3f0cc840685216b0d2e6f04882f")) {
//					hasStarted = true;
//				}
//				if (commit.getName().equals(
//						"7ef15a6f090fbec2b4af90203502553de5c56c17")) {
//					break;
//				}

				if (hasStarted) {
					Set<Object> gitObjects = extractCommit(commit, postCommit,
							component);
					HibernateUtils.saveAll(gitObjects,
							GitExploreConstants.HIBERNATE_CONF_PATH);
				}
				postCommit = commit.getName();
			}

			walk.dispose();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	static Set<Integer> loadBugIDs() {
		BugzillaBugDao dao = new BugzillaBugDao();
		Collection<BugzillaBug> bugs = dao
				.loadBugZillaBugs(GitExploreConstants.HIBERNATE_CONF_PATH);
		Set<Integer> bugIds = new HashSet<Integer>();
		for (BugzillaBug bug : bugs) {
			bugIds.add(bug.getId());
		}
		return bugIds;
	}

	/**
	 * @param commit
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws IOException
	 * @throws AmbiguousObjectException
	 * @throws RevisionSyntaxException
	 * @throws CorruptObjectException
	 * @throws GitAPIException
	 */
	private static Set<Object> extractCommit(RevCommit commit,
			String postCommit, String component) throws MissingObjectException,
			IncorrectObjectTypeException, IOException,
			AmbiguousObjectException, RevisionSyntaxException,
			CorruptObjectException, GitAPIException {

		Set<Object> gitObjs = new HashSet<Object>();
		String revisionId = null;
		if (commit == null || (revisionId = commit.getName()) == null) {
			return gitObjs;
		}
		System.out.println("revisionId:" + revisionId);
		boolean foundInThisBranch = false;
		String branchName = null;

		RevCommit targetCommit = walk.parseCommit(repo.resolve(revisionId));
		for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
			if (e.getKey().startsWith(Constants.R_HEADS)) {
				if (walk.isMergedInto(targetCommit,
						walk.parseCommit(e.getValue().getObjectId()))) {
					branchName = e.getValue().getName();
					foundInThisBranch = true;
					break;
				}
			}
		}
		TreeWalk treeWalk = new TreeWalk(repo);
		RevTree tree = commit.getTree();

		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);

		if (foundInThisBranch) {
			RevCommit prevTargetCommit = null;
			if (commit.getParents() != null && commit.getParents().length > 0) {
				prevTargetCommit = commit.getParents()[0];
			}
			GitCommitInfo commitInfo = null;
			if (prevTargetCommit == null) {
				commitInfo = extractCommitInfo(commit, postCommit, component,
						revisionId, branchName, null);
			} else {
				commitInfo = extractCommitInfo(commit, postCommit, component,
						revisionId, branchName, prevTargetCommit.getName());
			}
			if (commitInfo == null) {
				return gitObjs;
			}
			gitObjs.add(commitInfo);
			GitAuthor author = extractAuthor(commit);
			gitObjs.add(author);

			GitCommiter committer = extractCommitter(commit);
			gitObjs.add(committer);

			Set<Integer> bugIds = BugMatcher.fixedBugLink(commit
					.getFullMessage());
			for (Integer id : bugIds) {
				FixedBugCommitLink link = new FixedBugCommitLink();
				link.setBugId(id);
				link.setRevisionId(revisionId);
				gitObjs.add(link);
			}
			if (prevTargetCommit != null) {
				Set<Object> changeObjs = extractChange(commit, revisionId,
						prevTargetCommit);
				gitObjs.addAll(changeObjs);
			} else {
				System.out.println("First:" + revisionId);
				while (treeWalk.next()) {
					String fileName = treeWalk.getPathString();
					GitChange change = new GitChange();
					change.setFileName(fileName);
					change.setRevisionId(revisionId);
					change.setNewPath(fileName);
					change.setOldPath(null);
					change.setChangeType(ChangeType.ADD.name());
					change.setScore(0);
					gitObjs.add(change);
					if (fileName
							.endsWith(GitExploreConstants.SOURCE_FILE_SUFFIX)) {

						GitSourceFile sourceFile = new GitSourceFile();
						sourceFile.setFileName(fileName);
						sourceFile.setRevisionId(revisionId);
						sourceFile.setNewPath(fileName);
						sourceFile.setOldPath(null);
						sourceFile.setChangeType(ChangeType.ADD.name());
						sourceFile.setScore(0);
						long time = commit.getCommitTime();
						time = time * 1000;
						sourceFile.setTime(new Timestamp(time));
						gitObjs.add(sourceFile);
					}
				}
			}
		}
		return gitObjs;
	}

	/**
	 * @param commit
	 * @param revisionId
	 * @param prevTargetCommit
	 * @throws IncorrectObjectTypeException
	 * @throws IOException
	 * @throws GitAPIException
	 */
	private static Set<Object> extractChange(RevCommit commit,
			String revisionId, RevCommit prevTargetCommit)
			throws IncorrectObjectTypeException, IOException, GitAPIException {
		Set<Object> changeObjs = new HashSet<Object>();
		ObjectReader reader = repo.newObjectReader();
		CanonicalTreeParser currentTreeParser = new CanonicalTreeParser();
		currentTreeParser.reset(reader, commit.getTree());
		CanonicalTreeParser prevTreeParser = new CanonicalTreeParser();
		prevTreeParser.reset(reader, prevTargetCommit.getTree());

		List<DiffEntry> diffs = git.diff().setNewTree(currentTreeParser)
				.setOldTree(prevTreeParser).call();
		for (DiffEntry diff : diffs) {
			String newPath = diff.getNewPath();
			String oldPath = diff.getOldPath();

			String fileName = null;
			if (ChangeType.DELETE.name().equals((diff.getChangeType().name()))) {
				fileName = oldPath;
			} else if (newPath != null) {
				fileName = newPath;
			}
			if (fileName == null) {
				continue;
			}

			GitChange change = new GitChange();
			long time = commit.getCommitTime();
			time = time * 1000;
			change.setTime(new Timestamp(time));
			change.setFileName(fileName);
			change.setRevisionId(revisionId);
			change.setNewPath(newPath);
			change.setOldPath(oldPath);
			change.setChangeType(diff.getChangeType().name());
			change.setScore(diff.getScore());
			changeObjs.add(change);
			if (fileName.endsWith(GitExploreConstants.SOURCE_FILE_SUFFIX)) {
				GitSourceFile sourceFile = new GitSourceFile();
				sourceFile.setFileName(fileName);
				sourceFile.setRevisionId(revisionId);
				sourceFile.setNewPath(newPath);
				sourceFile.setOldPath(oldPath);
				sourceFile.setChangeType(diff.getChangeType().name());
				sourceFile.setScore(diff.getScore());
				sourceFile.setTime(new Timestamp(time));
				changeObjs.add(sourceFile);
			}
		}
		return changeObjs;
	}

	/**
	 * @param commit
	 * @return
	 */
	private static GitCommiter extractCommitter(RevCommit commit) {
		GitCommiter committer = new GitCommiter();
		committer.setCommitterId(commit.getCommitterIdent().getEmailAddress());
		committer.setCommitterName(commit.getCommitterIdent().getName());
		committer.setWhen(new Timestamp(commit.getCommitterIdent().getWhen()
				.getTime()));
		committer.setCommitterId(commit.getCommitterIdent().getEmailAddress());
		return committer;
	}

	/**
	 * @param commit
	 * @return
	 */
	private static GitAuthor extractAuthor(RevCommit commit) {
		GitAuthor author = new GitAuthor();
		author.setAuthorID(commit.getAuthorIdent().getEmailAddress());
		author.setAuthorName(commit.getAuthorIdent().getName());
		author.setWhen(new Timestamp(commit.getAuthorIdent().getWhen()
				.getTime()));
		return author;
	}

	/**
	 * @param commit
	 * @param postCommit
	 * @param component
	 * @param revisionId
	 * @param branchName
	 * @param prevTargetCommit
	 * @return
	 */
	private static GitCommitInfo extractCommitInfo(RevCommit commit,
			String postCommit, String component, String revisionId,
			String branchName, String previousRevision) {
		GitCommitInfo commitInfo = new GitCommitInfo();
		commitInfo.setAuthorID(commit.getAuthorIdent().getEmailAddress());
		commitInfo.setCommitterId(commit.getCommitterIdent().getEmailAddress());
		commitInfo.setRevisionID(revisionId);
		commitInfo.setBranchName(branchName);
		commitInfo.setComponent(component);
		commitInfo.setLog(commit.getFullMessage());
		commitInfo.setShortMsg(commit.getShortMessage());
		long time = commit.getCommitTime();
		time = time * 1000;
		commitInfo.setTime(new Timestamp(time));
		commitInfo.setPostRID(postCommit);
		commitInfo.setPreviousRID(previousRevision);
		return commitInfo;
	}

	public static void gitBlame() throws NoHeadException, GitAPIException,
			IOException {
		BlameCommand bcmd = git.blame();
		bcmd.setStartCommit(walk.parseCommit(repo
				.resolve("965ee55c83771df9f7f21b38b9a78903a86f630d")));
		bcmd.setFilePath("org.eclipse.jdt.apt.core/src/org/eclipse/jdt/apt/core/internal/generatedfile/GeneratedFileManager.java");
		BlameResult bresult = bcmd.call();
		System.out.println("getResultContents:"
				+ bresult.getResultContents().size());

		bresult.computeAll();
		System.out.println("getResultContents:"
				+ bresult.getResultContents().size());

		for (int i = 0; i < bresult.getResultContents().size(); i++) {
			System.out.println();
			System.out.println("getSourceLine:" + i + ">>"
					+ bresult.getSourceLine(i));
			System.out.println("getSourceCommit:"
					+ bresult.getSourceCommit(i).getName() + "\t"
					+ bresult.getSourceCommit(i).getShortMessage());
		}

		System.out.println(bresult);
	}
}

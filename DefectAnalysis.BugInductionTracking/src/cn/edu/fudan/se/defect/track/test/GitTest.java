package cn.edu.fudan.se.defect.track.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.WorkingTreeOptions;
import org.eclipse.jgit.util.io.AutoCRLFInputStream;

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;

public class GitTest {

	private static InputStream open(ObjectId blobId, Repository db)
			throws IOException, IncorrectObjectTypeException {
		if (blobId == null)
			return new ByteArrayInputStream(new byte[0]);

		try {
			WorkingTreeOptions workingTreeOptions = db.getConfig().get(
					WorkingTreeOptions.KEY);
			switch (workingTreeOptions.getAutoCRLF()) {
			case INPUT:
				// When autocrlf == input the working tree could be either CRLF
				// or LF, i.e. the comparison
				// itself should ignore line endings.
			case FALSE:
				return db.open(blobId, Constants.OBJ_BLOB).openStream();
			case TRUE:
			default:
				return new AutoCRLFInputStream(db.open(blobId,
						Constants.OBJ_BLOB).openStream(), true);
			}
		} catch (MissingObjectException notFound) {
			return null;
		}
	}

	static Repository repo;
	static Git git;

	public static void main(String[] args) {
		try {
			repo = new FileRepository(new File(
					BugTrackingConstants.ECLIPSE_CORE_GIT_REPO_PATH));
			git = new Git(repo);
			RevWalk walk = new RevWalk(repo);
			System.out.println(walk.parseCommit(repo.resolve("1b64b56ccf1417b4beca7bed7d97dae59a8cc803")).getShortMessage());
			track();
//			gitBlame();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * @throws RevisionSyntaxException
	 */
	static void track() throws RevisionSyntaxException {
		try {

			RevWalk walk = new RevWalk(repo);

			List<Ref> branches = git.branchList().setListMode(ListMode.ALL)
					.call();
			Ref branch = branches.get(0);
			String branchName = branch.getName();
			System.out.println("Commits of branch: " + branch.getName());
			System.out.println(walk.parseCommit(repo.resolve("1b64b56ccf1417b4beca7bed7d97dae59a8cc803")).getShortMessage());
			for (RevCommit commit : git.log().all().call()) {
				boolean foundInThisBranch = false;

				RevCommit targetCommit = walk.parseCommit(repo.resolve(commit
						.getName()));

				for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
					if (e.getKey().startsWith(Constants.R_HEADS)) {
						if (walk.isMergedInto(targetCommit,
								walk.parseCommit(e.getValue().getObjectId()))) {
							String foundInBranch = e.getValue().getName();
							if (branchName.equals(foundInBranch)) {
								foundInThisBranch = true;
								break;
							}
						}
					}
				}
				TreeWalk treeWalk = new TreeWalk(repo);
				RevTree tree = commit.getTree();

				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);

				
					@SuppressWarnings("static-access")
					TreeWalk w = treeWalk.forPath(repo,
							"", tree);
					System.out.println("found: " + w.getPathString());

					ObjectId id = w.getObjectId(0);
					InputStream is = open(id, repo);
					byte[] byteArray = IOUtils.toByteArray(is);
					String str = new String(byteArray);
					System.out.println(str);
				
				if (foundInThisBranch) {
					RevCommit prevTargetCommit = targetCommit.getParents()[0];
					if (prevTargetCommit != null) {
						System.out.println(commit.getName());

						ObjectReader reader = repo.newObjectReader();
						CanonicalTreeParser currentTreeParser = new CanonicalTreeParser();
						currentTreeParser.reset(reader, commit.getTree());

						CanonicalTreeParser prevTreeParser = new CanonicalTreeParser();
						prevTreeParser
								.reset(reader, prevTargetCommit.getTree());

						List<DiffEntry> diffs = git.diff()
								.setNewTree(currentTreeParser)
								.setOldTree(prevTreeParser).call();

						ByteArrayOutputStream out = new ByteArrayOutputStream();
						DiffFormatter df = new DiffFormatter(out);
						df.setRepository(git.getRepository());

						for (DiffEntry diff : diffs) {
							df.format(diff);
							diff.getOldId();
							out.reset();
						}

					} else {
						System.out.println(commit.getName());
					}
				}
			}

			walk.dispose();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public static void gitBlame() throws NoHeadException, GitAPIException,
			IOException {
		BlameCommand bcmd = git.blame();
		bcmd.setStartCommit(git.log().all().call().iterator().next());
		bcmd.setFilePath("org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/ReadManager.java");
		BlameResult bresult = bcmd.call();
		System.out.println("getResultContents:"+bresult.getResultContents().size());

		bresult.computeAll();
		System.out.println("getResultContents:"+bresult.getResultContents().size());

		for(int i=0;i<bresult.getResultContents().size();i++){
			System.out.println();
			System.out.println("getSourceLine:"+i+">>"+bresult.getSourceLine(i));
			System.out.println("getSourceCommit:"+bresult.getSourceCommit(i).getName()+"\t"+bresult.getSourceCommit(i).getShortMessage());
		}
		
		System.out.println(bresult);
	}
}

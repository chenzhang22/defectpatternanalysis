/**
 * 
 */
package cn.edu.fudan.se.defect.track.git;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.revwalk.RevCommit;

import cn.edu.fudan.se.defectAnalysis.bean.track.BugInduceBlameLine;
import cn.edu.fudan.se.defectAnalysis.bean.track.ChangeDistillerDiffEntity;
import cn.edu.fudan.se.defectAnalysis.bean.track.DiffEntity;
import cn.edu.fudan.se.defectAnalysis.bean.track.DiffJDiffEntity;

/**
 * @author Lotay
 * 
 */
public class GitBlameDiffFilter {

	/**
	 * @param args
	 * @throws GitAPIException
	 * @throws IOException
	 * @throws AmbiguousObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws MissingObjectException
	 * @throws RevisionSyntaxException
	 */
	public static void main(String[] args) throws RevisionSyntaxException,
			MissingObjectException, IncorrectObjectTypeException,
			AmbiguousObjectException, IOException, GitAPIException {
		// TODO Auto-generated method stub
		String startRevisionId = "95753835f26c983ebe5b704e4d93979ad7bfc13d";
		String fileName = "org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/parser/Parser.java";
		new GitBlameDiffFilter().changeLines(fileName, startRevisionId);
	}

	public List<DiffEntity> blameFilte(List<DiffEntity> entities,
			Set<String> filtedCommits, String lastRevisionId, String fileName,
			Set<BugInduceBlameLine> blameLines) throws RevisionSyntaxException,
			MissingObjectException, IncorrectObjectTypeException,
			AmbiguousObjectException, IOException, GitAPIException {
		if (filtedCommits == null) {
			return entities;
		}
		if (blameLines != null) {
			blameLines.clear();
		} else {
			return entities;
		}
		BlameCommand bcmd = GitUtils.git.blame();
		bcmd.setStartCommit(GitUtils.revWalk.parseCommit(GitUtils.repo
				.resolve(lastRevisionId)));
		bcmd.setFilePath(fileName);
		BlameResult bresult = bcmd.call();
		bresult.computeAll();

		List<DiffEntity> filtedEntities = new ArrayList<DiffEntity>();

		for (DiffEntity diffEntity : entities) {
			if (diffEntity instanceof DiffJDiffEntity) {
				DiffJDiffEntity diffjEntity = (DiffJDiffEntity) diffEntity;
				int startLine = diffjEntity.getInducedStartLineNumber();
				int endLine = diffjEntity.getInducedEndLineNumber();

				if (diffjEntity.getBasicType().equals("ADDED")
						&& !diffjEntity.getChangeType().equals("CODE_ADDED")) {
					continue;
				}
				int count = 0;
				for (int line = startLine - 1; line >= 0 && line < endLine; line++) {
					RevCommit commit = (bresult.getSourceCommit(line));
					if (commit == null) {
						continue;
					}
					String revisionName = commit.getName();
					if (filtedCommits.contains(revisionName)) {
						count++;
					} else {
						BugInduceBlameLine blameLine = new BugInduceBlameLine();
						blameLine.setBugId(diffjEntity.getBugId());
						blameLine.setInducedRevisionId(revisionName);
						blameLine.setFixedRevisionId(diffjEntity
								.getFixedRevisionId());
						blameLine.setFileName(diffjEntity.getFileName());
						blameLine.setInducedlineNumber(bresult
								.getSourceLine(line));
						blameLines.add(blameLine);
					}
				}
				if (count == endLine - startLine + 1) {
					filtedEntities.add(diffEntity);
				}
			} else if (diffEntity instanceof ChangeDistillerDiffEntity) {
				ChangeDistillerDiffEntity changeDistillerDiff = (ChangeDistillerDiffEntity) diffEntity;
				String changeType = changeDistillerDiff.getChangeType();
//				 Set<Integer>  changelines = this.changeLines(fileName, changeDistillerDiff.getFixedRevisionId());
				if (changeType == null) {
					continue;
				}
				if ("LINE_COMMENT".equals(changeType)) {
					filtedEntities.add(diffEntity);
					continue;
				}
				int startLine = changeDistillerDiff.getStartLine();
				int endLine = changeDistillerDiff.getEndLine();
				int count = 0;
				for (int line = startLine - 1; line >= 0 && line < endLine; line++) {
					RevCommit commit = (bresult.getSourceCommit(line));
					if (commit == null) {
						continue;
					}
					String revisionName = commit.getName();
					if (filtedCommits.contains(revisionName)) {
						count++;
					} else {
						BugInduceBlameLine blameLine = new BugInduceBlameLine();
						blameLine.setBugId(changeDistillerDiff.getBugId());
						blameLine.setInducedRevisionId(revisionName);
						blameLine.setFixedRevisionId(changeDistillerDiff
								.getFixedRevisionId());
						blameLine.setFileName(fileName);
						blameLine.setInducedlineNumber(bresult
								.getSourceLine(line) + 1);
						blameLine.setInducedTime(new Timestamp(new Date(bresult
								.getSourceCommit(line).getCommitTime())
								.getTime()));
						blameLines.add(blameLine);
					}
				}
				if (count == endLine - startLine + 1) {
					filtedEntities.add(diffEntity);
				}
			}
		}

		for (DiffEntity entity : filtedEntities) {
			if (entities.contains(entity)) {
				entities.remove(entity);
			}
		}
		return entities;
	}

	public Set<Integer> changeLines(String fileName, String revisionId)
			throws RevisionSyntaxException, MissingObjectException,
			IncorrectObjectTypeException, AmbiguousObjectException,
			IOException, GitAPIException {
		Set<Integer> changeLines = new HashSet<Integer>();
		if(revisionId==null||fileName==null){
			return changeLines;
		}
		BlameCommand bcmd = GitUtils.git.blame();
		bcmd.setStartCommit(GitUtils.revWalk.parseCommit(GitUtils.repo
				.resolve(revisionId)));
		bcmd.setFilePath(fileName);
		BlameResult bresult = bcmd.call();
		bresult.computeAll();
		
		int line =0;
		for(;line< bresult.getResultContents().size();line++){
			String revision = bresult.getSourceCommit(line).getName();
			if(revisionId.equals(revision)){
				changeLines.add(line+1);
				System.out.println("line:"+(line+1)+",revision:"+revision);
			}
		}
		
		return changeLines;
	}
}

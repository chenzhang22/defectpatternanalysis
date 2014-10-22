/**
 * 
 */
package cn.edu.fudan.se.defect.track.git;

import java.io.IOException;
import java.util.ArrayList;
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

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.BugInduceBlameLine;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffEntity;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffJDiffEntity;
import cn.edu.fudan.se.utils.hibernate.HibernateUtils;

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
		String startRevisionId = "bfaf9d23a02d05c466f9843a8f1857a7dba35c49";
		String fileName = "org.eclipse.jdt.core/model/org/eclipse/jdt/internal/core/util/HandleFactory.java";
		new GitBlameDiffFilter().blameFilte(null, null, startRevisionId,
				fileName);
	}

	public List<DiffEntity> blameFilte(List<DiffEntity> entities,
			Set<String> filtedCommits, String lastRevisionId, String fileName)
			throws RevisionSyntaxException, MissingObjectException,
			IncorrectObjectTypeException, AmbiguousObjectException,
			IOException, GitAPIException {
		if (filtedCommits == null || filtedCommits.isEmpty()) {
			return entities;
		}
		BlameCommand bcmd = GitUtils.git.blame();
		bcmd.setStartCommit(GitUtils.revWalk.parseCommit(GitUtils.repo
				.resolve(lastRevisionId)));
		bcmd.setFilePath(fileName);
		BlameResult bresult = bcmd.call();
		bresult.computeAll();

		List<DiffEntity> filtedEntities = new ArrayList<DiffEntity>();
		
		Set<BugInduceBlameLine> blameLines = new HashSet<BugInduceBlameLine>();
		
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
					String revisionName = bresult.getSourceCommit(line)
							.getName();
					if (filtedCommits.contains(revisionName)) {
						count++;
					} else {
						BugInduceBlameLine blameLine = new BugInduceBlameLine();
						blameLine.setBugId(diffjEntity.getBugId());
						blameLine.setInducedRevisionId(revisionName);
						blameLine.setFixedRevisionId(diffjEntity.getFixedRevisionId());
						blameLine.setFileName(diffjEntity.getFileName());
						blameLine.setInducedlineNumber(bresult.getSourceLine(line));
						blameLines.add(blameLine);
					}
				}
				if (count == endLine - startLine + 1) {
					filtedEntities.add(diffEntity);
				}
			}
		}
		
		
		//save blame line;
//		System.out.println("Saving blame lines:"+blameLines.size());
		HibernateUtils.saveAll(blameLines,
				BugTrackingConstants.HIBERNATE_CONF_PATH);
		
		for (DiffEntity entity : filtedEntities) {
			if (entities.contains(entity)) {
				entities.remove(entity);
			}
		}
		return entities;
	}

}

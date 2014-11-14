/**
 * 
 */
package cn.edu.fudan.se.defect.track.blame.execute;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.blame.BlameResult;

import cn.edu.fudan.se.defectAnalysis.bean.git.GitSourceFile;
import cn.edu.fudan.se.defectAnalysis.bean.track.CodeLineChangeBlock;

/**
 * @author Lotay
 *
 */
public class BlameLineChange {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BlameLineChange main = new BlameLineChange();
		main.extractChangeLine(null);
	}

	private BlameCMD blameCMD = new BlameCMD();

	public List<CodeLineChangeBlock> extractChangeLine(
			List<GitSourceFile> sourceFiles) {
		List<CodeLineChangeBlock> changeBlocks = new ArrayList<CodeLineChangeBlock>();
		if (sourceFiles == null) {
			return changeBlocks;
		}
		BlameResult blameResult = null;
		BlameResult preBlameResult = null;
		String preRevisionId = null;
		for (int i = 0; i < sourceFiles.size(); i++) {
			GitSourceFile sourceFile = sourceFiles.get(i);
			String revisionId = sourceFile.getRevisionId();
			String fileName = sourceFile.getFileName();
			if ("DELETE".equals(sourceFile.getChangeType())) {
				continue;
			}
			if("ADD".equals(sourceFile.getChangeType())){
				List<CodeLineChangeBlock> changes = extractBlameAdd(fileName,
						revisionId, blameResult);
				changeBlocks.addAll(changes);
				continue;
			}
			blameResult = blameCMD.gitBlame(revisionId, fileName);
//			System.out.println(i + "/" + sourceFiles.size());
			if (preBlameResult != null && preRevisionId != null) {
				List<CodeLineChangeBlock> changes = matchBlameDiff(fileName,
						revisionId, blameResult, preRevisionId, preBlameResult);
				changeBlocks.addAll(changes);
			} else {
				List<CodeLineChangeBlock> changes = extractBlameAdd(fileName,
						revisionId, blameResult);
				changeBlocks.addAll(changes);
			}

			preBlameResult = blameResult;
			preRevisionId = revisionId;
		}
		return changeBlocks;
	}

	private List<CodeLineChangeBlock> extractBlameAdd(String fileName,
			String revisionId, BlameResult blameResult) {
		List<CodeLineChangeBlock> changeBlocks = new ArrayList<CodeLineChangeBlock>();
		if (fileName == null || revisionId == null || blameResult == null) {
			return changeBlocks;
		}
		int thisVersionLOC = blameResult.getResultContents().size();
		CodeLineChangeBlock block = new CodeLineChangeBlock();
		block.setRevisionId(revisionId);
		block.setFileName(fileName);
		block.setCurStartLine(0);
		block.setCurEndLine(thisVersionLOC);
		changeBlocks.add(block);
		return changeBlocks;
	}

	private List<CodeLineChangeBlock> matchBlameDiff(String fileName,
			String revisionId, BlameResult blameResult,
			String comparedRevisionId, BlameResult preBlameResult) {
		int thisVersionLOC = blameResult.getResultContents().size();
		int preVersionLOC = preBlameResult.getResultContents().size();
		int thisVersionStartLine = 0, thisVersionLine = 0;
		int preVersionStartLine = 0, preVersionLine = 0;

		List<CodeLineChangeBlock> changeBlocks = new ArrayList<CodeLineChangeBlock>();

		while (thisVersionLine < thisVersionLOC) {
			String thisCodeContent = null;
			String thisRevisionId = null;
			String preCodeContent = null;
			String preRevisionId = null;

			// Skip the some code and change in the same commit.
			while (thisVersionLine < thisVersionLOC
					&& preVersionLine < preVersionLOC) {
				if (blameResult.getSourceCommit(thisVersionLine) == null
						|| preBlameResult.getSourceCommit(preVersionLine) == null) {
					return changeBlocks;
				}
				if ((thisRevisionId = blameResult.getSourceCommit(
						thisVersionLine).getName()) != null
						&& blameResult.getResultContents().getString(
								thisVersionLine) != null
						&& (thisCodeContent = blameResult.getResultContents()
								.getString(thisVersionLine)) != null
						&& preBlameResult.getSourceCommit(preVersionLine) != null
						&& (preRevisionId = preBlameResult.getSourceCommit(
								preVersionLine).getName()) != null
						&& preBlameResult.getResultContents().getString(
								preVersionLine) != null
						&& (preCodeContent = preBlameResult.getResultContents()
								.getString(preVersionLine)) != null
						&& thisCodeContent.equals(preCodeContent)
						&& thisRevisionId.equals(preRevisionId)) {
					thisVersionLine++;
					preVersionLine++;
				}else{
					break;
				}
			}

			boolean hasDiff = false;
			// Search the Delete code....
			if (thisVersionLine < thisVersionLOC && (thisRevisionId != null)
					&& !thisRevisionId.equals(revisionId)) {
				preVersionStartLine = preVersionLine;
				thisVersionStartLine = thisVersionLine;
				while (preVersionLine < preVersionLOC) {
					if ((preCodeContent = preBlameResult.getResultContents()
							.getString(preVersionLine)) != null
							&& preBlameResult.getSourceCommit(preVersionLine) != null
							&& (preRevisionId = preBlameResult.getSourceCommit(
									preVersionLine).getName()) != null
							&& preCodeContent.equals(thisCodeContent)
							&& preRevisionId.equals(thisRevisionId)) {
						break;
					}
					preVersionLine++;
				}
				if (preVersionLine == preVersionLOC) {
					thisVersionLine = thisVersionLOC;
				}
				hasDiff = true;
			} else if (thisVersionLine < thisVersionLOC
					&& (thisRevisionId != null)
					&& thisRevisionId.equals(revisionId)) {
				// search the insert code or update code.
				thisVersionStartLine = thisVersionLine;
				preVersionStartLine = preVersionLine;
				// Search the change Code in current revision
				while ((thisVersionLine < thisVersionLOC)
						&& blameResult.getSourceCommit(thisVersionLine) != null
						&& (thisRevisionId = blameResult.getSourceCommit(
								thisVersionLine).getName()) != null
						&& thisRevisionId.equals(revisionId)) {
					thisVersionLine++;
				}
				if (thisVersionLine < thisVersionLOC) {
					thisCodeContent = blameResult.getResultContents()
							.getString(thisVersionLine);
					// Search the corresponding code with the change codes.
					while ((preVersionLine < preVersionLOC
							&& preBlameResult.getSourceCommit(preVersionLine) != null
							&& (preRevisionId = preBlameResult.getSourceCommit(
									preVersionLine).getName()) != null && (preCodeContent = preBlameResult
							.getResultContents().getString(preVersionLine)) != null)
							&& (!thisCodeContent.equals(preCodeContent) || !thisRevisionId
									.equals(preRevisionId))) {
						preVersionLine++;
					}
				} else { // all the left code have been change.
					preVersionLine = preVersionLOC;
				}
				if (preVersionLine == preVersionLOC) {
					thisVersionLine = thisVersionLOC;
				}
				hasDiff = true;
			}
			if (hasDiff) {
				CodeLineChangeBlock block = new CodeLineChangeBlock();
				block.setFileName(fileName);
				block.setRevisionId(revisionId);
				block.setPreRevisionId(comparedRevisionId);
				block.setCurStartLine(thisVersionStartLine);
				block.setCurEndLine(thisVersionLine);
				block.setPreStartLine(preVersionStartLine);
				block.setPreEndLine(preVersionLine);
				changeBlocks.add(block);
			}
		}
		return changeBlocks;
	}
}

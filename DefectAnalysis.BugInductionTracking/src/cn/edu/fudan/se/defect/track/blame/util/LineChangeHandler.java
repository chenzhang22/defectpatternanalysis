/**
 * 
 */
package cn.edu.fudan.se.defect.track.blame.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.blame.BlameResult;

/**
 * @author Lotay
 *
 */
public class LineChangeHandler {
	enum LineType {
		CODE, LINE_COMMENT, BLOCK_COMMENT, CODE_AND_LINE_COMMENT, CODE_AND_BLOCK_COMMENT_START, CODE_AND_BLOCK_COMMENT_END, CODE_AND_BLOCK_COMMENT_END_CODE
	}

	public Map<Integer, Boolean> lineChange(BlameResult blameResult,
			String revisionId) {
		Map<Integer, Boolean> changeLines = new HashMap<Integer, Boolean>();

		LineType originalType = LineType.CODE;
		for (int i = 0; i < blameResult.getResultContents().size(); i++) {
			String line = blameResult.getResultContents().getString(i);
			line = line.trim();
			originalType = this.isInComment(line, originalType);
			if (revisionId.equals(blameResult.getSourceCommit(i).getName())) {
				if(line.isEmpty()||line.equals("")){
					changeLines.put(i, false);
					continue;
				}
				switch (originalType) {
				case CODE:
				case CODE_AND_LINE_COMMENT:
				case CODE_AND_BLOCK_COMMENT_START:
				case CODE_AND_BLOCK_COMMENT_END_CODE:
					changeLines.put(i, true);
					break;
				default:
					changeLines.put(i, false);
					break;
				}
			}

			if (originalType.equals(LineType.CODE_AND_BLOCK_COMMENT_START)) {
				originalType = LineType.BLOCK_COMMENT;
			} else if (originalType
					.equals(LineType.CODE_AND_BLOCK_COMMENT_END_CODE)
					|| originalType.equals(LineType.CODE_AND_BLOCK_COMMENT_END)
					|| originalType.equals(LineType.CODE_AND_LINE_COMMENT)
					|| originalType.equals(LineType.LINE_COMMENT)) {
				originalType = LineType.CODE;
			}
		}

		return changeLines;
	}

	public Map<Integer, Boolean> lineType(BlameResult blameResult) {
		Map<Integer, Boolean> changeTypes = new HashMap<Integer, Boolean>();
		LineType originalType = LineType.CODE;
		for (int i = 0; i < blameResult.getResultContents().size(); i++) {
			String line = blameResult.getResultContents().getString(i);
			line = line.trim();
			originalType = this.isInComment(line, originalType);
			if(line.isEmpty()||line.equals("")){
				changeTypes.put(i, false);
				continue;
			}
			switch (originalType) {
			case CODE:
			case CODE_AND_LINE_COMMENT:
			case CODE_AND_BLOCK_COMMENT_START:
			case CODE_AND_BLOCK_COMMENT_END_CODE:
				changeTypes.put(i, true);
				break;
			default:
				changeTypes.put(i, false);
				break;
			}
			if (originalType.equals(LineType.CODE_AND_BLOCK_COMMENT_START)) {
				originalType = LineType.BLOCK_COMMENT;
			} else if (originalType
					.equals(LineType.CODE_AND_BLOCK_COMMENT_END_CODE)
					|| originalType.equals(LineType.CODE_AND_BLOCK_COMMENT_END)
					|| originalType.equals(LineType.CODE_AND_LINE_COMMENT)
					|| originalType.equals(LineType.LINE_COMMENT)) {
				originalType = LineType.CODE;
			}
		}
		return changeTypes;
	}

	private LineType isInComment(String line, LineType originalType) {
		String trimStr = line.trim();
		if (originalType.equals(LineType.CODE)) {
			if (trimStr.startsWith("//")) {
				return LineType.LINE_COMMENT;
			} else if (trimStr.contains("//")) {
				return LineType.CODE_AND_LINE_COMMENT;
			} else if (trimStr.startsWith("/*")) {
				if (trimStr.endsWith("*/")) {
					return LineType.LINE_COMMENT;
				}
				int blockEnd = trimStr.indexOf("*/");
				if (blockEnd > 0) {
					return LineType.CODE_AND_LINE_COMMENT;
				}
				return LineType.BLOCK_COMMENT;
			} else {
				int blockStart = trimStr.indexOf("/*");
				if (blockStart > 0) {
					int blockEnd = trimStr.indexOf("*/");
					if (blockEnd > blockStart) {
						return LineType.CODE_AND_LINE_COMMENT;
					}
					return LineType.CODE_AND_BLOCK_COMMENT_START;
				}
			}
		} else if (originalType.equals(LineType.BLOCK_COMMENT)) {
			if (trimStr.endsWith("*/")) {
				return LineType.CODE_AND_BLOCK_COMMENT_END;
			} else if (trimStr.contains("*/")) {
				return LineType.CODE_AND_BLOCK_COMMENT_END_CODE;
			}
		}

		return originalType;
	}
}

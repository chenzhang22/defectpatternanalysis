package cn.edu.fudan.se.defect.track.factory;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import cn.edu.fudan.se.defect.track.file.ASTNodeFileLocation;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.ChangeDistillerDiffEntity;
import cn.edu.fudan.se.defectAnalysis.bean.track.diff.DiffEntity;

public class ChangeDistillerDiffEntityFactory {
	public List<DiffEntity> build(int bugId, String induceRevisionId,
			String fixedRevisionId, String fileName, FileDistiller distiller, String leftFileName,
			String rightFileName) {
		List<DiffEntity> diffEntities = new ArrayList<DiffEntity>();

		List<SourceCodeChange> sourceCodeChanges = distiller
				.getSourceCodeChanges();
		ASTNodeFileLocation astNodeLocation = null;
		try {
			astNodeLocation = new ASTNodeFileLocation(leftFileName,rightFileName);
		} catch (Exception e) {
			System.err.println("Exception @"+this.getClass().getName()+", message:"+e.getMessage());
			return diffEntities;
		}
		for (SourceCodeChange change : sourceCodeChanges) {
			ChangeDistillerDiffEntity chDiffEntity = new ChangeDistillerDiffEntity();
			chDiffEntity.setBugId(bugId);
			chDiffEntity.setInduceRevisionId(induceRevisionId);
			chDiffEntity.setFixedRevisionId(fixedRevisionId);
			chDiffEntity.setFileName(fileName);

			chDiffEntity.setBasicChangeType(change.getChangeType().name());
			chDiffEntity.setChangeEntityType(change.toString());
			chDiffEntity.setStartCode(change.getChangedEntity().getStartPosition());
			chDiffEntity.setEndCode(change.getChangedEntity().getEndPosition());
			
			
			chDiffEntity.setModifiers(change.getChangedEntity().getModifiers());
			
			chDiffEntity.setChangeType(change.getChangedEntity().getType().name());
			chDiffEntity.setSignificance(change.getChangeType().getSignificance().name());
			chDiffEntity.setBodyChange(change.getChangeType().isBodyChange());
			chDiffEntity.setDeclarationChange(change.getChangeType().isDeclarationChange());
						
			//13			
			chDiffEntity.setParentEntity(change.getLabel());
			chDiffEntity.setpStartCode(change.getParentEntity().getStartPosition());
			chDiffEntity.setpEndCode(change.getParentEntity().getEndPosition());
			chDiffEntity.setpType(change.getParentEntity().getType().name());
			chDiffEntity.setpUniqueName(change.getParentEntity().getUniqueName());
			//18
			if (change instanceof Delete) {
				chDiffEntity.setStartLine(astNodeLocation.readLineForLeftFile(change.getChangedEntity().getStartPosition()));
				chDiffEntity.setStartColumn(astNodeLocation.readColumnForLeftFile(change.getChangedEntity().getStartPosition()));
				chDiffEntity.setEndLine(astNodeLocation.readLineForLeftFile(change.getChangedEntity().getEndPosition()));
				chDiffEntity.setEndColumn(astNodeLocation.readColumnForLeftFile(change.getChangedEntity().getEndPosition()));
				
				chDiffEntity.setUniqueName(change.getChangedEntity().getUniqueName());
				
				chDiffEntity.setpStartLine(astNodeLocation.readLineForLeftFile(change.getParentEntity().getStartPosition()));
				chDiffEntity.setpStartColumn(astNodeLocation.readColumnForLeftFile(change.getParentEntity().getStartPosition()));
				chDiffEntity.setpEndLine(astNodeLocation.readLineForLeftFile(change.getParentEntity().getEndPosition()));
				chDiffEntity.setpEndColumn(astNodeLocation.readColumnForLeftFile(change.getParentEntity().getEndPosition()));
			} else if (change instanceof Insert) {
				chDiffEntity.setStartLine(astNodeLocation.readLineForRightFile(change.getChangedEntity().getStartPosition()));
				chDiffEntity.setStartColumn(astNodeLocation.readColumnForRightFile(change.getChangedEntity().getStartPosition()));
				chDiffEntity.setEndLine(astNodeLocation.readLineForRightFile(change.getChangedEntity().getEndPosition()));
				chDiffEntity.setEndColumn(astNodeLocation.readColumnForRightFile(change.getChangedEntity().getEndPosition()));
				
				chDiffEntity.setUniqueName(change.getChangedEntity().getUniqueName());
				
				chDiffEntity.setpStartLine(astNodeLocation.readLineForRightFile(change.getParentEntity().getStartPosition()));
				chDiffEntity.setpStartColumn(astNodeLocation.readColumnForRightFile(change.getParentEntity().getStartPosition()));
				chDiffEntity.setpEndLine(astNodeLocation.readLineForRightFile(change.getParentEntity().getEndPosition()));
				chDiffEntity.setpEndColumn(astNodeLocation.readColumnForRightFile(change.getParentEntity().getEndPosition()));
			} else if (change instanceof Move) {
				Move move = (Move) change;
				
				chDiffEntity.setStartLine(astNodeLocation.readLineForLeftFile(change.getChangedEntity().getStartPosition()));
				chDiffEntity.setStartColumn(astNodeLocation.readColumnForLeftFile(change.getChangedEntity().getStartPosition()));
				chDiffEntity.setEndLine(astNodeLocation.readLineForLeftFile(change.getChangedEntity().getEndPosition()));
				chDiffEntity.setEndColumn(astNodeLocation.readColumnForLeftFile(change.getChangedEntity().getEndPosition()));
				
				chDiffEntity.setNewModifiers(move.getNewEntity().getModifiers());
				chDiffEntity.setUniqueName(change.getChangedEntity().getUniqueName());
				
				//24
				chDiffEntity.setpStartLine(astNodeLocation.readLineForLeftFile(change.getParentEntity().getStartPosition()));
				chDiffEntity.setpStartColumn(astNodeLocation.readColumnForLeftFile(change.getParentEntity().getStartPosition()));
				chDiffEntity.setpEndLine(astNodeLocation.readLineForLeftFile(change.getParentEntity().getEndPosition()));
				chDiffEntity.setpEndColumn(astNodeLocation.readColumnForLeftFile(change.getParentEntity().getEndPosition()));
				
				chDiffEntity.setStartNewCode(move.getNewEntity().getStartPosition());
				chDiffEntity.setStartNewLine(astNodeLocation.readLineForRightFile(move.getNewEntity().getStartPosition()));
				chDiffEntity.setStartNewColumn(astNodeLocation.readColumnForRightFile(move.getNewEntity().getStartPosition()));
				chDiffEntity.setEndNewCode(move.getNewEntity().getEndPosition());
				chDiffEntity.setEndNewLine(astNodeLocation.readLineForRightFile(move.getNewEntity().getEndPosition()));
				chDiffEntity.setEndNewColumn(astNodeLocation.readColumnForRightFile(move.getNewEntity().getEndPosition()));
				//34
				chDiffEntity.setpStartNewCode(astNodeLocation.readLineForRightFile(move.getNewParentEntity().getStartPosition()));
				chDiffEntity.setpStartNewLine(astNodeLocation.readLineForRightFile(move.getNewParentEntity().getStartPosition()));
				chDiffEntity.setpStartNewColumn(astNodeLocation.readColumnForRightFile(move.getNewParentEntity().getStartPosition()));
				chDiffEntity.setpEndNewCode(move.getNewParentEntity().getEndPosition());
				chDiffEntity.setpEndNewLine(astNodeLocation.readLineForRightFile(move.getNewParentEntity().getEndPosition()));
				chDiffEntity.setpEndNewColumn(astNodeLocation.readColumnForRightFile(move.getNewParentEntity().getEndPosition()));
				
				chDiffEntity.setNewEntityType(move.getNewEntity().getType().name());
				chDiffEntity.setNewUniqueName(move.getNewEntity().getUniqueName());
				//42
				
				chDiffEntity.setpNewType(move.getNewParentEntity().getType().name());
				chDiffEntity.setpNewUniqueName(move.getNewParentEntity().getUniqueName());
			} else if (change instanceof Update) {
				Update update = (Update) change;
				//
				chDiffEntity.setStartLine(astNodeLocation.readLineForLeftFile(change.getChangedEntity().getStartPosition()));
				chDiffEntity.setStartColumn(astNodeLocation.readColumnForLeftFile(change.getChangedEntity().getStartPosition()));
				chDiffEntity.setEndLine(astNodeLocation.readLineForLeftFile(change.getChangedEntity().getEndPosition()));
				chDiffEntity.setEndColumn(astNodeLocation.readColumnForLeftFile(change.getChangedEntity().getEndPosition()));
				
				chDiffEntity.setNewModifiers(update.getNewEntity().getModifiers());
				chDiffEntity.setUniqueName(change.getChangedEntity().getUniqueName());
				
				//24
				chDiffEntity.setpStartLine(astNodeLocation.readLineForLeftFile(change.getParentEntity().getStartPosition()));
				chDiffEntity.setpStartColumn(astNodeLocation.readColumnForLeftFile(change.getParentEntity().getStartPosition()));
				chDiffEntity.setpEndLine(astNodeLocation.readLineForLeftFile(change.getParentEntity().getEndPosition()));
				chDiffEntity.setpEndColumn(astNodeLocation.readColumnForLeftFile(change.getParentEntity().getEndPosition()));
				
				chDiffEntity.setStartNewCode(update.getNewEntity().getStartPosition());
				chDiffEntity.setStartNewLine(astNodeLocation.readLineForRightFile(update.getNewEntity().getStartPosition()));
				chDiffEntity.setStartNewColumn(astNodeLocation.readColumnForRightFile(update.getNewEntity().getStartPosition()));
				chDiffEntity.setEndNewCode(update.getNewEntity().getEndPosition());
				chDiffEntity.setEndNewLine(astNodeLocation.readLineForRightFile(update.getNewEntity().getEndPosition()));
				chDiffEntity.setEndNewColumn(astNodeLocation.readColumnForRightFile(update.getNewEntity().getEndPosition()));
				//34
				
				chDiffEntity.setNewEntityType(update.getNewEntity().getType().name());
				chDiffEntity.setNewUniqueName(update.getNewEntity().getUniqueName());
				//36
			}else{
				System.err.println("Unknown change type");
			}
			diffEntities.add(chDiffEntity);
		}

		return diffEntities;
	}
}

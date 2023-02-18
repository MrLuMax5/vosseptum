package de.vosseptum.sql.entity.response;

import de.vosseptum.nosql.dao.BoardDTO;
import de.vosseptum.sql.entity.EditorialBoard;

public record EditorialBoardResponse(String institution, String subject, int budget, int reputation) {

	public static EditorialBoardResponse from(EditorialBoard board) {
		return new EditorialBoardResponse(board.editorialBoardKey.institution, board.editorialBoardKey.topic.subject, board.budget, board.reputation);
	}

	public static EditorialBoardResponse fromMongo(BoardDTO boardDTO) {
		return new EditorialBoardResponse(boardDTO.institution(), boardDTO.subject(), boardDTO.budget(), boardDTO.reputation());
	}
}

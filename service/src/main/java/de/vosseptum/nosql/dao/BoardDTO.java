package de.vosseptum.nosql.dao;

import de.vosseptum.sql.entity.EditorialBoard;

public record BoardDTO(String subject, Integer budget, Integer reputation, String institution) {

	public static BoardDTO from(EditorialBoard board) {
		return new BoardDTO(board.editorialBoardKey.topic.subject, board.budget, board.reputation, board.editorialBoardKey.institution);
	}

}

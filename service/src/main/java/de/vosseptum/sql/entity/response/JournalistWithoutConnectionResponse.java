package de.vosseptum.sql.entity.response;

import de.vosseptum.nosql.collection.MongoJournalist;
import de.vosseptum.nosql.dao.ConnectionDAO;
import de.vosseptum.nosql.dao.JournalistWithEmailDAO;
import de.vosseptum.sql.entity.Journalist;

public record JournalistWithoutConnectionResponse(long id,
												  String email,
												  String password,
												  String location) implements Comparable<JournalistWithoutConnectionResponse> {

	public static JournalistWithoutConnectionResponse from(Journalist journalist) {
		return new JournalistWithoutConnectionResponse(journalist.id, journalist.email, journalist.password, journalist.location);
	}

	@Override
	public int compareTo(JournalistWithoutConnectionResponse o) {
		return Math.toIntExact(this.id - o.id);
	}

	public static JournalistWithoutConnectionResponse fromMongo(ConnectionDAO connectionDAO) {
		return new JournalistWithoutConnectionResponse(connectionDAO.sql_id(), connectionDAO.email(), "", connectionDAO.location());
	}

	public static JournalistWithoutConnectionResponse fromMongoDbEntry(JournalistWithEmailDAO journalistWithEmailDAO) {
		MongoJournalist journalistById = MongoJournalist.findById(journalistWithEmailDAO.id());
		return new JournalistWithoutConnectionResponse(journalistById.sql_id, journalistWithEmailDAO.email(), "", journalistWithEmailDAO.location());
	}
}

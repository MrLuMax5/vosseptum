package de.vosseptum.nosql.dao;

import de.vosseptum.nosql.collection.MongoJournalist;
import org.bson.types.ObjectId;

public record ConnectionDAO(ObjectId journalist_id, long sql_id, String email, String location) {

	public static ConnectionDAO from(MongoJournalist journalist) {
		return new ConnectionDAO(journalist.id, journalist.sql_id, journalist.email, journalist.location);
	}

}

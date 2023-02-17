package edu.imse.nosql.dao;

import edu.imse.nosql.collection.MongoJournalist;
import edu.imse.sql.entity.response.JournalistWithoutConnectionResponse;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.types.ObjectId;

public record JournalistWithEmailDAO(ObjectId id, String email, String location) {

	public static JournalistWithEmailDAO from(MongoJournalist journalist) {
		return new JournalistWithEmailDAO(journalist.id, journalist.email, journalist.location);
	}

	public static JournalistWithEmailDAO from(JournalistWithoutConnectionResponse journalist) {
		PanacheMongoEntityBase queriedJournalist = MongoJournalist.find("sql_id", journalist.id())
				.firstResult();
		return new JournalistWithEmailDAO(((MongoJournalist) queriedJournalist).id, journalist.email(), journalist.location());
	}

}

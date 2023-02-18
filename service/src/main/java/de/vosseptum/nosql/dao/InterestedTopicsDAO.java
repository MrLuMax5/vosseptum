package de.vosseptum.nosql.dao;

import de.vosseptum.nosql.collection.MongoTopic;
import org.bson.types.ObjectId;

public record InterestedTopicsDAO(ObjectId topic_id, String subject, String category, Integer popularity) {

	public static InterestedTopicsDAO from(MongoTopic topic) {
		return new InterestedTopicsDAO(topic.id, topic.subject, topic.category, topic.popularity);
	}
}

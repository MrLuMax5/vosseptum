package de.vosseptum.model;

import de.vosseptum.nosql.collection.MongoTopic;
import de.vosseptum.sql.entity.Topic;

public record TopicDTO(String subject, String category, Integer popularity) {

	public static TopicDTO fromSQL(Topic topic) {
		return new TopicDTO(topic.subject, topic.category, topic.popularity);
	}

	public static TopicDTO fromMongo(MongoTopic topic) {
		return new TopicDTO(topic.subject, topic.category, topic.popularity);
	}
}

package edu.imse.model;

import edu.imse.nosql.collection.MongoTopic;
import edu.imse.sql.entity.Topic;

public record TopicDTO(String subject, String category, Integer popularity) {

	public static TopicDTO fromSQL(Topic topic) {
		return new TopicDTO(topic.subject, topic.category, topic.popularity);
	}

	public static TopicDTO fromMongo(MongoTopic topic) {
		return new TopicDTO(topic.subject, topic.category, topic.popularity);
	}
}

package edu.imse.nosql.collection;

import edu.imse.sql.entity.Topic;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.NoArgsConstructor;

@MongoEntity(collection="topic")
@NoArgsConstructor
public class MongoTopic extends PanacheMongoEntity {

	public String subject;
	public String category;
	public Integer popularity;

	public static MongoTopic from(Topic sqlTopic) {
		MongoTopic mongoTopic = new MongoTopic();
		mongoTopic.subject = sqlTopic.subject;
		mongoTopic.category = sqlTopic.category;
		mongoTopic.popularity = sqlTopic.popularity;
		return mongoTopic;
	}

}

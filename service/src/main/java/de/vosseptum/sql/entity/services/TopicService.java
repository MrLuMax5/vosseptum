package de.vosseptum.sql.entity.services;

import de.vosseptum.sql.entity.Topic;
import javax.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

@ApplicationScoped
public class TopicService extends AbstractFillerService{

	@Override
	protected void createEntity(Faker faker, int step) {
		Topic topic = new Topic();
		topic.subject = faker.lordOfTheRings().character();
		topic.category = faker.lordOfTheRings().location();
		topic.popularity = (step % 6) + 1;
		if (Topic.findById(topic.subject) == null) {
			topic.persist();
		}
	}
}

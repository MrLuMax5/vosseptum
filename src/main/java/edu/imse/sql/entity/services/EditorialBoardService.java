package edu.imse.sql.entity.services;

import edu.imse.sql.entity.EditorialBoard;
import edu.imse.sql.entity.Topic;
import edu.imse.sql.entity.keys.EditorialBoardKey;
import javax.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

@ApplicationScoped
public class EditorialBoardService extends AbstractFillerService{

	@Override
	protected void createEntity(Faker faker, int step) {
		EditorialBoard board = new EditorialBoard();
		EditorialBoardKey key = new EditorialBoardKey();
		key.institution = faker.company().name();
		key.topic = (Topic) Topic.listAll().get((int) (step % Topic.count()));
		board.editorialBoardKey = key;
		board.budget = faker.number().positive();
		board.reputation = (step % 6) + 1;
		if (EditorialBoard.findById(key) == null) {
			board.persist();
		}
	}
}

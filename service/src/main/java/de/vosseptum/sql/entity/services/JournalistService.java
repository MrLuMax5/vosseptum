package de.vosseptum.sql.entity.services;

import de.vosseptum.sql.entity.Journalist;
import javax.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

@ApplicationScoped
public class JournalistService extends AbstractFillerService{

	@Override
	protected void createEntity(Faker faker, int step) {
		Journalist journalist = new Journalist();
		journalist.email = faker.internet().emailAddress();
		journalist.password = faker.internet().password();
		journalist.location = faker.address().city();
		if (Journalist.findByEmail(journalist.email).isEmpty()) {
			journalist.persist();
		}
	}
}

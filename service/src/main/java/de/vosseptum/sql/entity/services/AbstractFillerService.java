package de.vosseptum.sql.entity.services;

import net.datafaker.Faker;

public abstract class AbstractFillerService {

	public void fill(int size) {
		Faker faker = new Faker();
		for (int i = 0; i < size; i++) {
			createEntity(faker, i);
		}
	}

	protected abstract void createEntity(Faker faker, int step);

}

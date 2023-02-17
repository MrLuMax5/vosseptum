package edu.imse.helper;

import edu.imse.nosql.collection.MongoJournalist;
import edu.imse.nosql.dao.JournalistWithEmailDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EmailFinder {

	public static List<String> getEmailsFromJournalists(Set<JournalistWithEmailDAO> journalists) {
		List<String> emails = new ArrayList<>();
		for (JournalistWithEmailDAO journalist: journalists) {
			MongoJournalist journalistById = MongoJournalist.findById(journalist.id());
			emails.add(journalistById.email);
		}
		return emails;
	}

}

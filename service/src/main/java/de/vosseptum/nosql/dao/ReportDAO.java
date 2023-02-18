package de.vosseptum.nosql.dao;

import de.vosseptum.helper.EmailFinder;
import de.vosseptum.nosql.collection.MongoReport;
import java.util.List;
import org.bson.types.ObjectId;

public record ReportDAO(ObjectId report_id, List<String> emails, Integer grade, String content) {

	public static ReportDAO from(MongoReport report) {
		return new ReportDAO(report.id, EmailFinder.getEmailsFromJournalists(report.authors), report.grade, report.content);
	}

}

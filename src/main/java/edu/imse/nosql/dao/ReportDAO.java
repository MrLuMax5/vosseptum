package edu.imse.nosql.dao;

import edu.imse.helper.EmailFinder;
import edu.imse.nosql.collection.MongoReport;
import java.util.List;
import org.bson.types.ObjectId;

public record ReportDAO(ObjectId report_id, List<String> emails, Integer grade, String content) {

	public static ReportDAO from(MongoReport report) {
		return new ReportDAO(report.id, EmailFinder.getEmailsFromJournalists(report.authors), report.grade, report.content);
	}

}

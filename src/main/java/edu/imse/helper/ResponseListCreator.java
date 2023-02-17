package edu.imse.helper;

import edu.imse.nosql.collection.MongoArticle;
import edu.imse.nosql.collection.MongoReport;
import edu.imse.nosql.dao.ReportDAO;
import edu.imse.sql.entity.TruthfulnessReport;
import edu.imse.sql.entity.bridges.JournalistWrittenReport;
import edu.imse.sql.entity.keys.ArticleKey;
import edu.imse.sql.entity.response.JournalistWithoutConnectionResponse;
import edu.imse.sql.entity.response.TruthfulnessReportResponse;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ResponseListCreator {

	private ResponseListCreator() {

	}

	public static List<TruthfulnessReportResponse> createResponseListSQL(List<TruthfulnessReport> reports, ArticleKey articleKey) {
		if (reports.isEmpty()) {
			return List.of();
		}
		List<TruthfulnessReportResponse> response = new ArrayList<>();
		for (TruthfulnessReport report: reports) {
			List<JournalistWithoutConnectionResponse> authors = JournalistWrittenReport.findByReport(report.id)
					.stream()
					.map(JournalistWithoutConnectionResponse::from)
					.sorted()
					.toList();
			TruthfulnessReportResponse articleResponse = new TruthfulnessReportResponse(report.grade,
					report.date,
					report.content,
					articleKey.number,
					articleKey.shortDescription,
					authors);
			response.add(articleResponse);
		}
		return response;
	}

	public static List<TruthfulnessReportResponse> createResponseListMongo(ArticleKey articleKey) {
		PanacheMongoEntityBase queriedArticle = MongoArticle.find("description_with_number", articleKey.shortDescription + "-" + articleKey.number)
				.firstResult();
		List<TruthfulnessReportResponse> responseList = new ArrayList<>();
		if (queriedArticle instanceof MongoArticle article) {
			for (ReportDAO reportDAO: article.verifications) {
				MongoReport report = MongoReport.findById(reportDAO.report_id());
				List<JournalistWithoutConnectionResponse> authors = report.authors.stream().map(JournalistWithoutConnectionResponse::fromMongoDbEntry).toList();
				responseList.add(new TruthfulnessReportResponse(report.grade,
						new Date(report.date.getTime()),
						report.content,
						articleKey.number,
						articleKey.shortDescription,
						authors));
			}
		}
		return responseList;
	}
}

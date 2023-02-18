package de.vosseptum.nosql.resources;

import de.vosseptum.helper.ResponseListCreator;
import de.vosseptum.nosql.collection.MongoArticle;
import de.vosseptum.nosql.collection.MongoReport;
import de.vosseptum.sql.entity.keys.ArticleKey;
import de.vosseptum.sql.entity.response.TruthfulnessReportResponse;
import de.vosseptum.nosql.dao.JournalistWithEmailDAO;
import de.vosseptum.nosql.dao.ReportDAO;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/mongo/truthfulness-report")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MongoReportResource {

	@GET
	public List<MongoReport> list() {
		return MongoReport.listAll();
	}

	@GET
	@Path("/count")
	public long count() {
		return MongoReport.count();
	}

	@POST
	@Path("/byArticleId")
	public Response getByArticle(ArticleKey articleKey) {
		return Response.ok(ResponseListCreator.createResponseListMongo(articleKey)).status(200).build();
	}

	@POST
	@Path("/add")
	public Response addReport(TruthfulnessReportResponse reportDTO) {
		postMongoReport(reportDTO);
		return Response.ok(true).status(200).build();
	}

	private void postMongoReport(TruthfulnessReportResponse reportDTO) {
		MongoReport report = new MongoReport();
		report.grade = reportDTO.grade();
		report.content = reportDTO.content();
		report.date = Date.valueOf(LocalDate.now());
		PanacheMongoEntityBase queriedArticle = MongoArticle.find("description_with_number", reportDTO.articleShortDescription() + "-" + reportDTO.articleNumber())
				.firstResult();
		if (queriedArticle instanceof  MongoArticle article) {
			report.verified_article = article.id;
		}
		report.authors = reportDTO.authors().stream().map(JournalistWithEmailDAO::from).collect(Collectors.toSet());
		report.persist();
		// Add to article...
		if (queriedArticle instanceof  MongoArticle article) {
			article.verifications.add(ReportDAO.from(report));
			article.update();
		}
	}

}

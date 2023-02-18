package de.vosseptum.sql.entity.resources;

import de.vosseptum.helper.ResponseListCreator;
import de.vosseptum.sql.entity.Article;
import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.sql.entity.TruthfulnessReport;
import de.vosseptum.sql.entity.bridges.JournalistWrittenReport;
import de.vosseptum.sql.entity.keys.ArticleKey;
import de.vosseptum.sql.entity.keys.JournalistWrittenReportKey;
import de.vosseptum.sql.entity.response.JournalistWithoutConnectionResponse;
import de.vosseptum.sql.entity.response.TruthfulnessReportResponse;
import de.vosseptum.sql.entity.services.TruthfulnessReportService;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("truthfulness-report")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class TruthfulnessReportResource {

	@GET
	@Produces("application/json")
	public Response list() {
		List<TruthfulnessReport> reports = TruthfulnessReport.listAll();
		return Response.ok(reports).status(200).build();
	}

	@GET
	@Path("/count")
	public long count() {
		return TruthfulnessReport.count();
	}

	@GET
	@Transactional
	@Path("/fill")
	public Response fill() {
		int size = 20;
		TruthfulnessReportService service = new TruthfulnessReportService();
		service.fill(size);
		return Response.ok("Filled with " + size + " Entities").status(200).build();
	}

	@POST
	@Transactional
	@Path("/add")
	public Response addReport(TruthfulnessReportResponse reportDTO) {
		postSQLReport(reportDTO);
		return Response.ok(true).status(200).build();
	}

	private void postSQLReport(TruthfulnessReportResponse reportDTO) {
		TruthfulnessReport report = new TruthfulnessReport();
		if (reportDTO.articleNumber() != null && reportDTO.articleShortDescription() != null) {
			ArticleKey articleKey = new ArticleKey();
			articleKey.number = reportDTO.articleNumber();
			articleKey.shortDescription = reportDTO.articleShortDescription();
			Article articleById = Article.findById(articleKey);
			if (articleById != null) {
				report.article = articleById;
			}
		}
		report.content = reportDTO.content();
		report.date = Date.valueOf(LocalDate.now());
		report.grade = reportDTO.grade();
		report.persist();
		for (JournalistWithoutConnectionResponse journalist: reportDTO.authors()) {
			JournalistWrittenReport journalistWrittenReport = new JournalistWrittenReport();
			JournalistWrittenReportKey journalistWrittenReportKey = new JournalistWrittenReportKey();
			journalistWrittenReportKey.journalist = Journalist.findById(journalist.id());
			journalistWrittenReportKey.report = report;
			if (JournalistWrittenReport.findById(journalistWrittenReportKey) == null) {
				journalistWrittenReport.journalistWrittenReportKey = journalistWrittenReportKey;
				journalistWrittenReport.persist();
			}
		}
	}

	@POST
	@Path("/byArticleId")
	public Response getByArticle(ArticleKey articleKey) {
		return Response.ok(ResponseListCreator.createResponseListSQL(TruthfulnessReport.findByArticle(articleKey), articleKey)).status(200).build();
	}

}

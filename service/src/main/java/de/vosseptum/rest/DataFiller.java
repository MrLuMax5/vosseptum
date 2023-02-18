package de.vosseptum.rest;

import com.mongodb.client.AggregateIterable;
import static com.mongodb.client.model.Sorts.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.UnwindOptions;
import de.vosseptum.model.ImpactDTO;
import de.vosseptum.model.TrustDTO;
import de.vosseptum.model.EntityAndBridgeCountDTO;
import de.vosseptum.sql.entity.Article;
import de.vosseptum.sql.entity.EditorialBoard;
import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.sql.entity.Topic;
import de.vosseptum.sql.entity.TruthfulnessReport;
import de.vosseptum.sql.entity.bridges.ArticleCoversTopic;
import de.vosseptum.sql.entity.bridges.JournalistInBoard;
import de.vosseptum.sql.entity.bridges.JournalistInterestedTopic;
import de.vosseptum.sql.entity.bridges.JournalistWrittenArticle;
import de.vosseptum.sql.entity.bridges.JournalistWrittenReport;
import de.vosseptum.sql.entity.services.ArticleService;
import de.vosseptum.sql.entity.services.BridgeService;
import de.vosseptum.sql.entity.services.EditorialBoardService;
import de.vosseptum.sql.entity.services.JournalistService;
import de.vosseptum.sql.entity.services.TopicService;
import de.vosseptum.sql.entity.services.TruthfulnessReportService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.bson.Document;

@Path("data")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class DataFiller {

	@Inject
	ArticleService articleService;
	@Inject
	EditorialBoardService editorialBoardService;
	@Inject
	JournalistService journalistService;
	@Inject
	TopicService topicService;
	@Inject
	TruthfulnessReportService truthfulnessReportService;
	@Inject
	BridgeService bridgeService;
	@Inject
	EntityManager entityManager;
	@Inject
	MongoClient mongoClient;

	@GET
	@Path("/fill")
	@Transactional
	@Produces("application/json")
	public Response fillAll() {
		int size = 20;
		articleService.fill(size);
		journalistService.fill(size);
		topicService.fill(size);
		editorialBoardService.fill(size);
		truthfulnessReportService.fill(size);
		bridgeService.fill(size + 1);
		EntityAndBridgeCountDTO dto = new EntityAndBridgeCountDTO(countEntities(), countBridges());
		return Response.ok(dto).status(200).build();
	}

	@GET
	@Path("/count")
	@Produces("application/json")
	public Response countAll() {
		EntityAndBridgeCountDTO dto = new EntityAndBridgeCountDTO(countEntities(), countBridges());
		return Response.ok(dto).status(200).build();
	}

	@GET
	@Path("/mostTrustworthies")
	@Produces("application/json")
	public Response getMostTrustworthyJournalists() {
		List<Object[]> queryResultList = entityManager.createNativeQuery(""
						+ "select grade, id, email "
						+ "from (SELECT avg(r.grade) as grade, j.id, j.email "
						+ "FROM journalist_written_article as jwa "
						+ "Inner JOIN journalist j on (j.id = jwa.journalist_id) "
						+ "Inner JOIN article a on ((jwa.article_number = a.number) AND (jwa.article_short_description = a.short_description)) "
						+ "Inner JOIN truthfulness_report r on ((r.article_number = a.number) AND (r.article_short_description = a.short_description)) "
						+ "GROUP BY j.id "
						+ "ORDER BY grade ASC) "
						+ "AS result")
				.getResultList();
		List<TrustDTO> responseListe = queryResultList.stream()
				.map(TrustDTO::from)
				.toList();
		return Response.ok(responseListe).status(200).build();
	}

	@GET
	@Path("/mostTrustworthies/mongo")
	@Produces("application/json")
	public Response getMostTrustworthyMongoJournalists() {
		AggregateIterable<Document> aggregate = mongoClient.getDatabase("imse").getCollection("article")
				.aggregate(
						Arrays.asList(
								Aggregates.unwind("$authors", new UnwindOptions().preserveNullAndEmptyArrays(false)),
								Aggregates.unwind("$verifications", new UnwindOptions().preserveNullAndEmptyArrays(false)),
								Aggregates.group("$authors.email", Accumulators.avg("avg_grade", "$verifications.grade")),
								Aggregates.sort(orderBy(ascending("avg_grade")))
						));
		List<TrustDTO> responseListe = new ArrayList<>();
		aggregate.forEach(doc -> responseListe.add(TrustDTO.fromMongo(doc)));
		return Response.ok(responseListe).status(200).build();
	}

	@GET
	@Path("/mostImpactful")
	@Produces("application/json")
	public Response getBestStandingJournalist() {
		List<Object[]> queryResultList = entityManager.createNativeQuery(""
						+ "select email, avg_popularity, avg_budget "
						+ "from (SELECT j.email, avg(t.popularity) as avg_popularity, avg(b.budget) as avg_budget "
						+ "FROM journalist_interested_topic as jit "
						+ "Inner JOIN journalist j on (j.id = jit.journalist_id) "
						+ "Inner JOIN topic t on (t.subject = jit.topic_subject) "
						+ "Inner JOIN journalist_in_board jib on (jib.journalist_id = j.id) "
						+ "Inner JOIN editorial_board b on ((jib.editorialBoard_institution = b.institution) AND (jib.editorialBoard_topic_subject = b.topic_subject)) "
						+ "GROUP BY j.email "
						+ "ORDER BY avg_popularity ASC ) "
						+ "AS result")
				.getResultList();
		List<ImpactDTO> responseListe = queryResultList.stream()
				.map(ImpactDTO::from)
				.toList();
		return Response.ok(responseListe).status(200).build();
	}

	@GET
	@Path("/mostImpactful/mongo")
	@Produces("application/json")
	public Response getBestStandingMongoJournalist() {
		AggregateIterable<Document> aggregate = mongoClient.getDatabase("imse").getCollection("journalist")
				.aggregate(
						Arrays.asList(
								Aggregates.unwind("$topics", new UnwindOptions().preserveNullAndEmptyArrays(false)),
								Aggregates.unwind("$boards", new UnwindOptions().preserveNullAndEmptyArrays(false)),
								Aggregates.group("$email",
										Accumulators.avg("avg_popularity", "$topics.popularity"),
										Accumulators.avg("avg_budget", "$boards.budget")),
								Aggregates.sort(orderBy(ascending("avg_popularity")))
						));
		List<ImpactDTO> responseListe = new ArrayList<>();
		aggregate.forEach(doc -> responseListe.add(ImpactDTO.fromMongo(doc)));
		return Response.ok(responseListe).status(200).build();
	}

	private int countEntities() {
		long entityCount = Article.count()
				+ Journalist.count()
				+ Topic.count()
				+ EditorialBoard.count()
				+ TruthfulnessReport.count();
		return (int) entityCount;
	}

	private int countBridges() {
		long bridgeCount = ArticleCoversTopic.count()
				+ JournalistInBoard.count()
				+ JournalistInterestedTopic.count()
				+ JournalistWrittenArticle.count()
				+ JournalistWrittenReport.count();
		return (int) bridgeCount;
	}
}

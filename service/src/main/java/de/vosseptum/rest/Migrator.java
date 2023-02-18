package de.vosseptum.rest;

import de.vosseptum.nosql.collection.MongoArticle;
import de.vosseptum.nosql.collection.MongoJournalist;
import de.vosseptum.nosql.collection.MongoReport;
import de.vosseptum.nosql.collection.MongoTopic;
import de.vosseptum.nosql.dao.BoardDTO;
import de.vosseptum.nosql.dao.ConnectionDAO;
import de.vosseptum.nosql.dao.InterestedTopicsDAO;
import de.vosseptum.nosql.dao.JournalistWithEmailDAO;
import de.vosseptum.nosql.dao.ReportDAO;
import de.vosseptum.nosql.dao.WrittenArticlesDAO;
import de.vosseptum.sql.entity.Article;
import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.sql.entity.Topic;
import de.vosseptum.sql.entity.TruthfulnessReport;
import de.vosseptum.sql.entity.bridges.ArticleCoversTopic;
import de.vosseptum.sql.entity.bridges.JournalistInBoard;
import de.vosseptum.sql.entity.bridges.JournalistInterestedTopic;
import de.vosseptum.sql.entity.bridges.JournalistWrittenArticle;
import de.vosseptum.sql.entity.bridges.JournalistWrittenReport;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Migrate all SQL data to NoSQL
 */
@Path("migrate")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class Migrator {

	@GET
	public Response migrateAll() {
		migrateDatabase();
		return Response.ok(true).status(200).build();
	}

	private void migrateDatabase() {
		migrateJournalists();
		migrateArticles();
		migrateReports();
		migrateTopics();
		migrateBridges();
	}

	private void migrateJournalists() {
		for (PanacheEntityBase queriedJournalist: Journalist.listAll()) {
			if (queriedJournalist instanceof Journalist journalist) {
				MongoJournalist mongoJournalist = MongoJournalist.from(journalist);
				if (MongoJournalist.find("sql_id", mongoJournalist.sql_id).firstResult() == null) {
					mongoJournalist.persist();
				}
			}
		}
	}

	private void migrateArticles() {
		for (PanacheEntityBase queriedArticle: Article.listAll()) {
			if (queriedArticle instanceof Article article) {
				MongoArticle mongoArticle = MongoArticle.from(article);
				if (MongoArticle.find("description_with_number", mongoArticle.description_with_number).firstResult() == null) {
					mongoArticle.persist();
				}
			}
		}
	}

	private void migrateReports() {
		for (PanacheEntityBase queriedReport: TruthfulnessReport.listAll()) {
			if (queriedReport instanceof TruthfulnessReport report) {
				MongoReport mongoReport = MongoReport.from(report);
				if (MongoReport.find("sql_id", mongoReport.sql_id).firstResult() == null) {
					mongoReport.persist();
				}
				PanacheMongoEntityBase queriedArticle = MongoArticle.find("description_with_number", report.article.articleKey.toString())
						.firstResult();
				if (queriedArticle instanceof MongoArticle mongoArticle) {
					mongoArticle.verifications.add(ReportDAO.from(mongoReport));
					mongoArticle.update();
				}
			}
		}
	}

	private void migrateTopics() {
		for (PanacheEntityBase queriedTopic: Topic.listAll()) {
			if (queriedTopic instanceof Topic topic) {
				MongoTopic mongoTopic = MongoTopic.from(topic);
				if (MongoTopic.find("subject", mongoTopic.subject).firstResult() == null) {
					mongoTopic.persist();
				}
			}
		}
	}

	private void migrateBridges() {
		migrateConnections();
		migrateJournalistWrittenArticle();
		migrateJournalistWrittenReport();
		migrateJournalistInterestedTopic();
		migrateArticleCoversTopic();
		migrateJournalistInBoard();
	}

	private void migrateConnections() {
		for (PanacheMongoEntityBase queriedJournalist: MongoJournalist.listAll()) {
			if (queriedJournalist instanceof MongoJournalist journalist) {
				Journalist sqlJournalist = Journalist.findById(journalist.sql_id);
				Set<Journalist> connections = sqlJournalist.connections;
				connections.addAll(sqlJournalist.connectionOf);
				for (Journalist connectedSQLJournalist: connections) {
					MongoJournalist connectedMongoJournalist = MongoJournalist.find("sql_id", connectedSQLJournalist.id).firstResult();
					journalist.connections.add(ConnectionDAO.from(connectedMongoJournalist));
					journalist.update();
				}
			}
		}
	}

	private void migrateJournalistWrittenArticle() {
		for (PanacheEntityBase queriedJournalistWrittenArticle: JournalistWrittenArticle.listAll()) {
			if (queriedJournalistWrittenArticle instanceof JournalistWrittenArticle journalistWrittenArticle) {
				MongoJournalist journalist = MongoJournalist.find("sql_id", journalistWrittenArticle.journalistWrittenArticleKey.journalist.id).firstResult();
				MongoArticle article = MongoArticle.find("description_with_number", journalistWrittenArticle.journalistWrittenArticleKey.article.articleKey.toString()).firstResult();
				journalist.articles.add(WrittenArticlesDAO.from(article));
				article.authors.add(JournalistWithEmailDAO.from(journalist));
				journalist.update();
				article.update();
			}
		}
	}

	private void migrateJournalistWrittenReport() {
		for (PanacheEntityBase queriedJournalistWrittenReport: JournalistWrittenReport.listAll()) {
			if (queriedJournalistWrittenReport instanceof JournalistWrittenReport journalistWrittenReport) {
				MongoJournalist journalist = MongoJournalist.find("sql_id", journalistWrittenReport.journalistWrittenReportKey.journalist.id).firstResult();
				MongoReport report = MongoReport.find("sql_id", journalistWrittenReport.journalistWrittenReportKey.report.id).firstResult();
				report.authors.add(JournalistWithEmailDAO.from(journalist));
				journalist.update();
				report.update();
			}
		}
	}

	private void migrateJournalistInterestedTopic() {
		for (PanacheEntityBase queriedJournalistInterestedTopic: JournalistInterestedTopic.listAll()) {
			if (queriedJournalistInterestedTopic instanceof JournalistInterestedTopic journalistInterestedTopic) {
				MongoJournalist journalist = MongoJournalist.find("sql_id", journalistInterestedTopic.journalistInterestedTopicKey.journalist.id).firstResult();
				MongoTopic topic = MongoTopic.find("subject", journalistInterestedTopic.journalistInterestedTopicKey.topic.subject).firstResult();
				journalist.topics.add(InterestedTopicsDAO.from(topic));
				journalist.update();
			}
		}
	}

	private void migrateArticleCoversTopic() {
		for (PanacheEntityBase queriedArticleCoversTopic: ArticleCoversTopic.listAll()) {
			if (queriedArticleCoversTopic instanceof ArticleCoversTopic articleCoversTopic) {
				MongoArticle article = MongoArticle.find("description_with_number", articleCoversTopic.articleCoversTopicKey.article.articleKey.toString()).firstResult();
				MongoTopic topic = MongoTopic.find("subject", articleCoversTopic.articleCoversTopicKey.topic.subject).firstResult();
				article.covered_topics.add(topic.id);
				article.update();
			}
		}
	}

	private void migrateJournalistInBoard() {
		for (PanacheEntityBase queriedJournalistInBoard: JournalistInBoard.listAll()) {
			if (queriedJournalistInBoard instanceof JournalistInBoard journalistInBoard) {
				MongoJournalist journalist = MongoJournalist.find("sql_id", journalistInBoard.journalistInBoardKey.journalist.id).firstResult();
				BoardDTO board = BoardDTO.from(journalistInBoard.journalistInBoardKey.editorialBoard);
				journalist.boards.add(board);
				journalist.update();
			}
		}
	}

}

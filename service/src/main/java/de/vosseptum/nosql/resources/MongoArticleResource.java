package de.vosseptum.nosql.resources;

import de.vosseptum.helper.SophoraIdHelper;
import de.vosseptum.model.ArticleWithMetaInformationDTO;
import de.vosseptum.nosql.collection.MongoArticle;
import de.vosseptum.nosql.collection.MongoJournalist;
import de.vosseptum.nosql.collection.MongoTopic;
import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.sql.entity.Topic;
import de.vosseptum.sql.entity.response.ArticleResponse;
import de.vosseptum.model.TopicDTO;
import de.vosseptum.nosql.dao.JournalistWithEmailDAO;
import de.vosseptum.nosql.dao.WrittenArticlesDAO;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.datafaker.Faker;

@Path("/mongo/article")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MongoArticleResource {

	Faker faker = new Faker();
	Random random = new Random();

	@GET
	public List<MongoArticle> list() {
		return MongoArticle.listAll();
	}

	@GET
	@Path("/count")
	public long count() {
		return MongoArticle.count();
	}

	@POST
	@Path("/related/byId")
	public Response getArticlesRelatedToUser(long id) {
		MongoJournalist byJournalistEmail = MongoJournalist.find("sql_id", id).firstResult();
		return Response.ok(createResponseList(byJournalistEmail.articles)).status(200).build();
	}

	@POST
	@Path("/related/byJournalists")
	public Response getArticlesRelatedToJournalists(List<Journalist> journalists) {
		Set<WrittenArticlesDAO> articles = new HashSet<>();
		for(Journalist journalist: journalists) {
			MongoJournalist bySQLId = MongoJournalist.find("sql_id", journalist.id)
					.firstResult();
			articles.addAll(bySQLId.articles);
		}
		return Response.ok(createResponseList(articles)).status(200).build();
	}

	@POST
	@Path("/related/byTopics")
	public Response getArticlesRelatedToTopics(List<Topic> topics) {
		Set<WrittenArticlesDAO> articles = new HashSet<>();
		for (PanacheMongoEntityBase queriedArticle: MongoArticle.listAll()) {
			if (queriedArticle instanceof MongoArticle article) {
				for (Topic topic: topics) {
					PanacheMongoEntityBase queriedTopic = MongoTopic.find("subject", topic.subject)
							.firstResult();
					if (queriedTopic instanceof MongoTopic mongoTopic && article.covered_topics.contains(mongoTopic.id)) {
						articles.add(WrittenArticlesDAO.from(article));
					}
				}
			}
		}
		return Response.ok(createResponseList(articles)).status(200).build();
	}

	@POST
	@Path("/post")
	public Response postArticleWithInformation(ArticleWithMetaInformationDTO articleDTO) {
		MongoArticle article;
		MongoArticle articleInDB = MongoArticle.find("description_with_number", getShortDescWithNumberFromDTO(articleDTO)).firstResult();
		boolean isAlreadyPresent = articleInDB instanceof MongoArticle;
		if (isAlreadyPresent) {
			article = articleInDB;
			article.authors = new HashSet<>();
			article.covered_topics = new HashSet<>();
		} else {
			 article = new MongoArticle();
		}
		if (!articleDTO.article().anonymous()) {
			article.authors = articleDTO.article()
					.authors()
					.stream()
					.map(JournalistWithEmailDAO::from)
					.collect(Collectors.toSet());
		}
		article.content = articleDTO.article().content();
		article.publication_date = Date.valueOf(LocalDate.now());
		for (TopicDTO topic: articleDTO.article().topics()) {
			PanacheMongoEntityBase subject = MongoTopic.find("subject", topic.subject())
					.firstResult();
			if (subject instanceof MongoTopic mongoTopic) {
				article.covered_topics.add(mongoTopic.id);
			} else {
				MongoTopic newMongoTopic = new MongoTopic();
				newMongoTopic.subject = topic.subject();
				newMongoTopic.popularity = random.nextInt(1, 5);
				newMongoTopic.category = faker.lordOfTheRings().location();
				newMongoTopic.persist();
				article.covered_topics.add(newMongoTopic.id);
			}
		}
		String descriptionWithNumber = articleDTO.article().shortDescription().trim();
		int id = 100;
		for (PanacheMongoEntityBase queriedArticle: MongoArticle.listAll()) {
			if (queriedArticle instanceof MongoArticle databaseArticle) {
				if (SophoraIdHelper.getDescriptionFromMongoArticle(databaseArticle.description_with_number).equals(articleDTO.article().shortDescription())) {
					int numberFromDatabase = SophoraIdHelper.getNumberFromMongoArticle(databaseArticle.description_with_number);
					if (id < numberFromDatabase) {
						id = numberFromDatabase + 2;
					}
				}
			}
		}
		article.description_with_number = descriptionWithNumber + "-" + id;
		if (isAlreadyPresent) {
			article.description_with_number = getShortDescWithNumberFromDTO(articleDTO);
			article.update();
		} else {
			article.persist();
		}

		// Add article to journalists
		if (!articleDTO.article().anonymous()) {
			for (JournalistWithEmailDAO journalistDAO: article.authors) {
				PanacheMongoEntityBase byId = MongoJournalist.findById(journalistDAO.id());
				if (byId instanceof MongoJournalist journalist) {
					List<String> emails = new ArrayList<>();
					for (JournalistWithEmailDAO jDAO: article.authors) {
						emails.add(jDAO.email());
					}
					journalist.articles.add(new WrittenArticlesDAO(article.id, article.content, emails));
					journalist.update();
				}
			}
		}
		return Response.ok(true).status(200).build();
	}

	private List<ArticleResponse> createResponseList(Set<WrittenArticlesDAO> articles) {
		if (articles.isEmpty()) {
			return List.of();
		}
		return articles.stream()
				.map(ArticleResponse::from)
				.toList();
	}

	private String getShortDescWithNumberFromDTO(ArticleWithMetaInformationDTO articleDTO) {
		return articleDTO.article().shortDescription() + "-" + articleDTO.article().number();
	}

}

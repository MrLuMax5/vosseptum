package edu.imse.sql.entity.resources;

import edu.imse.model.ArticleWithMetaInformationDTO;
import edu.imse.model.TopicDTO;
import edu.imse.sql.entity.Article;
import edu.imse.sql.entity.Journalist;
import edu.imse.sql.entity.Topic;
import edu.imse.sql.entity.bridges.ArticleCoversTopic;
import edu.imse.sql.entity.bridges.JournalistWrittenArticle;
import edu.imse.sql.entity.keys.ArticleCoversTopicKey;
import edu.imse.sql.entity.keys.ArticleKey;
import edu.imse.sql.entity.keys.JournalistWrittenArticleKey;
import edu.imse.sql.entity.response.ArticleResponse;
import edu.imse.sql.entity.response.JournalistWithoutConnectionResponse;
import edu.imse.sql.entity.services.ArticleService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import net.datafaker.Faker;

@Path("article")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class ArticleResource {

	Faker faker = new Faker();
	Random random = new Random();

	@GET
	@Produces("application/json")
	public Response list() {
		List<Article> articles = Article.listAll();
		return Response.ok(articles).status(200).build();
	}

	@GET
	@Path("/count")
	public long count() {
		return Article.count();
	}

	@GET
	@Transactional
	@Path("/fill")
	public Response fill() {
		int size = 20;
		ArticleService service = new ArticleService();
		service.fill(size);
		return Response.ok("Filled with " + size + " Entities").status(200).build();
	}

	@POST
	@Path("/related/byId")
	public Response getArticlesRelatedToUser(long id) {
		List<Article> byJournalistId = JournalistWrittenArticle.findByJournalistId(id);
		return Response.ok(createResponseList(byJournalistId)).status(200).build();
	}

	@POST
	@Path("/related/byJournalists")
	public Response getArticlesRelatedToJournalists(List<Journalist> journalists) {
		List<Article> byJournalistIds = new ArrayList<>();
		for(Journalist journalist: journalists) {
			byJournalistIds.addAll(JournalistWrittenArticle.findByJournalistId(journalist.id));
		}
		return Response.ok(createResponseList(byJournalistIds)).status(200).build();
	}

	@POST
	@Path("/related/byTopics")
	public Response getArticlesRelatedToTopics(List<Topic> topics) {
		List<Article> byTopics = new ArrayList<>();
		for (Topic topic: topics) {
			byTopics.addAll(ArticleCoversTopic.findBySubject(topic.subject));
		}
		return Response.ok(createResponseList(byTopics)).status(200).build();
	}

	@POST
	@Transactional
	@Path("/post")
	public Response postArticleWithInformation(ArticleWithMetaInformationDTO articleDTO) {
		postArticleSQL(articleDTO);
		return Response.ok(true).status(200).build();
	}

	private void postArticleSQL(ArticleWithMetaInformationDTO articleDTO) {
		Article article;
		ArticleKey key = new ArticleKey();
		String shortDescription = articleDTO.article().shortDescription().trim();
		if (articleDTO.article().number() != null) {
			key.shortDescription = shortDescription;
			key.number = articleDTO.article().number();
			article = Article.findById(key);
		}
		else {
			key.shortDescription = articleDTO.article().shortDescription().trim();
			int id = 100;
			for (PanacheEntityBase articleInDb: Article.listAll()) {
				if(((Article) articleInDb).articleKey.shortDescription.equals(key.shortDescription) && ((Article) articleInDb).articleKey.number >= id) {
					id = ((Article) articleInDb).articleKey.number + 2;
				}
			}
			key.number = id;
			article = new Article();
			article.articleKey = key;
		}
		article.content = articleDTO.article().content();
		article.date = Date.valueOf(LocalDate.now());
		article.persist();
		List<Journalist> byArticle = JournalistWrittenArticle.findByArticle(article.articleKey);
		for (Journalist journalist : byArticle) {
			if (!articleDTO.article().authors().contains(JournalistWithoutConnectionResponse.from(journalist)) || articleDTO.article().anonymous()) {
				JournalistWrittenArticleKey journalistWrittenArticleKey = new JournalistWrittenArticleKey();
				journalistWrittenArticleKey.article = article;
				journalistWrittenArticleKey.journalist = journalist;
				JournalistWrittenArticle.deleteById(journalistWrittenArticleKey);
			}
		}
		if (!articleDTO.article().anonymous()) {
			for (JournalistWithoutConnectionResponse journalist: articleDTO.article().authors()) {
				JournalistWrittenArticle journalistWrittenArticle = new JournalistWrittenArticle();
				JournalistWrittenArticleKey journalistWrittenArticleKey = new JournalistWrittenArticleKey();
				journalistWrittenArticleKey.journalist = Journalist.findById(journalist.id());
				journalistWrittenArticleKey.article = article;
				if (JournalistWrittenArticle.findById(journalistWrittenArticleKey) == null) {
					journalistWrittenArticle.journalistWrittenArticleKey = journalistWrittenArticleKey;
					journalistWrittenArticle.persist();
				}
			}
		}
		for (TopicDTO topic: articleDTO.article().topics()) {
			Topic topicInDB = Topic.findById(topic.subject());
			if (topicInDB == null) {
				topicInDB = new Topic();
				topicInDB.subject = topic.subject();
				topicInDB.popularity = random.nextInt(1, 5);
				topicInDB.category = faker.lordOfTheRings().location();
			} else {
				if (topicInDB.popularity == null) {
					topicInDB.popularity = random.nextInt(1, 5);
				}
				if (topicInDB.category == null) {
					topicInDB.category = faker.lordOfTheRings().location();
				}
			}
			topicInDB.persist();
			ArticleCoversTopic articleCoversTopic = new ArticleCoversTopic();
			ArticleCoversTopicKey articleCoversTopicKey = new ArticleCoversTopicKey();
			articleCoversTopicKey.topic = topicInDB;
			articleCoversTopicKey.article = article;
			if (ArticleCoversTopic.findById(articleCoversTopicKey) == null) {
				articleCoversTopic.articleCoversTopicKey = articleCoversTopicKey;
				articleCoversTopic.persist();
			}
		}
	}

	private List<ArticleResponse> createResponseList(List<Article> articles) {
		if (articles.isEmpty()) {
			return List.of();
		}
		return articles.stream()
				.map(ArticleResponse::fromSQLArticle)
				.toList();
	}

}

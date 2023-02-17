package edu.imse.sql.entity.response;

import edu.imse.helper.SophoraIdHelper;
import edu.imse.model.TopicDTO;
import edu.imse.nosql.collection.MongoArticle;
import edu.imse.nosql.collection.MongoTopic;
import edu.imse.nosql.dao.WrittenArticlesDAO;
import edu.imse.sql.entity.Article;
import edu.imse.sql.entity.bridges.ArticleCoversTopic;
import edu.imse.sql.entity.bridges.JournalistWrittenArticle;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

public record ArticleResponse(List<JournalistWithoutConnectionResponse> authors,
							  List<TopicDTO> topics,
							  boolean anonymous,
							  String shortDescription,
							  Integer number,
							  String content) {

	public static ArticleResponse from(WrittenArticlesDAO article) {
		MongoArticle articleInDb = MongoArticle.findById(article.article_id());
		List<JournalistWithoutConnectionResponse> authors = articleInDb.authors
				.stream()
				.map(JournalistWithoutConnectionResponse::fromMongoDbEntry)
				.toList();
		List<TopicDTO> topics = new ArrayList<>();
		for(ObjectId topicId: articleInDb.covered_topics) {
			MongoTopic topicInDB = MongoTopic.findById(topicId);
			topics.add(TopicDTO.fromMongo(topicInDB));
		}
		return new ArticleResponse(authors,
				topics,
				authors.isEmpty(),
				SophoraIdHelper.getDescriptionFromMongoArticle(articleInDb.description_with_number),
				SophoraIdHelper.getNumberFromMongoArticle(articleInDb.description_with_number),
				article.content());
	}

	public static ArticleResponse fromSQLArticle(Article article) {
		List<JournalistWithoutConnectionResponse> authors = JournalistWrittenArticle.findByArticle(article.articleKey)
				.stream()
				.map(JournalistWithoutConnectionResponse::from)
				.sorted()
				.toList();
		List<TopicDTO> topics = ArticleCoversTopic.findByArticle(article.articleKey)
				.stream()
				.map(TopicDTO::fromSQL)
				.toList();
		return new ArticleResponse(authors,
				topics,
				authors.isEmpty(),
				article.articleKey.shortDescription,
				article.articleKey.number,
				article.content);
	}
}

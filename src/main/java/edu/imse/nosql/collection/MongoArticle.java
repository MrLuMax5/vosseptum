package edu.imse.nosql.collection;

import edu.imse.nosql.dao.JournalistWithEmailDAO;
import edu.imse.nosql.dao.ReportDAO;
import edu.imse.sql.entity.Article;
import edu.imse.sql.entity.keys.ArticleKey;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@MongoEntity(collection="article")
@NoArgsConstructor
public class MongoArticle extends PanacheMongoEntity {
	public String description_with_number;
	public Date publication_date;
	public String content;
	public Set<JournalistWithEmailDAO> authors = new HashSet<>();
	public Set<ReportDAO> verifications = new HashSet<>();
	public Set<ObjectId> covered_topics = new HashSet<>();

	public static MongoArticle from(Article sqlArticle) {
		MongoArticle mongoArticle = new MongoArticle();
		mongoArticle.description_with_number = createDescriptionWithNumberFromArticleKey(sqlArticle.articleKey);
		mongoArticle.publication_date = sqlArticle.date;
		mongoArticle.content = sqlArticle.content;
		return mongoArticle;
	}

	private static String createDescriptionWithNumberFromArticleKey(ArticleKey articleKey){
		return articleKey.shortDescription + "-" + articleKey.number;
	}

}

package edu.imse.nosql.dao;

import edu.imse.helper.EmailFinder;
import edu.imse.nosql.collection.MongoArticle;
import java.util.List;
import org.bson.types.ObjectId;

public record WrittenArticlesDAO(ObjectId article_id, String content, List<String> authors) {

	public static WrittenArticlesDAO from(MongoArticle article) {
		return new WrittenArticlesDAO(article.id, article.content, EmailFinder.getEmailsFromJournalists(article.authors));
	}

}

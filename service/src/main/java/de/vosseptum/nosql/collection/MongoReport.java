package de.vosseptum.nosql.collection;

import de.vosseptum.sql.entity.TruthfulnessReport;
import de.vosseptum.nosql.dao.JournalistWithEmailDAO;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@MongoEntity(collection="truthfulness_report")
@NoArgsConstructor
public class MongoReport extends PanacheMongoEntity {

	public Long sql_id;
	public Integer grade;
	public String content;
	public Date date;
	public ObjectId verified_article;
	public Set<JournalistWithEmailDAO> authors = new HashSet<>();

	public static MongoReport from(TruthfulnessReport sqlReport) {
		MongoReport mongoReport = new MongoReport();
		mongoReport.sql_id = sqlReport.id;
		mongoReport.grade = sqlReport.grade;
		mongoReport.content = sqlReport.content;
		mongoReport.date = sqlReport.date;
		PanacheMongoEntityBase queriedArticle = MongoArticle.find("description_with_number", sqlReport.article.articleKey.toString())
				.firstResult();
		if (queriedArticle instanceof MongoArticle mongoArticle) {
			mongoReport.verified_article = mongoArticle.id;
		}
		return mongoReport;
	}

}

package edu.imse.sql.entity.bridges;

import edu.imse.sql.entity.Article;
import edu.imse.sql.entity.Topic;
import edu.imse.sql.entity.keys.ArticleCoversTopicKey;
import edu.imse.sql.entity.keys.ArticleKey;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.List;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "article_covers_topic")
@NamedQuery(name = "ArticleCoversTopic.findByTopic", query = "select articleCoversTopicKey.article from ArticleCoversTopic where topic_subject = ?1")
@NamedQuery(name = "ArticleCoversTopic.findByArticle", query = "select articleCoversTopicKey.topic from ArticleCoversTopic where article_number = ?1 and article_short_description = ?2")
public class ArticleCoversTopic extends PanacheEntityBase {
	@EmbeddedId
	public ArticleCoversTopicKey articleCoversTopicKey;

	public static List<Article> findBySubject(String subject) {
		return find("#ArticleCoversTopic.findByTopic", subject).list();
	}

	public static List<Topic> findByArticle(ArticleKey key) {
		return find("#ArticleCoversTopic.findByArticle", key.number, key.shortDescription).list();
	}
}

package edu.imse.sql.entity.keys;

import edu.imse.sql.entity.Article;
import edu.imse.sql.entity.Topic;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class ArticleCoversTopicKey implements Serializable {

	@ManyToOne
	public Article article;
	@ManyToOne
	public Topic topic;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ArticleCoversTopicKey that = (ArticleCoversTopicKey) o;
		return Objects.equals(article, that.article) && Objects.equals(topic, that.topic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(article, topic);
	}
}

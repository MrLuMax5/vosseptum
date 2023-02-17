package edu.imse.sql.entity.keys;

import edu.imse.sql.entity.Article;
import edu.imse.sql.entity.Journalist;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class JournalistWrittenArticleKey implements Serializable {

	@ManyToOne
	public Journalist journalist;
	@ManyToOne
	public Article article;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JournalistWrittenArticleKey that = (JournalistWrittenArticleKey) o;
		return Objects.equals(journalist, that.journalist) && Objects.equals(article, that.article);
	}

	@Override
	public int hashCode() {
		return Objects.hash(journalist, article);
	}
}

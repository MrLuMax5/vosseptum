package de.vosseptum.sql.entity.bridges;

import de.vosseptum.sql.entity.Article;
import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.sql.entity.keys.ArticleKey;
import de.vosseptum.sql.entity.keys.JournalistWrittenArticleKey;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.List;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "journalist_written_article")
@NamedQuery(name = "JournalistWrittenArticle.getByJournalistId", query = "select journalistWrittenArticleKey.article from JournalistWrittenArticle where journalist_id = ?1")
@NamedQuery(name = "JournalistWrittenArticle.getByArticle", query = "select journalistWrittenArticleKey.journalist from JournalistWrittenArticle where article_number = ?1 and article_short_description = ?2")
public class JournalistWrittenArticle extends PanacheEntityBase {

	@EmbeddedId
	public JournalistWrittenArticleKey journalistWrittenArticleKey;

	public static List<Article> findByJournalistId(long id) {
		return find("#JournalistWrittenArticle.getByJournalistId", id).list();
	}

	public static List<Journalist> findByArticle(ArticleKey key) {
		return find("#JournalistWrittenArticle.getByArticle", key.number, key.shortDescription).list();
	}
}

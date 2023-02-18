package de.vosseptum.sql.entity.services;

import de.vosseptum.sql.entity.Article;
import de.vosseptum.sql.entity.TruthfulnessReport;
import de.vosseptum.sql.entity.keys.ArticleKey;
import java.sql.Date;
import javax.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

@ApplicationScoped
public class ArticleService extends AbstractFillerService {

	@Override
	protected void createEntity(Faker faker, int step) {
		Article article = new Article();
		ArticleKey articleKey = new ArticleKey();
		articleKey.number = 100 + step;
		articleKey.shortDescription = faker.dessert().flavor();
		article.articleKey = articleKey;
		article.date = new Date(step * 1000000L);
		article.content = faker.elderScrolls().quote();
		if (Article.findById(articleKey) == null) {
			article.persist();
		}

		TruthfulnessReport report = new TruthfulnessReport();
		report.article = article;
		report.grade = (step % 6) + 1;
		report.date = new Date(step * 1000000L);
		report.content = faker.starWars().quotes();
		report.persist();
	}
}

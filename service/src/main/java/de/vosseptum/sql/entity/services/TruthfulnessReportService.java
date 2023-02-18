package de.vosseptum.sql.entity.services;

import de.vosseptum.sql.entity.Article;
import de.vosseptum.sql.entity.TruthfulnessReport;
import java.sql.Date;
import javax.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

@ApplicationScoped
public class TruthfulnessReportService extends AbstractFillerService{

	@Override
	protected void createEntity(Faker faker, int step) {
		TruthfulnessReport report = new TruthfulnessReport();
		report.article = (Article) Article.listAll().get((int) (step % Article.count()));
		report.grade = (step % 6) + 1;
		report.date = new Date(step * 1000000L);
		report.content = faker.movie().quote();
		report.persist();
	}
}

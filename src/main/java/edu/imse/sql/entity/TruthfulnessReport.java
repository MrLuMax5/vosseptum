package edu.imse.sql.entity;

import edu.imse.sql.entity.keys.ArticleKey;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.sql.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "truthfulness_report")
@NamedQuery(name = "TruthfulnessReport.getByArticle", query = "from TruthfulnessReport where article_number = ?1 and article_short_description = ?2")
public class TruthfulnessReport extends PanacheEntity {

	@Min(1)
	@Max(6)
	public Integer grade;
	public Date date;
	@Column(length = 64000, columnDefinition="varchar(64000)")
	public String content;
	@ManyToOne
	public Article article;

	public static List<TruthfulnessReport> findByArticle(ArticleKey key) {
		return find("#TruthfulnessReport.getByArticle", key.number, key.shortDescription).list();
	}

}

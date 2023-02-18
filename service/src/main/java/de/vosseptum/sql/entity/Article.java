package de.vosseptum.sql.entity;

import de.vosseptum.sql.entity.keys.ArticleKey;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "article")
public class Article extends PanacheEntityBase {

	@EmbeddedId
	public ArticleKey articleKey;
	public Date date;
	@Column(length = 64000, columnDefinition="varchar(64000)")
	public String content;

}

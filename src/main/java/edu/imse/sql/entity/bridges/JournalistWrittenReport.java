package edu.imse.sql.entity.bridges;

import edu.imse.sql.entity.Journalist;
import edu.imse.sql.entity.keys.JournalistWrittenReportKey;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.List;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "journalist_written_report")
@NamedQuery(name = "JournalistWrittenReport.getByArticle", query = "select journalistWrittenReportKey.journalist from JournalistWrittenReport where report_id = ?1")
public class JournalistWrittenReport extends PanacheEntityBase {

	@EmbeddedId
	public JournalistWrittenReportKey journalistWrittenReportKey;

	public static List<Journalist> findByReport(long id) {
		return find("#JournalistWrittenReport.getByArticle", id).list();
	}
}

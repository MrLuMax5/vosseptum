package edu.imse.sql.entity.keys;

import edu.imse.sql.entity.Journalist;
import edu.imse.sql.entity.TruthfulnessReport;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class JournalistWrittenReportKey implements Serializable {

	@ManyToOne
	public Journalist journalist;
	@ManyToOne
	public TruthfulnessReport report;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JournalistWrittenReportKey that = (JournalistWrittenReportKey) o;
		return Objects.equals(journalist, that.journalist) && Objects.equals(report, that.report);
	}

	@Override
	public int hashCode() {
		return Objects.hash(journalist, report);
	}
}

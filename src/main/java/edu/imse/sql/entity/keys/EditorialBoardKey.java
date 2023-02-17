package edu.imse.sql.entity.keys;

import edu.imse.sql.entity.Topic;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class EditorialBoardKey implements Serializable {

	@ManyToOne
	public Topic topic;
	public String institution;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EditorialBoardKey that = (EditorialBoardKey) o;
		return Objects.equals(topic, that.topic) && Objects.equals(institution, that.institution);
	}

	@Override
	public int hashCode() {
		return Objects.hash(topic, institution);
	}

	@Override
	public String toString() {
		return institution + "-" + topic;
	}
}

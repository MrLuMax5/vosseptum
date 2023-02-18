package de.vosseptum.sql.entity.keys;

import de.vosseptum.sql.entity.Journalist;
import de.vosseptum.sql.entity.Topic;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class JournalistInterestedTopicKey implements Serializable {

	@ManyToOne
	public Journalist journalist;
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
		JournalistInterestedTopicKey that = (JournalistInterestedTopicKey) o;
		return Objects.equals(journalist, that.journalist) && Objects.equals(topic, that.topic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(journalist, topic);
	}
}

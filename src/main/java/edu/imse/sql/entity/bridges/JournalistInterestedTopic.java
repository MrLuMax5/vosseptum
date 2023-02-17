package edu.imse.sql.entity.bridges;

import edu.imse.sql.entity.Topic;
import edu.imse.sql.entity.keys.JournalistInterestedTopicKey;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.List;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "journalist_interested_topic")
@NamedQuery(name = "JournalistInterestedTopic.getByJournalistId", query = "select journalistInterestedTopicKey.topic from JournalistInterestedTopic where journalist_id = ?1")
public class JournalistInterestedTopic extends PanacheEntityBase {

	@EmbeddedId
	public JournalistInterestedTopicKey journalistInterestedTopicKey;

	public static List<Topic> findByJournalistId(long id) {
		return find("#JournalistInterestedTopic.getByJournalistId", id).list();
	}
}
